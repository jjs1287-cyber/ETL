#include <konga/MailClient/SmtpClient.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <curl/curl.h>
#include <iostream>
#include <stdexcept>
#include <cstring>
int main(int argc,char** argv){
 try{
  if(argc!=2)throw std::runtime_error("Expected loopback fixture port");
  std::string wrapped="abcdefgh";konga::WrapLines(wrapped,3);
  if(wrapped!="abc\ndef\ngh")throw std::runtime_error("WrapLines public x64 signature or behavior failed");
  bool smtp=false;
  for(const char* const* p=curl_version_info(CURLVERSION_NOW)->protocols;*p;++p)if(std::strcmp(*p,"smtp")==0)smtp=true;
  if(!smtp)throw std::runtime_error("libcurl lacks SMTP support");
  konga::EmailProperties properties("\"Sender, Name\" <sender@example.invalid>");
  properties.addToAddress("to@example.invalid");properties.addCCAddress("cc@example.invalid");properties.addBCCAddress("hidden@example.invalid");
  properties.setReplyTo("reply@example.invalid");properties.setReturnPath("bounce@example.invalid");
  konga::SmtpClient client;
  client.addSmtpServer("127.0.0.1:1"); // A refused first connection must fall back before uploading.
  client.addSmtpServer(std::string("smtp://127.0.0.1:")+argv[1]);
  client.sendMessage(properties,u8"ETLForge delivery \u00e9 \u65e5\u672c",u8"Line one\r\n.UTF-8 \u65e5\u672c\r\n");
  bool rejected=false;
  try{client.sendMessage(properties,"Bad\r\nBcc: injected@example.invalid","payload");}catch(const konga::CrException&){rejected=true;}
  if(!rejected)throw std::runtime_error("Header injection accepted");
  std::cout<<"PASS: x64 libcurl SMTP, connection fallback, UTF-8 payload and header rejection\n";
  return 0;
 }catch(const konga::CrException& error){std::cerr<<error.GetErrorString()<<'\n';return 1;}
 catch(const std::exception& error){std::cerr<<error.what()<<'\n';return 1;}
}
