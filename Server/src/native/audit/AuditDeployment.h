#pragma once
#include "AuditObjectReader.h"
namespace etlforge::audit {
class DeploymentCapture {
public:
    DeploymentCapture(Database& database,const std::string& projectGuid);
    void complete(long long userId,const std::string& userName,const std::string& projectName);
private:
    Objects read();
    Database& database_;
    Store store_;
    std::string projectGuid_;
    bool enabled_=false;
    Objects before_;
    std::vector<unsigned char> key_;
};
}
