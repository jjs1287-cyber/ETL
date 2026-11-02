#pragma once
#include "CleanupRule.h"
namespace konga {
class OperationRetentionRule : public CleanupRule {
public:
    std::string getName() const { return "ETLForge operation temporary files and history retention"; }
    bool ValidateRule(std::vector<std::string>& warnings);
    bool Execute(std::vector<std::string>& warnings);
};
}
