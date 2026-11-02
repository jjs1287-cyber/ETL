#include "OperationRetentionRule.h"
#include "OperationTempRetention.h"
#include "OperationHistoryRetentionSql.h"
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Server/WDIPathFinder.h>
#include <iostream>
#include <atomic>
#include <chrono>

extern std::atomic<bool> g_cancel;
using namespace konga;
bool OperationRetentionRule::ValidateRule(std::vector<std::string>& warnings) {
    const int temp = GetOptionAsInt("FileCleanup", "FailedTempDays", 7);
    const int history = GetOptionAsInt("FileCleanup", "OperationHistoryDays", 30);
    const int occurrences = GetOptionAsInt("FileCleanup", "SchedulerOccurrenceDays", 90);
    if (temp < 1 || temp > 36500 || history < 1 || history > 36500 || occurrences < 30 || occurrences > 36500) {
        warnings.push_back("ETLForge retention days must be between 1 and 36500."); return false;
    }
    return true;
}
bool OperationRetentionRule::Execute(std::vector<std::string>& warnings) {
    if (!ValidateRule(warnings)) return false;
    SmartDbPtr<DbConnection> db(GetBackEndDbConnection("TranDb"));
    auto active = [&](long id) {
        if (g_cancel) return true;
        SmartDbPtr<DynamicDbRecordSet> rows(DynamicDbRecordSet::MakeInstance(db,
            "SELECT 1 FROM public.operationlogtab WHERE " + (id < 0 ? std::string("") : "operation_id=" + toString(id) + " AND ") + "status IN (0,2) LIMIT 1"));
        rows->Open(); return rows->Next();
    };
    const int days = GetOptionAsInt("FileCleanup", "FailedTempDays", 7);
    std::string home; WDIPathFinder::GetWDIPath(home);
    size_t failures = 0, removed = 0;
    removed += etlforge::cleanupExpiredOperationFiles(etlforge::cleanupWidePath(getTmpDir()), days, true, active, failures);
    removed += etlforge::cleanupExpiredOperationFiles(etlforge::cleanupWidePath(home + "DataInterchange/Temp"), days, true, active, failures);
    removed += etlforge::cleanupExpiredOperationFiles(etlforge::cleanupWidePath(home + "DataInterchange/Temp/LOG"), days, false, active, failures);
    removed += etlforge::cleanupExpiredOperationFiles(etlforge::cleanupWidePath(home + "DataInterchange/Temp/Debug"), days, false, active, failures);
    if (failures) warnings.push_back("ETLForge retention could not remove " + toString(failures) + " owned temporary files/directories; retrying next cycle.");
    size_t historyRemoved = 0;
    const auto deadline = std::chrono::steady_clock::now() + std::chrono::seconds(15);
    while (!g_cancel && std::chrono::steady_clock::now() < deadline) {
        SmartDbPtr<DynamicDbRecordSet> rows(DynamicDbRecordSet::MakeInstance(db,
            etlforge::operationHistoryRetentionSql(GetOptionAsInt("FileCleanup", "OperationHistoryDays", 30))));
        rows->Open();
        if (!rows->Next()) throw std::runtime_error("ETLForge retention query returned no count.");
        size_t batch = static_cast<size_t>(std::stoull(rows->GetFieldValue(0)));
        historyRemoved += batch;
        if (batch < 1000) break;
    }
    size_t archivesRemoved = 0;
    const auto archiveDeadline = std::chrono::steady_clock::now() + std::chrono::seconds(15);
    while (!g_cancel && std::chrono::steady_clock::now() < archiveDeadline) {
        SmartDbPtr<DynamicDbRecordSet> rows(DynamicDbRecordSet::MakeInstance(db,
            etlforge::archiveHistoryRetentionSql(GetOptionAsInt("FileCleanup", "OperationHistoryDays", 30))));
        rows->Open();
        if (!rows->Next()) throw std::runtime_error("ETLForge archive retention query returned no count.");
        size_t batch = static_cast<size_t>(std::stoull(rows->GetFieldValue(0)));
        archivesRemoved += batch;
        if (!batch) break;
    }
    {
        SmartDbPtr<DbConnection> messages(GetBackEndDbConnection("MsgDb"));
        messages->ExecuteSql("WITH expired AS (SELECT schedule_id,operation_id,scheduled_local FROM etlforge_cluster.schedule_occurrence WHERE recorded_at < now()-make_interval(days=>" +
            toString(GetOptionAsInt("FileCleanup", "SchedulerOccurrenceDays", 90)) +
            ") ORDER BY recorded_at LIMIT 1000 FOR UPDATE SKIP LOCKED) DELETE FROM etlforge_cluster.schedule_occurrence x USING expired e WHERE x.schedule_id=e.schedule_id AND x.operation_id=e.operation_id AND x.scheduled_local=e.scheduled_local");
    }
    std::cout << "ETLForge retention: removed " << removed << " expired temp entries, " << historyRemoved << " expired operation histories and " << archivesRemoved << " expired archive headers/transfers." << std::endl;
    return failures == 0;
}
