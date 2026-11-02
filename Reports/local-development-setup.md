# Build and source upload verification — 2026-11-02

The source import is on branch setup/local-ai. The application repository is https://github.com/jjs1287-cyber/ETL.git. Sample projects and SSH test keys are intentionally included as confirmed test data. Local runtime instances, private configuration, SDK installations and generated build output remain excluded.

## Verified results on the development laptop

- MSVC x64 build of the default native targets and etlforge_server_executables: PASS.
- Freshly linked ProcessEngine, Scheduler, OperationEngineProc, Control and FileCleanup executables: PASS.
- CTest: 12/12 local tests passed, including SMTP and SOAP loopback fixtures.
- Standalone SaxonC XSLT worker: compile/link PASS.
- Fresh CMake configuration in a separate empty build directory: PASS.
- Server Java compilation: PASS (existing upstream/deprecation warnings).
- Packaging: 32 rebuilt server modules.
- Server Java smoke test, including rebuilt JAR origin checks: PASS.
- Studio Ant build and smoke tests: PASS (23 reported checks plus XML/settings/resource/session checks).
- Fresh Eclipse workspace full build: zero error markers, 189 resolved classpath entries, Java 8 configured.
- PowerShell syntax audit of top-level Tools and Installer scripts: zero errors.

## Configuration repairs

- Added shared discovery of Visual Studio x64, CMake, Ninja and Python; updated 62 native wrappers.
- Removed per-account Python paths from active CMake/build configuration.
- Active CMake source/dependency paths derive from the checkout root.
- Added the installed dependency prefixes so libssh2 and curl work with a fresh cache.
- Every Ninja configuration wrapper explicitly uses the discovered Ninja executable, replacing stale short-name cache paths.
- Ant uses relative JDK/dependency paths; primary PowerShell build entry points share the workspace root.
- Server Java packaging discovers modules from source instead of relying on a generated historical CSV.
- Server Java tests include the XSLT dependency location and verify freshly rebuilt packaged modules.
- Eclipse's import helper is versioned and generates its configuration from the installed IDE; its JDK location is initialized before registration events.
- Studio packaging guards apply to test/run targets that also package JARs.

## Audit scope

The initial scan examined 16,392 candidate files (96.3 MiB). It identified intentionally versioned private SSH test keys and dummy token patterns in tests. Exact comparisons against local private password values and common encodings/hashes found no copied values in the candidate source. Raw local audit details are in source-import-audit.json and private-value-leak-audit.json; those reports are not uploaded.

This confirms compilation and the listed local checks. It does not certify all runtime service settings, external account credentials, deployment behavior, SFTP integration or multi-server failover. No integration services were reconfigured or restarted.

External SDKs and prepared dependencies are not included in this source repository. See DEPENDENCIES.md for the existing required layout; a clone alone does not provision a new machine.

## Local evidence

- git-import-native-build.log
- git-import-clean-configure.log
- git-import-native-xslt.log
- git-import-server-java.log
- git-import-server-package.log
- git-import-server-java-tests.log
- git-import-studio.log
- git-import-eclipse.log
- eclipse-studio-validation.txt
- powershell-config-audit.json

Desktop shortcuts remain available for Visual Studio, Eclipse and the local Aider C++ sandbox. Aider's earlier disposable edit/build/test passed 11 cases; it is not a dependency of the application build.

A Windows-reserved fixture filename, nul.bin, prevented Git from indexing the samples. Its three-byte NUL-content fixture is now versioned as nul-bytes.bin, and the integration test references the portable name. The old local file is preserved and ignored. Fixture inputs are stored without Git line-ending conversion.
