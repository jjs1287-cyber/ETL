# ETLForge service recovery

ETLForge uses two recovery layers. Windows Service Control Manager restarts a process that exits unexpectedly. The independent native `ETLForgeWatchdog` also detects processes that remain in the Running state but stop answering their functional health probe.

The default watchdog checks every 20 seconds. Three consecutive failures are required, so a component must fail for roughly one minute before restart. Each probe has a 10-second timeout and each service has a two-minute restart cooldown. Database and pooler probes run first. Application probes are suppressed while their declared dependency is unhealthy, preventing a database outage from causing every service to restart repeatedly.

PostgreSQL and PgBouncer are checked with `pg_isready`; Java and Apache are checked with HTTP requests; Process Engine, Scheduler and File Cleanup use their authenticated native `GetStatus` commands. Cloud Logging reconnects to PostgreSQL every 10 seconds and publishes a health heartbeat that must remain fresh. Watchdog decisions are written to `C:\ETL\Log\watchdog\watchdog.log`.

Native database connection acquisition pauses the calling component and makes 10 attempts with a 30-second delay between failed attempts. No sleep occurs after the final failure. A started operation remains owned by its original worker during this reconnect window and is not eligible for takeover by another node.

Scheduler, Process Engine and File Cleanup service-host startup timeouts are 360 seconds. This exceeds the 270 seconds of retry delays and prevents Windows from killing a component before its database recovery window completes.

After all 10 attempts are exhausted, the component exits so Windows recovery and the watchdog can start a clean process. This avoids both immediate failure on a brief interruption and a permanently wedged service on a prolonged outage.

The policy was verified on the second physical node by blocking the Process Engine's TCP connection to PgBouncer. The service remained in `StartPending` instead of being killed by its host timeout. After the block was removed, it reconnected on a later attempt, entered `Running`, and answered its authenticated `GetStatus` probe.

Run a read-only pass without restarting anything:

`ETLForgeWatchdog.exe --config C:\ETL\Server\instances\dev\services\Watchdog.ini --once --no-restart`

Install or update the watchdog with `C:\ETL\Tools\Install-ETLForgeWatchdog.ps1`.
