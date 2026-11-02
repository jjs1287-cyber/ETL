#include <konga/UiBackend/WebServices/Admin/AdminWrapper.hpp>
#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Crypto/Crypto.h>
#include <konga/Utils/StringUtils.h>
#include "AuditDatabase.h"
#include <memory>
#include <map>

namespace {
const char* proxyNamespace="urn:org.etlforge.integration.server.implementation.webservice.admin.Admin";
struct ProxySettings {
 xsd__boolean enabled=xsd_boolean_false,trust=xsd_boolean_false;
 xsd__int port=0;std::string host,user,password;std::vector<std::string> exceptions;bool exceptionsSpecified=false;
 ~ProxySettings(){if(!password.empty())SecureZeroMemory(&password[0],password.size());}
};
std::string readString(IWrapperSoapDeSerializer* reader,const char* field) {
 xsd__string text=reader->getElementAsString(field,0);std::string result=text?text:"";if(text)Axis::AxisDelete(text,XSD_STRING);return result;
}
void* createProxy(ProxySettings*,bool array,int size){return array?static_cast<void*>(new ProxySettings[size]):new ProxySettings();}
void deleteProxy(ProxySettings* proxy,bool array=false,int=0){if(array)delete[] proxy;else delete proxy;}
using Exceptions=std::vector<std::string>;
void* createExceptions(Exceptions*,bool array,int size){return array?static_cast<void*>(new Exceptions[size]):new Exceptions();}
void deleteExceptions(Exceptions* values,bool array=false,int=0){if(array)delete[] values;else delete values;}
int parseExceptions(Exceptions* values,IWrapperSoapDeSerializer* reader){std::unique_ptr<Axis_Array> array(reader->getBasicArray(XSD_STRING,"item",0));if(array){int count=0;const xsd__string* entries=static_cast<xsd__string_Array*>(array.get())->get(count);for(int i=0;entries&&i<count;++i)if(entries[i]&&*entries[i])values->push_back(entries[i]);}return reader->getStatus();}
int parseProxy(ProxySettings* proxy,IWrapperSoapDeSerializer* reader) {
 xsd__boolean* enabled=reader->getElementAsBoolean("Enabled",0);if(enabled){proxy->enabled=*enabled;Axis::AxisDelete(enabled,XSD_BOOLEAN);}
 
 proxy->host=readString(reader,"Host");xsd__int* port=reader->getElementAsInt("Port",0);if(port){proxy->port=*port;Axis::AxisDelete(port,XSD_INT);}
 
 proxy->user=readString(reader,"UserName");proxy->password=readString(reader,"Password");
 
 xsd__boolean* trust=reader->getElementAsBoolean("TrustAllCertificates",0);if(trust){proxy->trust=*trust;Axis::AxisDelete(trust,XSD_BOOLEAN);}
 
 std::unique_ptr<Exceptions> exceptions(static_cast<Exceptions*>(reader->getCmplxObject((void*)parseExceptions,(void*)createExceptions,(void*)deleteExceptions,"Exceptions",proxyNamespace)));
 if(exceptions){proxy->exceptionsSpecified=true;proxy->exceptions=*exceptions;}
 
 return reader->getStatus();
}
int serializeProxy(ProxySettings* proxy,IWrapperSoapSerializer* writer,bool array=false) {
 bool fresh=false;if(!array){const char* prefix=writer->getNamespacePrefix(proxyNamespace,fresh);if(fresh)writer->serialize(" xmlns:",prefix,"=\"",proxyNamespace,"\"",NULL);}
 writer->serialize(">",NULL);
 writer->serializeAsElement("Enabled",proxyNamespace,&proxy->enabled,XSD_BOOLEAN);
 writer->serializeAsElement("Host",proxyNamespace,const_cast<char*>(proxy->host.c_str()),XSD_STRING);
 writer->serializeAsElement("Port",proxyNamespace,&proxy->port,XSD_INT);
 writer->serializeAsElement("UserName",proxyNamespace,const_cast<char*>(proxy->user.c_str()),XSD_STRING);
 writer->serializeAsElement("Password",proxyNamespace,const_cast<char*>(proxy->password.c_str()),XSD_STRING);
 writer->serializeAsElement("TrustAllCertificates",proxyNamespace,&proxy->trust,XSD_BOOLEAN);
 bool ignored=false;const char* prefix=writer->getNamespacePrefix(proxyNamespace,ignored);writer->serialize("<",prefix,":Exceptions>",NULL);
 for(const auto& item:proxy->exceptions)writer->serializeAsElement("item",proxyNamespace,const_cast<char*>(item.c_str()),XSD_STRING);
 writer->serialize("</",prefix,":Exceptions>",NULL);
 if(!array&&fresh)writer->removeNamespacePrefix(proxyNamespace);return AXIS_SUCCESS;
}
class ProxyAdministrator:public konga::WsImplBaseAuthenticator {
public:ProxyAdministrator(const std::string& user,const std::string& password):WsImplBaseAuthenticator(user.c_str(),password.c_str()){if(!m_UserInfo.IsAdministrator())throw konga::CrException("Server proxy settings require an administrator account.");}
};
std::string decodeHex(const std::string& encoded){if(encoded.empty())return "";size_t size=0;std::unique_ptr<unsigned char[]> bytes(konga::fromHexString(encoded,size));return std::string(reinterpret_cast<char*>(bytes.get()),size);}
std::string encodeHex(const std::string& text){return konga::getHexString(text.data(),text.size());}
void validate(const ProxySettings& proxy) {
 if(proxy.port<0||proxy.port>65535)throw konga::CrException("Proxy port must be between 0 (default) and 65535.");
 if(proxy.enabled==xsd_boolean_true && proxy.host.empty())throw konga::CrException("An enabled proxy requires a host name.");
 if(proxy.host.size()>255 || proxy.host.find_first_of("\r\n\t /\\@?#")!=std::string::npos)throw konga::CrException("Enter a proxy host name or IP address without a URL or credentials.");
 if(proxy.user.size()>255||proxy.password.size()>128)throw konga::CrException("Proxy login or password is too long.");
 if(proxy.user.find_first_of("\r\n")!=std::string::npos||proxy.password.find_first_of("\r\n")!=std::string::npos)throw konga::CrException("Proxy credentials must not contain newlines.");
 size_t length=0;for(const auto& item:proxy.exceptions){if(item.find_first_of("\r\n,")!=std::string::npos)throw konga::CrException("Proxy exception hosts must not contain commas or newlines.");length+=item.size()+1;}if(length>512)throw konga::CrException("Proxy exception list is too long.");
}
}

