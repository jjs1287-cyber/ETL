# Multi-server foundation status

2026-10-30. Development machine: physical Windows laptop DESKTOP-33T8ST4, not a VM. This is an installed foundation milestone, not a completed active-active cluster.

## Installed behavior

- Administration → Multi-server Configuration, with an existing server-console icon. Displays cluster identity/name, scheduler timeout, registered nodes, advertised addresses, build and scheduler heartbeat. Administrator-only configuration updates use optimistic revisions. Node address edits are metadata; they do not bind a listener, change firewall rules or reconnect Studio. Save stays disabled until a successful load on the current connection.
- Native C++ administration uses the existing authenticated native SOAP entry point. Java code is limited to the existing Swing Studio UI; no Tomcat coordination call was introduced.
- This laptop has a persistent UUID in its instance etlforge.conf under [Cluster]/NodeId. Scheduler process incarnations are separate UUIDs. A copied identity on a different hostname or a second live scheduler with the same identity is rejected. Graceful scheduler shutdown releases its own heartbeat; a crash requires the configured heartbeat timeout before takeover. This registry currently monitors Scheduler only, not ProcessEngine/API health.
- Durable scheduler occurrence deduplication in MsgDb, keyed by schedule ID, operation ID and planned local wall-clock timestamp. The occurrence marker and queue message commit together on the same MsgDb connection. Skipped overlap occurrences are recorded. Per-operation transaction advisory locking serializes the existing scheduled-operation overlap check.
- A shared TranDb schedule revision, updated transactionally by statement triggers on schedules, schedule properties, operations and operation properties. Every scheduler checks it each tick and reloads changed configuration. It does not depend exclusively on a local update notification. Reload preserves an already-due cached slot when it is within 30 seconds, so an ordinary tick-time invalidation does not discard that slot. This bounded reload behavior is not a general outage catch-up policy.
- Queue acquisition now uses READ COMMITTED with FOR UPDATE SKIP LOCKED. Reserved single-thread messages are locked and restricted to their owner. Task dispatch follows commit. Rolled-back task objects are discarded before retry, and single-thread reservation state changes only after commit.
- FileCleanup removes up to 1,000 old occurrence markers per cycle. Default retention is 90 days, configurable as [FileCleanup]/SchedulerOccurrenceDays, minimum 30. Registry tables keep one heartbeat per node/service rather than appending one row per tick.
- SSH and OpenPGP key records are now encrypted with portable OpenSSL AES-256-GCM and stored in the shared PostgreSQL key vault. Cloud-logging passwords and bearer tokens use the same portable protection in their shared database configuration. Stable references work on every node; DPAPI is not used by these running paths. `Join-ETLForgeCluster.ps1` now authenticates to an active server and synchronizes the shared database topology, database credentials, cluster settings and vault master secret. It generates a unique local NodeId and restricts the resulting configuration ACL. The join requires an explicit trusted-network risk acknowledgement because the bootstrap values travel over the configured HTTP(S) connection.
- Process Engine startup no longer automatically replays queue entries left executing by its previous process. It marks the associated operation instance `Canceled`, records the interruption reason, and removes the message from the runnable queue. The local Watchdog restarts a crashed Process Engine, which activates this recovery path.
- Cloud logging now claims one delivery at a time with `FOR UPDATE SKIP LOCKED`, a process UUID, a two-minute database-time lease and a monotonically increasing generation. Only the current owner/generation can acknowledge it. Stable filenames/upload IDs make retry takeover idempotent for the supported transports.

## Files and setup

