/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>	       
#include <exception>
#include <string>

#ifdef WIN32
	#include <windows.h>
#endif

#include <httpd.h>	                            
#include <http_config.h>
#include <http_core.h>
#include <http_protocol.h>
#include <http_log.h>

#include <ap_compat.h>
#include <apr_tables.h>

#ifdef REG_EXTENDED	                                                                           
#undef REG_EXTENDED
#undef REG_ICASE
#undef REG_NEWLINE
#undef REG_NOTBOL
#undef REG_NOTEOL
#undef REG_NOSUB
#endif

#include <konga/Utils/CrException.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/FileUtils.h>

#include <konga/Authentication/Authentication.h>	                                     

#include <konga/Workflow/XmlReader/WfXmlReader.h>
#include <konga/Workflow/DbWriter/WfDbWriter.h>
#include <konga/Workflow/Process.h>

#include "mod_konga_wf.h"

using namespace std;
using namespace konga;

#ifdef WIN32

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
	switch (ul_reason_for_call)
	{
	case DLL_PROCESS_ATTACH:
	case DLL_THREAD_ATTACH:
	case DLL_THREAD_DETACH:
	case DLL_PROCESS_DETACH:
		break;
	}
    return TRUE;
}
#endif         

void ParseArgs(request_rec* r, string& stUserName, string& stPassword, int& iDeployOption) {
	if( !r || !r->args ) throw CrException("Cannot access URI arguments.");

	                                                                                                
	                                                                                     
	                                                 

	string stArgs = r->args;
	size_t pos1 = stArgs.find("User=");
	if( pos1 != 0 ) throw CrException("Cannot find user name in URI arguments. Args are: " + stArgs);
	pos1 += strlen("User=");
	size_t pos2 = stArgs.find("&Password=");
	stUserName = stArgs.substr(pos1, pos2-pos1);

	pos1 = pos2 + strlen("&Password=");
	pos2 = stArgs.find("&DeployOption=");
	stPassword = stArgs.substr(pos1, pos2-pos1);

	pos1 = pos2 + strlen("&DeployOption=");
	pos2 = stArgs.find("&ExternalDepCheck=");
	iDeployOption = atoi(stArgs.substr(pos1, pos2-pos1).c_str());
}

int HandleRequest(request_rec* r) {
	if( !r->handler || strcmp(r->handler, "konga_wf") != 0 )
        return DECLINED;

    if( r->method_number != M_POST )
		return DECLINED;

                                           

    if(r->header_only) return OK;

	                                                                
	             

	  
	                                            
	  
	string stUserName, stPassword;
    int iDeployOption;
	try {
		ParseArgs(r, stUserName, stPassword, iDeployOption);
	} catch( CrException& e) {
		cerr << e.GetErrorString() << endl;
		return HTTP_INTERNAL_SERVER_ERROR;
	}

	if( !Authentication::IsValidUser(stUserName, stPassword) ) {
		                                                                                                                     
		ap_rputs("Unrecognized user, or wrong password.", r);
		return OK;
	}

	int rstat =	ap_setup_client_block(r, REQUEST_CHUNKED_DECHUNK);
	if (rstat != OK)
		return rstat;

	                                                                                         
	                                                                          
	                                                                               
	                                                                 

	string stBody;
	  
	                          
	                                                                 
	                                                                             
	                                             
	  
	if( ap_should_client_block(r) ) {
		char buf[1024];
		size_t len;										                                              

		while( (len = ap_get_client_block(r, buf, sizeof(buf)-1)) > 0) {
			buf[len]=0;
			stBody += buf;
		}
	}

	if( !stBody.empty() ) {
		if( strncmp(stBody.c_str(), "--bb4077aa", 10) != 0 ) {
			ap_rputs("Unexpected data encountered. Expected --bb4077aa.", r);
			return HTTP_BAD_REQUEST;
		}

		size_t lStartPos = stBody.find("\r\n\r\n");
		size_t lEndPos = stBody.find("\r\n--bb4077aa--");
		size_t nChars = lEndPos - lStartPos - 4;

		string stTempDir = konga::getTmpDir();
		if( stTempDir.empty() ) {
			string stErr = "Failed to write get temp directory. Cannot proceed.";
			ap_rputs(stErr.c_str(), r);
			cerr << stErr << endl;
			return HTTP_INTERNAL_SERVER_ERROR;
		}

		string stPath = konga::getTmpFileName("postedBody.xml", stTempDir);
		if( stPath.empty() ) {
			string stErr = "Failed to write get temp file name. Cannot proceed.";
			ap_rputs(stErr.c_str(), r);
			cerr << stErr << endl;
			return HTTP_INTERNAL_SERVER_ERROR;
		}

		FILE* f = fopen(stPath.c_str(), "wb");
		if( !f ) {
			string stErr = "Failed to write to temp file. Cannot proceed.";
			ap_rputs(stErr.c_str(), r);
			cout << stErr << endl;
			return HTTP_INTERNAL_SERVER_ERROR;
		}

		fwrite(stBody.substr(lStartPos+4, nChars).c_str(), 1, nChars, f);
		fclose(f);

		try {
			Process* pProcess = WfXmlReader::ParseXml( stPath );

			DBConnectionInfo info("PDb");
			WfDbWriter dbWriter;
			dbWriter.WriteProcess(*pProcess, info.connectionString(), WfDbWriter::WorkflowDbDeployType(iDeployOption));
		} catch(CrException& e) {
			cerr << e.GetErrorString() << endl;
			ap_rputs(e.GetErrorString().c_str(), r);
			konga::kDeleteFile(stPath);
			return HTTP_INTERNAL_SERVER_ERROR;
		} 
		konga::kDeleteFile(stPath);
	}

	return OK;
}

extern "C"
{ 

static void module_init(apr_pool_t* p, server_rec* svr_rec) {
}

static int konga_handler(request_rec* r)
{
	try {
		return HandleRequest(r);
	} catch(CrException& e) {
		cout << e.GetErrorString() << endl;
		ap_rputs(("<HTML>"+e.GetErrorString()+"</HTML>").c_str(), r);
		return OK;
	}
#ifndef _DEBUG
	catch(...) {
		cout << "Unknown exception caught." << endl;
		return HTTP_INTERNAL_SERVER_ERROR;
	}
#endif
}

static void mod_konga_register_hooks(apr_pool_t* p)
{
                                                                   
    ap_hook_handler(konga_handler, NULL, NULL, APR_HOOK_MIDDLE);
}

module AP_MODULE_DECLARE_DATA konga_wf_module = {
    STANDARD20_MODULE_STUFF,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    mod_konga_register_hooks,
};

}              
