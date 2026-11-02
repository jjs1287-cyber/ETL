# ETLForge multi-server shared-database plan

Date: 2026-10-30. Status: foundation changes installed; full active-active clustering is NOT yet certified. See [implementation status](multi-server-foundation-status.md) for completed work, test evidence and remaining gaps. The code findings below describe the initial review baseline.

## Goal and recommended architecture

Two or more Windows x64 ETLForge nodes share the same logical configuration, message queue and transaction databases. Both accept Studio connections and execute queued operations. PostgreSQL 18 remains on 6543; each node can use its own PgBouncer on 5432 pointing to that same database server. Do not create separate independent databases with the same names and expect them to share work.

Use PostgreSQL for coordination, durable scheduling and ownership, with native C++ services. Start with one environment and matching builds on both nodes. A single PostgreSQL host remains a separate availability dependency; database HA is outside the first worker-clustering milestone.

## Initial code findings

* ProcessEngine/Engine/EngineCore.cpp, CheckIncomingDbTransaction: existing SERIALIZABLE transaction selects queue entries and updates IsExecuting and Owner before commit. Serialization failures are handled. Preserve useful behavior, but verify PostgreSQL driver behavior, rollback handling and that no task starts before successful commit. Do not assume the queue is entirely unlocked or already cluster-safe.
* Same file, CheckForTasksToReRun: recovery selects executing messages with blank/current-machine Owner. A dead remote node needs explicit recovery; a hostname is insufficient as a unique process incarnation.
* Same file, startup recovery: transfer-history reset updates every XferHist row with ProgressStatus 1 or 2, without node ownership. Audit and scope this before enabling multiple nodes.
* Scheduler/Scheduler.cpp: scheduled operations and next wake time are process-local. ScheduledOp.cpp checkOp/sendOpMessage must gain durable occurrence deduplication, including failover and restart.
* cloudlogging/Main.cpp: pending deliveries are selected without an exclusive durable delivery claim. Two uploaders could send the same payload. ON CONFLICT during delivery creation does not prevent duplicate sending.
* ssh/SshKeys.cpp: protected local key files are stored under the node's private directory. Database sharing alone does not provision usable keys on another machine; copying encrypted files is not an established portable provisioning method.
* Native/API runtime uses local DataInterchange, Resources, temporary files and Java backend. All referenced artifacts and node-local behavior require inventory before failover claims.

Paths above are relative to src/native/integration/konga except cloudlogging/Main.cpp and ssh/SshKeys.cpp, which are relative to src/native.

## Ordered implementation checklist

- [x] Record objective, inspect queue/scheduler/recovery/cloud delivery/key storage entry points.
- [ ] 1. Complete inventory: every shared database/table, startup recovery, host-specific configuration, local file dependency, session/cache and lock. Record current single-node regression timings and output before changing behavior.
- [ ] 2. Add cluster ID, persistent node UUID, process incarnation, advertised API address, build compatibility, heartbeat and drain state. Reject duplicate identities/configuration mismatches. Database time controls leases.
- [ ] 3. Add backward-compatible schema migration, backup and rollback procedure. Only one migrator; block incompatible binaries rather than running mixed schemas silently.
- [ ] 4. Atomic queue claims: bounded SELECT FOR UPDATE SKIP LOCKED and owner/lease/generation updates in one transaction, or prove equivalent existing SERIALIZABLE behavior. Dispatch only after commit. Track worker capacity and claim attempts. Do not hold a database transaction during operation execution.
- [ ] 5. Durable scheduler occurrences: unique key on schedule, operation/binding and planned UTC occurrence; enqueue occurrence and message atomically. Persist schedule revision/timezone and restart cursor. Both schedulers may compete safely; leadership alone is not deduplication. Define DST, missed-run and schedule-edit behavior explicitly.
- [ ] 6. Heartbeats, lease renewal and stale-worker fencing. Old generation cannot finalize or update current-owned work. Database loss stops new claims; prevent stale workers from continuing new side effects after ownership loss. Test long-running operations and delayed heartbeats.
- [ ] 7. Recovery policy: queued/unstarted work can be reclaimed; interrupted operations with uncertain external side effects default to interrupted/manual retry, not automatic replay. An external write followed by a crash cannot generally be guaranteed exactly once without destination idempotency/transactions. Surface that distinction in Studio.
- [x] Node-local service watchdog: functional probes, dependency-aware restart suppression, consecutive-failure threshold and restart cooldown. Each node runs its own watchdog; it never restarts a service on another node.
- [ ] 8. Parent/child operations, RunOperation/RunScript, synchronous results, in-memory globals and Temporary Storage: preserve context and define affinity or persisted handoff. Cross-node cancellation must reach the owning node. Enforce existing single-thread and operation overlap policies across the cluster, not just each process.
- [ ] 9. Deployments and files: versioned immutable artifact bundle, checksums, atomic activation and node acknowledgement. Include XSD/JTR/XSLT, scripts, plugins, drivers and other runtime files. Recommended first design is a durable shared artifact store with verified node-local cache; choose location after inventory. Never run an operation using a partially deployed/mismatched bundle.
- [ ] 10. Secrets: provision SSH/PGP keys, passphrases, cloud credentials and Salesforce/JDBC settings securely to eligible nodes, with consistent logical key IDs. Keep machine-local protection; design authenticated provisioning/rotation. Do not put plaintext keys in a shared file folder.
- [ ] 11. Either-IP Studio access: authentication/session behavior, permissions, project list, deploy, run/test, logs/history/audit, scheduler, proxy, cloud configuration and SSH/PGP administration. Node-specific settings/status must identify the node; shared settings must invalidate caches across nodes. Owner-local file download/log requests need routing or shared storage.
- [ ] 12. Hosted endpoints: request accepted on either node must enqueue durably; synchronous HTTP replies must remain attached to the request correctly. Optional DNS/load balancer later; direct-IP access is still required. Retries need request idempotency where applicable.
- [ ] 13. Local-file/SMB/SFTP semantics: a C: path is local to each node. Add eligible-node/affinity rules or require shared paths. Multiple separate operations polling the same source also need file reservation/idempotency; queue ownership alone does not prevent both consuming the same external file.
- [ ] 14. Cleanup: scope local temp cleanup to owned/local work; coordinate shared history/archive/audit retention and protect active leases. Review abandoned-operation detector and startup repair so one node cannot mark another node's work failed. Preserve bounded batches and retention defaults.
- [ ] 15. Cloud logging: claim deliveries with lease/generation, stable upload ID and bounded retries; acknowledge only current claim. Sender identity must refer to execution node rather than whichever uploader sent the record. HTTP receivers must tolerate retry duplicates.
- [ ] 16. Installer/configuration: join-existing-cluster option, matching runtime libraries, service identities, firewall/TLS, database TLS/access, shared-store permissions, NTP and unique node IDs. Do not clone private identity files from node A to B. Document total database connection budget across nodes.
- [ ] 17. Admin UI: node health/capacity, owning node on operation instance, drain/resume, interrupted state and compatible build. Stop/drain node without silently stopping other nodes.
- [ ] 18. Automated concurrent database/service tests, followed by two-machine acceptance and measured single-node versus two-node throughput. Only then enable active-active mode for customer work.

