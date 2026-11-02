# Central server logs — 2026-10-28

Current product root: C:/ETL. Central operational log root: C:/ETL/Log.
For the future installer, resolve this as <install-root>/Log (for example C:/Program Files/ETLForge/Log), and grant Modify only to the relevant service identities and administrators. Current development configurations still contain absolute C:/ETL paths; an installer must render them for its selected root.

| Subfolder | Contents |
|---|---|
| apache | Native API Apache error log |
| tomcat | JULI, HTTP access logs, Java service stdout/stderr |
| pgbouncer | Pooler log |
| postgresql | PostgreSQL service stdout/stderr and historical pg_ctl logs |
| native | Native engine, scheduler, cleanup, audit, Java component and plugin logs |
| services | Native service stdout/stderr and CloudLogging daily upload-error logs |

Existing logs were moved intact during a controlled stop of all eight services. Previous log directories are NTFS junctions into this tree to preserve server-log viewer, Java and hard-coded path compatibility. Do not remove these junctions until all consumers are migrated. Do not recursively rotate both a junction path and its target: scan the canonical root once, without following reparse points.

Explicit active service INI, Apache, PgBouncer, Tomcat JULI and access-log paths were changed. Four service installation scripts were updated to retain the new destinations. Source/config rollback copies: C:/ETL/Archives/central-logs-20261028. Migration procedure: C:/ETL/Tools/Move-ServerLogs.ps1 (one-time migration; deliberately rejects an already redirected source).

Verification: all eight services Running; rebuilt Studio SOAP login, project listing, scheduler/process status passed; PostgreSQL query via PgBouncer 5432 passed; native FileCleanup DoNow accepted and wrote output to the canonical tree. New startup entries observed in Apache, Tomcat, PostgreSQL, PgBouncer and native logs. Installer scripts parse successfully. Existing native log-viewing paths resolve through junctions; Studio UI was not changed.

This change centralizes logs; it does not add new rotation. Next: writer-side bounds for service stdout/stderr; Apache piped rotation; supported PgBouncer reopen/rotation; native component and CloudLogging retention. Tomcat JULI already has 90-day maxDays; access-log and stream retention still require bounds. Never truncate a live open stream externally. Preserve failures while diagnosing.

Operation working/temp files, downloaded customer data, database tables, PostgreSQL pg_wal and Windows Event Log are not ordinary diagnostic log files and were not relocated. Their existing lifecycle/retention rules remain separate. Old generated build/test reports also remain in Reports, not in the runtime log tree.
