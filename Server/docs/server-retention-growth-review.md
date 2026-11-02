# Server retention and growth review — 2026-10-28

## Installed fix
ETLForge.Dev.FileCleanup now purges the four TranDb archive tables hourly using FileCleanup.OperationHistoryDays (30). Each statement selects at most 1,000 operation archive headers and 1,000 transfer records, skips locked rows, and deletes associated archived messages/summaries in the same atomic SQL statement. Recent or active archive instances and live pending/running instances are protected. Transfer records for an operation with pending/running instances are preserved. Multiple archive versions are protected if any version is recent/active. Null timestamps and orphaned child rows are conservatively retained because their age cannot be established.

Expiry indexes are in database/migrations/009-archive-retention-indexes.sql. Source rollback copies are in C:/ETL/Archives/archive-retention-20261028. No customer source or target files were removed. Only FileCleanup was stopped for the build; all eight ETLForge services were running afterward.

## Verification
- 40 native cleanup checks passed.
- Actual generated C++ SQL passed PostgreSQL transaction tests: expired headers/messages/summaries/transfers removed; recent and active archive rows preserved. Test transaction rolled back.
- Real running FileCleanup service removed the expired fixture header and transfer; assertions verified children removed and recent/active fixtures preserved. Surviving fixtures were then removed explicitly.
- Evidence: archive-retention-tests.log, archive-retention-service-tests.log, archive-retention-build.log in this directory.

## Other growth locations found
| Area | Current behavior / risk | Next work |
|---|---|---|
| Native service stdout/stderr, cleanup/native component logs | Service host opens append-only files; no rotation found in service host. Growth is unbounded during sustained errors. | Add bounded rotation at the writer; avoid deleting/truncating live open files. |
| Apache NativeAPI.apache.log | Plain ErrorLog filename; no rotatelogs configured. | Configure bounded piped log rotation. |
| PgBouncer.log | Largest sampled service log, approximately 4.19 MiB; no demonstrated size cap. | Configure supported rotation/reopen mechanism. |
| Tomcat | JULI files have maxDays=90. service-stdout/stderr have no demonstrated cap; access-log cap not verified. | Bound service streams and verify access-log rotation/retention. |
| PostgreSQL | Autovacuum enabled, archive_mode off, no replication slots retaining WAL; max_wal_size=1 GB is a checkpoint target, not a hard cap. Service stderr also needs rotation. | Monitor WAL and disk free space; retain autovacuum. Do not remove pg_wal files manually. |
| Shared service-user temp CookieJar files and .mdmp crash dumps | Outside owned operation-directory cleanup. Cookie sessions may cross operation chains. | Add lifecycle-aware cookie cleanup and bounded crash-dump retention; do not delete active shared cookies. |
| Audit event/event_detail | Retention defaults 30/365 exist, but retention_enabled is currently false. Cleanup implementation exists; no native background caller was found by source search. | Wire periodic purge respecting Admin setting; keep separate from 30-day operation history. |
| Live xferhist | Transfer history accumulates. It can affect transfer tracking/retry semantics. | Establish safe expiry for completed transfers without changing active retries or deduplication. |
| deployhistory, projectbackuptab, UploadedJitterpaks | Deployment/backup history and backup files accumulate; 33 uploaded packages currently. | Define backup retention and delete DB/file pairs together, keeping recoverable recent versions. |
| WSDL, xml_dtd, UserFiles, jtr | Project assets/caches grow with deployments; 151 WSDL files currently. | Reference-aware cleanup of retired assets. Never age-delete active project definitions. |
| TemporaryStorage | 38 small files currently; lifecycle not proven bounded. | Trace owners and check orphan reclamation before deletion. |
| Salesforce/Bulk-associated live history | Existing live history cleanup intentionally excludes these references. | Separate lifecycle review; preserve current Salesforce/JDBC behavior. |
| Orphan operation/archive messages and null-date archive rows | Age cannot reliably be inferred without parent metadata. | Diagnose separately; no blind orphan purge added. |
| Stale Pending/Running operations after crashes | Protected from retention, so repeated crashes can accumulate working files/history. | Add verified crash reconciliation; an event-log entry alone is not proof that operation status was finalized. |
| Development builds/rollback archives/reports | Grow during development; not production operational retention. | Exclude from installer; use a separate developer archive policy. |

## Observed sizes
Snapshot is in server-growth-inventory.csv. PostgreSQL data directory was approximately 175 MiB, instance service logs 4.35 MiB, DataInterchange 4.20 MiB, Tomcat instance 0.78 MiB. Drive C had approximately 1,743 GiB free. No immediate capacity pressure was observed. This snapshot does not prove growth is bounded.

## Priority
1. Writer-side rotation for append-only service logs, Apache, and PgBouncer.
2. Scheduled audit purge honoring configured retention, plus crash-state reconciliation.
3. Cookie/crash-dump lifecycle, transfer history, and reference-aware backup/cache retention.

The archive-table cleanup gap is fixed. The other growth risks above remain open; this review does not claim the server is protected from every disk-exhaustion scenario. Batch deadlines are checked between statements; they are not a database statement timeout, and a large backlog may require multiple hourly cycles. PostgreSQL normally reuses vacuumed space rather than immediately shrinking table files.

## Central log migration — 2026-10-28
Operational filesystem logs are now centralized under C:/ETL/Log with component subfolders. Historical files preserved, legacy log directories redirected via junctions, and all eight services verified Running. See central-server-logs.md for the path map, verification and installer guidance. Rotation gaps listed above still require writer-specific fixes; moving the files does not cap growth.
