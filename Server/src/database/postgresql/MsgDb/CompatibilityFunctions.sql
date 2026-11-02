-- Queue API used by the recovered native engine. The newer Java schema retains
-- ProcessVariables but only installs SpInsertOperationMsg (operation-ID API).
-- This additive bridge preserves both APIs and existing queue data.
CREATE OR REPLACE FUNCTION SpInsertIncomingMsgTab(
    p_from varchar, p_to varchar, p_subject varchar, p_process_variables varchar,
    p_received_at integer, p_is_executing integer, p_owner varchar,
    p_priority integer, p_threads integer) RETURNS integer
LANGUAGE plpgsql AS $etlforge$
DECLARE
    inserted_id bigint;
BEGIN
    INSERT INTO IncomingMsgTab(
        MsgFrom, MsgTo, MsgSubject, ProcessVariables, ReceivedAt,
        IsExecuting, Owner, MsgPriority, NumThreads)
    VALUES(p_from, p_to, p_subject, p_process_variables, p_received_at,
        p_is_executing, p_owner, p_priority, p_threads)
    RETURNING MsgId INTO inserted_id;
    -- The native queue API currently uses signed 32-bit IDs. PostgreSQL checks
    -- this cast and rolls back on overflow instead of silently truncating.
    RETURN inserted_id::integer;
END;
$etlforge$;
