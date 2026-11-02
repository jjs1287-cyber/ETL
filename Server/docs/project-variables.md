# Project variables

Implemented and installed on 2026-10-29 for the Windows x64 server and ETLForge 42.0.0.0.

In Design, use the **Project Variables** category to create a Project Variable. Enter its name without `$` and enter its preset value, then save and deploy. For example, name `ApiBaseUrl` with preset `https://example.invalid/api`.

Use it in a script or mapping:

```
<trans>
WriteToOperationLog($ApiBaseUrl);
$ApiBaseUrl = 'a value for this run';
</trans>
```

Or in JavaScript:

```
<javascript>
var baseUrl = ETLForge.GetVar('ApiBaseUrl');
ETLForge.SetVar('ApiBaseUrl', baseUrl + '/customers');
</javascript>
```

Presets are literal text, including empty strings. They are not scripts, expressions, or references to other variables. Use the existing conversion functions when a number or Boolean is needed. Names start with an ASCII letter or underscore and may then contain letters, digits, underscores and periods, up to 256 characters. Do not use system prefixes `etlforge.`, `jitterbit.`, or `__`. For dotted names in JavaScript, use GetVar/SetVar.

Each operation initializes its project's defaults in C++ before constructing the operation and reading its source. Existing globals take precedence, including inherited globals and server predefined globals. Explicit operation-session globals are then restored and take precedence over presets. Synchronous and asynchronous calls retain the existing global-variable inheritance behavior. A child operation fills missing defaults without resetting an inherited runtime value. An independent run initializes again from the deployed presets. Runtime assignments do not modify the project or its deployed defaults.

There is no Tomcat call for initialization and no per-row database lookup. Initialization reads the project variable definitions once at the operation boundary. Hosted web service operations also use this initialization path. Studio expression Test seeds globals from the editor's project and preserves explicit test values.

Partial project creation includes project variables, so deploying a selected operation or script also includes the definitions. Project save/reopen, cloning, and generic project persistence include them. Renaming a definition does not rewrite `$OldName` references in scripts; update those references as part of the rename.

Definitions are ordinary project configuration, not a secret vault. Values are limited to 1 MiB of valid UTF-8 text without NUL. They are stored as UTF-8 text in PostgreSQL. The deployment transport uses Base64-encoded UTF-8 to preserve supplementary characters and exact whitespace through the legacy SOAP stack. Native deployment and runtime database boundaries also use ASCII-safe encoded transport, with a single UTF-8 decode into the runtime value.

Audit capture includes Project Variables, including creation, preset/name changes and deletion. Unchanged redeployments do not create an event. Presets appear as readable previous/current values. Audit ODBC statements, parameters and results now use wide APIs with strict UTF-8 boundary conversion; no system ANSI conversion is involved.

## Implementation

- Studio entity type 35, editor and Project Variables category; names available through the existing global-variable browser/completion data.
- Migration: `C:/ETL/Server/database/migrations/010-project-variables.sql`.
- Native initialization: `C:/ETL/Server/src/native/integration/konga/OperationContext/ProjectVariableDefaults.h` and OperationEngine initialization paths.
- Deployment: HandleChangedDataImpl and the existing generic entity properties protocol; the SOAP contract is unchanged.
- Project variable membership, GUID and permission tables follow the existing project entity convention. Duplicate names within a project and reserved/invalid names are rejected by the server inside the deployment transaction.
- Same-named variables in different projects have independent defaults.
- Studio uses Java for its existing Swing editor/model; execution and initialization remain C++.

## Verification

Installed JARs, without the source classes directory:

- 20 Studio checks: entity identity, names, value validation, exact Unicode/whitespace save/reopen, clone, partial project inclusion, discovery, Test values/overrides, deployment serialization and icon.
- 18 live checks: initialization, blank/literal values, trans/JavaScript sharing, script/operation inheritance, fresh independent runs, preset-only redeployment, reserved-name rejection, duplicate rollback, deletion, project isolation, exact audit details, no event on unchanged deployment, and live Studio Test.
- 65 PostgreSQL audit checks, including wide Unicode statement/parameter/result handling, streaming boundaries, malformed UTF-8 rejection, transaction/retention behavior and stable read-only snapshots of existing objects. Test connection uses `LFConversion=0` to avoid the PostgreSQL ODBC driver's optional newline translation.
- 26 existing live deployment/audit regression checks and 8 XSLT audit lifecycle checks.
- 21 existing live mixed JavaScript/trans runtime checks; the native JavaScript suite also passed its 166 checks.

Reports: `C:/ETL/Reports/project-variables-*.txt`. Isolated runtime fixtures use project GUID prefix `42480000-` and files under `C:/ETL/Samples/ProjectVariables`. No customer project was modified. The failed isolated audit test schema was removed; the passing audit database test removes its own schema. There are no pending/running fixture operations at closeout.

## Build and rollback

`C:/ETL/Tools/Build-ProjectVariables.cmd` builds native components into a staging directory. `C:/ETL/Tools/Install-ProjectVariables.ps1` installs the coherent component set with service stop/restart and rollback on copy failure. Build Studio with `C:/ETL/Tools/Build-Studio.ps1 jar` while ETLForge is closed.

Original source files, native binaries and Studio JARs are archived under `C:/ETL/Archives/project-variables-20261029`. Restore the native binary set together with the archived Studio JAR set if rolling back. Preserve the migration tables and project backups; older binaries will not initialize the new definitions. Do not deploy a project containing the new entity type through an older Studio.

## Project variable editor (2026-10-29)

The editor shows Name, a single-line Value, an Is password checkbox, and a four-line Description box. Checking Is password masks the value with asterisks while retaining its current contents. Unchecking it clears the value immediately rather than revealing it. Save/deploy the change to persist the cleared value. Canceling an unsaved edit retains the previously saved definition.

Password mode masks this editor only; it does not introduce encrypted storage or a secret vault. Runtime access remains the same global variable behavior. Description and password mode are retained in project files and deployment metadata (migration 011-project-variable-editor.sql). Empty/default metadata is omitted from audit normalization so pre-existing project-variable audit baselines stay compatible.

Verification: 11 editor checks against installed JARs, 20 Studio regression checks, 18 live runtime regression checks, live deployment and PostgreSQL assertions for password mode, exact Unicode description, and value clearing. Render inspected at C:/ETL/Reports/project-variable-editor.png. Prior sources archived at C:/ETL/Archives/project-variable-editor-20261029. Swing editor changes use Java because Studio is a Java Swing application; deployment processing remains native C++.
