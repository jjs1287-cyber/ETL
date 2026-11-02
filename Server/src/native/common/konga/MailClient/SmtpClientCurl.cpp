/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 * ETLForge: replace the UW c-client transport with libcurl.
 */
#include "SmtpClient.h"
#include "rfc822Date.h"
#include <konga/Utils/CrException.h>
#include <konga/Utils/util.h>
#include <curl/curl.h>
#include <algorithm>
#include <cstring>
#include <limits>
#include <memory>
#include <sstream>

namespace {
void checkHeader(const std::string& value) {
    if(value.find_first_of("\r\n")!=std::string::npos || value.find('\0')!=std::string::npos)
        throw konga::CrException("Email header contains a prohibited line break or NUL.");
}
std::string base64(const std::string& value) {
    static const char alphabet[]="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    std::string result;
    for(size_t i=0;i<value.size();i+=3) {
        unsigned a=static_cast<unsigned char>(value[i]);
        unsigned b=i+1<value.size()?static_cast<unsigned char>(value[i+1]):0;
        unsigned c=i+2<value.size()?static_cast<unsigned char>(value[i+2]):0;
        result+=alphabet[a>>2];result+=alphabet[((a&3)<<4)|(b>>4)];
        result+=i+1<value.size()?alphabet[((b&15)<<2)|(c>>6)]:'=';
        result+=i+2<value.size()?alphabet[c&63]:'=';
    }
    return result;
}
std::string headerText(const std::string& text) {
    checkHeader(text);
    if(text.size()>70 || std::any_of(text.begin(),text.end(),[](unsigned char c){return c>=128;})){
        std::string result;
        for(size_t start=0;start<text.size();){
            size_t end=std::min(start+45,text.size());
            while(end<text.size() && (static_cast<unsigned char>(text[end])&0xc0)==0x80)--end;
            if(end==start)throw konga::CrException("Invalid UTF-8 email header.");
            if(!result.empty())result+="\r\n ";
            result+="=?UTF-8?B?"+base64(text.substr(start,end-start))+"?=";start=end;
        }
        return result;
    }
    return text;
}
std::string address(const konga::EmailAddress& value) {
    checkHeader(value.getEmailAddress());
    if(value.getPersonalName().empty())return value.getMailBoxAtHost();
    const std::string personal=headerText(value.getPersonalName());
    if(personal.compare(0,2,"=?")==0)return personal+" <"+value.getMailBoxAtHost()+">";
    std::string quoted;
    for(char c:personal){if(c=='"' || c=='\\')quoted+='\\';quoted+=c;}
    return "\""+quoted+"\" <"+value.getMailBoxAtHost()+">";
}
void addAddresses(std::ostringstream& message,const char* name,const konga::EmailProperties::vAddrT& values) {
    if(values.empty())return;
    message<<name<<": ";bool first=true;
    for(const auto& item:values){if(!first)message<<", ";message<<address(item);first=false;}
    message<<"\r\n";
}
struct Upload {
    const std::string& text;size_t position=0;bool started=false;
    explicit Upload(const std::string& value):text(value){}
};
size_t readPayload(char* buffer,size_t size,size_t count,void* context) {
    if(size && count>std::numeric_limits<size_t>::max()/size)return CURL_READFUNC_ABORT;
    Upload& upload=*static_cast<Upload*>(context);
    const size_t length=std::min(size*count,upload.text.size()-upload.position);
    if(length){std::memcpy(buffer,upload.text.data()+upload.position,length);upload.position+=length;upload.started=true;}
    return length;
}
template<class T> void option(CURL* handle,CURLoption key,T value) {
    const CURLcode result=curl_easy_setopt(handle,key,value);
    if(result!=CURLE_OK)throw konga::CrException(std::string("SMTP configuration failed: ")+curl_easy_strerror(result));
}
struct Recipients {
    curl_slist* values=nullptr;
    ~Recipients(){curl_slist_free_all(values);}
    void add(const konga::EmailProperties::vAddrT& addresses){
        for(const auto& item:addresses){
            checkHeader(item.getMailBoxAtHost());
            curl_slist* next=curl_slist_append(values,item.getMailBoxAtHost().c_str());
            if(!next)throw std::bad_alloc();values=next;
        }
    }
};
struct Endpoint {std::string url;long tls=CURLUSESSL_TRY;};
Endpoint endpoint(std::string host,bool authenticated) {
    checkHeader(host);
    if(host.find_first_of("@?#")!=std::string::npos)throw konga::CrException("SMTP server must not contain URL credentials, queries or fragments.");
    Endpoint result;result.tls=authenticated?CURLUSESSL_ALL:CURLUSESSL_TRY;
    if(host.compare(0,7,"smtp://")==0 || host.compare(0,8,"smtps://")==0){
        const size_t start=host.find("://")+3;const size_t slash=host.find('/',start);
        if(slash!=std::string::npos && slash!=host.size()-1)throw konga::CrException("SMTP server URL must not contain a path.");
        result.url=host;return result;
    }
    const size_t slash=host.find('/');
    if(slash!=std::string::npos){
        const std::string mode=host.substr(slash);host.erase(slash);
        if(mode=="/ssl"){result.url="smtps://"+host;return result;}
        if(mode=="/tls")result.tls=CURLUSESSL_ALL;
        else if(mode=="/notls" && !authenticated)result.tls=CURLUSESSL_NONE;
        else throw konga::CrException("Unsupported SMTP option; use /ssl or /tls. Certificate verification remains enabled.");
    }
    if(host.empty() || host.find("://")!=std::string::npos)throw konga::CrException("Invalid SMTP server.");
    result.url="smtp://"+host;return result;
}
bool connectionFailure(CURLcode result) {
    return result==CURLE_COULDNT_CONNECT || result==CURLE_COULDNT_RESOLVE_HOST || result==CURLE_OPERATION_TIMEDOUT ||
        result==CURLE_SSL_CONNECT_ERROR || result==CURLE_PEER_FAILED_VERIFICATION || result==CURLE_USE_SSL_FAILED || result==CURLE_LOGIN_DENIED;
}
}
namespace konga {
void SmtpClient::addSmtpServer(const std::string& host){
    if(!host.empty() && std::find(m_vSmtpServers.begin(),m_vSmtpServers.end(),host)==m_vSmtpServers.end())m_vSmtpServers.push_back(host);
}
void SmtpClient::addSmtpServers(const std::vector<std::string>& hosts){for(const auto& host:hosts)addSmtpServer(host);}
void SmtpClient::sendMessage(const EmailProperties& props,const std::string& subject,const std::string& body){
    if(m_vSmtpServers.empty())throw CrException("No SMTP servers were specified.");
    if(props.getFromByRef().empty())throw CrException("From address is empty.");
    if(props.getToAddressesByRef().empty())throw CrException("No message recipient was specified.");
    static const CURLcode initialized=curl_global_init(CURL_GLOBAL_DEFAULT);
    if(initialized!=CURLE_OK)throw CrException("SMTP runtime initialization failed.");
    std::ostringstream message;
    message<<"From: "<<address(props.getFromByRef())<<"\r\n";
    addAddresses(message,"To",props.getToAddressesByRef());addAddresses(message,"Cc",props.getCCAddressesByRef());
    if(!props.getReplyToByRef().empty())message<<"Reply-To: "<<address(props.getReplyToByRef())<<"\r\n";
    message<<"Subject: "<<headerText(subject)<<"\r\nDate: "<<rfc822Date()<<"\r\nMessage-ID: <"<<GetGUIDString()<<"@etlforge.invalid>\r\n";
    message<<"MIME-Version: 1.0\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Transfer-Encoding: base64\r\n\r\n";
    const std::string encoded=base64(body);
    for(size_t i=0;i<encoded.size();i+=76)message<<encoded.substr(i,76)<<"\r\n";
    const std::string payload=message.str();
    if(payload.size()>static_cast<size_t>(std::numeric_limits<curl_off_t>::max()))throw CrException("Email message is too large.");
    Recipients recipients;recipients.add(props.getToAddressesByRef());recipients.add(props.getCCAddressesByRef());recipients.add(props.getBCCAddressesByRef());
    const std::string sender=(props.getReturnPathByRef().empty()?props.getFromByRef():props.getReturnPathByRef()).getMailBoxAtHost();checkHeader(sender);
    CURLcode last=CURLE_COULDNT_CONNECT;
    for(const auto& host:m_vSmtpServers){
        const Endpoint server=endpoint(host,!m_sAccount.empty());
        std::unique_ptr<CURL,decltype(&curl_easy_cleanup)> handle(curl_easy_init(),&curl_easy_cleanup);
        if(!handle)throw CrException("SMTP handle allocation failed.");
        Upload upload(payload);CURL* curl=handle.get();
        option(curl,CURLOPT_URL,server.url.c_str());option(curl,CURLOPT_PROTOCOLS_STR,"smtp,smtps");option(curl,CURLOPT_PROXY,"");
        option(curl,CURLOPT_USE_SSL,server.tls);option(curl,CURLOPT_SSL_VERIFYPEER,1L);option(curl,CURLOPT_SSL_VERIFYHOST,2L);
        option(curl,CURLOPT_SSL_OPTIONS,static_cast<long>(CURLSSLOPT_NATIVE_CA));
        option(curl,CURLOPT_NOSIGNAL,1L);option(curl,CURLOPT_CONNECTTIMEOUT,10L);option(curl,CURLOPT_TIMEOUT,120L);
        if(!m_sAccount.empty()){option(curl,CURLOPT_USERNAME,m_sAccount.c_str());option(curl,CURLOPT_PASSWORD,m_sAccountPassword.c_str());}
        option(curl,CURLOPT_MAIL_FROM,sender.c_str());option(curl,CURLOPT_MAIL_RCPT,recipients.values);
        option(curl,CURLOPT_UPLOAD,1L);option(curl,CURLOPT_READFUNCTION,&readPayload);option(curl,CURLOPT_READDATA,&upload);
        option(curl,CURLOPT_INFILESIZE_LARGE,static_cast<curl_off_t>(payload.size()));
        last=curl_easy_perform(curl);
        if(last==CURLE_OK)return;
                                                                                                              
        if(upload.started || !connectionFailure(last))break;
    }
    throw CrException(std::string("SMTP delivery failed: ")+curl_easy_strerror(last));
}
}
