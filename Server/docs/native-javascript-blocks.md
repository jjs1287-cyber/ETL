# Native JavaScript blocks

ETLForge 42 runs synchronous JavaScript inside its x64 C++ operation worker using the pinned QuickJS-NG 0.17.0 core. Studio uses the same engine through a compile-only x64 JNI validator. No Tomcat JavaScript evaluator is involved. Existing JDBC and Salesforce implementations are unchanged; choosing a JDBC connector can still use its existing backend.

## Writing expressions

Use canonical lowercase tags. Literal text, multiple `<trans>` blocks and multiple `<javascript>` blocks can be interleaved. Blocks run from left to right. Tags cannot nest. Uppercase tags remain literal text. Closing-tag text inside JavaScript strings, comments and regular expressions is parsed as part of that JavaScript construct.

```
Hello <trans>$greeting = 'world'; $greeting</trans>!
<javascript>
var message = ETLForge.GetVar('$greeting').toUpperCase();
SetScriptResult(message);
</javascript>
<javascript>SetScriptOutput(message + ' again');</javascript>
```

`SetScriptResult(value)` and `SetScriptOutput(value)` are aliases. The last setter in a block supplies its result. A block with no setter emits nothing; its last expression is not implicitly returned. Result state resets between blocks. Literal gaps preserve their exact contents. Existing target formatting/null rules apply. Use `JSON.stringify(object)` when a text target needs an object; an object is not implicitly emitted as `[object Object]`.

JavaScript locals persist between JavaScript blocks in one expression. Each new script, child script, mapping expression, row and worker receives fresh JavaScript state. Trans locals and JavaScript locals are separate. Strict mode rejects accidental undeclared assignments. JavaScript retains its own `var` hoisting rules; trans assignment-before-read rules are unchanged.

The documented initial baseline is synchronous ES5.1-style code. Template literals and modules are rejected. This is not a promise of identical Nashorn behavior or every modern ECMAScript feature.

## Host functions

| Function | Behavior |
|---|---|
| `ETLForge.ReadFile(source[, fileName])` | Read through an existing source connector and its decoding/authorization rules. |
| `ETLForge.WriteFile(target, content[, fileName])` | Write through an existing target connector; flush automatically. |
| `ETLForge.DbExecute(database, sql, ...)` | Existing native database command adapter; row arrays returned for queries. |
| `ETLForge.DbLookup(database, sql)` | Existing scalar database lookup. |
| `ETLForge.GetVar(name)` | Read an operation global. |
| `ETLForge.SetVar(name, value)` | Stage an operation-global value. |
| `WriteToOperationLog(message)` | Write to the current operation's local log. |
| `SetScriptResult(value)` | Set this block's emitted result. |
| `SetScriptOutput(value)` | Alias of SetScriptResult. |
| `ETLForge.GetSource(literalSourcePath)` | ETLForge mapping extension: read the current source row through the existing mapper. |

Studio offers these functions, 13 common JavaScript helpers and nine Math helpers. Array, String, Object, Date, JSON and regular-expression language methods are available within the bounded runtime. `eval` and `Function` remain in that same restricted context. No Java/Packages, COM, Node, shell, environment, raw filesystem, sockets, fetch, async APIs or generic native-function dispatcher are registered. RunScript/RunOperation are trans functions, not JavaScript host functions.

Select connector references through Studio Project Items so dependencies are deployed and stable GUID references are preserved. Mapping Data Elements insertion/drag emits an escaped `ETLForge.GetSource('...')`. Its argument must be a direct string literal and is bound during mapping load. For a repeating XML row, an example is `ETLForge.GetSource('rows$row.name$')`. Source values are read-only snapshots.

## Globals and values

Use GetVar/SetVar for dotted or computed global names. Source-visible `$identifier` expressions are bridged through native accessors to the operation store. Do not declare `$identifier` as a JavaScript local or parameter. Do not generate `$identifier` access dynamically in eval/Function; use GetVar/SetVar in generated code. Complex lexical bindings of dollar names are outside the supported profile.

