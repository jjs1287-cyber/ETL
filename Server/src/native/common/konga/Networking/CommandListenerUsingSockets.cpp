/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <string>
#include <memory>	           

#include <konga/Utils/BinaryData.h>
#include <konga/Utils/StringUtils.h>	             
#include <konga/Utils/PortDefs.h>		                        

#include "CommandListenerUsingSockets.h"
#include "CommandUtils.h"
#include "Command.h"
#include "Request.h"
#include "Result.h"
#include "Response.h"

using namespace std;

namespace konga {

using namespace networking;

void CommandListenerUsingSockets::AddCommandFunction(const std::string& sCommand, const CommandFunctionT f) {
	m_mFunctions[ sCommand ] = f;
}

void CommandListenerUsingSockets::StartListening() {
	if( m_lListenPort < 1 )
		throw CrException(toString(m_lListenPort) + " is not a valid port for listening to commands.");

	m_sock.SetListeningPort(m_lListenPort);

	while(!m_bShouldExit) {
		auto_ptr<CrStreamSocket> pSock(m_sock.ServerStreamSocket::Accept());
		if( m_bShouldExit ) return;
		HandleCommand(pSock);
	}
}

void CommandListenerUsingSockets::StopListening() {
	m_bShouldExit = true;
	m_sock.Close();                              
}


void CommandListenerUsingSockets::HandleCommand(auto_ptr<CrStreamSocket>& pSock) {
	try {
		BinaryData bdRequest;
		pSock->Read<BinaryData>(bdRequest);
#ifdef _DEBUG
		cout << "Server Received: " + bdRequest.GetDebugString() << endl;
#endif
		Request* pReq = BinaryDataToRequest(bdRequest, GetModuleName());
		auto_ptr<Request> adopt_pReq(pReq);

		Response resp;
		Request::const_iterator cmdIt = pReq->cmd_begin();
		for(; cmdIt != pReq->cmd_end(); ++cmdIt) {
			CommandToFunctionMapT::const_iterator funcIt = m_mFunctions.find( (*cmdIt)->GetCommandName() );
			Result* pResult;
			if( funcIt == m_mFunctions.end() ) {
				throw CrException("The command '" + (*cmdIt)->GetCommandName() +
					"' was not recognized as a valid command.");
			} else
				pResult = funcIt->second(**cmdIt);
			resp.AddResult( pResult );
		}
		BinaryData bdResponse;
		ResponseToBinaryData(resp, bdResponse, GetModuleName());
		unsigned long nSize = 0;
		const char* pData = bdResponse.GetPointerToData(nSize);
#ifdef _DEBUG
		cout << "Server Sending: " << bdResponse.GetDebugString() << endl;
#endif
		pSock->Write<const char>(pData, nSize);
	} catch(CrException& e) {
		Response resp(e);
		BinaryData bdResponse;
		ResponseToBinaryData(resp, bdResponse, GetModuleName());
		unsigned long nSize = 0;
		const char* pData = bdResponse.GetPointerToData(nSize);
#ifdef _DEBUG
		cout << "Exception: " << e.GetErrorString() << "\n\tServer Sending: " << bdResponse.GetDebugString() << endl;
#endif
		pSock->Write<const char>(pData, nSize);
	}
}

}                   
