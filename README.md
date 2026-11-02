# ETLForge

Windows x64 ETL reconstruction workspace. Server: C:\ETL\Server. Studio: C:\ETL\Studio. CMake rejects 32-bit builds.

Current build status and commands: Reports/toolchain-verification.md.

- Tools/Build-Native.cmd builds native foundation libraries and runs four tests.
- Tools/Probe-Native.cmd compiles the complete 39-file legacy utility library.
- Tools/Build-ServerJava.ps1 attempts the Java Server candidate build; compiler repairs remain.
- Server/src/native contains the working v4 native source baseline.
- Server/src/java/modules contains the current Server Java recovery candidates.
- Reference and component/recovered preserve the upstream source and binary recovery evidence.

This workspace is not yet a runnable ETLForge Server/Studio release. Original package/class names remain during baseline reconstruction. Required upstream attribution and third-party notices must be preserved during rebranding.

PostgreSQL 18 x64 is downloaded under Downloads. Existing installed Jitterbit, PostgreSQL and PgBouncer services have not been changed.
