# ETLForge

Windows x64 ETL application derived from the Jitterbit 4.1 source baseline. Active server code is under `Server/src`; Studio source is under `Studio/src`. Upstream notices and licenses are retained.

## Development on this computer

The configured workspace is `C:\ETL`. Desktop shortcuts open Visual Studio on `Server` and Eclipse on `C:\ETL\EclipseWorkspace`. The Eclipse launch named **ETLForge Studio** uses `Studio/run-user-home-etlforge` for test state.

Prerequisites are Visual Studio's Desktop development with C++ workload and CMake/Ninja tools, Python 3, the portable Java 8 JDK, Ant, and the pre-provisioned native/Java dependencies. IDEs, SDKs, compiled dependencies, runtime instances and build output are not stored in Git. See `DEPENDENCIES.md` before building a new checkout.

From PowerShell in the workspace:

```powershell
# Native default targets, five server executables, and all registered local CTest tests.
& .\Tools\Build-Native.cmd

# A fresh CMake configuration without relying on the existing build cache.
& .\Tools\Verify-CleanConfigure.cmd

# Java server compilation, JAR packaging, and smoke tests.
& .\Tools\Build-ServerJava.ps1
& .\Tools\Package-ServerJava.ps1
& .\Tools\Test-ServerJava.ps1

# Studio compilation, packaging, and smoke tests; close a running Studio application first.
& .\Tools\Build-Studio.ps1 -Target test

# Standalone native XSLT worker.
& .\Tools\Build-NativeXslt.cmd
```

`Set-DevEnvironment.cmd` finds Visual Studio using vswhere and selects the x64 compiler. `DevEnvironment.ps1` supplies shared Python/JDK/Ant settings. Set `ETLFORGE_PYTHON` to select Python explicitly. CMake resolves source and dependency paths from its checkout root. Some legacy recovery/integration tools retain `C:\ETL` as their documented test workspace; their historical upstream project files are not the active build system.

The server baseline is C++14; newer components request C++17 individually. Studio targets Java 8. Eclipse runs on its own bundled Java runtime and uses Java 8 for the project.

## Eclipse workspace validation

```powershell
# Close Eclipse if using the default workspace, or use a separate workspace as shown.
& .\Tools\Validate-StudioEclipse.ps1 -WorkspacePath C:\ETL\Reports\eclipse-check
```

The helper is built from versioned source and generates its configuration from the installed Eclipse. It does not depend on a copied machine-specific Eclipse configuration.

## Test fixtures and source control

Sample projects and SSH regression keys under `Samples` are intentionally included test data. Build artifacts, generated results, IDE caches, local private configuration and runtime instances are excluded. The test-data Git repository `FTLForge-Project1` is independent of this application repository.

Keep implementation tasks on branches/worktrees and review before merging. Local Aider is configured separately under `C:\Tools\LocalAI`; it is not required to compile the application.

A successful build and local smoke tests do not certify SFTP credentials, running Windows services, database migrations, or multi-server integrations. Those tests require their configured test targets and are run separately.
