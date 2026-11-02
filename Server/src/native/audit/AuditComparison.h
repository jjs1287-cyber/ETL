#pragma once
#include <boost/json.hpp>
#include <optional>
#include <set>
#include <string>
#include <vector>

namespace etlforge::audit {
struct Rules {
    std::set<std::string> ignoredPaths;
    std::set<std::string> secretPaths;
    std::set<std::string> unorderedArrays;
    std::string version = "1";
};
struct Snapshot {
    std::string fingerprint;
    std::string protectedCanonical;
    boost::json::value visible;
};
struct Change {
    std::string action;
    std::vector<std::string> paths;
};
std::string canonicalJson(const boost::json::value& value);
Snapshot restoreProtectedSnapshot(const boost::json::value& envelope, const boost::json::value& visible);
Snapshot prepareSnapshot(const boost::json::value& value, const Rules& rules,
                         const std::vector<unsigned char>& secretKey, const std::string& keyVersion);
std::optional<Change> compareSnapshots(const std::optional<Snapshot>& previous,
                                     const std::optional<Snapshot>& current);
}
