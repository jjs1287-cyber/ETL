/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <fstream>	           
#include <exception>

#include <konga/Utils/CrException.h>

#include "ApacheUtils.h"
extern "C" { APLOG_USE_MODULE(etlforge_hosted); }

using namespace konga;
using std::string;
using std::vector;
using std::ofstream;
using std::map;

                                                                  
extern "C" int AddHeader(void *httpRequest, const char *key, const char *value) {
	HttpRequest *request = (HttpRequest*)httpRequest;
	if( key == 0 )
		return 1;
	request->Headers[key]=value?value:"";

	return 1;
}

int konga::FillRequest(HttpRequest& request, request_rec* r) {
	const bool writeToFile = !request.BodyFileName.empty();

	ofstream file;
	if ( writeToFile ) {
		try {
			file.exceptions(ofstream::eofbit | ofstream::failbit | ofstream::badbit);
			file.open(request.BodyFileName.c_str(), ofstream::out | ofstream::trunc | ofstream::binary);
		}
		catch( const std::exception& e ) {
			throw CrException("Failed to open request file '" + request.BodyFileName
				+ "'. Reason: " + string(e.what()));
		}
	}
	request.Body.clear();

	const int rstat =	ap_setup_client_block(r, REQUEST_CHUNKED_DECHUNK);
	if (rstat != OK) {
		ap_log_rerror(APLOG_MARK, APLOG_ERR, 0, r, "ap_setup_client_block error: %d", rstat);
		return rstat;
	}

	                                                                                     
	                                                                        
	                                                                               
	                                                                 
	  
	                          
	                                                                 
	                                                                             
	                                             
	  
	if( ap_should_client_block(r) ) {
		char buf[1025];
		long len;
		while( (len = ap_get_client_block(r, buf, 1024)) > 0) {
			if ( writeToFile ) {
				                            
				file.write(buf, len);
			}
			else {
				                                
				buf[len]=0;
				request.Body.append(buf, len);
			}
		}
	}
	if (writeToFile) file.close();

	                   
	if ( r->headers_in ) {
		apr_table_do(AddHeader, &request, r->headers_in, 0);
	}

	                         
	if ( r->parsed_uri.query ) {
		const string query(r->parsed_uri.query);
		if ( !query.empty() ) {
			vector<string> vQuery = SplitString(query, "&");
			if ( !vQuery.empty() ) {
				for ( vector<string>::const_iterator cit = vQuery.begin(); cit != vQuery.end(); ++cit ) {
					const string& q = *cit;
					const string::size_type ipos = q.find("=");
					string name, value;
					if ( ipos != string::npos ) {
						name = q.substr(0, ipos);
						value = q.substr(ipos+1);
					}
					else {
						name = q;
					}
					if ( !name.empty() ) request.Query[name] = value;
				}
			}
		}
	}

	                  
	if ( r->connection && r->useragent_ip ) {
		request.IpAddress = r->useragent_ip;
	}

	return OK;
}

int konga::GetHttpRequestUser(request_rec* r, HttpRequestUser& user) {
    const char* name = NULL;
    const char* password = NULL;
    const apr_status_t status = ap_get_basic_auth_components(r, &name, &password);
    user.IsAnonymous = status != APR_SUCCESS || !name || !password;
    if (!user.IsAnonymous) { user.UserName = name; user.Password = password; }
    return OK;
}
void konga::AddOutputHeaders(const HeadersT& vHeaders, request_rec* r) {
	if ( r == 0 || vHeaders.empty() )
		return;

	                                                                          
	apr_table_t* headers = r->err_headers_out;
	if ( !headers ) {
		headers = apr_table_make(r->pool, vHeaders.size());
		r->err_headers_out = headers;
	}

	for( HeadersT::const_iterator cit = vHeaders.begin();
		cit != vHeaders.end(); ++cit )
	{
		apr_table_set(headers, cit->first.c_str(), cit->second.c_str());
	}
}


