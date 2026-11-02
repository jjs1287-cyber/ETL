from pathlib import Path
Path('C:/ETL/Server/docs/pgp-endpoints.md').write_text('''# Source and target PGP encryption

Installed 2026-10-29 in the x64 development stack.

## User interface

Administration → SSH and PGP Keys now has a Type selector. Existing records default to SSH. Import one ASCII-armored PGP public key or private key per record (64 KiB maximum). For a protected private key, supply its passphrase when importing. Public-only keys are sufficient for targets; sources require private keys. Type cannot be changed on an existing record; create a new record instead. Public export is supported; private material is never returned by the API.

File sources and targets now contain a PGP/GPG panel next to the existing compression settings. Enable it, reload server keys, and select the key. Targets optionally use ASCII armor (.asc); otherwise output uses .pgp. The endpoint stores only pgp_enabled, pgp_key_id and pgp_armor. Passphrases and key material stay in the server key vault. Keys are server-specific; configure the corresponding key reference when moving an endpoint to another server.

Processing order:

- Source transfer → PGP decryption → archive extraction (when enabled) → normal processing.
- Target processing → compression (when enabled) → PGP encryption → transfer.

Both combined and per-file archive modes retain their existing semantics. Encryption applies to each final output artifact. FTP text mode is suppressed for encrypted endpoints so ciphertext bytes are not changed. Underlying source files are preserved according to existing endpoint settings. Plaintext working files use the normal operation working directory and existing success/failure cleanup policy.

## Native implementation

C++ orchestration in src/native/ssh/PgpFiles.cpp uses an isolated, bundled x64 GnuPG 2.4.9 executable. No Java or Tomcat call is made. The existing DPAPI-protected, administrator-only key vault is extended with type, hasPrivate and fingerprint metadata. Existing SSH selection filters out PGP records; PGP selectors filter out SSH records.

Each request creates a protected temporary keyring, imports only the selected key, disables option files and automatic key discovery, and supplies a passphrase through an inherited stdin pipe. There is no shell or command-line secret. Only explicitly selected pipe/status handles are inherited. The child process runs in a kill-on-close Windows job; each command has a 120-second timeout. Decrypted/encoded output is capped at 2 GiB. Output is published only after GnuPG returns success; decryption also requires DECRYPTION_OKAY. Wrong passwords, damaged ciphertext and public-only decryption fail without delivering partial target files.

Private keyrings and leases are removed after use. Orphan PGP workspaces older than 24 hours are swept when the key vault is accessed. Normal operation cleanup remains responsible for operation working files. This sweep is separate from the encrypted .key records, which are retained until an administrator deletes them.

The runtime is discovered by looking for runtime/gnupg/bin/gpg.exe in ancestors of the instance key vault. The current bundle is the x64 MSYS2 GnuPG build plus its runtime dependencies, about 10.1 MB of binaries. The MSYS2 process boundary uses POSIX paths and Windows short-path aliases when available. Long installation paths with 8.3 aliases disabled need further deployment testing before producing the installer. Package metadata, hashes and available upstream notices are saved with the runtime; finalize the full corresponding-source/license distribution package before shipping an installer. The bundled sqlite DLL is an internal GnuPG package dependency, not an ETLForge SQLite endpoint/database.

## Scope

This feature implements public-key encryption/decryption, binary and armored messages, and integrity checking. Key generation, signing controls, separate sender-signature trust management, symmetric passphrase-only encryption, and hardware tokens are not exposed. Malformed, expired, unsupported or unverifiable input is rejected by GnuPG. A signed message needing a separate sender key can fail verification; no signature checks are bypassed.

## Verification

- tests/native/pgp_files_tests.cpp: binary/Unicode names, armor, empty files, wrong passphrase, public-only decryption, fingerprint mismatch, invalid input, tampering, output protection and cleanup.
- tests/integration/PgpEndpointLiveTest.java: key API, real deployment, encrypted target, decrypted source, combined ZIP+PGP with two files, negative credentials, armored output, disabled-path regression and temporary-key cleanup.
- Existing SSH native/UI and compression UI checks rerun.

Reports: C:/ETL/Reports/pgp-native-results.txt; pgp-live-results.txt; pgp-ssh-native-regression.txt; pgp-ssh-ui-regression.txt; pgp-compression-regression.txt.

Build: C:/ETL/Tools/Build-SftpKeys.cmd. Install: C:/ETL/Tools/Install-PgpEndpoints.ps1. Server binaries are backed up under C:/ETL/Archives/pgp-project-compare-20261029/bin. Studio packaging uses Build-Studio.ps1 jar.

Upstream unattended-operation documentation: https://www.gnupg.org/documentation/manuals/gnupg/Unattended-Usage-of-GPG.html
MSYS2 package/source indexes: https://packages.msys2.org/base/gnupg and https://repo.msys2.org/msys/sources/
''',encoding='utf-8')
Path('C:/ETL/Server/docs/project-comparison.md').write_text('''# Project Differences

Installed 2026-10-29. Administration → Project Differences allows selecting two saved local Studio .jip projects, including GitHub working copies. Server-deployed project selection is not implemented; deployed projects can first be downloaded to local projects for comparison.

The comparison reads temporary project snapshots through Studio's existing project loader. Original projects are not edited, deployed or migrated. Unsaved editor changes are not included; save first when they should participate.

Objects match by type and folder/name path. Different project/object UUIDs are normalized to their logical references. A renamed or moved object appears as left-only/right-only. Definition fields are displayed in a differences grid; select an object to see changed fields and a field to inspect full left/right values below.

Excluded: GraphLayout.xml resources, graph-layout nodes, deployment flags, transient state and creation/modification timestamps. Pipeline execution ordering and mapping logic remain significant; only visual positioning is ignored. Same values with different object identities, creation dates or deployment flags compare equally. Business text and script whitespace remain significant except CRLF/LF normalization in persisted text.

Passwords, passphrases, tokens, secrets and private-key properties are masked in the UI. A password project variable is masked. Protected values are compared using SHA-256 digests held in the comparison snapshot, not displayed in the UI. This is a change detector, not an exported audit log or a general scanner for credentials embedded in script literals.

Entity resources and project filestore assets are compared by streaming SHA-256. Binary/external resource differences show hashes rather than a textual edit diff. Symbolic links/reparse entries in compared resource trees are rejected. Folder structure affects logical object paths; visual diagram positions do not.

Java implementation exception: the Swing UI and the comparison consume Studio's existing Java project/entity persistence model. Reimplementing the binary project loader and object serializers in C++ would duplicate that model. No Tomcat call is involved.

Implementation: org.etlforge.integration.client.project.compare.ProjectComparison and ui.admin.compare.

Verification: ProjectComparisonTest (12 checks) and ProjectComparisonStudioTest (11 checks, including real Compare button, asynchronous snapshot loading, selection details, resource changes and ignored layout). The packaged Studio JARs were used for the final UI test. Screenshot: C:/ETL/Reports/pgp-compare/project-differences-loaded.png.
''',encoding='utf-8')
Path('C:/ETL/Server/runtime/gnupg/NOTICE.txt').write_text('''ETLForge bundled x64 GnuPG runtime

Copied from the locally installed MSYS2 packages. gpg reports GnuPG 2.4.9 and libgcrypt 1.12.4. This is a separately executed third-party program, not cryptographic code written by ETLForge.

GnuPG: https://gnupg.org/ ; GPL-3.0-or-later.
MSYS2 package metadata is in package-metadata/; exact binary hashes are in SHA256SUMS.txt. Available upstream license texts/notices are in licenses/. Dependency licenses and source links are recorded in package metadata. Source packages: https://repo.msys2.org/msys/sources/ . Final installer packaging must include the full required notices and corresponding-source distribution arrangement; the development runtime folder is not a completed redistribution bundle.

Files include gpg, gpg-agent, gpgconf and only their selected runtime DLLs. No system installation, user keyring or global GPG configuration is used. gpg is launched with a private homedir, no-options, batch, loopback pinentry, passphrase-fd and automatic key discovery disabled.
''',encoding='utf-8')
p=Path('C:/ETL/Server/docs/server-library-review.md');s=p.read_text(encoding='utf-8');s+='\n\n## PGP runtime added 2026-10-29\n\nSource/target OpenPGP uses a separate x64 GnuPG 2.4.9 process controlled by C++, without Tomcat. The selected MSYS2 runtime is approximately 10.1 MB of binaries and includes GnuPG, libgcrypt, libgpg-error, libassuan, libnpth and support DLLs (including an internal SQLite dependency). This does not add an ETLForge SQLite connector. Preserve runtime/gnupg notices and finalize redistribution/source packaging before the installer. Consider an optional PGP installer component and test a native Windows x64 distribution to remove MSYS2/short-path assumptions. See pgp-endpoints.md.\n';p.write_text(s,encoding='utf-8')
