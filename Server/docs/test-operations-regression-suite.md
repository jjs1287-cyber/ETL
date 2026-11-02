# Test Operations regression suite

## Running it

Open **Test Operations**, connect to **localhost:47100**, expand **Operations > Regression runner**, and run **00 - Run All Regression Tests**. The runner and its executable dependencies are already deployed on this computer.

The master runs 134 checks sequentially and continues after a failed check. Each check writes PASS or FAIL in its operation log. The master writes a summary and fails if any check fails. Read the wrapper/check operation's result: some underlying fixtures intentionally fail to test rejection of invalid input. A green operation with an information icon/status 4 is normal when its script writes a PASS message.

The latest CSV is `C:\ETL\Samples\RegressionSuite\results\latest.csv`. It is overwritten each run. A command-line equivalent is `C:\ETL\Tools\Run-RegressionSuite.ps1`; it returns a failure if the master fails or the CSV is incomplete.

Run one master at a time. Fixtures deliberately share output paths and preset values. This is a development regression project for this laptop, not a production workload or an installer acceptance certificate.

## Project contents

The original five user operations remain in place. The 234 original deployed operation definitions, including those five, are retained; the older ones are grouped under **Historical fixtures**. The default runner uses current scenarios instead of repeatedly executing obsolete copies of the same fixture. It includes 134 check operations, underlying fixtures, scripting fixtures, archive verifiers, sources, targets, formats, XSLT assets, project variables, and required schema/WSDL files. There are 468 operations total, including all setup/helper operations.

Some old prototypes reused an ID for different object types. Those local historical objects were given distinct IDs, with their links repaired. `C:\ETL\Reports\regression-suite\historical-id-remap.json` records the additional metadata remapping; recovered cloud-logging scripts also have distinct deterministic IDs. The original catalog remains under `C:\ETL\Reports\regression-suite\catalog` and `operations.txt` for traceability.

Historical prototypes are reference material. Some use old data, accounts or configuration and are not certified by the default run. Do not start all historical operations indiscriminately, attach an automatic schedule to the whole suite, or treat an intentionally invalid scripting fixture as a coding example to deploy independently. If you edit a regression fixture, update the builder/deployment manifest and rerun the checks.

## What the master checks

| Area | Checks and assertions |
|---|---|
| Native scripting | Existing local-variable cases: assignment, reassignment, operators, arrays/maps, case sensitivity, empty/null/false values, branches/loops, arguments and read-before-assignment rejection. |
| JavaScript | Existing mixed-block cases, static text, multiple blocks, shared globals, exact results, Unicode, JSON, Math, strict mode and sandbox rejection; timeout fixture; trans/JavaScript XML, JSON and CSV output parity. |
| Project/global variables | Presets, exact Unicode/whitespace/literal values, child inheritance/overrides; global-variable source/target reads, writes, replacement, XML mapping and rejection of unsupported values. |
| File/UTF-8 transformations | Local file copy and byte comparison; XML/JSON transformations; UTF-16 S3 XML to UTF-8; multilingual payloads and Unicode SFTP filenames. |
| XSLT | Named XSLT operation and RunXSLT scripting examples. |
| Compression | ZIP, encrypted ZIP, 7z, GZIP, TAR.GZ, combined and individual targets, source detection, compression disabled and invalid inputs. Generated archives are extracted and their member bytes compared with the originals. |
| PGP | Encryption/decryption, combined ZIP plus PGP, ASCII armor, disabled encryption and invalid key/passphrase cases. Decrypted binary/Unicode members are compared with originals. |
| SFTP | Key authentication for sources/targets, encrypted keys, host pinning, invalid/missing key/fingerprint/passphrase rejection, Unicode filenames and binary output comparisons. |
| Amazon S3 | Setup of owned test keys, source/target transfer, recursive matching, compression, invalid credentials, overwrite refusal, delete-after-success, empty prefix, XML conversion, and prefix cleanup. Cleanup verifies the prefix is empty. |
| Web services | Local SOAP call through the hosted Echo service. This exercises caller and hosted operation types together. |
| Service examples | Invocation of the scheduler example, cleanup success/failure fixtures and cloud-log producer. These are execution checks; they do not prove every service-management/retention/upload/failover behavior. |

