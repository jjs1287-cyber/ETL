/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_PACKET_H
#define KONGA_PACKET_H

#include <string>

#include "Packet.h"

namespace konga {
namespace networking {

class KongaPacket : public Packet {
public:
	static const unsigned long KONGA_PROTOCOL_VERSION = 1;

public:
	KongaPacket() {}
	virtual ~KongaPacket() {}

	std::string GetProtocolName() const { return "KONGA"; };
	std::string GetProtocolEndString() const { return "AGNOK"; };
	unsigned long GetProtocolVersion() const { return KONGA_PROTOCOL_VERSION; };

private:
	unsigned long m_nVersion;
	std::string m_sUserName;
	std::string m_sPassword;
};

}                   
}                        


#endif                  
