# ETLForge Windows x64 installers and release build

Proposed design, 2026-10-30. This document does not implement an installer or declare the current runtime ready for customer distribution. Development takes place on a physical Windows laptop. Keep separate Studio and Server installers.

## Installer recommendations

| Option | Fit | Trade-off |
|---|---|---|
| Inno Setup for Studio and Server — recommended initial implementation | One consistent wizard framework, command-line builds, small bootstrap overhead, Unicode, compression and silent installation. Supports a small online bootstrapper and a complete offline installer. | Service setup, migration recovery and application health validation are ETLForge responsibilities. EXE setup does not provide MSI deployment semantics. Current project requests commercial users purchase a license; review the pinned release's terms before adopting it. |
| WiX Toolset MSI + Burn EXE | Prefer if customer IT requires Windows Installer packages, enterprise repair/deployment and chained prerequisites. Can package both products in separate bundles. | More authoring and upgrade/component discipline. External database changes and custom service configuration still require explicit recovery logic. Review current maintenance-fee terms, separate from optional commercial support. |
| NSIS | Scriptable EXE alternative with silent install and a small installer engine. | Lower-level custom scripting and more work maintaining the server workflow. No particular advantage over Inno for this project. |

Use the same framework for both products initially. Implement privileged server configuration in an x64 C++ setup helper with bounded operations, structured results, secret redaction and explicit undo steps. Keep wizard scripting thin. Do not treat the existing development PowerShell scripts as a customer installer.

Primary references:
- Inno Setup: https://jrsoftware.org/isinfo.php and https://jrsoftware.org/isorder.php
- WiX Burn: https://docs.firegiant.com/wix/tools/burn/
- WiX maintenance terms: https://docs.firegiant.com/wix/osmf/
- NSIS: https://nsis.sourceforge.io/Features

## Build process

One release command should produce Studio and Server artifacts from the same source revision and pinned dependency manifest:

1. **Validate inputs:** release/build identifier, source revision, Windows x64 toolchain, exact dependency versions/hashes, supported runtime versions and redistribution materials. Fail on uncommitted changes in a formal release; allow explicitly marked development packages.
2. **Build in a clean workspace:** CMake/Ninja/MSVC Release for native binaries; Ant/JDK for Studio; existing server Java build for retained backend functions. Build into an isolated output directory, never into the running installation. Refactor current absolute C:\ETL and Administrator-specific paths first.
3. **Run release checks:** native unit tests; Java/Studio validation; native API, deployment, operation, scheduler, encoding, archive, PGP and endpoint smoke tests against disposable test databases and owned test files. Keep Salesforce/JDBC functionality unchanged. External-service tests use explicit test fixtures rather than customer credentials.
4. **Stage separate allowlisted payloads:** copy only files declared in studio-payload.json and server-payload.json. Include transitive DLL imports and verified dynamically loaded modules. Generate a staged dependency report and size breakdown. Reject unlisted files rather than copying entire runtime folders.
5. **Generate release metadata:** file SHA-256 hashes, component versions, source revision, schema compatibility range, SBOM, license notices, corresponding-source materials where needed, and installed-size estimates. Record the installer tool version too.
6. **Sign and package:** Authenticode-sign ETLForge executables/DLLs as appropriate, construct installers, then sign and timestamp the finished installers. Use a protected signing service/key; unsigned builds must be clearly marked development-only. Verify third-party payload authenticity rather than modifying their signatures.
7. **Test the installers:** unattended and interactive fresh installation, custom paths, standard-user Studio operation, upgrade, repair/reconfigure, rollback/failure recovery and uninstall. Use disposable physical machines or clean test environments; never reset this development laptop to test uninstall.
8. **Publish release artifacts:** installers, checksums, manifest/SBOM, notices/source materials and release notes. Keep the tested artifacts immutable and retain the previous release for recovery.

Suggested artifact names:

    ETLForge-Studio-42.0.0.0-build.<build-id>-win-x64.exe
    ETLForge-Server-42.0.0.0-build.<build-id>-win-x64.exe
    ETLForge-Server-42.0.0.0-build.<build-id>-win-x64-offline.exe

