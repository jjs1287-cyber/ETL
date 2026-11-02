                                                                         
#include <windows.h>
#include <winhttp.h>
#include <openssl/evp.h>
#pragma comment(lib, "winhttp.lib")
#include <memory>
#include <string>
#include <vector>
#include <xercesc/parsers/XercesDOMParser.hpp>
#include <xercesc/framework/MemBufInputSource.hpp>
#include <xercesc/dom/DOM.hpp>
#include <xercesc/util/XMLString.hpp>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>
#include <konga/OperationContext/OperationContext.h>
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/DataElement/DataElement.h>
#include <konga/Crypto/Crypto.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/XmlInit.h>
using namespace konga;
namespace {
struct InternetHandle {
 HINTERNET value;
 explicit InternetHandle(HINTERNET handle):value(handle){ if(!value) throw CrException("Salesforce session bridge could not open the Java backend connection."); }
 ~InternetHandle(){WinHttpCloseHandle(value);}
};
std::wstring wide(const std::string& text){return std::wstring(text.begin(),text.end());}
std::string xmlEscape(const std::string& text){
 std::string result; for(char c:text){switch(c){case '&':result+="&amp;";break;case '<':result+="&lt;";break;case '>':result+="&gt;";break;case '"':result+="&quot;";break;case '\'':result+="&apos;";break;default:result+=c;}}
 return result;
}
std::string post(const std::string& endpoint,const std::string& body){
 std::wstring url=wide(endpoint); URL_COMPONENTS parts={};parts.dwStructSize=sizeof(parts);parts.dwHostNameLength=(DWORD)-1;parts.dwUrlPathLength=(DWORD)-1;
 if(!WinHttpCrackUrl(url.c_str(),0,0,&parts))throw CrException("Invalid Java backend endpoint for Salesforce.");
 InternetHandle session(WinHttpOpen(L"ETLForge/42",WINHTTP_ACCESS_TYPE_NO_PROXY,WINHTTP_NO_PROXY_NAME,WINHTTP_NO_PROXY_BYPASS,0));
 WinHttpSetTimeouts(session.value,15000,15000,15000,60000);
 std::wstring host(parts.lpszHostName,parts.dwHostNameLength),path(parts.lpszUrlPath,parts.dwUrlPathLength);
 InternetHandle connection(WinHttpConnect(session.value,host.c_str(),parts.nPort,0));
 InternetHandle request(WinHttpOpenRequest(connection.value,L"POST",path.c_str(),NULL,WINHTTP_NO_REFERER,WINHTTP_DEFAULT_ACCEPT_TYPES,parts.nScheme==INTERNET_SCHEME_HTTPS?WINHTTP_FLAG_SECURE:0));
 DWORD policy=WINHTTP_OPTION_REDIRECT_POLICY_NEVER;WinHttpSetOption(request.value,WINHTTP_OPTION_REDIRECT_POLICY,&policy,sizeof(policy));
 const wchar_t* headers=L"Content-Type: text/xml; charset=UTF-8\r\nSOAPAction: \"loginToSalesforce\"\r\n";
 if(!WinHttpSendRequest(request.value,headers,(DWORD)-1L,(void*)body.data(),(DWORD)body.size(),(DWORD)body.size(),0)||!WinHttpReceiveResponse(request.value,NULL))throw CrException("Salesforce session bridge request failed. Check the Java backend service.");
 DWORD status=0,length=sizeof(status);WinHttpQueryHeaders(request.value,WINHTTP_QUERY_STATUS_CODE|WINHTTP_QUERY_FLAG_NUMBER,WINHTTP_HEADER_NAME_BY_INDEX,&status,&length,WINHTTP_NO_HEADER_INDEX);
 if(status!=200)throw CrException("Salesforce session bridge returned a backend HTTP error.");
 std::string result;char buffer[4096];DWORD count;
 do{if(!WinHttpReadData(request.value,buffer,sizeof(buffer),&count))throw CrException("Cannot read Salesforce session bridge response.");if(result.size()+count>65536)throw CrException("Salesforce session bridge response is too large.");result.append(buffer,count);}while(count);
 return result;
}
std::string field(xercesc::DOMDocument* document,const char* name){
 XMLCh* local=xercesc::XMLString::transcode(name);XMLCh* any=xercesc::XMLString::transcode("*");
 xercesc::DOMNodeList* nodes=document->getElementsByTagNameNS(any,local);
 xercesc::XMLString::release(&local);xercesc::XMLString::release(&any);
 if(nodes->getLength()!=1)return "";
 char* content=xercesc::XMLString::transcode(nodes->item(0)->getTextContent());std::string result(content);xercesc::XMLString::release(&content);return result;
}
void setVariable(OperationDataElementHolder& holder,const std::string& key,const std::string& value){DataElement* element=holder.getOperationDataElement(key);element->setType(DataElement::t_string);element->setValue(value.c_str());}
}
namespace konga {
                                                                                      
std::string SalesforceStudioCredential(const std::string& value,bool encrypt){
 const std::string password="ybd?)5!j}X3^Ki${>Dq}Sz$/Le50c[";
 const unsigned char salt[]={'!',';','.','\n','\t',' ',';',':'};
 unsigned char key[EVP_MAX_KEY_LENGTH],iv[EVP_MAX_IV_LENGTH];
 if(EVP_BytesToKey(EVP_aes_128_cbc(),EVP_md5(),salt,(const unsigned char*)password.data(),(int)password.size(),1,key,iv)!=16)throw CrException("Studio credential key derivation failed.");
 std::unique_ptr<EVP_CIPHER_CTX,void(*)(EVP_CIPHER_CTX*)> cipher(EVP_CIPHER_CTX_new(),EVP_CIPHER_CTX_free);
 if(!cipher||EVP_CipherInit_ex(cipher.get(),EVP_aes_128_cbc(),NULL,key,iv,encrypt?1:0)!=1)throw CrException("Studio credential codec initialization failed.");
 int length=(int)value.size();std::unique_ptr<unsigned char[]> decoded;
 const unsigned char* input=(const unsigned char*)value.data();
 if(!encrypt){decoded.reset(DecodeBase64(value,length));input=decoded.get();}
 std::vector<unsigned char> output(length+EVP_MAX_BLOCK_LENGTH);int first=0,last=0;
 if(EVP_CipherUpdate(cipher.get(),output.data(),&first,input,length)!=1||EVP_CipherFinal_ex(cipher.get(),output.data()+first,&last)!=1)throw CrException("Studio credential codec failed.");
 return encrypt?EncodeBase64(output.data(),first+last):std::string((const char*)output.data(),first+last);
}
void PrepareSalesforceOperationSession(long operationId,DbConnection& database,OperationDataElementHolder& holder,const UserInfo& currentUser){
 const char* names[]={"salesforcequery","salesforceupsert","salesforcecreate","salesforceupdate","salesforcedelete","salesforcebulkload"};
 std::string select;
 for(const char* name:names){std::string prefix(name);if(!select.empty())select+=" UNION ";select+="SELECT p.keyvalue AS guid FROM "+prefix+"propertiestab p JOIN "+prefix+(prefix=="salesforcequery"?"iestab":"stab")+" t ON p."+prefix+"_id=t."+prefix+"_id WHERE t.operation_id="+toString(operationId)+" AND lower(p.keyname)='connector_guid'";}
                                         
 const std::string bad="salesforcequeryiestab";size_t badPos=select.find(bad);if(badPos!=std::string::npos)select.replace(badPos,bad.size(),"salesforcequeriestab");
 select="SELECT g.item_id,p.guid FROM ("+select+") p JOIN GuidsForSalesforceConnectorsTab g ON lower(g.guid)=lower(p.guid)";
 SmartDbPtr<DynamicDbRecordSet> rows(DynamicDbRecordSet::MakeInstance(&database,select));rows->Open();
 if(!rows->Next())return;
 const std::string id=rows->GetFieldValue(0),guid=rows->GetFieldValue(1);if(rows->Next())throw CrException("An operation references multiple Salesforce connectors.");rows.CloseAndDelete();
 std::string user,password;GetCredentials(database,currentUser,user,password);
 std::string overrides;
 SmartDbPtr<DynamicDbRecordSet> settings(DynamicDbRecordSet::MakeInstance(&database,"SELECT auth_type,instance_url,client_id,client_secret,api_version,server_host FROM SalesforceConnectorsTab WHERE salesforceconnector_id="+id)); settings->Open();
 if(settings->Next() && settings->GetFieldValue(0)=="oauth") {
   const std::string instance=holder.replaceOperationDataElementInString(settings->GetFieldValue(1));
   const std::string client=holder.replaceOperationDataElementInString(settings->GetFieldValue(2));
   const std::string secret=holder.replaceOperationDataElementInString(SalesforceStudioCredential(settings->GetFieldValue(3),false));
   overrides="<AuthType>oauth</AuthType><InstanceUrl>"+xmlEscape(instance)+"</InstanceUrl><ClientId>"+xmlEscape(client)+"</ClientId><ClientSecret>"+xmlEscape(SalesforceStudioCredential(secret,true))+"</ClientSecret><Version>"+xmlEscape(settings->GetFieldValue(4))+"</Version><LoginUrl>"+xmlEscape(settings->GetFieldValue(5))+"</LoginUrl>";
 }
 settings.CloseAndDelete();
 const std::string ns="http://salesforce.webservice.backend.server.integration.etlforge.invalid";
 const std::string body="<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\"><s:Body><loginToSalesforce xmlns=\""+ns+"\"><etlforgeUser>"+xmlEscape(user)+"</etlforgeUser><etlforgePasswordMD5>"+xmlEscape(password)+"</etlforgePasswordMD5><sfdcLoginParameters><SalesforceConnectorId>"+xmlEscape(id)+"</SalesforceConnectorId>"+overrides+"</sfdcLoginParameters></loginToSalesforce></s:Body></s:Envelope>";
 const std::string response=post(GetEndpointUrlForService("konga_salesforce_engine"),body);
 XmlInitializer::InitXerces(); xercesc::XercesDOMParser parser;parser.setDoNamespaces(true);parser.setLoadExternalDTD(false);parser.setDisableDefaultEntityResolution(true);parser.setCreateEntityReferenceNodes(false);
 xercesc::MemBufInputSource input((const XMLByte*)response.data(),response.size(),"salesforce-session",false);
 try{parser.parse(input);}catch(...){throw CrException("Invalid XML returned by the Salesforce session bridge.");}
 if(parser.getErrorCount()!=0||!parser.getDocument()||parser.getDocument()->getDoctype())throw CrException("Invalid Salesforce session response.");
 const std::string token=field(parser.getDocument(),"SessionId"),endpoint=field(parser.getDocument(),"ServiceEndpoint");
 if(token.empty()||endpoint.empty())throw CrException("Salesforce authentication failed. Check connector app credentials and Run As permissions.");
 setVariable(holder,"__Salesforce.SessionID."+guid,token);setVariable(holder,"__Salesforce.SessionID",token);setVariable(holder,"__Salesforce.ServiceURL",endpoint);
 setVariable(holder,"salesforce.sessionID",token);setVariable(holder,"salesforce.serverURL",endpoint);
}
}