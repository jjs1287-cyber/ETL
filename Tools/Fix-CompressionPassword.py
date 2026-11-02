from pathlib import Path
p=Path('C:/ETL/Server/src/native/integration/konga/OperationObjects/DataLocation.cpp');s=p.read_text().replace('#include "DataLocation.h"','#include "DataLocation.h"\n#include "OperationEngineException.h"\n#include <openssl/evp.h>\n#include <memory>\n#include <vector>\n#include <windows.h>');start=s.index('std::string DataLocation::compressionPassword() const {');s=s[:start]+'''
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
  if(result.find('\\0')!=std::string::npos)throw OperationEngineException("Archive password contains a NUL character");
  return replace_de(result);
 }catch(const CrException& error){throw OperationEngineException("Cannot decode protected archive password");}
}
''';p.write_text(s,encoding='utf-8')
