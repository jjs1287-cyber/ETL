#include <iostream>

#include <konga/Utils/CrException.h>

#include <konga/UiBackend/WebServices/Authenticate/Authenticate.hpp>


using namespace std;

int authCommandLine(int argc, char* argv[]);


int main (int argc, char *argv[])
{
	return authCommandLine(argc, argv);
}


int authCommandLine(int argc, char* argv[]) {
	if ( argc < 3 ) {
		cout << "Usage: " << argv[0] << " UserName Password" << endl;
		return 2;
	}

	char* user = argv[1];
	char* pwd = argv[2];

	try {
		Authenticate auth;
		AuthenticateResult* pRes = auth.login(user, pwd);
		cout << pRes->getserverGuid() << endl;
		cout << pRes->getuserGuid() << endl;
		delete pRes;
		cout << "done" << endl;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}

