/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_CRYPTO_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_CRYPTO_H

#include <vector>

#include "SpecialHandlingDeploy.h"

namespace konga {
namespace interchange_deploy {

   
                                                                         
    
class Crypto : public SpecialHandlingDeploy {
public:
	typedef std::vector<int> ColPosT;

	                                                   
	Crypto(unsigned int pos) { m_vColPosToEncrypt.push_back(pos); }

	                                                         
	Crypto(const ColPosT& vColPosToEncrypt) : m_vColPosToEncrypt(vColPosToEncrypt) {}

	virtual ~Crypto() {};

	                                                              
	virtual bool HandleRow(InterchangeDataRowStruct& row);

private:
	ColPosT m_vColPosToEncrypt;
};

class CryptoInsert : public Crypto {
public:
	CryptoInsert(unsigned int pos) : Crypto(pos) {}
	CryptoInsert(const ColPosT& vColPosToEncrypt) : Crypto(vColPosToEncrypt) {}
};

class CryptoUpdate : public Crypto {
public:
	CryptoUpdate(unsigned int pos) : Crypto(pos) {}
	CryptoUpdate(const ColPosT& vColPosToEncrypt) : Crypto(vColPosToEncrypt) {}

};

}                   
}                                

#endif                                             
