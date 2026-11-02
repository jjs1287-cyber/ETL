#pragma once
#include <cstdint>
#include <functional>
#include <string>
#include <vector>
#include <boost/json.hpp>

namespace etlforge { namespace s3 {
struct Configuration {
    std::string bucket, region, accessKey, secretKey, sessionToken;
    std::function<bool()> cancelled;
};
struct Object { std::string key, etag, modified; std::uint64_t size=0; };
std::string decryptProjectSecret(const std::string& encrypted);
std::string encode(const std::string& text, bool keepSlash=false);
std::string decode(const std::string& text);
void validate(const Configuration& config);
class Client {
public:
    explicit Client(Configuration config);
    ~Client();
    Client(const Client&)=delete;
    Client& operator=(const Client&)=delete;
    std::vector<Object> list(const std::string& prefix, bool recursive=true, std::size_t limit=0, std::size_t pageSize=1000) const;
    void download(const std::string& key,const std::string& file,const std::string& etag="") const;
    void upload(const std::string& key,const std::string& file,bool overwrite=true) const;
    void remove(const std::string& key,const std::string& etag="") const;
private:
    Configuration config_;
    std::string request(const std::string& method,const std::string& key,const std::string& query,
        const std::string& file="",const std::vector<std::string>& headers={}) const;
};
std::string request(const std::string& json);
}}
