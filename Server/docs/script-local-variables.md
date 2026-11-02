# Script-local variables

Implemented and installed on 2026-10-28 in Windows x64 ETLForge 42.0.0.0.

```text
<trans>
$NewGlobalVariable = 'hello world';
NewLocalVariable = 'hello script';
WriteToOperationLog(NewLocalVariable);
</trans>
```

A bare variable belongs to the current script invocation. Assignment creates its value. Reading it before assignment is a syntax error in Studio and in native compilation. A first assignment such as `x=x+1` is invalid: the right-hand side runs before assignment completes. Empty strings, Null(), false and zero all count as assigned values. `$` globals retain their existing behavior. No `#` local syntax was added.

Local names are case-sensitive, matching native storage: `value` and `Value` are separate locals. Studio's additional validation now enforces this instead of accepting case variants that the native map could not resolve.

Initialize a container before accessing its elements:

```text
<trans>
values = {1,2};
values[0] = 3;
lookup = Map();
lookup['key'] = 'value';
</trans>
```

Studio performs assignment-order validation, not all-path definite-assignment analysis. An assignment inside an If/While may pass Studio validation; the native runtime checks whether that assignment actually executed before a later read. Skipped assignments produce an unassigned-local error. Syntax checking does not execute operations or functions.

RunScript and RunOperation do not inherit the parent's locals. Parent locals survive a child returning or throwing. RunScript arguments are explicitly copied, including collection contents. A child can use `_1`, `_2`, etc. or `ArgumentList(first,second)`. The native ArgumentList implementation was added because Studio already recognized it but the original native function table did not. Reserved `_N` arguments retain legacy missing-argument behavior (empty value); ordinary locals do not receive that exception.

Each RunScript invocation owns a fresh local map. C++ RAII restores the parent map, compiled token lists and evaluator error state during normal return and exception unwinding. Child-owned tokens and locals are released. Existing returned-value conversion/copying completes before child storage is destroyed. Cancellation that unwinds through the normal C++ exception path uses this same cleanup; a debugger cancellation scenario was not separately exercised. Abrupt process termination releases process memory through Windows.

## Validation and installation

- Compared 13 identical parser examples against the current Studio and newer 11.67 Studio in isolated JVMs. Both accepted the old self-read bug; ETLForge intentionally rejects it.
- Recorded 29 old native execution cases before replacement.
- Installed JAR: `C:/ETL/Studio/dist/lib/etlforge-integration-client-data.jar`.
- Built native transformation library, PluginManager, operation worker, process engine, scheduler, cleanup, control and TreeMapperInfoProvider with the x64 toolchain.
- 32 checks pass against the installed Studio JAR: assignment, compound operations, null/empty/zero/false, arrays/maps, source-field recognition, template boundaries and line diagnostics.
- 29 native Test endpoint checks pass, including UTF-8 multilingual/supplementary values, uninitialized reads, self-reads and skipped control-flow paths.
- Child-script checks pass for parent restoration, caught errors, argument passing, deep collection copying, recursion and repeated calls.
- Twelve concurrent parent/child HTTP requests pass with distinct results.
- Synchronous and queued child operations record `LOCAL_SCOPE:isolated` in their own operation messages. Their completion and message records were checked in PostgreSQL.
- Existing native RunXSLT Test endpoint regression passes.
- Eight ETLForge Windows services are running after installation. Studio was closed normally for packaging and remains ready to reopen.

Only generated verification projects were deployed; existing customer projects were not edited. Verification project names start with `ETLForge Local Variables Verification`. Their latest project/operation GUIDs are recorded in `C:/ETL/Reports/locals-isolation-fixture.txt`.

## Files and repeatable checks

Native changes are in `C:/ETL/Server/src/native/integration/konga/DataElement/Token.{h,cpp}`, `TransformationEngine/LocalVariableValidation.h`, `Transform.{h,cpp}` and `Transform_fcn.cpp`. Studio changes are in `C:/ETL/Studio/src/main/java/org/etlforge/integration/data/script/Transform.java`.

Tests are under `C:/ETL/Server/tests/integration`: `LocalValidatorBaseline.java`, `LocalVariablesRuntimeTest.java`, `LocalVariablesIsolationTest.java`, `LocalVariablesValidatorTest.java` and `script_locals_smoke.py`. Compile those Java test sources with JDK 8, classpath `C:/ETL/Server/build-probe;C:/ETL/Studio/dist/lib/*;C:/ETL/Studio/lib/*`, output directory `C:/ETL/Server/build-probe`, and UTF-8 source encoding. The Python runner executes the installed-JAR/native checks and verifies queued completion in PostgreSQL. It requires the existing private password/passfile and running development services.

Reports:
- `C:/ETL/Reports/locals-current-validator-before.txt`
- `C:/ETL/Reports/locals-new-validator-baseline.txt`
- `C:/ETL/Reports/locals-native-before.txt`
- `C:/ETL/Reports/locals-validator-final.txt`
- `C:/ETL/Reports/locals-runtime-after.txt`
- `C:/ETL/Reports/locals-isolation-after.txt`
- `C:/ETL/Reports/locals-worker-scope.txt`
- `C:/ETL/Reports/locals-xslt-regression.txt`
- Build/package logs named `script-locals-*` in the same Reports directory.

Original changed sources, Studio data JAR and native binaries were preserved under `C:/ETL/Archives/script-locals-20261028`. Rollback requires closing Studio and stopping affected native services before restoring matching binaries and the JAR. Restore the corresponding sources for subsequent builds. No database schema rollback is needed. `C:/ETL/Tools/Build-ScriptLocals.ps1` stops/restarts the affected development native services around relinking; compilation can be done first while services run.
