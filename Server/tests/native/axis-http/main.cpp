// Integration fixture only: loopback HTTP transport for the real native Axis services.
#include <winsock2.h>
#include "SimpleAxisTransport.h"
#include <iostream>
extern "C" int initialize_module(int);
extern "C" int uninitialize_module();
extern int process_request(SOAPTransport*);
int main(){
 WSADATA data;if(WSAStartup(MAKEWORD(2,2),&data))return 1;
 SOCKET listener=socket(AF_INET,SOCK_STREAM,IPPROTO_TCP);if(listener==INVALID_SOCKET)return 2;
 sockaddr_in address={};address.sin_family=AF_INET;address.sin_addr.s_addr=htonl(INADDR_LOOPBACK);address.sin_port=htons(47113);
 if(bind(listener,reinterpret_cast<sockaddr*>(&address),sizeof(address))||listen(listener,5)){closesocket(listener);return 3;}
 std::cerr<<"Before Axis server initialization"<<std::endl;
 std::cerr<<"Preloading checkout DLL"<<std::endl;
 if(!LoadLibraryA("C:/ETL/Server/dist/bin/ETLForgeCheckoutInterchangeData.dll"))return 5;
 std::cerr<<"Checkout DLL loaded"<<std::endl;
 if(initialize_module(1)!=0){closesocket(listener);return 4;}
 std::cout<<"Native Axis HTTP test fixture on 127.0.0.1:47113"<<std::endl;
 for(;;){
  SOCKET client=accept(listener,nullptr,nullptr);if(client==INVALID_SOCKET)break;
  DWORD timeout=10000;setsockopt(client,SOL_SOCKET,SO_RCVTIMEO,reinterpret_cast<char*>(&timeout),sizeof(timeout));setsockopt(client,SOL_SOCKET,SO_SNDTIMEO,reinterpret_cast<char*>(&timeout),sizeof(timeout));
  try{SimpleAxisTransport transport(client);std::cerr<<"Dispatching SOAP request"<<std::endl;process_request(&transport);std::cerr<<"SOAP dispatch finished"<<std::endl;}catch(const std::exception& error){std::cerr<<error.what()<<std::endl;}
 }
 closesocket(listener);uninitialize_module();WSACleanup();return 0;
}
