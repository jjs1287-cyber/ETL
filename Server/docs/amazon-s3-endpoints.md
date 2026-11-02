# Amazon S3 source and target

Implemented and installed on the development Windows x64 system, version 42.0.0.0, 2026-10-30. Select **Amazon S3** when creating either a source or a target. Endpoint wire ID is 18. Transfers and connection testing execute in C++ through the existing libcurl 8.23.0 AWS Signature Version 4 support; they do not call Tomcat or require an AWS CLI or SDK.

## Configuration

Common fields: bucket name, region, authentication, Access Key ID, masked Secret Access Key, and optional folder/prefix. The region selector displays friendly names for common regions and also accepts a region code directly. Select Temporary Credentials to reveal a masked session-token field.

For the supplied test bucket: bucket `testing-source-target`, region `us-east-1`. Credentials are never hardcoded in the application. Do not copy them into this document or test source.

Sources support wildcard/regular-expression file matching through the existing filter options, recursive listing, and deletion after successful processing. Prefixes are literal, case-sensitive S3 key prefixes; `input/` is an example. Source rename-after-success is rejected because an S3 rename is a separate copy/delete operation. Downloads use the listed ETag to reject a changed object. Delete-after-success also uses the original ETag to avoid removing a replacement object.

Targets support the existing filename substitutions, prefixes, overwrite enabled/disabled, empty-file handling, compression, and PGP encryption through the common file pipeline. Disabled overwrite uses an atomic `If-None-Match: *` upload, rather than a check-then-write race. Duplicate output keys in one operation are rejected before upload. A combined ZIP produces one S3 object; archive source members are processed individually.

Source Test Connection checks authenticated listing and returns up to 100 object names. Target Test Connection checks listing, uploads a uniquely named `.etlforge-connection-test-<random>.txt` object within the selected prefix, then deletes it. Target testing therefore requires delete permission as well as upload permission. Supplied credentials can be tested by an authenticated non-administrator ETLForge user; anonymous access is rejected.

## Project credentials and portability

As requested, **all three credential values are encrypted in project properties and travel with save/reopen, backups, deployment, migration, and Git source control**. The fields are `s3_access_key_protected`, `s3_secret_key_protected`, and `s3_session_token_protected`. They reuse the existing portable ETLForge project-password format, decoded by OpenSSL in the native server. No Windows DPAPI store or machine-specific credential reference is introduced. Audit and project-difference views redact the access key as well as secret/token values.

This is the existing shared-application-key project protection, not encryption with a separately held project password. Someone with the repository and ETLForge application code can decrypt these values. Keep repository access equivalent to permission to use its embedded credentials. A separate portable key-management/envelope design is a future production-hardening item; do not describe the current project format as a secrets vault.

The new S3 client uses standard C++17 filesystem/streams, OpenSSL, Boost XML/JSON, and libcurl. It has no Windows API dependency. The complete product build and older shared engine/runtime remain Windows x64; macOS/Linux builds are not implemented or certified by this change.

## Transfer behavior and limits

- HTTPS only, certificate verification enabled, explicit AWS region/service signing, fixed AWS S3 host construction, and no redirect following. Dotted bucket names use path-style addressing to avoid wildcard-certificate mismatches. Commercial, China and GovCloud-style regional names are accepted; only the supplied commercial region was exercised live.
- Streaming uploads/downloads preserve binary bytes. XML/text transformation inputs are normalized once before legacy null-byte processing. Original downloaded files are preserved; UTF-16 XML transforms to UTF-8 output.
- Encoded listing keys distinguish form-encoded spaces (`+`) from literal plus (`%2B`) and correctly retain Unicode and literal percent characters. Continuation tokens are followed until listing is complete. Listing is bounded to 100,000 objects; narrow the prefix for larger collections.
- Three attempts for transient connection failures and HTTP 408/429/500/502/503/504, with one- and two-second delays. Permission failures and conditional-write failures are not retried. Failed downloads remove their partial local file. The client supports a cancellation callback; operation cancellation continues through the existing worker lifecycle.
- Single PUT uploads are limited to **5 GiB** in this implementation. Multipart upload/resume is not implemented. Downloads stream without that upload limit. S3-compatible custom endpoints, S3 Express/directory buckets, access-point ARNs, IAM-role discovery, and automatic temporary-session refresh are not implemented.
- Keys containing standalone `.` or `..` path segments are rejected to avoid libcurl URL normalization changing the requested object.
- A prefix matching no files follows the existing engine behavior: completed with warning, with no output file.
- Existing compression extraction limits and PGP key management remain in effect. Genuine AWS temporary credentials and a live S3 PGP round-trip were not included in this access-key test run.

