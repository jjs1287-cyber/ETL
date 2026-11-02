#include "AuditDatabase.h"
#include <algorithm>
#include <cctype>
#include <limits>
#include <stdexcept>

namespace etlforge::audit {
namespace {
std::wstring wide(const std::string& value) {
    if(value.empty())return {};
    if(value.size()>static_cast<std::size_t>(INT_MAX))throw std::runtime_error("Audit text is too large");
    int length=MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),nullptr,0);
    if(!length)throw std::runtime_error("Invalid UTF-8 audit text");
    std::wstring result(length,L'\0');
    if(MultiByteToWideChar(CP_UTF8,MB_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),&result[0],length)!=length)throw std::runtime_error("Cannot decode audit UTF-8 text");
    return result;
}
std::string utf8(const std::wstring& value) {
    if(value.empty())return {};
    if(value.size()>static_cast<std::size_t>(INT_MAX))throw std::runtime_error("Audit text is too large");
    int length=WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),nullptr,0,nullptr,nullptr);
    if(!length)throw std::runtime_error("Invalid Unicode from audit database");
    std::string result(length,'\0');
    if(WideCharToMultiByte(CP_UTF8,WC_ERR_INVALID_CHARS,value.data(),static_cast<int>(value.size()),&result[0],length,nullptr,nullptr)!=length)throw std::runtime_error("Cannot encode audit UTF-8 text");
    return result;
}
void check(SQLRETURN result) {
    if(!SQL_SUCCEEDED(result)) throw std::runtime_error("Audit database request failed; deployment must roll back");
}
std::string text(const Cell& cell) {
    if(!cell) throw std::runtime_error("Unexpected null audit database result");
    return *cell;
}
void validate(const Objects& objects) {
    for(const auto& pair : objects) {
        const auto& item = pair.second;
        if(pair.first != std::make_pair(item.type,item.guid) || item.type <= 0 || item.guid.empty())
            throw std::runtime_error("Invalid audit object identity");
    }
}
std::string projectArray(const std::vector<std::string>& projects) {
    if(projects.size()>10000)throw std::runtime_error("Too many audit project permissions");
    std::string result="{";
    for(const auto& project: projects) {
        if(project.size()!=36)throw std::runtime_error("Invalid audit project GUID");
        for(std::size_t i=0;i<project.size();++i) {
            bool separator = i==8 || i==13 || i==18 || i==23;
            if(separator ? project[i]!='-' : !std::isxdigit(static_cast<unsigned char>(project[i])))
                throw std::runtime_error("Invalid audit project GUID");
        }
        if(result.size()>1)result+=',';
        result+=project;
    }
    return result+"}";
}
}
Database::Database(SQLHDBC connection,std::string schema) : connection_(connection),schema_(std::move(schema)) {
    if(schema_.empty() || !std::all_of(schema_.begin(),schema_.end(),[](unsigned char c){return std::islower(c) || std::isdigit(c) || c=='_';}))
        throw std::runtime_error("Invalid audit schema name");
}
std::vector<Row> Database::query(const std::string& sql, const std::vector<Cell>& parameters) {
    std::string statementText = sql;
    if(schema_ != "audit") {
        std::size_t offset = 0;
        while((offset = statementText.find("audit.",offset)) != std::string::npos) {
            statementText.replace(offset,6,schema_+"."); offset += schema_.size()+1;
        }
    }
    SQLHSTMT statement = SQL_NULL_HSTMT;
    check(SQLAllocHandle(SQL_HANDLE_STMT,connection_,&statement));
    struct Release { SQLHSTMT s; ~Release(){SQLFreeHandle(SQL_HANDLE_STMT,s);} } release{statement};
    check(SQLSetStmtAttr(statement,SQL_ATTR_QUERY_TIMEOUT,reinterpret_cast<SQLPOINTER>(30),0));
    std::wstring sqlWide=wide(statementText);
    check(SQLPrepareW(statement,reinterpret_cast<SQLWCHAR*>(&sqlWide[0]),SQL_NTS));
    std::vector<SQLLEN> lengths(parameters.size());
    std::vector<std::wstring> values(parameters.size());
    for(std::size_t i=0;i<parameters.size();++i) {
        if(parameters[i] && parameters[i]->find('\0')!=std::string::npos)throw std::runtime_error("Audit database text contains NUL");
        if(parameters[i])values[i]=wide(*parameters[i]);
    }
    for(std::size_t i=0;i<parameters.size();++i) {
        lengths[i]=parameters[i]?static_cast<SQLLEN>(values[i].size()*sizeof(wchar_t)):SQL_NULL_DATA;
        check(SQLBindParameter(statement,static_cast<SQLUSMALLINT>(i+1),SQL_PARAM_INPUT,SQL_C_WCHAR,SQL_WLONGVARCHAR,
            std::max<std::size_t>(values[i].size(),1),0,
            parameters[i]?const_cast<wchar_t*>(values[i].c_str()):nullptr,
            parameters[i]?static_cast<SQLLEN>((values[i].size()+1)*sizeof(wchar_t)):0,&lengths[i]));
    }
    SQLRETURN executed = SQLExecute(statement);
    if(executed == SQL_NO_DATA) return {};
    if(!SQL_SUCCEEDED(executed)) {
        SQLCHAR state[6] = {}, diagnostic[1024] = {}; SQLINTEGER native = 0; SQLSMALLINT diagnosticLength=0;
        SQLGetDiagRecA(SQL_HANDLE_STMT,statement,1,state,&native,diagnostic,sizeof(diagnostic),&diagnosticLength);
        throw std::runtime_error("Audit database request failed (SQLSTATE " + std::string(reinterpret_cast<char*>(state)) + "); deployment must roll back; statement: " + statementText.substr(0,95));
    }
    SQLSMALLINT columns = 0;
    check(SQLNumResultCols(statement,&columns));
    std::vector<Row> rows;
    if(!columns) return rows;
    SQLRETURN fetched;
    while((fetched=SQLFetch(statement)) != SQL_NO_DATA) {
        check(fetched);
        Row row;
        for(SQLUSMALLINT col = 1; col <= columns; ++col) {
            std::wstring value;
            SQLLEN length;
            bool isNull = false;
            for(;;) {
                wchar_t buffer[4096] = {};
                SQLRETURN result = SQLGetData(statement,col,SQL_C_WCHAR,buffer,sizeof(buffer),&length);
                if(result == SQL_NO_DATA) break;
                check(result);
                if(length == SQL_NULL_DATA) {isNull = true;break;}
                value.append(buffer);
                if(result == SQL_SUCCESS) break;
                if(value.size() > 64*1024*1024) throw std::runtime_error("Audit database result exceeds 64 MiB");
            }
            row.push_back(isNull ? Cell{} : Cell(utf8(value)));
        }
        rows.push_back(std::move(row));
    }
    return rows;
}
void Database::requireTransaction() {
    SQLULEN autocommit = SQL_AUTOCOMMIT_ON;
    check(SQLGetConnectAttr(connection_,SQL_ATTR_AUTOCOMMIT,&autocommit,0,nullptr));
    if(autocommit != SQL_AUTOCOMMIT_OFF) throw std::runtime_error("Audit writes require the deployment transaction");
}
void Store::lockProject(const std::string& projectGuid) {
    database_.requireTransaction();
    database_.query("SELECT pg_advisory_xact_lock(hashtextextended(CAST(CAST(? AS uuid) AS text),417603))",{projectGuid});
    lockedProject_ = projectGuid;
    lockedTransaction_ = text(database_.query("SELECT pg_current_xact_id()::text")[0][0]);
}
std::size_t Store::record(const Deployment& deployment, const Objects& before, const Objects& after) {
    database_.requireTransaction();
    if(lockedProject_ != deployment.projectGuid) throw std::runtime_error("Audit project must be locked before snapshots are read");
    if(lockedTransaction_ != text(database_.query("SELECT pg_current_xact_id()::text")[0][0]))
        throw std::runtime_error("Audit project lock belongs to a previous transaction");
    validate(before); validate(after);
    std::string rulesVersion = "1", keyVersion;
    auto inspectVersion = [&](const Objects& objects) {
        for(const auto& pair : objects) {
            auto envelope = boost::json::parse(pair.second.snapshot.protectedCanonical);
            std::string rules(envelope.at("rulesVersion").as_string()), key(envelope.at("keyVersion").as_string());
            if(keyVersion.empty()) {rulesVersion=rules;keyVersion=key;}
            if(rules != rulesVersion || key != keyVersion) throw std::runtime_error("Mixed audit key or normalization versions");
        }
    };
    inspectVersion(before); inspectVersion(after);
    if(!keyVersion.empty()) {
        auto baseline = database_.query("SELECT normalization_version,key_version FROM audit.project_baseline WHERE project_guid=CAST(? AS uuid)",{deployment.projectGuid});
        if(!baseline.empty() && (text(baseline[0][0])!=rulesVersion || text(baseline[0][1])!=keyVersion))
            throw std::runtime_error("Audit key/rules changed; controlled rebaseline required");
        database_.query("INSERT INTO audit.project_baseline(project_guid,normalization_version,key_version) VALUES(CAST(? AS uuid),?,?) ON CONFLICT DO NOTHING",
            {deployment.projectGuid,rulesVersion,keyVersion});
    }
    std::set<std::pair<int,std::string>> identities;
    for(const auto& pair: before) identities.insert(pair.first);
    for(const auto& pair: after) identities.insert(pair.first);
    std::size_t count = 0;
    for(const auto& identity: identities) {
        auto old = before.find(identity), now = after.find(identity);
        std::optional<Snapshot> previous = old == before.end() ? std::nullopt : std::optional<Snapshot>(old->second.snapshot);
        std::optional<Snapshot> current = now == after.end() ? std::nullopt : std::optional<Snapshot>(now->second.snapshot);
        auto change = compareSnapshots(previous,current);
        auto& object = now == after.end() ? old->second : now->second;
        std::string type = std::to_string(object.type);
        auto states = database_.query("SELECT revision,protected_snapshot::text,is_deleted::text FROM audit.object_state WHERE project_guid=CAST(? AS uuid) AND object_type=CAST(? AS integer) AND object_guid=CAST(? AS uuid)",
            {deployment.projectGuid,type,object.guid});
        long long revision = states.empty() ? (previous ? 1 : 0) : std::stoll(text(states[0][0]));
        if(!states.empty() && previous && (text(states[0][2])=="true" || canonicalJson(boost::json::parse(text(states[0][1]))) != previous->protectedCanonical))
            throw std::runtime_error("Audit baseline differs from current object; reconcile before deployment");
        if(change) {
            auto maxima = database_.query("SELECT COALESCE(MAX(revision),0)::text FROM audit.event WHERE project_guid=CAST(? AS uuid) AND object_type=CAST(? AS integer) AND object_guid=CAST(? AS uuid)",
                {deployment.projectGuid,type,object.guid});
            revision = std::max(revision, std::stoll(text(maxima[0][0]))) + 1;
            auto inserted = database_.query("INSERT INTO audit.event(deployment_id,project_guid,project_name,object_guid,object_type,object_type_name,object_name,action,user_id,user_name,previous_fingerprint,current_fingerprint,revision) VALUES(CAST(? AS uuid),CAST(? AS uuid),?,CAST(? AS uuid),CAST(? AS integer),?,?,?,CAST(? AS bigint),?,?,?,CAST(? AS bigint)) RETURNING event_id::text",
                {deployment.id,deployment.projectGuid,deployment.projectName,object.guid,type,object.typeName,object.name,change->action,std::to_string(deployment.userId),deployment.userName,
                 previous ? Cell(previous->fingerprint) : Cell{},current ? Cell(current->fingerprint) : Cell{},std::to_string(revision)});
            boost::json::array paths;
            for(const auto& path: change->paths) paths.push_back(boost::json::value(path));
            database_.query("INSERT INTO audit.event_detail(event_id,previous_value,current_value,changed_paths) VALUES(CAST(? AS bigint),CAST(? AS jsonb),CAST(? AS jsonb),CAST(? AS jsonb))",
                {text(inserted[0][0]),previous ? Cell(canonicalJson(previous->visible)) : Cell{},current ? Cell(canonicalJson(current->visible)) : Cell{},canonicalJson(paths)});
            ++count;
        }
        if(current) database_.query("INSERT INTO audit.object_state(project_guid,object_type,object_guid,object_name,normalization_version,key_version,fingerprint,protected_snapshot,visible_snapshot,revision) VALUES(CAST(? AS uuid),CAST(? AS integer),CAST(? AS uuid),?,?,?,?,CAST(? AS jsonb),CAST(? AS jsonb),CAST(? AS bigint)) ON CONFLICT(project_guid,object_type,object_guid) DO UPDATE SET object_name=EXCLUDED.object_name,normalization_version=EXCLUDED.normalization_version,key_version=EXCLUDED.key_version,fingerprint=EXCLUDED.fingerprint,protected_snapshot=EXCLUDED.protected_snapshot,visible_snapshot=EXCLUDED.visible_snapshot,revision=EXCLUDED.revision,is_deleted=false",
            {deployment.projectGuid,type,object.guid,object.name,rulesVersion,keyVersion,current->fingerprint,current->protectedCanonical,canonicalJson(current->visible),std::to_string(revision)});
        else database_.query("INSERT INTO audit.object_state(project_guid,object_type,object_guid,object_name,normalization_version,key_version,fingerprint,protected_snapshot,visible_snapshot,revision,is_deleted) VALUES(CAST(? AS uuid),CAST(? AS integer),CAST(? AS uuid),?,?,?,?,'null'::jsonb,'null'::jsonb,CAST(? AS bigint),true) ON CONFLICT(project_guid,object_type,object_guid) DO UPDATE SET is_deleted=true,revision=EXCLUDED.revision,protected_snapshot='null'::jsonb,visible_snapshot='null'::jsonb",
            {deployment.projectGuid,type,object.guid,object.name,rulesVersion,keyVersion,previous->fingerprint,std::to_string(revision)});
    }
    return count;
}
boost::json::value Store::settings() {
    auto rows = database_.query("SELECT jsonb_build_object('detailDays',detail_days,'headerDays',header_days,'retentionEnabled',retention_enabled)::text FROM audit.settings WHERE singleton");
    if(rows.size()!=1) throw std::runtime_error("Audit settings missing");
    return boost::json::parse(text(rows[0][0]));
}
void Store::saveSettings(long long userId,const std::string& userName,int detailDays,int headerDays,bool retentionEnabled) {
    database_.requireTransaction();
    if(detailDays<1 || headerDays<detailDays || headerDays>36500) throw std::runtime_error("Audit retention requires 1 <= detail days <= header days <= 36500");
    database_.query("SELECT singleton FROM audit.settings WHERE singleton FOR UPDATE");
    auto previous = settings();
    boost::json::object current{{"detailDays",detailDays},{"headerDays",headerDays},{"retentionEnabled",retentionEnabled}};
    if(canonicalJson(previous)==canonicalJson(current)) return;
    database_.query("UPDATE audit.settings SET detail_days=CAST(? AS integer),header_days=CAST(? AS integer),retention_enabled=CAST(? AS boolean),updated_at=clock_timestamp(),updated_by=? WHERE singleton",
        {std::to_string(detailDays),std::to_string(headerDays),retentionEnabled ? "true" : "false",userName});
    database_.query("INSERT INTO audit.settings_event(user_id,user_name,previous_value,current_value) VALUES(CAST(? AS bigint),?,CAST(? AS jsonb),CAST(? AS jsonb))",
        {std::to_string(userId),userName,canonicalJson(previous),canonicalJson(current)});
}
std::size_t Store::cleanup(std::size_t batchSize) {
    database_.requireTransaction();
    if(batchSize<1 || batchSize>5000) throw std::runtime_error("Audit cleanup batch must be 1..5000");
    auto setting = settings();
    if(!setting.at("retentionEnabled").as_bool()) return 0;
    auto deleted = database_.query("WITH expired AS (SELECT d.event_id FROM audit.event_detail d JOIN audit.event e USING(event_id) CROSS JOIN audit.settings s WHERE e.deployed_at < now()-make_interval(days=>s.detail_days) ORDER BY e.deployed_at,e.event_id LIMIT CAST(? AS integer) FOR UPDATE OF d SKIP LOCKED) DELETE FROM audit.event_detail d USING expired x WHERE d.event_id=x.event_id RETURNING d.event_id::text",{std::to_string(batchSize)});
    auto headers = database_.query("WITH expired AS (SELECT e.event_id FROM audit.event e CROSS JOIN audit.settings s WHERE e.deployed_at < now()-make_interval(days=>s.header_days) ORDER BY e.deployed_at,e.event_id LIMIT CAST(? AS integer) FOR UPDATE OF e SKIP LOCKED) DELETE FROM audit.event e USING expired x WHERE e.event_id=x.event_id RETURNING e.event_id::text",{std::to_string(batchSize)});
    return deleted.size()+headers.size();
}
boost::json::value Store::list(const std::vector<std::string>& authorizedProjects,const Filter& filter) {
    if(filter.days<1 || filter.days>36500 || filter.limit<1 || filter.limit>200 || filter.offset<0 || filter.offset>1000000 || filter.objectType<0 || filter.search.size()>256)
        throw std::runtime_error("Invalid audit history filter");
    std::string nameFilter=filter.search;
    if(nameFilter.find_first_not_of(" \t\r\n\f\v")==std::string::npos)nameFilter.clear();
    auto rows=database_.query("SELECT jsonb_build_object('id',e.event_id,'projectGuid',e.project_guid,'projectName',e.project_name,'objectGuid',e.object_guid,'objectName',e.object_name,'objectType',e.object_type,'objectTypeName',e.object_type_name,'action',e.action,'userName',e.user_name,'deployedAt',to_char(e.deployed_at AT TIME ZONE 'UTC','YYYY-MM-DD\"T\"HH24:MI:SS.US\"Z\"'),'detailAvailable',EXISTS(SELECT 1 FROM audit.event_detail d WHERE d.event_id=e.event_id))::text FROM audit.event e WHERE e.project_guid=ANY(CAST(? AS uuid[])) AND e.deployed_at>=now()-make_interval(days=>CAST(? AS integer)) AND (CAST(? AS integer)=0 OR e.object_type=CAST(? AS integer)) AND position(lower(CAST(? AS text)) in lower(e.object_name))>0 ORDER BY e.deployed_at DESC,e.event_id DESC LIMIT CAST(? AS integer) OFFSET CAST(? AS integer)",
        {projectArray(authorizedProjects),std::to_string(filter.days),std::to_string(filter.objectType),std::to_string(filter.objectType),nameFilter,std::to_string(filter.limit+1),std::to_string(filter.offset)});
    boost::json::array entries;
    bool hasMore=rows.size()>static_cast<std::size_t>(filter.limit);
    for(std::size_t i=0;i<rows.size() && i<static_cast<std::size_t>(filter.limit);++i)entries.push_back(boost::json::parse(text(rows[i][0])));
    return boost::json::object{{"entries",std::move(entries)},{"hasMore",hasMore}};
}
boost::json::value Store::detail(const std::vector<std::string>& authorizedProjects,long long eventId) {
    if(eventId<1)throw std::runtime_error("Invalid audit event ID");
    auto rows=database_.query("SELECT jsonb_build_object('id',e.event_id,'detailAvailable',d.event_id IS NOT NULL,'previous',d.previous_value,'current',d.current_value,'changedPaths',d.changed_paths)::text FROM audit.event e LEFT JOIN audit.event_detail d USING(event_id) WHERE e.project_guid=ANY(CAST(? AS uuid[])) AND e.event_id=CAST(? AS bigint)",
        {projectArray(authorizedProjects),std::to_string(eventId)});
    if(rows.empty())throw std::runtime_error("Audit entry not found or access denied");
    return boost::json::parse(text(rows[0][0]));
}
}
