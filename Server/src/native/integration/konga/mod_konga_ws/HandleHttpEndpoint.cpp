/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>
#include <map>

#include "apr_strings.h"	              

#ifndef WIN32
#include <konga/Utils/PortDefs.h>		                                    
#endif
#include <konga/Utils/StringUtils.h>	                   
#include <konga/Utils/FileUtils.h>		              
#include <konga/Utils/util.h>			                  
#include <konga/Utils/CrException.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/ErrorResolver.h>

#include <konga/Crypto/Crypto.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/TransformationTypeIdConstants.h>

#include <konga/ItemAccess/UserInfo.h>
#include <konga/ItemAccess/UserAccess.h>

#include <konga/Authentication/Authentication.h>

#include <konga/HttpEndpoint/HttpEndpointSelector.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/DefaultOperationDataElementHolder.h>
#include <konga/OperationEngine/OperationEngine.h>

                          
#ifdef strcasecmp
#undef strcasecmp
#endif
#ifdef strncasecmp
#undef strncasecmp
#endif

#include "ApacheUtils.h"
#include "mod_konga_ws.h"
extern "C" { APLOG_USE_MODULE(etlforge_hosted); }

using namespace konga;
using std::vector;
using std::string;
using std::map;

namespace {
	string GetTmpDirForHttpEndpointRequest(const string& tag) {
		string dir( konga::getTmpDir() );
		dir += "HttpEndpoint";
		AppendPathSeparator(dir);
		dir += tag;
		AppendPathSeparator(dir);
		dir += GetGUIDString();
		kCreateDirectory(dir);
		AppendPathSeparator(dir);
		return dir;
	}

	void initDeHolder(OperationDataElementHolder& deHolder, const string& requestVariable, const string& requestFileName, const HttpRequest& request) {
		if ( !requestVariable.empty() ) {
			string sRequest;
			read_file_to_string(requestFileName, sRequest, true);
			DataElement* de = deHolder.getOperationDataElement(requestVariable);
			de->setType(DataElement::t_string);
			de->setValue(sRequest.c_str());
		}
		OperationEngine::SetNetworkingInfo(request, deHolder);
	}

	void extractResponseInfo(OperationDataElementHolder& deHolder, string& responseContentType) {
		const string ctDEName("etlforge.networking.http.response.content_type");
		if ( deHolder.isOperationDataElementDefined(ctDEName) ) {
			DataElement* de = deHolder.getOperationDataElement(ctDEName);

			int err = 0;
			responseContentType = (const char*)de->GetString(err);
		}
	}

	void readResponse(OperationDataElementHolder& deHolder, const string& responseVariable, string& response) {
		if ( !responseVariable.empty() ) {
			if ( deHolder.isOperationDataElementDefined(responseVariable) ) {
				DataElement* de = deHolder.getOperationDataElement(responseVariable);
				int err = 0;
				response = (const char*)de->GetString(err);
			}
		}
	}

