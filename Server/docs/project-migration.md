# Local project migration

File → Migrate copies the current saved Studio project into another existing local Studio project. Select its `.jip` file, preview the object list, then choose Migrate. Close the destination project before starting.

Matching objects use the same GUID first. If GUIDs differ, migration matches the same object type and name, preferring the same folder path. Ambiguous names stop migration. Destination object identities are retained and copied references are translated to those identities. Project Variables are runtime names: if a destination variable sharing an old GUID was renamed, it remains intact and the missing incoming variable name is created with a new identity.

Existing destination Project Variables, sources and targets are preserved in full, including values, password flags, descriptions, location settings and key references. Missing objects of these types are created. Other matching objects are replaced. Destination-only objects are retained. The destination project name and identity remain unchanged. Runtime global variables are not persistent objects; this feature migrates saved Project Variables.

Operation pipelines, child operation references, script tags, schema assets, object resources and plugin associations are copied. Migration does not deploy or run operations. Schema and binary resource bytes are copied unchanged.

The destination is locked while a sibling working directory is prepared. Its original content is copied to `<destination-folder>-before-migration-<timestamp>` before merging. The working copy is saved before destination files are replaced. A reported commit failure attempts to restore the backup and remove newly added files. If Windows stops or the process is forcibly terminated during the file replacement phase, the complete sibling backup remains available for recovery. Retain or remove these backups deliberately; they are not automatically rotated.

The preview lists names, types and Create/Replace/Preserve actions without displaying variable values or passwords. The destination is reread under its project lock during execution. Incompatible references or ambiguous matches fail without committing the working copy.

## Implementation choice

This feature runs in Java inside Studio because the existing project object model, GUID/reference replacers, resource stores, plugin metadata and persistence services are Java APIs. Reimplementing their formats in C++ would duplicate that model and make project compatibility less reliable. No server runtime function or per-record transformation uses Java as part of migration, and no Tomcat calls are added.

Implementation: `C:\ETL\Studio\src\main\java\org\etlforge\integration\client\project\migration\ProjectMigration.java`.

Regression runner: `C:\ETL\Server\tests\integration\ProjectMigrationTest.java`.

Recovery copies of modified existing source/configuration files: `C:\ETL\Archives\project-migration-20261029`.