- TranDb migrations: database/migrations/012-cluster-registry.sql, 014-shared-schedule-revision.sql, 015-key-vault.sql and 016-cloud-delivery-leases.sql.
- MsgDb migration: database/migrations/013-scheduler-occurrences-msgdb.sql. Do not apply this only to TranDb: the queue uses a separate database.
- C:\ETL\Tools\Initialize-ClusterNode.ps1 creates a local UUID without replacing an existing valid identity and backs up the configuration first. Its optional parameter is -InstanceHome. Never copy the first laptop's NodeId to another computer.
- C:\ETL\Tools\Install-ClusterFoundation.ps1 validates migration presence, requires an empty message queue, backs up current binaries to a timestamped archive, replaces staged x64 components with SHA-256 checks, and restarts only previously running affected services. It is a development installer, not the final customer installer.
- Original modified native sources: C:\ETL\Archives\multi-server-20261030. Each installation also has a separate timestamped binary backup.
- Schema additions are backward compatible; rolling back binaries does not require dropping these tables. Do not drop coordination objects while updated services are running.

## Verification

Reports are under C:\ETL\Reports:

| Evidence | Result |
|---|---|
| multi-server-database-tests.txt | 22 checks: concurrent occurrence insertion, transactional rollback, 1,000 queue claims, multiple workers, duplicate identities, stale registration and transactional schedule invalidation |
| multi-server-api-tests.txt | 11 native API checks: identity, readiness, registration, Unicode, revision conflicts, invalid inputs and authorization |
| multi-server-ui/results.txt | 12 installed-JAR Studio checks: Spring action, Administration page, native data, edits, disconnected controls and no new application errors |
| multi-server-ui/configuration.png | Rendered and visually checked dark-mode screen |
| multi-server-two-native-schedulers.txt | Two actual x64 Scheduler processes on this physical laptop, unique node identities, one durable occurrence and one successful operation instance; helper shut down normally and fixture disabled afterward |
| multi-server-two-physical-nodes.txt | Two physical Windows laptops: Scheduler race and both failover directions; 200 concurrent side-effect-free worker operations split 102/98; primary-only and second-node-only 20-operation survivor tests; no duplicates or queue remainder |
| multi-server-scheduler-tests.txt | 18 existing scheduler console checks, including forecast, enable/disable, filters and invalid requests |
| multi-server-runtime-before.txt / multi-server-runtime-after.txt | Twenty real SOAP submissions completed before and after the queue change; 40 messages with identical SHA-256 output digest |
| cloud_delivery_leases.sql | Delivery owner and generation fencing, stale-owner rejection and current-owner acknowledgement |

Latest isolated SQL comparison: baseline SERIALIZABLE took 1,294 ms with 452 serialization retries; READ COMMITTED/SKIP LOCKED took 605 ms with zero serialization retries. This is one laptop test of queue coordination, not a production throughput claim. Timing varies between runs. The real operation test measured 8,918 ms before; after measurements ranged from 7,644 to 9,373 ms. The latest run was slower. Output equivalence is verified; an end-to-end speed improvement is not established.

These tests establish two-machine queue contention and graceful worker availability for side-effect-free operations. They do not establish safe crash recovery for an already-started operation or exactly-once external side effects. Initial test harness issues (successful-with-information status, rendering a non-visible editor, helper shutdown environment, missing second-node ODBC/Visual C++ prerequisites and mismatched time zones) were corrected; final reports supersede those exploratory failures.

The second physical laptop is `DESKTOP-HT9BS5M` (`192.168.99.148`). Its Scheduler is installed as Manual and currently running. Its Process Engine is installed as Manual and deliberately left stopped after controlled testing. Both nodes use Central Standard Time with Windows Time synchronization. PgBouncer listens on `192.168.99.11:5432`; its inbound firewall rule permits only the second-node address. PostgreSQL remains loopback-only behind PgBouncer.

## Still required before enabling two production workers

