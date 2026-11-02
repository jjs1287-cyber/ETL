CREATE INDEX IF NOT EXISTS etlforge_archive_history_expiry_idx ON public.archiveoperationlogtab(status_ts,archive_operation_instance_id) WHERE status IN (1,3,4,5,6);
CREATE INDEX IF NOT EXISTS etlforge_archive_history_instance_idx ON public.archiveoperationlogtab(operation_instance_id,status_ts);
CREATE INDEX IF NOT EXISTS etlforge_archive_message_instance_idx ON public.archiveoperationmessagetab(operation_instance_id);
CREATE INDEX IF NOT EXISTS etlforge_archive_summary_instance_idx ON public.archiveoperationsummarytab(operation_instance_id);
CREATE INDEX IF NOT EXISTS etlforge_archive_transfer_expiry_idx ON public.archivexferhist(xferdatetime,id);
