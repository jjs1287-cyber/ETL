# ETLForge Studio development

Windows x64 is the supported target. Build and run with the portable JDK 8 at `C:\ETL\Tools\jdk8u504-b01`. Eclipse uses its own bundled Java runtime.

Open `Open-in-Eclipse.cmd` to use the dedicated `C:\ETL\EclipseWorkspace` workspace. The project is ETLForgeStudio. `C:\ETL\Tools\Validate-StudioEclipse.ps1` imports the project, registers its Java 8 JDK and performs an Eclipse build.

Build and smoke-test from PowerShell:

```powershell
& C:\ETL\Tools\Build-Studio.ps1 -Target test
```

Run `Launch-Studio.cmd` after building. Development runtime state is isolated under `run-user-home`. A functioning agent connection is a separate integration milestone.

Data Loader mode is disabled in ClientInfo regardless of legacy configuration. Its standalone migration component and Spring bean have been removed from the active project. Ordinary project deserialization classes named ProjectDataLoader remain essential to Studio project loading.

SQLite source and its JDBC driver are excluded from the active source and dependency paths. Entity settings and file resources use the original XML/filesystem implementation, adapted from v4 to the current interface. The old XML-to-SQLite migration and XML deletion are disabled. Existing SQLite project stores are not converted by this build.

Operation profiling remains excluded. See `features/profiling/README.md` for restoration requirements. Removed feature sources are preserved under `features` and the original recovery directories.

Original package and module names remain during reconstruction. Product-wide branding changes and the agent build are separate work still to complete.
