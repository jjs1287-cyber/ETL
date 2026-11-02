# ETLForge Windows x64 installer build

Run `C:\ETL\Installer\Build-Installers.ps1`, or use the **Build ETLForge Installers** shortcut on the desktop.

Each successful invocation increments the fourth component of `C:\ETL\product-version.json`, removes the previous installer artifacts, and writes exactly two files to `C:\ETL\Installers`:

- `ETLForge-Studio-<version>-win-x64.exe`
- `ETLForge-Server-<version>-win-x64.exe`

The installers are online bootstrap installers. They download the pinned Temurin Java runtime when needed; the Server installer also downloads the pinned PostgreSQL 18 binary distribution. Download hashes are embedded in the setup programs. ETLForge program files are staged from allowlisted directories. Development sources, projects, databases, logs, credentials, test fixtures and build caches are rejected.

These are unsigned development installers until an Authenticode certificate and timestamp service are configured.