int AdminWrapper::proxySettings(void* message,bool save) {
 IMessageData* data=static_cast<IMessageData*>(message);IWrapperSoapSerializer* writer=NULL;IWrapperSoapDeSerializer* reader=NULL;data->getSoapSerializer(&writer);data->getSoapDeSerializer(&reader);if(!writer||!reader)return AXIS_FAIL;
 const char* method=save?"setProxySettings":"getProxySettings";if(reader->checkMessageBody(method,proxyNamespace)!=AXIS_SUCCESS)return AXIS_FAIL;
 writer->createSoapMethod(save?"setProxySettingsResponse":"getProxySettingsResponse",proxyNamespace);
 try {
  const auto user=readString(reader,"user"),password=readString(reader,"password");ProxyAdministrator authenticated(user,password);
  std::unique_ptr<ProxySettings> proposed;
  if(save){proposed.reset(static_cast<ProxySettings*>(reader->getCmplxObject((void*)parseProxy,(void*)createProxy,(void*)deleteProxy,"proxySettings",proxyNamespace)));if(!proposed)throw konga::CrException("Proxy settings are required.");validate(*proposed);}
  if(reader->getStatus()!=AXIS_SUCCESS)return AXIS_FAIL;
  konga::SmartDbPtr<konga::DbConnection> connection;connection.Attach(konga::GetBackEndDbConnection("TranDb"));konga::AutoRollbackTransaction transaction(connection);etlforge::audit::Database database(connection->GetConnectionHandle());
  if(save) {
   database.query("SELECT pg_advisory_xact_lock(20261028,42)");
   std::string exceptions;for(const auto& item:proposed->exceptions){if(!exceptions.empty())exceptions+=',';exceptions+=item;}
   std::map<std::string,std::string> values{{"http_proxy_usage",proposed->enabled==xsd_boolean_true?"1":"0"},{"http_proxy_host",proposed->host},{"http_proxy_port",std::to_string(proposed->port)},{"http_proxy_username",proposed->user},{"http_proxy_password",proposed->password.empty()?"":konga::EncryptString(proposed->password)},{"http_proxy_trust_all_certs",proposed->trust==xsd_boolean_true?"1":"0"},{"http_proxy_exceptions",exceptions}};
   if(!proposed->exceptionsSpecified)values.erase("http_proxy_exceptions");
   for(const auto& pair:values){if(pair.second.size()>512)throw konga::CrException("Proxy settings exceed the supported storage size.");database.query("UPDATE key_values SET key_value=convert_from(decode(?,'hex'),'UTF8'),modtime=now() WHERE key_name=?",{encodeHex(pair.second),pair.first});database.query("INSERT INTO key_values(key_name,key_value) SELECT ?,convert_from(decode(?,'hex'),'UTF8') WHERE NOT EXISTS(SELECT 1 FROM key_values WHERE key_name=?)",{pair.first,encodeHex(pair.second),pair.first});}
   transaction.Commit();return AXIS_SUCCESS;
  }
  std::map<std::string,std::string> values;for(const auto& row:database.query("SELECT key_name,encode(convert_to(COALESCE(key_value,''),'UTF8'),'hex') FROM key_values WHERE key_name LIKE 'http_proxy_%' ORDER BY key_valuesid"))values[*row[0]]=decodeHex(*row[1]);
  std::unique_ptr<ProxySettings> result(new ProxySettings());result->enabled=values["http_proxy_usage"]=="1"||values["http_proxy_usage"]=="true"?xsd_boolean_true:xsd_boolean_false;result->host=result->enabled==xsd_boolean_true?values["http_proxy_host"]:"";
  const auto& port=values["http_proxy_port"];result->port=port.empty()?0:konga::toLong(port);result->user=values["http_proxy_username"];const auto& encrypted=values["http_proxy_password"];result->password=encrypted.empty()?"":konga::DecryptString(encrypted);result->trust=values["http_proxy_trust_all_certs"]=="1"||values["http_proxy_trust_all_certs"]=="true"?xsd_boolean_true:xsd_boolean_false;
  if(!values["http_proxy_exceptions"].empty())result->exceptions=konga::SplitString(values["http_proxy_exceptions"],",");transaction.Commit();
  return writer->addOutputCmplxParam(result.release(),(void*)serializeProxy,(void*)deleteProxy,"getProxySettingsReturn",proxyNamespace);
 }catch(konga::CrException& error){konga::SetSoapFault(*writer,method,error);}catch(std::exception&){konga::SetSoapFault(*writer,method,proxyNamespace,"Proxy settings request failed.","Unable to load or save server proxy settings.");}
 throw AxisServiceException(AXISC_SERVICE_THROWN_EXCEPTION);
}
