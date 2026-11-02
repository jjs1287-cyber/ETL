#pragma once
#include "AuditComparison.h"
#include <windows.h>
#include <sql.h>
#include <sqlext.h>
#include <map>

namespace etlforge::audit {
using Cell = std::optional<std::string>;
using Row = std::vector<Cell>;
class Database {
public:
    explicit Database(SQLHDBC connection, std::string schema = "audit");
    std::vector<Row> query(const std::string& sql, const std::vector<Cell>& parameters = {});
    void requireTransaction();
private:
    SQLHDBC connection_;
    std::string schema_;
};
struct Object {
    int type;
    std::string guid, name, typeName;
    Snapshot snapshot;
};
using Objects = std::map<std::pair<int,std::string>,Object>;
struct Deployment {
    std::string id, projectGuid, projectName, userName;
    long long userId;
};
struct Filter {
    int days = 30;
    int objectType = 0;
    int offset = 0;
    int limit = 50;
    std::string search;
};
class Store {
public:
    explicit Store(Database& database) : database_(database) {}
    void lockProject(const std::string& projectGuid);
    std::size_t record(const Deployment& deployment, const Objects& before, const Objects& after);
    boost::json::value settings();
    void saveSettings(long long userId, const std::string& userName, int detailDays, int headerDays, bool retentionEnabled);
    std::size_t cleanup(std::size_t batchSize = 500);
    boost::json::value list(const std::vector<std::string>& authorizedProjects, const Filter& filter);
    boost::json::value detail(const std::vector<std::string>& authorizedProjects, long long eventId);
private:
    Database& database_;
    std::string lockedProject_;
    std::string lockedTransaction_;
};
}