The application currently reports 42.0.0.0 everywhere. Add a separate monotonically increasing release/build identifier so setup can distinguish upgrades even when the display version is unchanged. Do not rely on equal embedded file versions to decide whether an updated JAR or DLL needs replacing. If using MSI, design its package version/upgrade identity separately within MSI's version constraints.

## Payload boundaries and size

Studio includes the application JARs/resources, x64 launcher/native libraries, required notices and a tested private Java runtime. A JDK, Eclipse, compilers, source checkout and build tools are not needed on the customer computer. Prefer an appropriate existing Git installation when compatible; offer Git support as a selectable component with an authenticated, hash-pinned download or an offline payload. GitHub sign-in belongs in Studio, not in setup.

Server includes only validated ETLForge executables/modules, the selected Apache modules, retained Tomcat/Java backend, PgBouncer and their actual dependencies. Include the supported x64 ODBC driver/runtime prerequisites as required by the backend, not every database client tool. Keep JDBC and Salesforce support. Preserve current feature decisions: no JMS service, Dataloader or ETLForge SQLite endpoint.

Offer PostgreSQL 18 as a downloadable payload for a new local database, and include it in the offline Server installer. A join/external-database node does not need a second PostgreSQL server. Verify package publisher/signature where available plus pinned SHA-256; download failure must leave installation recoverable. Do not use an unpinned 'latest' URL in released builds.

Saxon-C, archive/SSH/PGP dependencies and required notices remain part of applicable feature payloads. Excluding a feature requires testing that no shipped executable still imports its libraries. A disabled UI item is not proof that its libraries can be deleted.

Use C:\ETL\Reports\server-library-review.md and server-library-files.csv as the starting inventory. Never ship current private/, populated data directories, development service definitions, logs, samples containing credentials, customer projects, Git checkouts, database clusters, backups, tests, archives, build caches or obsolete binaries. Do not copy dist wholesale: it still contains legacy/development candidates such as jms-lib that require deliberate exclusion and dependency verification.

Do not promise an installer size before measuring the staged runtime closure. Report Studio, Server base, PostgreSQL, Java, Git and optional feature sizes separately.

## Installation layout

Suggested defaults:

    C:\Program Files\ETLForge\Studio\
    C:\Program Files\ETLForge\Server\
    C:\Program Files\ETLForge\Log\<instance>\apache\
    C:\Program Files\ETLForge\Log\<instance>\tomcat\
    C:\Program Files\ETLForge\Log\<instance>\postgresql\
    C:\Program Files\ETLForge\Log\<instance>\pgbouncer\
    C:\ProgramData\ETLForge\<instance>\configuration\
    C:\ProgramData\ETLForge\<instance>\private\
    C:\ProgramData\ETLForge\<instance>\data\

Keep logs under the chosen main installation folder as requested, with component subdirectories and rotation. Grant service-account write access only to Log and appropriate mutable data/configuration locations, not the application binaries. Allow a different data volume during setup. Studio projects/settings are per-user and must remain writable without administrator privileges.

The current code discovers some resources and GnuPG relative to instance/ancestor directories. Separating Program Files binaries from ProgramData data therefore requires explicit path configuration and regression tests; it is not a drop-in directory change. Preserve user-selected locations during upgrades. Shared ETLForge folders must not be removed when uninstalling only one product.

## Single-port gateway update — 2026-10-30

The Studio single-port prerequisite is now implemented and tested. Ship the Apache proxy template and mod_proxy/mod_proxy_http modules. Render the local Java destination per instance; Studio must not ask for or store a separate Tomcat port. Network setup should offer the one gateway endpoint, with either direct HTTPS or the future SSH tunnel mode. Keep Tomcat bound to loopback in both modes. Actual SSH server/account/tunnel provisioning and actual TLS configuration remain separate outstanding implementation work. See [gateway and SSH follow-up](studio-single-port-gateway.md).

## Studio wizard screens

| Screen | User interaction |
|---|---|
| 1. Welcome / detected installation | Install, upgrade, repair or uninstall. Show current and proposed build. |
| 2. Product terms and notices | Present applicable product terms and third-party notices. No activation key or feature licensing gate. |
| 3. Installation scope and location | Current user or all users, application directory. Elevate only where needed. |
| 4. Components | Required private Java runtime/native support selected automatically; optional Git integration and examples. Use a compatible existing Git where selected. Show download/installed sizes. |
| 5. Shortcuts and associations | Start-menu entry, optional desktop shortcut and ETLForge project/backup associations. |
| 6. Review | Locations, components, downloads, disk space and applications that must close. |
| 7. Installation and completion | Progress and actionable failures; optional launch as the original non-elevated user. Show support log location. |

