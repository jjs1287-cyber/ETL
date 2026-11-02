/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_CRYPTO_H
#define KONGA_CRYPTO_H

#include <string>

namespace konga {

	std::string EncryptString(const std::string& stPlainText, const std::string& stPassphrase = std::string());
	std::string DecryptString(const std::string& stSipherText, const std::string& stPassphrase = std::string());

	std::string EncodeBase64(const unsigned char* pcToEncode, int len);

	                                                                                           
	                                                    
	unsigned char* DecodeBase64(const std::string& stToDecode, int& len);
	unsigned char* DecodeBase64(const char* pchToDecode, int& len);

	std::string md5(const char* s, unsigned long length);
	std::string md5(const std::string& s);

	void md5(const std::string& s, long long& low, long long& high);

	                                                               
	                                 
	bool IsMD5(const std::string& md5);

	                                                                       
	void initCrypto();

}                   


#endif                  

