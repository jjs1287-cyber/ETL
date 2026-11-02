/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string.h>		           
#include <cassert>		           
#include <string>
#include <map>

#include <konga/Utils/CrException.h>
#include <konga/Crypto/Crypto.h>		                   

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Authentication/Authentication.h>		                                     
#include <konga/ItemAccess/UserAccess.h>				                           
#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/FileTransfer/HttpUtils.h>	                                        

#include <konga/OperationEngine/OperationEngine.h>		                         
#include <konga/OperationEngine/OperationUtils.h>		                         

#include "WsdlUtils.h"
#include "ApacheUtils.h"
#include "mod_konga_ws.h"
extern "C" { APLOG_USE_MODULE(etlforge_hosted); }

using namespace konga;
using std::string;
using std::map;

namespace {
	const string sID("id=");
	const string sWSDL_PATH("/wsdl");

	void ParseArgs(request_rec* r, string& stOperationGuid, bool& bWsdlFileRequested) {
		if( !r || !r->args ) throw CrException("Cannot access URI arguments.");
		
		const string stArgs(r->args);                         
		const std::string::size_type pos = stArgs.find(sID);
		if( pos != 0 )
			throw CrException("Invalid URI. Cannot find operation id in URI arguments. Args are: " + stArgs);

		stOperationGuid = stArgs.substr(pos + sID.size());

		                                                                          
		const string uri(r->uri ? r->uri : "");
		if ( (r->path_info && EqualsIgnoreCase(string(r->path_info), sWSDL_PATH)) ||
			 (uri.size() >= sWSDL_PATH.size() && uri.compare(uri.size()-sWSDL_PATH.size(), sWSDL_PATH.size(), sWSDL_PATH) == 0) )
			bWsdlFileRequested = true;
		else
			bWsdlFileRequested = false;
	}

	                                                                            
#ifdef _DEBUG
	const bool bAllowAnonymous = false;
#else
	const bool bAllowAnonymous = false;	                          
#endif

	const char SOAP_RESPONSE_CONTENT_TYPE[] = "text/xml; charset=utf-8";
}

namespace {                       
int HandleHostedWebServiceImpl(request_rec* r) {
	                                                             
	if( r->method_number != M_POST && r->method_number != M_GET )
		return HTTP_METHOD_NOT_ALLOWED;

	HttpRequestUser requestUser;
	int res = GetHttpRequestUser(r, requestUser);
	if ( res != OK )
		return res;

	string stOperationGuid;
	bool bWsdlFileRequested = false;
	try {
		ParseArgs(r, stOperationGuid, bWsdlFileRequested);
	} catch( CrException& e) {
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "Failed to parse request: %s",
			e.GetErrorString().c_str());
		return HTTP_INTERNAL_SERVER_ERROR;
	}

