#pragma once
#include <boost/json.hpp>
#include "../security/PortableSecret.h"
#include <stdexcept>
#include <string>
#include <vector>
namespace etlforge::cloud {
inline std::string hex(const std::string& input,bool decode=false) {
 std::string output;const char* digits="0123456789abcdef";
 if(!decode){for(unsigned char c:input){output+=digits[c>>4];output+=digits[c&15];}}
 else {if(input.size()%2)throw std::runtime_error("Invalid cloud settings encoding");for(size_t i=0;i<input.size();i+=2){auto digit=[](char c)->int{if(c>='0'&&c<='9')return c-'0';if(c>='a'&&c<='f')return c-'a'+10;throw std::runtime_error("Invalid cloud settings encoding");};output+=static_cast<char>(digit(input[i])*16+digit(input[i+1]));}}
 return output;
}
inline std::string value(const boost::json::object& o,const char* key,const char* fallback="") {
 auto p=o.if_contains(key);return p && p->is_string()?std::string(p->as_string()):fallback;
}
inline std::string protect(const std::string& input,const std::string& master,bool decrypt=false) {return decrypt?security::open(input,master):security::seal(input,master);}
inline boost::json::object publicSettings(boost::json::object settings) {
 settings["hasPassword"]=!value(settings,"passwordProtected").empty();settings["hasBearerToken"]=!value(settings,"bearerProtected").empty();
 settings.erase("passwordProtected");settings.erase("bearerProtected");return settings;
}
inline void validate(const boost::json::object& s) {
 auto enabled=s.if_contains("enabled");if(!enabled || !enabled->is_bool())throw std::runtime_error("Enabled must be true or false");
 auto kind=value(s,"type");if(kind!="sftp"&&kind!="http"&&kind!="smb")throw std::runtime_error("Select SFTP, HTTP or SMB");
 if(!enabled->as_bool())return;
 if(value(s,"environment").empty())throw std::runtime_error("Environment is required");
 auto exclusions=value(s,"excludeOperationIds");if(exclusions.find_first_not_of("0123456789,")!=std::string::npos || (!exclusions.empty() && (exclusions.front()==',' || exclusions.back()==',' || exclusions.find(",,")!=std::string::npos)))throw std::runtime_error("Excluded operation IDs must be comma-separated numbers");
 if(kind=="sftp") {
  auto host=value(s,"host");if(host.empty()||host.find_first_not_of("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.-:")!=std::string::npos)throw std::runtime_error("Enter an SFTP host name or IP address");
  auto port=s.if_contains("port");if(!port||!port->is_int64()||port->as_int64()<1||port->as_int64()>65535)throw std::runtime_error("SFTP port must be 1 to 65535");
  if(value(s,"login").empty()||value(s,"passwordProtected").empty())throw std::runtime_error("SFTP login and password are required");
  if(value(s,"hostKeySha256").empty())throw std::runtime_error("Enter the verified SFTP host key SHA256 fingerprint");
  if(value(s,"path").empty()||value(s,"path")[0]!='/')throw std::runtime_error("SFTP path must start with /");
 }else if(kind=="http") {
  auto url=value(s,"url");if(url.rfind("https://",0)!=0 && url.rfind("http://",0)!=0)throw std::runtime_error("HTTP endpoint must use http:// or https://");
  if(url.find_first_of("\r\n@#")!=std::string::npos)throw std::runtime_error("Do not put credentials or fragments in the endpoint URL");
  if(value(s,"bearerProtected").find_first_of("\r\n")!=std::string::npos)throw std::runtime_error("Invalid bearer token");
 }else {auto path=value(s,"path");if(path.rfind("\\\\",0)!=0 && path.rfind("//",0)!=0)throw std::runtime_error("SMB destination must be a Windows UNC path");}
}
}
