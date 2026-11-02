# Global Variable sources and targets

Select **Global Variable** in either the new Source or new Target chooser. Enter a variable name such as `Payload` or `$Payload`. Both spellings refer to `$Payload`; the field names the variable rather than evaluating its value.

A source reads the current operation's string value. Set it in an earlier script or use a deployed Project Variable preset. An unset, null, numeric, collection, or binary value is an error; an empty string is valid. The source leaves the variable unchanged.

A target replaces the named variable with the complete output text. Read it from a later script as `$Payload` or JavaScript `ETLForge.GetVar('Payload')`. The value has the same lifetime and inheritance rules as other operation globals; writing a target does not edit the deployed Project Variable preset.

The endpoint supports archive/copy operations, ordinary transformations, and script `ReadFile`, `WriteFile`, and `FlushFile`. Flush a script target before reading its new value. Multiple writes before a flush use the existing WriteFile buffer; a later flush replaces the prior variable value.

Only UTF-8 text without NUL is accepted. Multiple output files in one transfer are rejected before assignment, rather than silently keeping only the last file. This endpoint is not binary storage. It uses the existing per-operation working-file pipeline and cleanup, so it does create temporary working files. Global-variable access and transfer are native C++; no Tomcat request is required.

**Test Connection** validates the variable name, without checking its value. Runtime variables are available only inside the operation. The result message states that distinction.

## Implementation

- Source/target type ID 17; existing IDs and PostgreSQL schemas unchanged.
- Studio: GlobalVariableLocation, GlobalVariableLocationPanel, connection/configuration validation, search collection, flat endpoint chooser, persistence and JAR include lists.
- Native: GlobalVariableSource and GlobalVariableTarget in OperationObjects/GlobalVariableLocation.h, wired through both database and key/value location factories.
- GlobalVariableTarget uses the bound operation holder. Legacy WriteFile's flush path passes a separate temporary holder; using that temporary holder would discard the assigned value.
- Java changes provide the Swing editor and Studio model only; native C++ implements execution.

## Verification

`Server/tests/integration/GlobalVariableEndpointStudioTest.java` checks the installed JARs, names, save/reopen, wire properties, validation, source/target editors, and dark rendering. Preview: `C:/ETL/Reports/global-variable-endpoint-editor.png`.

`Server/tests/integration/GlobalVariableEndpointLiveTest.java` deploys an isolated verification project and runs script, archive and XML mapping tests, exact Unicode/newline comparisons, unset/non-string failures, malformed UTF-8/NUL rejection, and multiple-output rejection. Fixtures are under `C:/ETL/Samples/GlobalVariableEndpoints`.

Reports use `C:/ETL/Reports/global-variable-endpoint-*`. Source and native binary backups are under `C:/ETL/Archives/global-variable-endpoint-20261029`.

Installed validation: 23 Studio checks, 20 live checks, existing local-file copy regression and local/temporary-storage connection regression passed. The final rejection tests terminate with error status 6, including malformed UTF-8. An earlier test run exposed an uncaught standard exception; the new endpoint now translates validation exceptions to OperationEngineException. Its isolated test instance 348 was marked failed after confirming its fixture operation identity; see the fixture-recovery report.

The previously paused SFTP work remains paused.

Chooser correction: Data.LocationTypes.xml now registers GlobalVariable for both source and target. The installed XML metadata drives creation dialogs and the type dropdowns independently of the enum. The Studio regression now loads this metadata and verifies the actual radio choices and selection for both directions (29 checks total). Backup: C:/ETL/Archives/global-variable-chooser-fix-20261029. Reports: global-variable-chooser-fix-build.txt and global-variable-chooser-fix-tests.txt.
