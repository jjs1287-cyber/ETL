# Source and target compression

Implemented for Windows x64 in C++ using libarchive 3.8.9 and liblzma 5.8.4. No Tomcat calls, external archiver process, or Java archive processing is used at runtime. The Studio editor remains Java/Swing.

## Studio

Open a file-based source or target, expand **Options**, and use **Compression**. Compression is disabled by default. Existing endpoints retain their behavior and do not acquire compression properties merely by opening and saving the editor.

Sources expose an enable checkbox and masked password field. The server detects the archive by contents rather than its extension. Each regular file becomes a separate input to the existing operation pipeline. Nested folders are extracted safely under the operation's working directory; target filename patterns use each member's leaf filename. Nested archives are not recursively unpacked.

Targets expose enable, format, output mode, combined archive name, and masked password. The target's existing filename/pattern determines the member filename before compression. Use `[file].[ext]` or another unique pattern for multiple files. Repeated member/output names are rejected rather than overwritten. The combined archive name defaults to `output`; its format extension is added automatically.

| Format | Source | Target | Password |
|---|---|---|---|
| ZIP | Auto-detected | Combined or individual | AES-256 writing; supported ZIP encryption reading |
| TAR.GZ | Auto-detected | Combined or individual | Not supported by the format |
| GZIP | Auto-detected | Individual, or combined only when exactly one file exists | Not supported by the format |
| 7z | Auto-detected | Combined or individual | Encrypted 7z is not supported by this implementation |
| RAR/RAR5 | Auto-detected where supported by libarchive | No writer | Encrypted/multipart RAR is not offered |

**Combined:** all successful generated files are compressed into one staged archive after transformation and pre-target plugins, then sent to the existing target transport. Nothing is delivered until archive creation succeeds. An existing file error prevents creating a partial combined archive.

**Individual:** every generated file is compressed into its own archive before delivery. For example, `customer.xml` becomes `customer.xml.zip`. Output archives are prepared before the target transport starts. Delivery failures retain the transport's existing behavior; this does not add a cross-file or remote atomic transaction.

Source delete/rename processing applies once to the original archive, after successful processing/delivery. A failed member prevents that original archive's success action. Ordinary source/target behavior remains on its existing path when compression is disabled.

## Passwords and scripts

Archive passwords use Studio's existing protected password representation for project persistence and deployment. A dedicated C++ decoder handles that representation; passwords are not sent to Tomcat. Masking is not a replacement for restricting project and database access. Audit fields named `compression_password` are covered by the existing password redaction rule.

`ReadFile` can read a compressed source containing one regular member. It reports an error for multiple members; use an operation source to process all members. `WriteFile`/`FlushFile` compress the file being flushed; combined mode there means one archive per flush, not an operation-wide accumulator. Use the normal operation target for an archive containing every file in the operation.

FTP uses binary transfer when compression is enabled, even if ASCII mode was previously selected. SFTP enablement remains a separate, previously paused task; compression does not change transport availability.

## Limits and cleanup

Extraction uses streaming I/O and a cumulative operation budget of 2 GiB and 10,000 regular files; individual archives also limit header entries, including directories. Oversized declared members are rejected before writing their contents. Wrong passwords, malformed archives, absolute/traversal paths, Windows reserved names, links, special files, and duplicate case-insensitive member names are rejected. Partial extraction directories and failed archive output files are removed.

Working files are staged beneath the operation working directory and follow the existing successful-operation cleanup and failed-operation retention rules. Original source archives are preserved unless their configured after-processing action explicitly removes or renames them.

## Build, deployment and recovery

- `C:\ETL\Tools\Build-EndpointCompression.cmd`
- `C:\ETL\Tools\Install-EndpointCompression.ps1`
- `C:\ETL\Tools\Build-Studio.ps1 jar` (requires Studio closed)
- Previous sources and installed server binaries: `C:\ETL\Archives\endpoint-compression-20261029`
- Native dependencies: `archive.dll`, `liblzma.dll`, existing `z.dll`, Windows CNG and VC/UCRT runtime.
- Dependency license notices: `C:\ETL\Server\dist\licenses\libarchive.txt` and `liblzma.txt`.

## Verification

Reports under `C:\ETL\Reports\endpoint-compression-*` cover the native archive engine, editor persistence and deployment, service-backed operations, and an XML mapping with multiple compressed members. Generated archives are independently extracted with 7-Zip and compared byte for byte with the originals. Wrong-password and unsupported-format combinations must finish with operation status 6 and no delivered files. Compression-disabled transfer must preserve the original archive bytes.