Use the existing first-run login UI to enter a server address and credentials. Do not require a live server to install Studio. Never bundle a development admin password or ask for a GitHub token in setup.

## Server wizard screens

Show advanced pages only when applicable, so a typical standalone install does not require every advanced option.

| Screen | User interaction |
|---|---|
| 1. Welcome / detected installation | Fresh install, upgrade, repair or reconfigure; identify the existing instance/build. |
| 2. Product terms and component notices | Applicable notices and terms; no license activation restrictions. |
| 3. Deployment role | Standalone/new environment, standalone using existing PostgreSQL, or join an existing ETLForge environment. Join asks for an active server origin and ETLForge administrator credentials. It displays that database credentials and the vault master secret will be copied over the selected HTTP(S) connection and requires an explicit trusted-network risk acceptance. |
| 4. Instance identity | Instance name, display name and advertised hostname/IP. Generate a new node UUID locally; never clone the first node's identity. Generate unique Windows service names. |
| 5. Locations | Application, data, temporary working storage and main Log location. Show required/free disk space. Respect the main-folder logging layout. |
| 6. Windows service identity | Default supported least-privilege service identity or a custom account; validate service logon and access to selected folders/network resources. Use separate database service identity where appropriate. |
| 7. Database topology | Install bundled/downloaded PostgreSQL 18 or use an existing supported instance. PostgreSQL defaults to 6543. Existing-database page asks host, port, TLS and provisioning/connection credentials, with Test Connection. Provision/validate all required logical ETLForge databases, not TranDb alone. Join must preserve existing environment data and shared settings. |
| 8. Connection pool and capacity | Local PgBouncer defaults to 5432, pointing to the selected PostgreSQL host/6543. Advanced pool size, worker capacity and aggregate connection budget. Validate ports and existing services; never take over another application's 5432 automatically. |
| 9. Network access | Studio/API advertised address and ports, validated HTTPS/certificate options, permitted remote networks/computers and explicit firewall scope. Keep Java-backend/control ports private. Confirm server listener and TLS support before exposing the relevant choices. |
| 10. Initial administrator | New environments only: administrator username/password or securely generated one-time bootstrap credential. Joining/upgrading keeps existing accounts. No built-in shared default password. |
| 11. Retention | Show defaults: failed working files 7 days, operation history 30 days, audit details 30 days, audit headers 365 days; successful working-file cleanup stays automatic. Offer Advanced settings rather than mandatory edits. Cloud logging stays disabled for a new environment until configured in Administration. |
| 12. Upgrade safety — conditional | Database backup/checkpoint location, compatible schema check, operation draining and service restart window. Explain database recovery separately from binary rollback. Reject unsupported downgrades. Shared-environment migration must be coordinated once. |
| 13. Review and preflight | Role, instance, services, folders, ports, database destinations, downloads and permissions. Redact secrets. Resolve conflicts before changing the machine. |
| 14. Install and configure | Progress for downloads, files, configuration, ACLs, database provisioning/migrations, service registration/start and health checks. Failures show the failed step and log location; avoid partial-success messages. |
| 15. Completion | Actual service/database/API health, Studio connection address, optional configuration export without secrets, and documentation. Do not report success solely because SCM says services are running. |

PGP/SSH key import, cloud destinations and project migration belong in Administration after setup. This keeps secret handling and operational setup out of the main wizard. SSH and PGP key records are stored in PostgreSQL using the portable database key vault. A new cluster generates one protected vault master secret. A joining server authenticates to an active ETLForge server and downloads the existing database topology/credentials, cluster settings and vault master secret; it never generates a replacement. The installer generates a unique node identity and then restricts the local configuration to Administrators and LocalSystem. The bootstrap API requires administrator authentication and explicit risk acceptance. HTTP is permitted for the requested locked-down environment; HTTPS is recommended when traffic can leave that trusted network.

