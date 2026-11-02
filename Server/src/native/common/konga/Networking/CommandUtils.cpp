/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <string.h>			             

#ifdef WIN32
	#include <winsock2.h>	          
#else
	#include <netinet/in.h>				          
	#include <konga/Utils/PortDefs.h>	             
#endif

#include <memory>	           
#include <string>
#include <cassert>

#include <boost/scoped_array.hpp>

#include <konga/Networking/CrStreamSocket.h>

#include <konga/Utils/BinaryData.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             

#include "Request.h"
#include "Response.h"
#include "Command.h"
#include "Result.h"
#include "KongaPacket.h"
#include "RemoteException.h"

#include "CommandUtils.h"

using namespace std;

namespace {
	const unsigned long nMaxUserNameSize = 256;
	const unsigned long nMaxPasswordSize = 1024;
	const unsigned long nMaxCommandSize = 256;

	const unsigned long nMaxSize = 1024;                                                                   

	const unsigned long nMaxReturnValueSize = 1024 * 1024;
	const unsigned long nMaxArgumentSize = nMaxReturnValueSize;
}

namespace {
	                                        
	void FinishMessage(konga::BinaryData& header, konga::BinaryData& data, konga::BinaryData& data_out) {
		const unsigned long tmp_length = header.Size() + sizeof(unsigned long) + data.Size();
		header += htonl(tmp_length);

		data_out += header;
		data_out += data;
		assert(header.Size() + data.Size() == tmp_length &&
			tmp_length == data_out.Size());
		}
}

