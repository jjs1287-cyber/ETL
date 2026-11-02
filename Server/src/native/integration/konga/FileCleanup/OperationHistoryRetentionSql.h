#pragma once
#include <string>
namespace etlforge {
inline std::string archiveHistoryRetentionSql(int days) {
    if (days < 1 || days > 36500) return "SELECT 0";
    const std::string age = "(now() AT TIME ZONE 'UTC') - interval '" + std::to_string(days) + " days'";
    return "WITH expired AS MATERIALIZED (SELECT l.archive_operation_instance_id,l.operation_instance_id FROM public.archiveoperationlogtab l "
        "WHERE l.status IN (1,3,4,5,6) AND l.status_ts < " + age +
        " AND NOT EXISTS (SELECT 1 FROM public.operationlogtab live WHERE live.operation_instance_id=l.operation_instance_id AND live.status IN (0,2)) "
        "AND NOT EXISTS (SELECT 1 FROM public.archiveoperationlogtab newer WHERE newer.operation_instance_id=l.operation_instance_id AND (newer.status IN (0,2) OR newer.status_ts >= " + age + ")) "
        "ORDER BY l.status_ts LIMIT 1000 FOR UPDATE OF l SKIP LOCKED), "
        "messages AS (DELETE FROM public.archiveoperationmessagetab WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)), "
        "summaries AS (DELETE FROM public.archiveoperationsummarytab WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)), "
        "logs AS (DELETE FROM public.archiveoperationlogtab WHERE archive_operation_instance_id IN (SELECT archive_operation_instance_id FROM expired)), "
        "transfers AS (DELETE FROM public.archivexferhist WHERE id IN (SELECT t.id FROM public.archivexferhist t WHERE t.xferdatetime < " + age +
        " AND NOT EXISTS (SELECT 1 FROM public.operationlogtab live WHERE live.operation_id=t.operationid AND live.status IN (0,2)) ORDER BY t.xferdatetime LIMIT 1000 FOR UPDATE OF t SKIP LOCKED) RETURNING 1) "
        "SELECT (SELECT count(*) FROM expired)+(SELECT count(*) FROM transfers)";
}
inline std::string operationHistoryRetentionSql(int days) {
    if (days < 1 || days > 36500) return "SELECT 0";
    return "WITH expired AS MATERIALIZED (SELECT l.operation_instance_id FROM public.operationlogtab l "
        "WHERE l.status IN (1,3,4,5,6) AND l.status_ts < (now() AT TIME ZONE 'UTC') - interval '" + std::to_string(days) + " days' "
        "AND NOT EXISTS (SELECT 1 FROM cloudlogging.delivery d WHERE d.operation_instance_id=l.operation_instance_id AND d.uploaded_at IS NULL AND d.attempts<5) "
        "AND NOT EXISTS (SELECT 1 FROM public.operationinstancetab child JOIN public.operationlogtab cl ON cl.operation_instance_id=child.operation_instance_id WHERE child.parent_operation_instance_id=l.operation_instance_id AND cl.status IN (0,2)) "
        "AND NOT EXISTS (SELECT 1 FROM public.salesforceoperationstatustab s WHERE s.operation_instance_id=l.operation_instance_id) "
        "AND NOT EXISTS (SELECT 1 FROM public.bulkload_instance_tab s WHERE s.operation_instance_id=l.operation_instance_id) "
        "AND NOT EXISTS (SELECT 1 FROM public.bulkload_batch_tab s WHERE s.operation_instance_id=l.operation_instance_id) "
        "ORDER BY l.status_ts LIMIT 1000 FOR UPDATE OF l SKIP LOCKED), "
        "messages AS (DELETE FROM public.operationmessagetab WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)), "
        "files AS (DELETE FROM public.operationfilestatustab WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)), "
        "summaries AS (DELETE FROM public.operationsummarytab WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)), "
        "progress AS (DELETE FROM public.operation_progress_tab WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)), "
        "delivery AS (DELETE FROM cloudlogging.delivery WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)), "
        "logs AS (DELETE FROM public.operationlogtab WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)), "
        "instances AS (DELETE FROM public.operationinstancetab WHERE operation_instance_id IN (SELECT operation_instance_id FROM expired)) "
        "SELECT count(*) FROM expired";
}
}
