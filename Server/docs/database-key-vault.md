# ETLForge database key vault

## Design

PostgreSQL is the authoritative store for SSH and OpenPGP key records. Records live in
`etlforge_security.key_vault`; the schema and table are not accessible to `PUBLIC`.
Logical key UUIDs remain stable, so project endpoint references work on every server in
the cluster. Cloud-logging passwords and bearer tokens are also stored in their existing
shared database configuration using the same portable authenticated-encryption scheme.

Private keys and passphrases are encrypted with AES-256-GCM before they are sent to
PostgreSQL. A random 96-bit nonce and a 128-bit authentication tag are stored with each
ciphertext. OpenSSL supplies the portable cryptography implementation. The active server
does not use Windows DPAPI.

The cluster vault master secret is stored outside the database in the protected server
configuration as `[Security]/KeyVaultMasterKey`. Every server joined to the same cluster
must receive the same secret through the authenticated join/install workflow. Keeping the
secret outside PostgreSQL prevents a database-only disclosure from exposing private key
material. Backups must include both the database and a separately protected copy of this
secret. Never print it in logs or place it in a project repository.

## Runtime behavior

Administration APIs return metadata and public keys only. They never return private keys
or passphrases. Libraries that require a filename receive a restricted, short-lived PEM
file. It is deleted when its lease ends, and orphan files from an abnormal termination are
removed by key-vault access and FileCleanup. The database remains authoritative; the
temporary directory is not a key store.

## Migration

Migration `015-key-vault.sql` creates the database objects. The one-time
`C:\ETL\Tools\Migrate-LegacyKeyVault.ps1` utility may use DPAPI only to read records made
by the previous Windows-only implementation on their originating machine. It imports and
verifies every record through the normal server API before archiving the old files. DPAPI
is not linked into or called by the running key-vault implementation. The corresponding
`Migrate-LegacyCloudCredentials.ps1` utility rewraps legacy cloud credentials through the
authenticated administration API.

## Cluster and recovery requirements

- Joining nodes must prove cluster membership and receive the existing vault master secret;
  generating a different secret would make existing records unreadable.
- Restoring PostgreSQL without the matching master secret is insufficient for recovery.
- Rotating the master secret requires decrypting and re-encrypting every record in one
  coordinated maintenance transaction. Automated rotation is future work.
- A node must fail closed when the secret is missing or a GCM authentication tag fails.
- Deleting a key invalidates future endpoint leases but does not change the key UUID stored
  in a project.