	if ( !bWsdlFileRequested && r->method_number != M_POST ) {
		                                                                
		return HTTP_METHOD_NOT_ALLOWED;
	}

	                                          
	long lOperationId = -1;
	try {
		lOperationId = DbGuidToIdMapper().GetId(stOperationGuid, OperationEntityId);
		if( lOperationId == GuidToIdMapper::invalid_id ) {
			ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r,
				"There is no operation with GUID '%s' on this server.",
				stOperationGuid.c_str());
			return HTTP_NOT_FOUND;
		}
	} catch(CrException& e) {
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r,
			"Failed to convert operation GUID '%s' to operation id. Reason: %s",
			stOperationGuid.c_str(), e.GetErrorString().c_str());
		return HTTP_INTERNAL_SERVER_ERROR;
	}

	                                                             
	  
	try {
		int iUserId = 0;
		if ( requestUser.IsAnonymous ) {
			                                                       
			iUserId = -2;
			if ( !bAllowAnonymous && !UserHasExecutePermission(iUserId, lOperationId, OperationEntityId) ) {
				ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r,
					"%s", "A hosted web service was called anonymously, but the operation that implements this web service has not been configured to allow anonymous access. " \
					"This can be done by giving the user 'Anonymous' execute permission on the operation.");
				                                                              
				HeadersT headers;
				headers["WWW-Authenticate"] = "Basic realm=\"Restricted Web Service\"";
				AddOutputHeaders(headers, r);
				return HTTP_UNAUTHORIZED;
			}
		}
		else {
			try {
				                                                 
				if( !Authentication::IsValidUser(requestUser.UserName, md5(requestUser.Password), iUserId) ) {
					ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r,
						"Hosted web service: Wrong username (%s) and/or password.", requestUser.UserName.c_str());
					return HTTP_FORBIDDEN;
				}
			} catch(CrException& e) {
				LogOperationError(lOperationId, -1, "Internal Error while validating user '" + requestUser.UserName
					+ "'. " + e.GetErrorString());
				ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "Authentication error: %s", e.GetErrorString().c_str());
				return HTTP_INTERNAL_SERVER_ERROR;
			}

			                                                                  
			if( !UserHasExecutePermission(iUserId, lOperationId, OperationEntityId) ) {
				LogOperationError(lOperationId, -1, "The user '" + requestUser.UserName
					+ "' is authenticated but does not have execute permission for this operation.");
				ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r,
					"User '%s' does not have execute permission for operation with OperationId=%ld.",
					requestUser.UserName.c_str(), lOperationId);
				return HTTP_FORBIDDEN;
			}
		}
	} catch(CrException& e) {
		LogOperationError(lOperationId, -1, "Internal error while verifying user access. " + e.GetErrorString());
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "Authentication error: %s",
			e.GetErrorString().c_str());
        return HTTP_INTERNAL_SERVER_ERROR;
	}

	                                                           
    if(r->header_only)
		return OK;

	if ( bWsdlFileRequested ) {
		try {
			string stWsdl;
			konga::GetWsdlForHostedWebService(lOperationId, stWsdl);
			ap_set_content_type(r, SOAP_RESPONSE_CONTENT_TYPE);
			ap_rputs(stWsdl.c_str(), r);
			return OK;
		}
		catch ( const InternalErrorException& ex ) {
			ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "Failed to get WSDL contents: %s",
				ex.GetErrorString().c_str());
			return HTTP_INTERNAL_SERVER_ERROR;
		}
		catch( const CrException& ex ) {
			ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "Failed to get WSDL contents: %s",
				ex.GetErrorString().c_str());
			return HTTP_NOT_FOUND;
		}
	}

	HttpRequest request;
	res = FillRequest(request, r);
	if ( res != OK ) {
		return res;
	}

	if( request.Body.empty() ) {
		LogOperationError(lOperationId, -1, "Bad web service request (hosted web service), no data received.");
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "Bad web service request, no data received.");
		return HTTP_BAD_REQUEST;
	}

	string sWarnings;
	try {
		HttpResponse response;

		const UserInfo currentUser;
		OperationEngine opEngine(currentUser, 0);	                                   
		opEngine.HandleHostedWebServiceCall(lOperationId, request, response, sWarnings);

		ap_set_content_type(r, SOAP_RESPONSE_CONTENT_TYPE);
		ap_rputs(response.Body.c_str(), r);
	} catch( const CrException& e ) {
		LogOperationError(lOperationId, -1, e.GetErrorString());
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "Execution error: %s", e.GetErrorString().c_str());
		ap_rputs(e.GetErrorString().c_str(), r);
		return HTTP_INTERNAL_SERVER_ERROR;
	}

	LogOperationSuccess(lOperationId, sWarnings);
	return OK;
}

}

bool konga::HandleHostedWebService(request_rec* r, int& res) {
	if ( !r->handler || strcmp(r->handler, "konga_ws") != 0 ) {
		return false;
	}

	res = HandleHostedWebServiceImpl(r);
	return true;
}


