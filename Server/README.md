# ETLForge server development

Windows x64 only. Java build and validation:

```powershell
& C:\ETL\Tools\Build-ServerJava.ps1
& C:\ETL\Tools\Package-ServerJava.ps1
& C:\ETL\Tools\Test-ServerJava.ps1
```

The Java compiler uses only third-party dependencies, never original product JARs. Rebuilt modules are in `dist/lib`. Packaging retains non-code resources from the original packages and uses rebuilt classes only. SQLite classes/drivers are excluded; DbInfo rejects SQLite configuration. PostgreSQL is the default Java server store. Runtime deployment and schema migration are not complete.

Native foundation build/tests: `C:\ETL\Tools\Build-Native.cmd`. Complete legacy Utils archive: `C:\ETL\Tools\Probe-Native.cmd`. Windows platform support: `C:\ETL\Tools\Build-WindowsSupport.cmd`. CMake rejects non-Windows and non-x64 targets.

These are reconstruction milestones, not a running operation engine or installable service. Remaining work is tracked in `C:\ETL\Reports\compilation-checklist.md`.
