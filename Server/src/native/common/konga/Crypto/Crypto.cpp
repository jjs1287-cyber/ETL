/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <string>
#include <cassert>
#include <memory>
#include <vector>
#include <limits>

#include <openssl/err.h>
#include <openssl/evp.h>
#include <openssl/md5.h>



#include <konga/Utils/util.h>
#include <konga/Utils/AutoPtrCleanup.h>	                    
#include <konga/Utils/CrException.h>

#include "Crypto.h"

using namespace konga;
using namespace std;

namespace {

	const unsigned int ERR_BUF_SIZE = 4096;

	string GetOpenSslErrorString(unsigned long lErr = ERR_get_error()) {
		ERR_load_crypto_strings();
		char buf[ERR_BUF_SIZE];
		ERR_error_string_n(lErr, buf, ERR_BUF_SIZE-1);
		return buf;
	}

    using CipherContext = std::unique_ptr<EVP_CIPHER_CTX, decltype(&EVP_CIPHER_CTX_free)>;
    using EncodeContext = std::unique_ptr<EVP_ENCODE_CTX, decltype(&EVP_ENCODE_CTX_free)>;
}

void konga::initCrypto() {
                                                                           
    if (OPENSSL_init_crypto(0, nullptr) != 1)
        throw CrException("Cryptography initialization failed: " + GetOpenSslErrorString());
}
                                                                                 
                                                                    
                                                            
string konga::EncryptString(const string& stPlainText, const string& stPassphraseIn                 ) {
	if( stPlainText.empty() ) return stPlainText;
    if (stPlainText.size() > static_cast<size_t>((std::numeric_limits<int>::max)() - EVP_MAX_BLOCK_LENGTH)) throw CrException("Encryption input is too large");
	initCrypto();

	const string stPassphrase( stPassphraseIn.empty() ? "this will have to do for now" : stPassphraseIn );

	unsigned char key[EVP_MAX_KEY_LENGTH], iv[EVP_MAX_IV_LENGTH];

	CipherContext ctx(EVP_CIPHER_CTX_new(), EVP_CIPHER_CTX_free);
    if (!ctx) throw CrException("Cannot allocate cipher context");

	if (EVP_BytesToKey(EVP_aes_128_cbc(), EVP_md5(), reinterpret_cast<const unsigned char*>("(..\n\t \n:"), reinterpret_cast<const unsigned char*>(stPassphrase.c_str()), stPassphrase.length(), 1, key, iv) != 16) throw CrException("Credential key derivation failed: " + GetOpenSslErrorString());
	if (EVP_EncryptInit_ex(ctx.get(), EVP_aes_128_cbc(), NULL, key, iv) != 1) throw CrException("Cipher initialization failed: " + GetOpenSslErrorString());

	std::vector<unsigned char> output(stPlainText.size() + EVP_MAX_BLOCK_LENGTH);
    unsigned char* outbuf = output.data();
	int outlen;
	if(!EVP_EncryptUpdate(ctx.get(), outbuf, &outlen, reinterpret_cast<const unsigned char*>(stPlainText.c_str()), stPlainText.length()))
		throw CrException("Encryption failed. Reason: " + GetOpenSslErrorString());

	                                                                                                 
	int tmplen;
	if(!EVP_EncryptFinal_ex(ctx.get(), outbuf + outlen, &tmplen))
		throw CrException("Encryption failed.  Reason: " + GetOpenSslErrorString());
	outlen += tmplen;

    return EncodeBase64(outbuf, outlen);
}

                                                                                                
                                                               
string konga::DecryptString(const string& stSipherText, const string& stPassphraseIn                 ) {
	if( stSipherText.empty() ) return stSipherText;
	initCrypto();

	int inlen = 0;
	unsigned char* pcToDecrypt = DecodeBase64(stSipherText, inlen);
	AutoArrayDelete<unsigned char> adopt_pcToDecrypt(pcToDecrypt);

	const string stPassphrase( stPassphraseIn.empty() ? "this will have to do for now" : stPassphraseIn );

	unsigned char key[EVP_MAX_KEY_LENGTH], iv[EVP_MAX_IV_LENGTH];

                                                                
	CipherContext ctx(EVP_CIPHER_CTX_new(), EVP_CIPHER_CTX_free);
    if (!ctx) throw CrException("Cannot allocate cipher context");

	if (EVP_BytesToKey(EVP_aes_128_cbc(), EVP_md5(), reinterpret_cast<const unsigned char*>("(..\n\t \n:"), reinterpret_cast<const unsigned char*>(stPassphrase.c_str()), stPassphrase.length(), 1, key, iv) != 16) throw CrException("Credential key derivation failed: " + GetOpenSslErrorString());
    if (EVP_CipherInit_ex(ctx.get(), EVP_aes_128_cbc(), NULL, NULL, NULL, 0) != 1) throw CrException("Cipher initialization failed: " + GetOpenSslErrorString());
                                                    

                                                                    
    if (EVP_CipherInit_ex(ctx.get(), NULL, NULL, key, iv, 0) != 1) throw CrException("Cipher initialization failed: " + GetOpenSslErrorString());

    std::vector<unsigned char> output(inlen + EVP_MAX_BLOCK_LENGTH + 1);
    unsigned char* outbuf = output.data();
    int outlen = 0;
	if(!EVP_CipherUpdate(ctx.get(), outbuf, &outlen, pcToDecrypt, inlen))
		throw CrException("Decryption failed. Reason: " + GetOpenSslErrorString());

	int tmplen = 0;
	if(!EVP_CipherFinal_ex(ctx.get(), outbuf + outlen, &tmplen))
		throw CrException("Decryption failed.  Reason: " + GetOpenSslErrorString());
	outlen += tmplen;
	outbuf[outlen] = 0;

	return reinterpret_cast<char*>(outbuf);
}