## Required acceptance tests

1. Concurrent manual submissions through either API: every distinct request has one operation instance and one owner; intentionally duplicate requests have documented behavior.
2. Two schedulers, repeated restarts and simultaneous tick: one queue item per planned occurrence, with no missed slot under the chosen missed-run policy.
3. At least 1,000 uniquely numbered safe test operations: both nodes process work, no missing/duplicate completions. Compare outputs and messages with single-node baseline.
4. Kill worker before claim commit, after commit before execution, during processing and after external output before success commit. Confirm expected recovery versus explicit uncertain/interrupted status.
5. Partition DB/network, expire lease, resume old worker: stale ownership cannot corrupt current state. Verify limits on external side effects explicitly.
6. Restart one node while the other runs: no global running-status reset, archive deletion or temp deletion of live remote work.
7. Deploy/edit on node A and run on B, then reverse: identical artifact revisions, keys and results. Test XSD refresh, native JavaScript, native XSLT and compression/PGP without changing Salesforce/JDBC semantics.
8. Studio login through both IPs, cross-node cancellation, history/files, admin changes, hosted HTTP request/reply and invalidation of cached configuration.
9. Source file contention, child operations, globals, Temporary Storage and concurrency policy: explicit expected results, no context leakage.
10. Two cleanup/cloud services: bounded cleanup, no active-data loss, one current delivery owner and stable retry IDs.
11. Drain/rejoin, version mismatch, identity collision, unavailable shared store and credential provisioning failure: fail clearly and safely.
12. Record throughput, p50/p95 queue latency, DB locks/connections, CPU, memory and file growth. Two nodes need not halve runtime of one operation; the target is increased aggregate throughput and continued service after a node stops.

## Second Windows test machine

A second physical Windows x64 laptop/computer on the same network is useful for final testing. Provide its hostname/IP and available RAM/CPU when ready; no passwords in chat. It must reach the database host on 6543 (through its local PgBouncer 5432), the artifact location and test source/targets. Studio must reach each advertised API address. Exact service ports, installation steps and storage access will be documented after inventory. Use disposable test endpoints and matching binaries. Do not start a second production worker against the existing databases until coordination changes pass the acceptance tests.

## Next work

The native registry, Administration page, scheduler deduplication, shared schedule invalidation and queue-claim foundation are installed and tested as described in [implementation status](multi-server-foundation-status.md). The broad checklist items above remain unchecked where their full acceptance criteria are not complete. Next: worker incarnation/lease ownership and startup recovery, followed by artifact/secret distribution and cross-node routing.