## Verification

Reports under `C:/ETL/Reports`:

| Report | Coverage |
|---|---|
| `s3-studio-tests-final.txt` | Installed chooser entries, editors, encrypted persistence/copy/deploy properties, masked fields, dirty tracking and friendly region codes |
| `s3-native-tests.txt` | URI/UTF-8 validation, pagination, downloads by listed key, binary/Unicode transfers, overwrite protection, ETag checks, partial-file cleanup and authentication failures |
| `s3-test-connection-live.txt` | Source listing, target write/delete probe, error validation and authenticated non-admin access |
| `s3-live-operations.txt` | Real deployed source/target operations, ZIP creation/extraction, Unicode, expected failures, delete-after-success and empty prefix |
| `s3-live-xml-mapping.txt` | Real XML mapping using S3 for both source and target, with UTF-16 input |
| `s3-verified-outputs.txt` | Independent native retrieval and byte/content verification of actual operation outputs |
| `s3-existing-endpoint-editors.txt` | All 16 existing source/target editors still create/display/redisplay |
| `s3-utf8-regression.txt` | Existing 16 live text/binary/encoding/XSLT checks |
| `s3-local-file-regression.txt` | Existing deployed local file-copy operation remains byte-identical |

Tests use an isolated `etlforge-tests/<UUID>/` prefix. Remote test objects are removed after verification; the test bucket and AWS key remain active. Development-only protected test configuration is under Server/private and is not a production default.

Two shared-engine gaps found by these tests were repaired: legacy narrow-path file existence/size/copy checks for extracted Unicode names, and normalization of XML/text input before the legacy null-byte stripping stage. JDBC and Salesforce connector logic was not changed.

## Build and install

`C:/ETL/Tools/Build-S3Endpoint.cmd` builds staged native services and the separate test executable. `C:/ETL/Tools/Install-S3Endpoint.ps1` checks for an empty operation queue, backs up and checksums staged components, and stops/restarts only the affected services that were already running. Studio uses the normal `Build-Studio.ps1 jar` guard and must be closed for JAR installation.

No database schema migration is needed. Keep Studio and server endpoint definitions together when deploying this update. Source backups are under `C:/ETL/Archives/s3-endpoint-20261030-164954`; shared Unicode-helper originals are under `C:/ETL/Archives/s3-unicode-file-helpers`. Installed server binary backups are recorded in `s3-server-install-final.txt`.

Primary references: [libcurl AWS SigV4](https://curl.se/libcurl/c/CURLOPT_AWS_SIGV4.html), [S3 ListObjectsV2](https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListObjectsV2.html), [S3 PutObject conditional writes](https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html).

## Disconnected Studio behavior (2026-10-30)

Test Connection checks the Studio connection before saving or starting a test. When disconnected, it displays only 'Not connected to a server.' in an informational Test Connection dialog; it creates no exception entry or stack-trace Details dialog. A disconnect during testing receives the same simple notice. The shared request helper now uses feature-neutral disconnected wording. This is existing Java/Swing UI handling; the native C++ S3 transport is unchanged. Installed-JAR verification passed 16 disconnected-state checks and 34 S3 editor regression checks. Reports: s3-disconnected-tests-installed.txt and s3-disconnected-editor-regression.txt.