namespace {
	void md5_impl(unsigned char* md, const char* s, unsigned long length) {
		memset(md, 0, MD5_DIGEST_LENGTH);
		initCrypto();
		unsigned int written = 0;
        if (EVP_Digest(s, length, md, &written, EVP_md5(), nullptr) != 1 || written != MD5_DIGEST_LENGTH)
            throw CrException("MD5 digest failed: " + GetOpenSslErrorString());
	}
}

                                                                
string konga::md5(const char* s, unsigned long length) {
	unsigned char md[MD5_DIGEST_LENGTH];
	md5_impl(md, s, length);

	char buf[10];
	string sRet;
	sRet.reserve(MD5_DIGEST_LENGTH*2);
	for(int i=0; i<MD5_DIGEST_LENGTH; i++) {
		sprintf(buf, "%02x", md[i]);
		sRet += buf;
	}
	return sRet;
}

void konga::md5(const std::string& s, long long& low, long long& high) {
	unsigned char md[MD5_DIGEST_LENGTH];
	md5_impl(md, s.c_str(), s.size());

	memcpy(&high, md, sizeof(high));
	const int offset = MD5_DIGEST_LENGTH/2;
	memcpy(&low, md + offset, sizeof(low));
}

string konga::md5(const string& s) {
	return md5(s.c_str(), s.length());
}

namespace {
	const string VALID_MD5_CHARS_LOWER("0123456789abcdef");
}

bool konga::IsMD5(const string& md5_in) {
	if ( md5_in.size() != 32 )
		return false;

	string md5Lower(md5_in);
	ToLower(md5Lower);

	for( string::const_iterator cit = md5Lower.begin(); cit != md5Lower.end(); ++cit ) {
		if ( VALID_MD5_CHARS_LOWER.find(*cit) == string::npos )
			return false;
	}

	return true;
}

string konga::EncodeBase64(const unsigned char* pcToEncode, int len) {
	if ( pcToEncode == 0 || len < 1 ) return "";

	if (len > (std::numeric_limits<int>::max)() / 2) throw CrException("Base64 input is too large");
    size_t bufSize = (static_cast<size_t>(len)*4)/3 + 10;
	                                                                              
	bufSize += static_cast<int>(2*bufSize/60);

	unsigned char* buf = new unsigned char[bufSize];
	AutoArrayDelete<unsigned char> adopt_buf(buf);

	EncodeContext ctx(EVP_ENCODE_CTX_new(), EVP_ENCODE_CTX_free);
    if (!ctx) throw CrException("Cannot allocate Base64 context");
	EVP_EncodeInit(ctx.get());

	int nEncodedLength = 0;
	                                                                                                               
	EVP_EncodeUpdate(ctx.get(), buf, &nEncodedLength, const_cast<unsigned char*>(pcToEncode), len);
	len = nEncodedLength;

	EVP_EncodeFinal(ctx.get(), buf + nEncodedLength, &nEncodedLength);
	len += nEncodedLength;

	if ( len > bufSize - 1 )
		throw CrException("This is a bug: Not enough space was allocated in encode Base64");
	buf[len-1] = 0;
	return string( reinterpret_cast<char*>(buf) );
}

unsigned char* konga::DecodeBase64(const char* pchToDecode, int& len) {
	assert(pchToDecode);
	if ( pchToDecode == 0 ) {
		len = 0;
		return 0;
	}

	const size_t strLen = strlen(pchToDecode);
	unsigned char* buf = new unsigned char[ strLen + 1 ];
	AutoArrayDelete<unsigned char> adopt_buf(buf);

	EncodeContext ctx(EVP_ENCODE_CTX_new(), EVP_ENCODE_CTX_free);
    if (!ctx) throw CrException("Cannot allocate Base64 context");
	EVP_DecodeInit(ctx.get());

	int iRet = EVP_DecodeUpdate(ctx.get(), buf, &len, 
		reinterpret_cast<unsigned char*>( const_cast<char*>(pchToDecode)), strLen);
	if( iRet < 0 )
		throw CrException("Base64 decoding failed. Reason: " + GetOpenSslErrorString());
	int iOutLen = len;

	if (EVP_DecodeFinal(ctx.get(), buf + iOutLen, &len) < 0) throw CrException("Base64 decoding failed: " + GetOpenSslErrorString()); 
	iOutLen += len;

	unsigned char* pcDecoded = new unsigned char[ iOutLen ];
	memcpy(pcDecoded, buf, iOutLen);

	len = iOutLen;
	return pcDecoded;
}

unsigned char* konga::DecodeBase64(const string& stToDecode, int& len) {
	return DecodeBase64(stToDecode.c_str(), len);
}

