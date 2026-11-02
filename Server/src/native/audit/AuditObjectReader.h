#pragma once
#include "AuditDatabase.h"
#include <functional>

namespace etlforge::audit {
struct GuidReference {
    std::string column, table;
};
struct TablePlan {
    std::string table, primaryKey, ownerColumn;
    std::set<std::string> ignoredColumns;
    std::vector<GuidReference> references;
};
struct EntityPlan {
    int type;
    std::string typeName, nameColumn, guidTable, membershipTable;
    TablePlan root;
    std::vector<TablePlan> children;
    bool requiresFileSnapshot = false;
};
class ObjectReader {
public:
    using FileSnapshot = std::function<boost::json::value(const EntityPlan&, const boost::json::object&)>;
    ObjectReader(Database& database, std::vector<EntityPlan> plans, Rules rules,
                 std::vector<unsigned char> secretKey, std::string keyVersion, FileSnapshot files = {}, FileSnapshot supplement = {});
    Objects readProject(const std::string& projectGuid);
private:
    boost::json::object normalizeRow(boost::json::object row, const TablePlan& plan);
    Database& database_;
    std::vector<EntityPlan> plans_;
    Rules rules_;
    std::vector<unsigned char> key_;
    std::string keyVersion_;
    FileSnapshot files_;
    FileSnapshot supplement_;
};
}
