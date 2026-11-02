#include <signal.h>		                   

#include <iostream>
#include <string>

#include <boost/thread/thread.hpp>
#include <boost/thread/condition.hpp>

#include <konga/Networking/Request.h>
#include <konga/Networking/Response.h>
#include <konga/Networking/Command.h>
#include <konga/Networking/Result.h>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             

#include "Client.h"
#include "Server.h"

using namespace konga;
using namespace konga::CrNetworking;
using namespace std;

const unsigned long lCOMMAND_PORT = 445566;
string sSERVER_MODULE = "MyServer";

boost::condition g_exitCond;

void SignalHandler(int iSig) {
	switch(iSig) {
	case SIGINT:
		std::cout << "Caught Ctrl-C. Goodbye... " << std::endl;
		g_exitCond.notify_all();
		break;
	}
}

int main() {
	                                     
	signal(SIGINT, SignalHandler);

	CommandListenerUsingSockets commandListener(sSERVER_MODULE);
	boost::thread* pCommandListenerThread = new boost::thread(CommandListenerFunctor(lCOMMAND_PORT, commandListener));
	auto_ptr<boost::thread> adopt_pCommandListenerThread(pCommandListenerThread);

	                                                                            
	#ifdef WIN32
		Sleep(1000);
	#else
		sleep(1);
	#endif

	                          

	int iDetailLevel = 0;
	Request req("user", "pass");
	Command* pCmd = new Command("GetStatus");
	pCmd->AddArgument( toString(iDetailLevel) );
	req.AddCommand( pCmd );
	
	try {
		Response* pResp = SendRequest(req, lCOMMAND_PORT);

		if( pResp->res_size() != 1 )
			throw CrException("Incorrect number of command results in response to GetStatus command. Got " +
				toString(pResp->res_size()) + ", expected 1.");
		const Result& result = **pResp->res_begin();

		if( result.ret_val_size() != 2 )
			throw CrException("Incorrect number of return values in response to GetStatus command. Got " +
				toString(result.ret_val_size()) + ", expected 2.");

		const string& sStatusEnum = *result.ret_val_begin();
		const string& sStatusString = *(result.ret_val_begin() + 1);
	} catch(CrSocketException& e) {
		cout << "CrSocketException: " << e.GetErrorNumberAsString() << endl;
	} catch(CrException& e) {
		cout << "CrException: " << e.GetErrorNumberAsString() << endl;
	}

	boost::mutex mu;
	boost::mutex::scoped_lock lock(mu);
	g_exitCond.wait(lock);

	return 0;
}