	int CheckOperationPermissions(const HttpEndpoint& httpEndpoint, const string& method, const UserInfo& userInfo, request_rec* r, DbConnection& dbConn) {
		UserAccess userAccess(userInfo, dbConn);

		vector<HttpEndpointOperation*> operations = httpEndpoint.getOperations(method);
		for( vector<HttpEndpointOperation*>::const_iterator cit = operations.begin(); cit != operations.end(); ++cit ) {
			const HttpEndpointOperation& operation = *(*cit);
			const long lOperationId = operation.getOperationId();
			if ( !userAccess.HasExecutePermission(lOperationId, OperationEntityId) ) {
				ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "The current user (%s) does not have access to one or more operations triggered by this HTTP end-point",
					userInfo.GetLogin().c_str());
				return HTTP_UNAUTHORIZED;
			}
		}
		return OK;
	}

	void AddResponseHeaders(const OperationDataElementHolder& deHolder, request_rec* r) {
		map<string, DataElement*> headersDeMap;
		const string startsWith("etlforge.networking.http.response.header.");
		deHolder.getOperationDataElementsStartingWith(startsWith, headersDeMap);
		if ( headersDeMap.empty() ) return;

		HeadersT headers;
		for( map<string, DataElement*>::const_iterator cit = headersDeMap.begin(); cit != headersDeMap.end(); ++cit ) {
			const string& deName = cit->first;
			if ( deName.empty() ) continue;

			const string name( deName.substr(startsWith.size()) );
			if ( name.empty() ) continue;;

			const DataElement* const de = cit->second;
			int err = 0;
			const string value((const char*)de->GetString(err));
			if ( err != 0 || value.empty() ) continue;

			headers.insert( HeadersT::value_type(name, value) );
		}

		if ( headers.empty() ) return;

		AddOutputHeaders(headers, r);
	}

	void SetStatusCode(const OperationDataElementHolder& deHolder, request_rec* r) {
		if ( r == 0 ) return;
		const string statusDeName("etlforge.networking.http.response.status_code");
		if ( deHolder.isOperationDataElementDefined(statusDeName) ) {
			const DataElement* const statusDE = deHolder.getOperationDataElement(statusDeName);
			int err = 0;
			const int tmpStatus = statusDE->GetInteger(err);
			if ( err == 0 && tmpStatus >= HTTP_CONTINUE && tmpStatus <= HTTP_NOT_EXTENDED ) {
				r->status = tmpStatus;
			}
		}
	}

	void WriteFileToResponse(request_rec* r, const string& response_file) {
		FILE* f = fopen(response_file.c_str(), "rb");
		if ( f == 0 ) {
			const long lErr = GetLastError();
			throw CrException("Failed to open response file '" + response_file + "'. " + ErrorResolver(lErr).Resolve(lErr));
		}
		AutoFileHandle adopt_f(f);

		char buf[1024];
		size_t bytesRead;
		while( (bytesRead = fread(buf, 1, 1023, f)) > 0 ) {
			buf[bytesRead] = 0;
			ap_rputs(buf, r);
		}
	}

	const char CONTENT_TYPE_TEXT_PLAIN[] = "text/plain";
	const char CONTENT_TYPE_TEXT_HTML[] = "text/html";
	const char CONTENT_TYPE_TEXT_XML[] = "text/xml";
}

