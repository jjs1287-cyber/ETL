#include <iostream>
#include <memory>	           

#include <konga/Utils/CrException.h>

#include <konga/Networking/RemoteException.h>

#include <konga/UiBackend/WebServices/Admin/Impl/AdminImpl.h>

using namespace axiscpp;
using namespace konga;
using namespace konga::CrNetworking;
using namespace std;

  
                                                         
int main(int argc, char* argv[])
{
	if ( argc < 3 )
		return 1;

	const char* pUser = argv[1];
	const char* pPwd = argv[2];

	try {
		AdminImpl impl;
		ServerStatus* pServerStatus = impl.getEngineStatus(pUser,pPwd,-1);
		cout << pServerStatus->getsDescription() << endl;
		auto_ptr<ServerStatus> adopt_pServerStatus(pServerStatus);

		                     
	} catch(RemoteException& e) {
		cout << "Caught RemoteException: " << e.GetErrorString()  << endl;
		return 1;
	} catch(CrException& e) {
		cout << "Caught CrException: " << e.GetErrorString()  << endl;
		return 1;
	}
	cout << "done" << endl;
	return 0;
}

