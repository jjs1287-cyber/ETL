/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <memory>	                
#include <konga/Networking/CrStreamSocket.h>
#include <konga/Networking/Request.h>
#include <konga/Networking/Response.h>
#include <konga/Networking/Command.h>
#include <konga/Networking/Result.h>
#include <konga/Networking/CommandUtils.h>

#include <konga/Utils/BinaryData.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>

#include "BasicCommands.h"

using namespace std;

namespace konga {
namespace networking {
  
                                                                
                                                                
 
                                   
                                            
      
                                                                    
                                        

                              
                                                                                                   
                                                   

                                              
                                  
                                                                                                 
                                                       

                                                  
                        
                              
                                                                
                                                                                                    
               
  
 
  

void Start(const string& sUserName, const string& sPassword,
		   unsigned long lCommandPort, const string& stComponentName)
{
	Request req(sUserName, sPassword);
	req.AddCommand( new Command("Start") );
	Response* pResp = SendRequest(req, lCommandPort, stComponentName);
	delete pResp;
}

void Stop(const string& sUserName, const string& sPassword,
		   unsigned long lCommandPort, const string& stComponentName)
{
	Request req(sUserName, sPassword);
	req.AddCommand( new Command("Stop") );
	Response* pResp = SendRequest(req, lCommandPort, stComponentName);
	delete pResp;
}

void Shutdown(const string& sUserName, const string& sPassword,
		   unsigned long lCommandPort, const string& stComponentName)
{
	Request req(sUserName, sPassword);
	req.AddCommand( new Command("Shutdown") );
	Response* pResp = SendRequest(req, lCommandPort, stComponentName);
	delete pResp;
}

vector<string> GenericCmdWithArgs(const string& sCmd, const vector<string>& vArgs,
								const string& sUserName, const string& sPassword,
								unsigned long lCommandPort, const string& stComponentName)
{
	Request req(sUserName, sPassword);
	Command *pCmd = new Command(sCmd.c_str());

	                            
	vector<string>::const_iterator citArg = vArgs.begin();
	for(; citArg != vArgs.end(); ++citArg)
		pCmd->AddArgument( *citArg );

	req.AddCommand( pCmd );
	std::auto_ptr<Response> pResp( SendRequest(req, lCommandPort, stComponentName) );

	std::vector<std::string> v;
	if(pResp->res_size()>0){
		int i=0;
		for(Response::const_iterator it=pResp->res_begin();
			it!=pResp->res_end();it++){
			i++;
			Result* pResult=*it;
			int i1=0;
			for(Result::const_iterator it1=pResult->ret_val_begin();
				it1!=pResult->ret_val_end();it1++){
				i1++;
				std::string line="resp"+konga::toString(i)+"_result"+konga::toString(i1)+":\""+*it1+"\"";
				v.push_back(line);
			}
		}
	}
	return v;
}

std::vector<std::string> GenericCmd(const string& sCmd, const string& sUserName, const string& sPassword,
									unsigned long lCommandPort, const string& stComponentName)
{
	std::vector<std::string> v;
	return GenericCmdWithArgs(sCmd, v, sUserName, sPassword, lCommandPort, stComponentName);
}

}                   
}                        