bool konga::HandleHttpEndpoint(request_rec* r, int& res, DbConnection& dbConn, long httpEndpointId) {
	                                        
	if( !r || !r->method ) {
		return false;
	}

	                                           
	HttpEndpointSelector selector(&dbConn);
	std::auto_ptr<HttpEndpoint> pHttpEndpoint( selector.GetHttpEndpoint(httpEndpointId) );
	if ( pHttpEndpoint.get() == 0 )
		return false;

	                        
	HttpRequestUser requestUser;
	res = GetHttpRequestUser(r, requestUser);
	if ( res != OK ) {
		return true;	                        
	}

	UserInfo userInfo;
	if ( requestUser.IsAnonymous ) {
		userInfo = konga::GetUserInfo(UserInfo::ANONYMOUS_UID, dbConn, true);
	}
	else {
		                     
		int iUserId = 0;
		if( !Authentication::IsValidUser(requestUser.UserName, md5(requestUser.Password), iUserId, &dbConn) ) {
			ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r,
				"Wrong username (%s) and/or password.", requestUser.UserName.c_str());
			res = HTTP_FORBIDDEN;
			return true;
		}
		userInfo = konga::GetUserInfo(iUserId, dbConn, true);
	}

	                                              
	const string requestMethod(r->method);
	res = CheckOperationPermissions(*pHttpEndpoint, requestMethod, userInfo, r, dbConn);
	if ( res != OK )
		return true;

	                                                   
    if(r->header_only)
		return OK;

	const string tmp_dir(GetTmpDirForHttpEndpointRequest("sync"));
	HttpRequest request;
	request.BodyFileName = tmp_dir + "request";
	res = FillRequest(request, r);
	if ( res != OK )
		return true;	                                                           

	const string response_file(tmp_dir + "response");

	const string& requestVariable = pHttpEndpoint->getRequestVariable();
	const string& responseVariable = pHttpEndpoint->getResponseVariable();

	const vector<HttpEndpointOperation*> syncOperations = pHttpEndpoint->getSynchronousOperations(requestMethod);

	HttpEndpointResponseMode responseMode = pHttpEndpoint->getHttpEndpointResponseMode();
	bool gotResponseInFile = false;
	string responseFromVariable, responseContentType;
	int finalTargetType = UnknownStructureType;

	                     
	UserInfo owner;	                                                                 
	DefaultOperationDataElementHolder deHolder(owner);
	initDeHolder(deHolder, requestVariable, request.BodyFileName, request);
	for( vector<HttpEndpointOperation*>::const_iterator cit = syncOperations.begin(); cit != syncOperations.end(); ++cit ) {
		const HttpEndpointOperation& operation = *(*cit);
		const long operationId = operation.getOperationId();

		OperationEngine opEngine(owner, 0, &dbConn);
		if ( operation.useRequestAsSource() ) {
			opEngine.overrideSourceFile(request.BodyFileName, false);
		}
		if ( responseMode == ResponseMode_Operation && operation.finalTargetIsResponse() ) {
			opEngine.saveFinalTargetToThisFile(response_file);
		}

		try {
			opEngine.RunOperation(operationId, -1, -1, &deHolder, true);
		}
		catch( const CrException& e ) {
			ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "Execution error: %s", e.GetErrorString().c_str());
			res = HTTP_INTERNAL_SERVER_ERROR;
			return true;
		}

		                    
		if ( responseMode == ResponseMode_Operation && operation.finalTargetIsResponse() && FileExists(response_file) ) {
			gotResponseInFile = true;
			finalTargetType = opEngine.getFinalTargetType();
		}
		else if ( responseMode == ResponseMode_Variable ) {
			readResponse(deHolder, responseVariable, responseFromVariable);
		}
	}

	extractResponseInfo(deHolder, responseContentType);

	                                                                         
	const vector<HttpEndpointOperation*> asyncOperations = pHttpEndpoint->getAsynchronousOperations(requestMethod);
	if ( !asyncOperations.empty() ) {
		const string async_base_dir(GetTmpDirForHttpEndpointRequest("async"));
		int iop = 1;
		for( vector<HttpEndpointOperation*>::const_iterator cit = asyncOperations.begin(); cit != asyncOperations.end(); ++cit, ++iop ) {
			const HttpEndpointOperation& operation = *(*cit);
			const long operationId = operation.getOperationId();

			OperationEngine opEngine(owner, 0, &dbConn);
			if ( operation.useRequestAsSource() ) {
				                                                                                            
				const string request_file(async_base_dir + "request_" + toString(iop));
				kCopyFile(request.BodyFileName, request_file);
				opEngine.overrideSourceFile(request_file, true);
			}

			                                  
			opEngine.RunOperationAsync(operationId, &deHolder, "Http Trigger");
		}
	}

	                                               
	if ( responseMode != ResponseMode_None ) {
		if ( !responseContentType.empty() ) {
			ap_set_content_type(r, apr_pstrdup(r->pool, responseContentType.c_str()));
		}
		else if ( responseMode == ResponseMode_Operation && ( finalTargetType == TextTypeId || finalTargetType == XmlTypeId ) ) {
			ap_set_content_type(r, finalTargetType == TextTypeId ? CONTENT_TYPE_TEXT_PLAIN : CONTENT_TYPE_TEXT_XML);
		}
		else {
			                                
			ap_set_content_type(r, CONTENT_TYPE_TEXT_HTML);
		}
	}

	AddResponseHeaders(deHolder, r);
	SetStatusCode(deHolder, r);

	switch(responseMode) {
		case ResponseMode_None:
			ap_rputs("", r);
			break;
		case ResponseMode_Operation:
			if ( gotResponseInFile ) {
				WriteFileToResponse(r, response_file);
			}
			break;
		case ResponseMode_Variable:
			ap_rputs(responseFromVariable.c_str(), r);
			break;
	};
	int flushed = ap_rflush(r);

	try {
		kRemoveDirectory(tmp_dir);
	}
	catch( const CrException& ) {
		                      
	}

	return true;
}
