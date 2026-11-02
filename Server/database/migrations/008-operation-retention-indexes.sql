CREATE INDEX IF NOT EXISTS etlforge_operationlog_completed_retention_idx
ON public.operationlogtab(status_ts, operation_instance_id)
WHERE status IN (1,3,4,5,6);
CREATE INDEX IF NOT EXISTS etlforge_operationlog_active_retention_idx
ON public.operationlogtab(operation_id)
WHERE status IN (0,2);