Global writes and nested data changes commit after a successful JavaScript block; an error discards that block's staged global changes. Earlier successful blocks and completed file/database/log side effects are not rolled back. Config globals staged before a connector call are visible during that call, then native state is restored until commit. A trans block refreshes the JavaScript global cache; retained JavaScript object aliases are snapshots across that boundary.

Supported host values are null, undefined, booleans, finite numbers, strict Unicode strings, arrays and plain data objects. Unsafe integral numbers beyond the JavaScript safe range, cycles, accessors, functions, BigInt, Date objects and custom prototypes are rejected at the host boundary. Date methods can still produce strings. Legacy text/global interfaces reject embedded NUL. No silent ANSI conversion or malformed-Unicode replacement is added.

## Limits and configuration

`C:\ETL\Server\instances\dev\etlforge.conf`:

```
[JavaScript]
Enabled=1
HeapLimitMiB=64
ResultLimitMiB=16
ExecutionTimeoutMs=5000
```

Limits apply across the JavaScript blocks in an invocation, including eval. Stack limit is 256 KiB; transferred values are additionally bounded to 16 MiB, depth 64 and 100,000 nodes. Config ranges: heap 4–1,024 MiB, result 1–64 MiB, timeout 1–300,000 ms. Raising result size does not raise the independent host-transfer cap. Source templates are bounded to 16 MiB and 1,024 blocks. Global names and counts are bounded.

A fresh runtime/context is created per expression invocation for isolation. A thread-local FIFO cache holds at most 32 trusted-source bytecode entries, each with at most 128 KiB source and 128 KiB bytecode. Larger code executes without that cache. Bytecode is neither accepted from clients nor persisted. Heap limits are per invocation, not a total service memory cap.

VM interrupts stop loops, regex execution and allocation failures safely. A VM interrupt cannot preempt a blocking native database/file connector call; those calls retain their existing connector timeouts, and the JavaScript deadline is checked when control returns. Do not treat this in-process VM as an operating-system security boundary against engine vulnerabilities. Keep the pinned engine maintained.

Uncaught script errors now propagate to operation failure instead of being logged and then reported as success. Timeouts were verified as failed operation status 6, and subsequent operations still run.

## Build, packaging and rollback

`C:\ETL\Tools\Build-NativeJavaScript.ps1 -Install` builds/tests in staging, replaces the seven native consumers coherently while affected services are stopped, and restarts previously running services. `C:\ETL\Tools\Build-Studio.ps1 jar` packages Studio and its x64 validator; Studio must be saved and closed first. Source/Eclipse execution also locates the validator under Studio/dist/bin.

The policy at `Server/features/javascript/host-policy.json` generates native and Studio signatures. The dependency pin and source hashes are in `C:\ETL\Dependencies\reconstruction-lock.json`. Ship both product license directories, including QuickJS-NG's MIT notice. The core is statically linked; there is no additional server JavaScript runtime DLL. Studio needs `dist/bin/ETLForgeJavaScript.dll`. Preserve that DLL alongside the changed JARs in installers.

Original native sources, seven native binaries, three Studio JARs and the original runtime configuration are archived at `C:\ETL\Archives\native-javascript-20261028`. This is a development rollback archive, not an installer payload. A complete pre-edit Studio source snapshot was not captured; the original JARs provide binary rollback.

For feature rollback, set Enabled=0 in the new binaries; JavaScript blocks then fail explicitly and trans-only scripts continue. A full restore of old pre-feature binaries/JARs must be coordinated and JavaScript projects prevented from executing: the old parser can treat unrecognized JavaScript markup as literal text. Do not mix old/new native consumers.

## Verification

Fixtures and test sources are under `C:\ETL\Server\tests\native` and `tests/integration`. Owned live fixtures use project GUID prefix `42470000-0000-4000-8000` and files under `C:\ETL\Samples\NativeJavaScriptMapping`; database tests use read-only SELECTs. Reports are under `C:\ETL\Reports\javascript-*`. See `native-javascript-validation.md` for measured counts, timings and access-log evidence.