| Service | Current multi-server state |
|---|---|
| PostgreSQL | One shared primary behind PgBouncer. It is not installed active/active; all nodes use the same logical databases. |
| PgBouncer | Per-node pooler is supported. Connection limits are shared-budgeted against the primary. |
| Native API / Java backend | May listen on each node, but deployment-artifact replication and cross-node file/cancellation routing are still required. |
| Scheduler | Shared revision, durable occurrence deduplication and two-physical-node failover tested. UTC/DST policy remains. |
| Process Engine | Concurrent queue claiming on two physical nodes tested. After a local Process Engine restart, interrupted work is canceled and is never automatically replayed. Detecting an indefinitely offline node from another node remains a separate stale-node cleanup task. |
| FileCleanup | Local temp cleanup and bounded database retention are safe foundations. Two-node live cleanup/archive race acceptance remains. |
| Cloud Logging | Database-time delivery leases, process ownership, generation fencing and idempotent upload identities implemented. Two-node transport failover acceptance remains. |
| Watchdog | Independently protects each local node. It is deliberately not a cluster-wide failover coordinator. |
| SSH / OpenPGP keys | Shared database authority, portable authenticated encryption, and administrator-authenticated trusted-network join synchronization implemented. Production TLS remains recommended but is not forced; the installer must display the transferred fields and require explicit risk acceptance. |


1. Add remote stale-node cancellation for a server that never returns. Local Process Engine crash recovery now marks interrupted operations Canceled and never replays them; existing queue Owner still uses machine names.
2. Repair remaining startup recovery SQL: broad XferHist status reset and unscoped error-data copy must not affect live remote work. Review all abandoned-operation and child-operation recovery paths.
3. Transaction/MsgDb commit gap: AddOperationToQueue still commits TranDb and MsgDb separately. Occurrence + message are atomic in MsgDb, but a crash can leave an orphan pending TranDb instance. Implement reconciliation/outbox handling and failure injection tests.
4. Cluster-wide single-thread/overlap rules, child-operation affinity, synchronous responses, in-memory globals and Temporary Storage semantics. The current single-thread flag remains process-local.
5. Shared versioned deployments/artifacts and cache invalidation beyond scheduling: XSD/JTR/XSLT, files, plugins and driver configuration. A database-only connection does not distribute local files.
6. Integrate the implemented `Join-ETLForgeCluster.ps1` bootstrap into the final native installer wizard and add final two-physical-node acceptance. Other endpoint/project credential formats still require a separate inventory.
7. Cross-node cancellation, local file/log download routing, hosted request/reply behavior and authentication/session checks through either physical IP. Advertised addresses currently only describe nodes.
8. Cloud delivery leases and generation fencing are implemented. Add execution-node identity to payload staging and complete two-physical-node transport/failover testing. Review shared cleanup and archive races with live remote work.
9. Convert scheduler slots to a defined canonical UTC/timezone model and test DST/missed-run/schedule-edit policies. Current dedup key uses existing local schedule times; both test schedulers had the same timezone. No general UTC scheduling or catch-up guarantee is claimed.
10. Bind/listener, database access rules, TLS/firewall, total connection budget, join installer and version compatibility enforcement. Cluster schema and MsgDb must describe the same logical environment.
11. Second physical laptop acceptance: worker concurrency, node kill, network partition, DB loss, lease expiry, deployment on A/run on B, keys, source-file contention, Studio through both IPs, cleanup/cloud coordination and drain/rejoin.

## Second laptop preparation

Prepare Windows x64 on the same network and provide its hostname/IP when ready. The laptop does not need Eclipse to run the server. We will install matching server/runtime files and a local PgBouncer on 5432 pointing to the first laptop's shared PostgreSQL 18 on 6543. All four ETLForge logical databases must point to the shared environment. Exact firewall/database access will be scoped to the two machines; no broad remote-access rule has been added by this change. The second worker must stay stopped until the remaining ownership, artifact and recovery work is implemented and tested.

## Single-port Studio follow-up — 2026-10-30

Studio native and Java API traffic now uses one gateway port (47100 by default), including a custom local forwarded port. Tomcat stays loopback-only on 47112. See [configuration, verification and SSH follow-up](studio-single-port-gateway.md). This completes the one-port transport prerequisite; it does not complete cross-node cancellation/file routing, worker failover or remote SSH access.