## Upgrade, uninstall and unattended behavior

- Support unattended installs through a validated configuration/response file containing non-secret settings. Supply secrets through a protected channel/temporary protected file, not normal command-line arguments or logs. Return documented exit/reboot codes.
- Preserve customer configuration, projects, databases, keys and logs on normal upgrade. Use explicit schema migrations and backups; do not imply an installer engine can automatically undo committed PostgreSQL changes.
- Stop/drain only the selected ETLForge instance, then restart in dependency order. Do not disrupt unrelated PostgreSQL services or another ETLForge instance.
- Uninstall removes owned binaries, services, shortcuts and firewall entries, and preserves data by default. Any destructive database/data removal is a separate explicit choice, disabled for shared/external databases. Never remove another node's shared databases.
- Repair restores missing program files and service registration without resetting passwords, changing node identity, recreating databases or overwriting custom configuration.

## Work required before producing a release installer

- [x] Choose and pin Inno Setup 6.7.3 for the initial development build; commercial distribution terms still require review.
- [ ] Replace development-only absolute paths in build scripts, launchers, runtime discovery and service configuration.
- [x] Create isolated staging, allowlisted top-level payload selection, forbidden-content checks and two-artifact output validation. A fully clean source checkout build remains outstanding.
- [ ] Resolve supported Java/runtime redistribution and required VC++/ODBC prerequisites with an exact manifest.
- [ ] Finish library closure/size review; exclude verified unused modules and development content.
- [ ] Complete GnuPG corresponding-source/notices packaging and long-path/8.3-disabled installation tests recorded in docs/pgp-endpoints.md.
- [ ] Build the native server configuration/migration/service helper with an explicit recovery journal.
- [ ] Implement the Studio wizard and fresh-install smoke test first.
- [ ] Implement standalone Server, then external-database setup and upgrade/repair/uninstall tests.
- [ ] Finish multi-server ownership/artifacts/keys/routing before enabling the Join role.
- [ ] Add signing, unattended-install tests and immutable release publication.

The existing Build-Studio.ps1, Build-Native.cmd, Java packaging scripts and development installation scripts are useful inputs, not a complete reproducible customer release process.

## Development installer implementation — 2026-10-31

The first repeatable Windows x64 online-installer build now lives in `C:\ETL\Installer` and is driven by `Build-Installers.ps1`. It stages compiled allowlisted content, rejects development/user/private trees, downloads hash-pinned Temurin Java and PostgreSQL 18 archives during setup, compiles separate Studio and Server EXEs, and enforces a two-file-only `C:\ETL\Installers` output directory. Each successful two-installer build increments the fourth component in `C:\ETL\product-version.json`; failed builds do not advance it. A desktop shortcut invokes this release command.

Two-machine testing identified two mandatory Windows Server prerequisites that the installer must verify/install explicitly: the Microsoft Visual C++ x64 redistributable matching the native build toolchain, and the PostgreSQL Unicode(x64) ODBC driver. Do not infer their presence from PostgreSQL client binaries. A join-node screen must also validate Windows time synchronization/timezone compatibility until scheduling uses a canonical UTC model.

The Studio installer has a relocatable launcher and per-user writable project/settings directory. The Server development installer currently lays down the x64 runtime and writable ProgramData/Log directory structure but deliberately does **not** register services or provision a fresh database. Those actions remain blocked on the native configuration/recovery helper listed below; the installed configuration command reports that limitation rather than presenting an unusable server as healthy. These installers are unsigned development artifacts and are not yet customer release packages.

Incremental packaging observation: removing a class from an Ant fileset does not necessarily invalidate an existing JAR. The retired JavaWebServicePorts class required a clean rebuild of etlforge-integration-client.jar during the single-port change. Formal release builds must use clean outputs and verify absent/forbidden classes as well as required payload files.

Amazon S3 endpoint (2026-10-30): ship matching Studio/native endpoint ID 18 definitions and preserve libcurl AWS SigV4/HTTPS support. No AWS CLI/SDK or extra runtime package is needed. Credentials travel encrypted in the project; do not assume a machine-bound vault must be migrated. See docs/amazon-s3-endpoints.md for the existing shared-key project-encryption limitation and the 5 GiB single-PUT limit.