namespace konga {
namespace networking {

void RequestToBinaryData(const Request& req, BinaryData& data_out, const string& sModuleName) {

	KongaPacket packet;
	BinaryData header;
	header += packet.GetProtocolName();				                        
	header += htonl( packet.GetProtocolVersion() );	                   
	                                                                         

	BinaryData data;
	data.Reserve(1024);
	data += htonl( sModuleName.length() );
	data += sModuleName;							              
	const char c = Packet::DIRECTION_REQUEST;
	data.append(&c,1);								                     
	data += 0ul;									                                     

	                    
	data += htonl( req.GetUserName().length() );
	data += req.GetUserName();						            
	data += htonl( req.GetPassword().length() );
	data += req.GetPassword();						           

	                      
	data += htonl( req.cmd_size() );
	Request::const_iterator cmdIt = req.cmd_begin();
	for(; cmdIt != req.cmd_end(); ++cmdIt)
		CommandToBinaryData(**cmdIt, data);

	data += packet.GetProtocolEndString();			                                

	FinishMessage(header, data, data_out);
}

void CommandToBinaryData(const Command& cmd, BinaryData& data) {
	data += htonl( cmd.GetCommandName().length() );
	data += cmd.GetCommandName();
	data += htonl( cmd.arg_size() );

	                                              
	Command::const_iterator argIt = cmd.arg_begin();
	for(; argIt != cmd.arg_end(); ++argIt) {
		data += htonl( argIt->length() );
		data += *argIt;
	}
}

void ResponseToBinaryData(const Response& resp, BinaryData& data_out, const string& sModuleName) {

	KongaPacket packet;
	BinaryData header;
	header += packet.GetProtocolName();				                        
	header += htonl( packet.GetProtocolVersion() );	                   
	                                                                   

	BinaryData data;
	data.Reserve(1024);
	data += htonl( sModuleName.length() );
	data += sModuleName;							              
	const char c = Packet::DIRECTION_RESPONSE;
	data.append(&c,1);								                      
	data += 0ul;									                                     

	                     

	if( resp.HasExceptionOccurred() ) {
		const CrException& e = resp.GetException();
		data += htonl( e.GetErrorNumber() );
		data += htonl( e.GetErrorString().length() );
		data += e.GetErrorString();
	} else {
		data += 0ul;                          
		                      
		data += htonl( resp.res_size() );
		Response::const_iterator resIt = resp.res_begin();
		for(; resIt != resp.res_end(); ++resIt) {
			const Result& res = **resIt;
			data += htonl( res.GetCommandName().length() );	                        
			data += res.GetCommandName();					                    
			data += htonl( res.ret_val_size() );			                           
			Result::const_iterator retValIt = res.ret_val_begin();
			for(; retValIt != res.ret_val_end(); ++retValIt) {
				data += htonl( retValIt->length() );		                        
				data += *retValIt;							                                        
			}
		}		
	}
	data += packet.GetProtocolEndString();					                                

	FinishMessage(header, data, data_out);
}

                                                        
                                                                         
void VerifyPacketPos(unsigned long nPos, unsigned long nClaimedPacketSize, unsigned long nNextBlockLength) {
	if( (nPos + nNextBlockLength) > nClaimedPacketSize )
		throw CrException("Cannot decode packet. Packet is smaller than it claims to be (" +
			toString(nClaimedPacketSize) + " bytes).");
}

unsigned long GetLongAndVerifyPos(const BinaryData& data, unsigned long nClaimedSize, unsigned long& nPos) {
	VerifyPacketPos(nPos, nClaimedSize, sizeof(unsigned long));
	const unsigned long nRetVal = ntohl( data.GetUL(nPos) );
	nPos += sizeof(unsigned long);
	return nRetVal;
}

string GetStringAndVerifyPos(const char* const pData, unsigned long nClaimedSize, unsigned long nClaimedStringSize, unsigned long& nPos) {
	VerifyPacketPos(nPos, nClaimedSize, nClaimedStringSize);

	boost::scoped_array<char> buf( new char[nClaimedStringSize+1] );
	memcpy(buf.get(), pData + nPos, nClaimedStringSize);
	buf[nClaimedStringSize] = 0;
	nPos += nClaimedStringSize;
	return string(buf.get());
}

void VerifyPacketEnd(const char* const pData, unsigned long nClaimedSize, unsigned long nPos) {
	KongaPacket packet;
	const string sReverseProtocolName(packet.GetProtocolEndString());
	VerifyPacketPos(nPos, nClaimedSize, sReverseProtocolName.length());
	if( memcmp(pData + nPos, sReverseProtocolName.c_str(), sReverseProtocolName.length()) != 0 )
		throw CrException("Cannot decode packet. Packet does not end with '" + sReverseProtocolName + "'.");
}

void DecodeHeader(const BinaryData& data, unsigned long& nClaimedSize, unsigned long& nPos, const string& sModuleName) {
	nPos = 0;
	unsigned long nSize = 0;
	const char* const pData = data.GetPointerToData(nSize);

	if( !pData || nSize < 13 )                                                                                
		throw CrException("Invalid command packet received.");

	                                           
	KongaPacket packet;
	const string& sProtocolName = packet.GetProtocolName();
	if( strnicmp(pData, sProtocolName.c_str(), sProtocolName.length()) != 0 ) 
		throw CrException("Cannot decode command packet: protocol must be '" + sProtocolName + "'.");

	nPos = sProtocolName.length();

	nClaimedSize = nSize;                                                   

	                           
	unsigned long lVer = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	if( lVer != packet.GetProtocolVersion() )
		throw CrException("Cannot decode command packet. I only understand version " + 
			toString(packet.GetProtocolVersion()) + " but packet version is " + toString(lVer) + ".");

	                                       
	nClaimedSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	if ( nClaimedSize != nSize )
		throw CrException("Failed to decode header. Claimed message size is " + toString(nClaimedSize)
		+ " but the actual size is " + toString(nClaimedSize) + ".");
	
	                      
	unsigned long nModuleNameSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	string sPacketModuleName = GetStringAndVerifyPos(pData, nClaimedSize, nModuleNameSize, nPos);

	if( strnicmp(sPacketModuleName.c_str(), sModuleName.c_str(), nModuleNameSize) != 0 )
		throw CrException("Cannot decode command packet: Module must be '" + sModuleName + "'.");
}

void BinaryDataToResponse(const BinaryData& data, Response& resp, const string& sModuleName) {
	unsigned long nClaimedSize, nPos;
	DecodeHeader(data, nClaimedSize, nPos, sModuleName);

	                                         
	VerifyPacketPos(nPos, nClaimedSize, 1);
	if( data[nPos] != Packet::DIRECTION_RESPONSE )
		throw CrException("Cannot decode packet. Reason: It is not a response packet.");
	++nPos;

	                                                                      
	                                      
	const unsigned long nModuleSpecificDataSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	if( nModuleSpecificDataSize != 0 )
		throw CrException("Cannot decode packet. Reason: Unexpected module specific data encountered.");

	const char* const pData = data.GetPointerToData();

	                                  
	const unsigned long nError = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	if( nError > 0 ) {
		unsigned long nErrorStringLength = GetLongAndVerifyPos(data, nClaimedSize, nPos);
		const string sError( GetStringAndVerifyPos(pData, nClaimedSize, nErrorStringLength, nPos) );
		throw RemoteException(sError, nError);
	} else {
		                   
		const unsigned long nCommands = GetLongAndVerifyPos(data, nClaimedSize, nPos);
		for(unsigned long i=0; i<nCommands; ++i) {
			                                    
			const unsigned long nCommandSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
			if( nCommandSize >= nMaxCommandSize )
				throw CrException("Cannot decode packet. Command size must be smaller than " +
					toString(nMaxCommandSize) + ", but is claimed to be " + toString(nCommandSize) + " in the packet.");

			                        
			auto_ptr<Result> pRes( new Result( GetStringAndVerifyPos(pData, nClaimedSize, nCommandSize, nPos) ) );

			                        
			const unsigned long nRetVals = GetLongAndVerifyPos(data, nClaimedSize, nPos);
			for(unsigned long nRetValIdx = 0; nRetValIdx< nRetVals; ++nRetValIdx) {
				                                    
				unsigned long nRetValSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
				if( nRetValSize >= nMaxReturnValueSize )
					throw CrException("Cannot decode packet. Return value size must be smaller than " +
						toString(nMaxReturnValueSize) + ", but is claimed to be " + toString(nRetValSize) + " in the packet.");

				VerifyPacketPos(nPos, nClaimedSize, nRetValSize);

				const boost::scoped_array<char> pRetVal( new char[nRetValSize + 1] );
				strncpy(pRetVal.get(), pData + nPos, nRetValSize);
				pRetVal[nRetValSize] = 0;
				nPos += nRetValSize;
				pRes->AddReturnValue(string(pRetVal.get()));
			}

			resp.AddResult(pRes.release());
		}
	}

	VerifyPacketEnd(pData, nClaimedSize, nPos);
}

Request* BinaryDataToRequest(const BinaryData& data, const string& sModuleName) {
	unsigned long nClaimedSize, nPos;
	DecodeHeader(data, nClaimedSize, nPos, sModuleName);

	                                        
	VerifyPacketPos(nPos, nClaimedSize, 1);
	if( data[nPos] != Packet::DIRECTION_REQUEST )
		throw CrException("Cannot decode packet. Reason: It is not a request packet.");
	++nPos;

	                                                                      
	                                      
	const unsigned long nModuleSpecificDataSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	if( nModuleSpecificDataSize != 0 )
		throw CrException("Cannot decode packet. Reason: Unexpected module specific data encountered.");

	                   
	const unsigned long nUserNameSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	if( nUserNameSize >= nMaxUserNameSize )
		throw CrException("Cannot decode command packet: User names can be at most " + 
			toString(nMaxUserNameSize) + " characters long. User name size in packet is " + 
			toString(nUserNameSize) + ".");

	const char* pData = data.GetPointerToData();

	const string sPacketUserName( GetStringAndVerifyPos(pData, nClaimedSize, nUserNameSize, nPos) );

	                   
	unsigned long nPasswordSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	if( nPasswordSize >= nMaxPasswordSize )
		throw CrException("Cannot decode command packet: Passwords can be at most " + 
			toString(nMaxPasswordSize) + " characters long. Password size in packet is " + 
			toString(nPasswordSize) + ".");

	const string sPacketPassword( GetStringAndVerifyPos(pData, nClaimedSize, nPasswordSize, nPos) );

	auto_ptr<Request>pReq( new Request(sPacketUserName, sPacketPassword) );

	                   
	const unsigned long nCommands = GetLongAndVerifyPos(data, nClaimedSize, nPos);
	for(unsigned long iCommand = 0; iCommand < nCommands; ++iCommand) {
		                                    
		const unsigned long nCommandSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
		if( nCommandSize >= nMaxCommandSize )
			throw CrException("Cannot decode packet. Command size must be smaller than " +
				toString(nMaxCommandSize) + ", but is claimed to be " + toString(nCommandSize) + " in the packet.");

		                        
		auto_ptr<Command> pCmd( new Command( GetStringAndVerifyPos(pData, nClaimedSize, nCommandSize, nPos) ) );

		                    
		const unsigned long nArgs = GetLongAndVerifyPos(data, nClaimedSize, nPos);
		for( unsigned long iArg = 0; iArg < nArgs; ++iArg ) {
			                                
			const unsigned long nArgSize = GetLongAndVerifyPos(data, nClaimedSize, nPos);
			if( nArgSize >= nMaxArgumentSize ) {
				throw CrException("Cannot decode packet. Argument size must be smaller than "
					+ toString(nMaxArgumentSize) + ", but is claimed to be "
					+ toString(nArgSize) + " for argument number " + konga::toString(iArg));
			}

			VerifyPacketPos(nPos, nClaimedSize, nArgSize);
			boost::scoped_array<char> pArg( new char[ nArgSize+1 ] );
			strncpy(pArg.get(), pData + nPos, nArgSize);
			pArg[nArgSize] = 0;
			nPos += nArgSize;

			pCmd->AddArgument(string(pArg.get()));
		}

		pReq->AddCommand(pCmd.release());
	}

	VerifyPacketEnd(pData, nClaimedSize, nPos);
	return pReq.release();
}

Response* SendRequest(const Request& req, long lCommandPort, const string& stDestinationModuleName) {
	CrStreamSocket sock;
	sock.Connect("127.0.0.1", lCommandPort);

	BinaryData reqData;
	RequestToBinaryData(req, reqData, stDestinationModuleName);
                                                          
	sock.Write<const char>(reqData.GetPointerToData(), reqData.Size());

	BinaryData respData;
	long lRead = sock.Read<BinaryData>(respData);
	if( lRead < 0 )
		throw CrException("Command sent to server, but connection to server timed out while waiting for the resonse.");
                                                             

	auto_ptr<Response> pResp( new Response() );
	BinaryDataToResponse(respData, *pResp, stDestinationModuleName);

	return pResp.release();
}

void SendCommand(Command* pCmd, const string& sUserName, const string& sPassword,
				 const string& sComponentName, long lPort) {
	std::auto_ptr<Command> pCommand( pCmd );
	if ( lPort < 0 ) {
		throw CrException("Server Configuration Error: " + sComponentName + 
			"/CommandPort is negative (" + toString(lPort) + ").");
	}

	Request request(sUserName, sPassword);
	request.AddCommand( pCommand.release() );

	std::auto_ptr<Response> pResponse;
	try {
		pResponse.reset(
			SendRequest(request, lPort, sComponentName)
			);
	}
	catch( const RemoteException& ) {
		                                                                      
		                                                                       
		throw;
	}
	catch( const CrException& e ) {
		                                                                    
		                                                  
		throw CrException("Failed to test the JMS location. The ETLForge JMS Engine is not running. " \
			"Make sure the ETLForge JMS Engine is running and try again. " \
			"This error was reported when trying to connect to the ETLForge JMS Engine:\n"
			+ e.GetErrorString());
	}

	if ( pResponse->HasExceptionOccurred() ) {
		CrException e = pResponse->GetException();
		throw e;
	}
}


}                        
}                   