Output readers use freshness markers before execution where applicable. Expected failure checks examine the relevant error, rather than treating an arbitrary exception as a pass. Fixture operations ignore file-history suppression so an unchanged input is processed again on the next run.

## Prerequisites and bounded artifacts

The local ETLForge services and databases must be running. SFTPGo is the external test server on 127.0.0.1:2022; a dedicated key-only test user and dedicated regression SSH/PGP records are retained. Test key material stays outside the project. The persistent PGP fixture has no expiration; it is for tests only and must never be used in production.

The AWS test bucket is `testing-source-target`, region `us-east-1`. Only the owned prefix recorded in the protected local S3 fixture is used. Setup recreates the inputs; the last check removes the prefix's objects and verifies deletion. The bucket and access key stay active. Cleanup overwrites one local ZIP instead of creating unlimited unique output files. Other test outputs use fixed paths. Existing server policies still govern successful/failed working files and operation-history retention.

Project credentials remain in the project's existing encrypted format; there are no embedded private SSH/PGP keys. This format is application encryption, not an independent secrets vault. No GitHub push was performed as part of consolidation.

## Coverage that still needs separate checks

Use [the screen runbook](studio-screen-test-runbook.md). Opening an editor is not an end-to-end connector test. The manual/separate-test inventory includes:

- Live LDAP, standalone FTP/FTPS, SMB endpoint and JDBC scenarios with dedicated configured fixtures; Salesforce organization-specific tests, especially writes.
- Audit capture/deletion/retention/layout-only behavior; cloud-upload destination/retry/backlog/restart behavior; scheduler timing and occurrence deduplication; cleanup retention/crash recovery; proxy/configuration/log retrieval; Git clone/commit/push and migration/differences execution.
- Multi-computer behavior, installer/upgrade/uninstall, TLS/SSH tunnel and failover scenarios. A one-laptop run cannot certify these.
- The legacy server **Download Project** API remains unimplemented. Git/source-control project opening is a separate feature.
- Fixture preparation exposed an Apache/Axis failure when supplementary Unicode characters were used in deployment metadata names. Fixture names use ASCII; Unicode payloads and SFTP filenames are tested. The metadata-deployment failure remains a separate issue.
- The function catalog advertises DeleteFile/DeleteFiles, but the native evaluator rejected DeleteFiles during preparation. The suite uses freshness markers instead. These advertised scripting functions require a separate implementation/compatibility review.

These are coverage gaps/known issues, not silently skipped PASS results. Do not describe the suite as proof that every advertised feature is complete.

## Verification and maintenance

Evidence lives in `C:\ETL\Reports\regression-suite`: manifests, CSVs, deployment/run reports, project loading, schema-reference checks, credential checks and editor checks. The full registered screen-opening companion passed 107 checks with zero logged errors; the consolidated project's eight persisted editor checks also passed.

The build found and fixed two native UTF-8 filesystem boundaries: local files opened by curl transfers now use strict UTF-8 to Windows-wide paths, and directory traversal uses UTF-8 filesystem paths instead of the Windows code page. The resulting Windows x64 components were built, backed up, installed and exercised by the suite.

Maintenance sources: `Build-RegressionProject.py`, `ExportRegressionCatalog.java`, `CompleteRegressionCatalog.java`, `DeployRegressionProject.java`, `FinalizeRegressionProject.java`, and the fixture/asset tools beside them. Java here assembles Studio test models and calls existing test APIs; product transfer/file functionality remains native C++.

When adding a product feature, register a deterministic check, expected result, prerequisites and cleanup in this project. Add its screens to the runbook. Retain expected-failure assertions and verify a second unchanged run to catch file-history suppression, stale outputs and fixture expiry.
