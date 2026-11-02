/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <string>
#include <vector>

#include <konga/Networking/BasicCommands.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>		             

using namespace konga;
using namespace std;
using namespace konga::CrNetworking;

int Usage(char* argv[], const string& sErr = "") {
	if( !sErr.empty() )
		cerr << sErr << endl;
	std::string a0="kongaCmd";
	cerr << "Usage: " << a0 << " <module> <port>" << endl;
	cerr << "e.g. " << a0 << " Schedule 46906" << endl;
	cerr << "or " << a0 << " ProcessEngine 46907" << endl;
	cerr << "or " << a0 << " CleanupService 46905" << endl;
	return 1;
}

string GetCmd() {
	cout << "Cmd: ";
	string s;
	cin >> s;
	return s;
}

string GetPwd() {
	cout << "Password: ";
	string sPwd;
	cin >> sPwd;
	return sPwd;
}


int main(int argc, char* argv[])
{
	cout << "Konga Command Utility" << endl;
	if( argc < 3 )
		return Usage(argv, "Wrong number of arguments.");

	string sModule(argv[1]);
	string sPort(argv[2]);

	string sCmd, sUser="Win_Admin", sPassword="****";
	long lPort=atol(sPort.c_str());


	if( lPort <= 0 )
		return Usage(argv, toString(lPort) + " (" + sPort + ") is not a valid port number." );

	try {
		while(1){
			sCmd=GetCmd();

			std::vector<std::string> v=GenericCmd(sCmd, sUser, sPassword, lPort, sModule);
			for(std::vector<std::string>::iterator it=v.begin();it!=v.end();it++){
				cout << *it << endl;
			}
		}
	} catch(CrException& e){
		cout << "Exception occured: " << e.GetErrorString() << endl;
	}

	return 0;
}

