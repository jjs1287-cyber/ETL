/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifdef _DEBUG
	#include <konga/Utils/Settings.h>
#endif

#include <konga/Crypto/Crypto.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include "DataLocation.h"
#include "OperationEngineException.h"
#include <openssl/evp.h>
#include <memory>
#include <vector>
#include <windows.h>

using namespace konga;
using std::string;

DataLocation::DataLocation(void) : m_pDataElementHolder(0) { }

long DataLocation::getId() const {
	return m_CommonData.m_id;
}

string DataLocation::getName() const {
	return m_CommonData.m_stName;
}

string DataLocation::getEncryptedPassword() const {
	return m_CommonData.m_stPassword;
}

string DataLocation::getDecryptedPassword() const {
#ifdef _DEBUG
		const string stOveridePwd( konga::GetOptionAsString("Debug", "DataLocationPwd", "" ) );
		if ( !stOveridePwd.empty() ) {
			return stOveridePwd;
		}
#endif

	
	const string& stEncryptedPwd = getEncryptedPassword();
	if ( stEncryptedPwd.empty() )
		return stEncryptedPwd;

	const string& stDecryptedPwd = DecryptString(stEncryptedPwd);
	if ( stDecryptedPwd.empty() )
		return stDecryptedPwd;

	return replace_de(stDecryptedPwd);
}

string DataLocation::replace_de(const std::string& str) const {
	if ( m_pDataElementHolder )
		return m_pDataElementHolder->replaceOperationDataElementInString(str);
	return str;
}

         
int DataLocation::getPercentDone(int iFile, int iFileMax) {
	double d = iFile - 1;
	d /= iFileMax;
	d *= 100;
	return (int)d;
}


std::string DataLocation::compressionPassword() const {
 const std::string secret=m_extendedProperties.GetString("compression_password", "");
 if(secret.empty())return secret;
 try {
  initCrypto();int size=0;std::unique_ptr<unsigned char[]> encrypted(DecodeBase64(secret,size));
  if(size<=0||size>65536)throw OperationEngineException("Invalid protected archive password");
  const std::string passphrase="ybd?)5!j}X3^Ki${>Dq}Sz$/Le50c[";
  const unsigned char salt[]={33,59,46,10,9,32,59,58};unsigned char key[EVP_MAX_KEY_LENGTH],iv[EVP_MAX_IV_LENGTH];
  if(EVP_BytesToKey(EVP_aes_128_cbc(),EVP_md5(),salt,reinterpret_cast<const unsigned char*>(passphrase.data()),static_cast<int>(passphrase.size()),1,key,iv)!=16)throw OperationEngineException("Cannot decode protected archive password");
  std::unique_ptr<EVP_CIPHER_CTX,decltype(&EVP_CIPHER_CTX_free)> cipher(EVP_CIPHER_CTX_new(),EVP_CIPHER_CTX_free);
  std::vector<unsigned char> clear(size+EVP_MAX_BLOCK_LENGTH);int count=0,last=0;
  bool valid=cipher&&EVP_DecryptInit_ex(cipher.get(),EVP_aes_128_cbc(),nullptr,key,iv)==1&&EVP_DecryptUpdate(cipher.get(),clear.data(),&count,encrypted.get(),size)==1&&EVP_DecryptFinal_ex(cipher.get(),clear.data()+count,&last)==1;
  SecureZeroMemory(key,sizeof(key));SecureZeroMemory(iv,sizeof(iv));
  if(!valid){SecureZeroMemory(clear.data(),clear.size());throw OperationEngineException("Cannot decode protected archive password; re-enter it in the endpoint settings");}
  std::string result(reinterpret_cast<const char*>(clear.data()),count+last);SecureZeroMemory(clear.data(),clear.size());
  if(result.find('\0')!=std::string::npos)throw OperationEngineException("Archive password contains a NUL character");
  return replace_de(result);
 }catch(const CrException& error){throw OperationEngineException("Cannot decode protected archive password");}
}
