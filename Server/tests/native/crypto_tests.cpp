#include <konga/Crypto/Crypto.h>
#include <konga/Utils/CrException.h>
#include <iostream>
#include <memory>
#include <cstring>
int main() {
 try {
  const std::string fixture="WcAxFBsb9QLgzANLJ6PFlFGw9C7RQ6dh7CysSRBsznA=";
  if(konga::EncryptString("ETLForge compatibility","compatibility-pass")!=fixture)return 1;
  if(konga::DecryptString(fixture,"compatibility-pass")!="ETLForge compatibility")return 2;
  for(const std::string input : {std::string(""),std::string("ASCII and UTF-8 \xe6\x97\xa5\xe6\x9c\xac"),std::string(32768,'x')}) {
   if(konga::DecryptString(konga::EncryptString(input))!=input)return 3;
  }
  const unsigned char bytes[]={0,1,2,127,128,254,255};
  const auto encoded=konga::EncodeBase64(bytes,sizeof(bytes));int size=0;
  std::unique_ptr<unsigned char[]> decoded(konga::DecodeBase64(encoded,size));
  if(size!=sizeof(bytes)||memcmp(decoded.get(),bytes,size)!=0)return 4;
  if(konga::md5("abc")!="900150983cd24fb0d6963f7d28e17f72" || !konga::IsMD5(konga::md5("abc")))return 5;
  bool rejected=false;try{konga::DecryptString(fixture,"wrong-password");}catch(const konga::CrException&){rejected=true;}
  if(!rejected)return 6;
  std::cout << "PASS: legacy AES ciphertext fixture, UTF-8/empty/long round trips, binary Base64, MD5 and wrong-password rejection\n";
  return 0;
 } catch(const konga::CrException& error){std::cerr<<error.GetErrorString()<<"\n";return 7;}
}
