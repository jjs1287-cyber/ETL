#ifndef KONGA_CLIENT_H
#define KONGA_CLIENT_H

#include <string>

extern const unsigned long lCOMMAND_PORT;
extern std::string sSERVER_MODULE;

namespace konga {
namespace CrNetworking {

class Request;
class Response;

Response* SendRequest(const Request& req, long lCommandPort);

}                   
}                          

#endif                  
