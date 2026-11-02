/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_COMMAND_UTILS_H
#define KONGA_COMMAND_UTILS_H

#include <string>

namespace konga {

class BinaryData;

namespace networking {

class Command;
class Request;
class Response;

void RequestToBinaryData(const Request& req, BinaryData& data, const std::string& sModuleName);
void ResponseToBinaryData(const Response& resp, BinaryData& data, const std::string& sModuleName);

Request* BinaryDataToRequest(const BinaryData& data, const std::string& sModuleName);
void BinaryDataToResponse(const BinaryData& data, Response& resp, const std::string& sModuleName);

void CommandToBinaryData(const Command& cmd, BinaryData& data);

void DecodeHeader(const BinaryData& data, unsigned long& nClaimedSize, unsigned long& nPos, const std::string& sModuleName);
void VerifyPacketPos(unsigned long nPos, unsigned long nClaimedPacketSize, unsigned long nNextBlockLength);
void VerifyPacketEnd(const char* const pData, unsigned long nClaimedSize, unsigned long nPos);

unsigned long GetLongAndVerifyPos(const BinaryData& data, unsigned long nClaimedSize, unsigned long& nPos);
std::string GetStringAndVerifyPos(const char* const pData, unsigned long nClaimedSize, unsigned long nClaimedStringSize, unsigned long& nPos);
Response* SendRequest(const Request& req, long lCommandPort, const std::string& stDestinationModuleName);

void SendCommand(Command* pCmd, const std::string& sUserName, const std::string& sPassword,
				 const std::string& sComponentName, long lPort);

}                   
}                        

#endif                         
