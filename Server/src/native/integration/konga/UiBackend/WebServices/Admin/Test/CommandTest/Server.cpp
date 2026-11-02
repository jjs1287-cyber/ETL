#include <string>

#include <konga/Networking/CommandListenerUsingSockets.h>
#include <konga/Networking/Request.h>
#include <konga/Networking/Response.h>
#include <konga/Networking/Command.h>
#include <konga/Networking/Result.h>

#include <konga/Utils/StringUtils.h>	             

#include "Server.h"

using namespace std;

namespace konga {
namespace CrNetworking {

             

Result* gStop(const Command& cmd)
{
	Result* pResult = new Result(cmd.GetCommandName());
	pResult->AddReturnValue("Server is stopped.");
	return pResult;
}

Result* gGetStatus(const Command& cmd)
{
	bool bRunning = true;

	Result* pResult = new Result(cmd.GetCommandName());

	if( bRunning ) {
		pResult->AddReturnValue(toString(1));
		pResult->AddReturnValue("Server is running.");
	} else {
		pResult->AddReturnValue(toString(0));
		pResult->AddReturnValue("Server is stopped.");
	}

	return pResult;
}

}                   
}                          
