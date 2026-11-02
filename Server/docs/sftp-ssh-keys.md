# SFTP endpoints and SSH keys

Implemented for the Windows x64 server and Studio, version 42.0.0.0.

## Library

Transfers use the existing native libcurl transport with its libssh2 backend. The installed runtime reports `libcurl/8.23.0 OpenSSL/3.6.5 (Schannel) zlib/1.3.2 libssh2/1.11.1_DEV`; the vcpkg package is libssh2 1.11.1. No Tomcat call or external SSH process is used for endpoint transfers.

SSH 2 negotiates algorithms automatically. There is no SSH 3 option, and SSH 1 is not supported. SFTP protocol version numbers are separate from SSH protocol versions.

## Administration

Open **Administration → SSH Keys** while connected to the server.

1. Choose **New key**, enter a descriptive name, and import a matching private key and OpenSSH `.pub` file.
2. Enter the private key's passphrase if it is encrypted, then save.
3. Select a saved key to rename it, replace its material, export its public key, or delete it.

Only administrators can import, replace, rename, or delete keys. Authenticated users can list key metadata for endpoint selection. The server never returns private keys or passphrases. Public keys may be exported for installation on the destination SFTP server.

PEM/OpenSSH key files are accepted; public key types are RSA, ECDSA and Ed25519. Actual authentication depends on the server and libssh2/OpenSSL support for that key's format. Unencrypted and passphrase-encrypted OpenSSH Ed25519 keys were verified against the local SFTP server. Convert PuTTY PPK files to OpenSSH format before importing. Private files are limited to 64 KiB.

## Each source or target

Create an **FTP/SFTP/FTPS** endpoint, select SFTP under its security options, and enter the hostname, port, login, remote directory and file/filter as usual. The local test server listens on port **2022**; the usual SFTP port is 22.

Under the SFTP options:

- Select a saved authentication key. **Reload keys** fetches the current server list. The same key can be reused, or different endpoints can select different keys.
- Select **Password** to use the endpoint's existing login/password instead.
- Enter the verified server host SHA256 fingerprint. Both OpenSSH `SHA256:...` and base64 digest forms are accepted. Obtain the fingerprint from the server administrator through a trusted channel. It must match the host key negotiated by libssh2; a server can have different fingerprints for its RSA, ECDSA and Ed25519 host keys.

An assigned key uses public-key authentication only; it does not silently fall back to a password. A missing/deleted key, incorrect passphrase, or missing/incorrect host fingerprint ends the operation with an error. Ordinary FTP/FTPS endpoints do not use the SFTP options.

Projects contain `sftp_key_id` and `sftp_host_sha256` extended properties. A project backup does not contain private key material. Renaming a key preserves its ID. Deleting it causes future operations that reference it to fail until the endpoint is assigned another key. An operation already holding a key lease can finish using that lease. PostgreSQL is the authoritative key store; see `database-key-vault.md`.

## Storage and cleanup

Keys are stored beneath `<instance home>/private/ssh-keys`, currently `C:\ETL\Server\instances\dev\private\ssh-keys`. Records are encrypted with Windows machine-scoped DPAPI and restricted to SYSTEM and Administrators. The current ETLForge services run as LocalSystem. A future installer that supports a different service identity must grant that identity the appropriate vault access.

Libcurl's libssh2 interface reads private keys from filenames. Each transfer therefore gets restricted temporary `.pem` files in that directory. An open handle prevents cleanup from deleting an active lease. Files are removed when the lease closes; orphaned files after a crash are removed on the next vault access and by the FileCleanup service. The directory is transient only; persistent encrypted records are in `etlforge_security.key_vault` and are shared by every authenticated cluster node.

DPAPI records are tied to this Windows machine. Reimport keys when moving to another machine. Back up this protected directory separately from project backups. Use HTTPS for remote Studio administration when importing private material.

Windows key-file paths use strict UTF-8 to wide-path conversion. The underlying SSH library requires an ASCII path to its temporary files; a Windows short-path alias is used for non-ASCII installation paths, or an explicit error is returned if no usable alias exists.

## Verification

Reports are in `C:\ETL\Reports\sftp-keys-*.txt`.

- 17 native vault/security/cleanup checks.
- 14 Studio endpoint persistence and appearance checks.
- 23 live checks: administrator import, authenticated non-admin authorization, source download, target upload, binary/UTF-8 byte comparisons, encrypted private key, Unicode remote filename using a wildcard filter, wrong passphrase, missing key, host verification, rename stability, and transient cleanup.
- Existing global-variable endpoint tests: 20 passed.
- Existing compressed XML mapping tests: 4 passed.

The native Unicode URL percent-escape bug exposed by these transfers was fixed: UTF-8 bytes now produce two hexadecimal digits rather than sign-extended values such as `%FFFFFFC3`.

One separate legacy deployment-parser issue was observed when submitting a literal supplementary Unicode character in a filename field: the Apache child faulted inside Xerces. The SFTP Unicode-name test uses an ASCII wildcard filter and successfully transfers the actual Unicode filename. This general SOAP deployment issue remains to be investigated; it is not an SFTP authentication failure.

During development, missing-key error handling initially left owned test instance 417 running after its worker exited. The endpoint now converts key errors to OperationEngineException, and repeated negative tests finish with status 6. Only that owned test instance was recovered to error. No user project was altered.

## Build and recovery

`C:\ETL\Tools\Build-SftpKeys.cmd` builds the staged native executables, services and tests. `Install-SftpKeys.ps1` installs those binaries plus SFTP-enabled libcurl and libssh2, restarting the affected ETLForge services. `Build-Studio.ps1 jar` packages the Studio changes when Studio is closed.

Prior source/binaries are preserved in `C:\ETL\Archives\sftp-keys-20261029`. libssh2's license is included in `Server\dist\licenses\libssh2.txt`. The extra libssh2 DLL is approximately 259 KiB.
