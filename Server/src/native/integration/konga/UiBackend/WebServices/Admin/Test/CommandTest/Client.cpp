#include <string>

#include <konga/Networking/CommandUtils.h>

#include <konga/Networking/CommandListenerUsingSockets.h>
#include <konga/Networking/Request.h>
#include <konga/Networking/Response.h>
#include <konga/Networking/Command.h>
#include <konga/Networking/Result.h>

#include <konga/Utils/BinaryData.h>

#include "Client.h"

using namespace std;

             

namespace konga {
namespace CrNetworking {

Response* SendRequest(const Request& req, long lCommandPort) {
	CrStreamSocket sock;
	sock.Connect("127.0.0.1", lCommandPort);
	                                          

	BinaryData reqData;
	RequestToBinaryData(req, reqData, sSERVER_MODULE);
	sock.Write<const char>(reqData.GetPointerToData(), reqData.Size());

	BinaryData respData;
	sock.Read<BinaryData>(respData);

	Response* pResp = new Response();
	BinaryDataToResponse(respData, *pResp, sSERVER_MODULE);

	return pResp;
}

}                   
}                          
