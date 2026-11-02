#pragma once
#include <string>
namespace etlforge { namespace pgp {
struct KeyInfo {std::string fingerprint,publicKey;bool hasPrivate=false;};
KeyInfo inspect(const std::string& runtime,const std::string& protectedRoot,const std::string& keyFile);
void transform(const std::string& runtime,const std::string& protectedRoot,const std::string& keyFile,const std::string& fingerprint,const std::string& passphrase,const std::string& input,const std::string& output,bool decrypt,bool armor=false);
}}
