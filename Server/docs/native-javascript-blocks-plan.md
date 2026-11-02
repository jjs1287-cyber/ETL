# Native mixed JavaScript blocks: implementation plan

Date: 2026-10-28; verified closeout 2026-10-29. Status: implemented, installed and tested for the documented synchronous profile. Current behavior and intentional design adjustments are documented in [native-javascript-blocks.md](native-javascript-blocks.md). Measured verification results are in [native-javascript-validation.md](native-javascript-validation.md). The design discussion below records the original plan, not a second list of unimplemented features.

Delivered architecture uses a fresh runtime/context per expression invocation, a bounded thread-local trusted-bytecode cache, native accessor bindings for source-visible dollar globals, a 256 KiB stack, and a compile-only native Studio validator. Script parsing, execution, mapper source binding, host adapters and policy are implemented. Studio supports mixed-language editing, background/debounced validation, dependencies, source insertion and Test. Existing trans behavior, JDBC and Salesforce implementations are retained. Blocking connector calls retain their own timeouts; VM interrupts cannot preempt them. Dynamic/complex dollar-name bindings and modern language features beyond the documented profile are not promised. See the current guide for limits and rollback precautions.

## Intended behavior

Any existing script or mapping expression can contain literal text, `<trans>...</trans>` and `<javascript>...</javascript>` blocks in any order and number. Evaluate left to right and preserve literal text exactly. Do not introduce a separate JavaScript-only project entity or follow the commercial editor's separate-language workflow. Existing trans-only projects must retain their behavior.

Example:

```
Before <trans>$greeting = 'Hello'; $greeting</trans>
<javascript>SetScriptResult(ETLForge.GetVar('$greeting') + ' from JavaScript');</javascript>
After
```

Proposed semantics to lock down in fixtures before implementation:

- Canonical tags are lowercase. Preserve current trans handling; do not silently change existing literal uppercase text into executable code. Offer lowercase insertion commands.
- Each JavaScript block emits the value of its last SetScriptResult/SetScriptOutput call; these are aliases. No setter means no output, allowing side-effect-only blocks. Do not infer output from the last JavaScript expression. Reset the result slot for every block.
- A standalone JavaScript block preserves its result type through the existing DataElement conversion path; mixed literal/code output concatenates strings through the existing target formatting rules. Establish null versus empty behavior against current trans fixtures before changing that evaluator path. Undefined means no output; reject unsupported result values explicitly rather than returning '[object Object]' accidentally.
- JavaScript variables/functions persist between JavaScript blocks within one expression invocation. They are discarded on return and never leak to the next row, expression, child script, child operation or another worker. Trans locals retain their existing lifetime. The two local namespaces are separate. Explicit ETLForge.GetVar/SetVar and existing $ globals provide cross-language communication.
- Standard JavaScript local rules apply (including var hoisting); the new trans assignment-before-read rule is not imposed on JavaScript. Strict-mode execution rejects accidental undeclared assignments.
- GetVar/SetVar use the shared operation global store with a staged overlay visible to later JavaScript blocks. Commit that overlay at successful JavaScript-block completion; discard it on a block error. File/database/log side effects are not transactional. Do not promise rollback of completed earlier blocks or external I/O.
- Support typed $identifier access through parsed global-access lowering or equivalent safe binding, not plain JavaScript global properties or regex replacement. Route reads/writes to the same overlay, including nested object mutations; dotted global names use GetVar/SetVar. Prototype fixtures must settle exact bridging semantics before rollout. Until implemented, reject unsupported global syntax explicitly; never silently treat $identifier as an isolated JS local.
- Source-field access in mappings uses Studio's Data Elements insertion and a separately specified read-only binding to the current TreeMapper row. Reuse source-path resolution and array/occurrence semantics; do not interpolate unescaped paths into JavaScript source. This is an ETLForge extension beyond the reference host list, required for useful mappings, and must be documented distinctly.

## What was verified in the local code

Native directory: C:/ETL/Server/src/native/integration/konga/TransformationEngine/.

- Transform.cpp CTransform::TokenizeTemplate (~1031) searches only for TRANS using strstr. TokenArray assumes fixed trans opening/closing lengths. Replace this assumption with explicit block kinds and source spans.
- Transform.cpp CTransform::Evaluate (~834) concatenates literal gaps and evaluated results; its single-trans optimization (~848) and target type/format/null behavior need a language-independent block dispatch. Preserve the old trans fast path where applicable.
- Transform.cpp createTokenArrayListItem (~739) attaches compiled token arrays to mapping attributes. Extend this existing compile-once location, not per-row parsing.
- DETransform.cpp runScript (~298) goes through TokenizeTemplate/Evaluate. Cover the Studio test-expression route and deployed execution through the same block engine.
- Transform_fcn.cpp RunScript and Transform.h LocalScope now isolate child local/token state. Extend RAII cleanup to JavaScript contexts, error state and result values without reopening the recently fixed isolation bugs.
- TreeMapper.cpp also calls TokenizeTemplate. Audit every caller and token-array owner/destructor, including recursive execution and debug contexts.

Studio directory: C:/ETL/Studio/src/main/java/.

- org/etlforge/integration/data/script/Transform.java tokenize (~201) only locates trans blocks. ScriptValidator.java delegates to that parser; entity/Script.java and ui/script/debug/Debugger.java use ScriptValidator.
- org/etlforge/integration/client/ui/script/partition/{ScriptTokenScanner,ScriptPartitioner,Tokens,NoCodeToken}.java assume inside/outside-trans states. Add separate literal/trans/JavaScript partitions and language-aware completion/hover/highlighting.
- Include mapping formula builders, script pad, project validation, dependency collection, project-item TAG resolution, deployment, Test and debugger entrypoints. A correct runtime alone will still leave Studio rejecting or corrupting mixed expressions.

## Reference function inventory and security findings

Inspected FormulaBuilder.JavaScriptFunctions.xml in the supplied Studio 11.67 compiled/lib/jitterbit-studio-config-1.0.0-SNAPSHOT.jar. Extracted names/signatures and a resource SHA-256 to javascript-reference-function-inventory.json beside this plan. Counts: 9 host functions, 23 keyword/constants entries, 13 common helpers, 9 Math helpers. No credentials or project data are included.

Reference host names -> ETLForge public names:

| Reference | Proposed ETLForge | Routing |
|---|---|---|
| Jitterbit.ReadFile | ETLForge.ReadFile | Existing source connector and authorization |
| Jitterbit.WriteFile | ETLForge.WriteFile | Existing target connector; preserve automatic flush semantics |
| Jitterbit.DbExecute | ETLForge.DbExecute | Existing database implementation unchanged |
| Jitterbit.DbLookup | ETLForge.DbLookup | Existing database implementation unchanged |
| Jitterbit.GetVar | ETLForge.GetVar | Operation global-store bridge |
| Jitterbit.SetVar | ETLForge.SetVar | Operation global-store bridge |
| WriteToOperationLog | WriteToOperationLog | Existing native operation log |
| SetScriptResult | SetScriptResult | Invocation/block result slot |
| SetScriptOutput | SetScriptOutput | Alias of SetScriptResult |

Common helpers observed: decodeURI, decodeURIComponent, encodeURI, encodeURIComponent, eval, isFinite, isNaN, Number, parseFloat, parseInt, String, JSON.parse, JSON.stringify.
Math entries: abs, ceil, exp, floor, max, min, pow, random, sqrt.

Do not use the catalog as a complete security allowlist. Some built-ins have placeholder zero-argument grammar entries. Array/String/Date/Object methods are language intrinsics, and user-defined functions must work. Derive actual signatures from ECMAScript and behavioral fixtures. Keep ES5.1 compatibility as the initial documented language baseline; do not accidentally promise identical Nashorn behavior or a complete modern-JavaScript profile.

javap inspection of the supplied JavaScriptSecurityFilter confirms a Nashorn Java-class allowlist (empty by default). JavaScriptEngineConfig removes 15 shell globals: quit, exit, print, echo, readFully, readLine, load, loadWithNewGlobal, $ARG, $ENV, $EXEC, $OPTIONS, $OUT, $ERR, $EXIT. These observations describe local defaults, not proof of all commercial server restrictions.

The native host must expose only the nine reviewed host functions and the explicit mapping binding. No Java/Packages, CLR, COM, DLL loading, Node require, shell/process/environment APIs, raw sockets, DOM, fetch, console or filesystem access outside approved source/target connector functions. No generic dispatch to every native scripting function; RunScript/RunOperation are not in the reference JavaScript host list. JDBC and Salesforce internals remain unchanged. A database call or connector can retain its existing backend dependency; the JavaScript evaluator itself must never call Tomcat.

Keep reference eval support only inside the same bounded native context: eval and Function-created code cannot gain host privileges or escape budgets. Removing one global name is not a sandbox. Test indirect/computed/prototype access and constructor chains. Native bindings validate argument types, reference permissions, paths, lengths and operation ownership independently of Studio. Enforce limits in database/file callbacks too; VM interrupts cannot cancel a blocked native call by themselves.

## Native engine recommendation

Prototype QuickJS-NG embedded behind a C++ RAII adapter. Its C API executes inside the C++ worker; no JVM, Tomcat, Node process, or row-by-row IPC. It provides a Windows/MSVC and CMake build route suitable for the existing x64 toolchain. Pin a reviewed source release/hash, preserve license notices, record dependency size, and build only the core engine. Do not link/register the optional libc std/os helpers or module/native-library loaders.

This is a recommendation pending a native build/security/performance spike, not a claim that the engine is already installed or validated. Compare Duktape if the QuickJS-NG spike fails size/build/compatibility gates; do not write a custom JavaScript interpreter.

Proposed adapter: NativeJavaScriptRuntime, NativeJavaScriptBindings, ScriptTemplateParser and ScriptBlock/CompiledScriptTemplate. Keep engine-specific value handles out of exported legacy DLL interfaces. Catch every C++ exception at C callback boundaries and convert to JS errors; never unwind through C frames. Use strict UTF-8 boundaries, RAII and length-aware conversion including embedded NUL, Unicode surrogate handling and bounded recursive value conversion. Explicitly handle integers beyond JS safe range, dates, null, booleans, arrays/maps, cycles, functions, NaN/Infinity and binary values.

One runtime per executing worker/thread, never simultaneously entered from multiple threads. Fresh invocation realm/state for each expression/row. Preserve realms between blocks of that invocation only. Cache immutable compiled code per worker keyed by exact source, engine version and policy version; cap entry count/bytes and evict. Prove the chosen compile artifact can be reused safely across fresh contexts before relying on it. Do not reuse mutable globals to save time. No persisted/client-supplied bytecode; compile trusted source locally. Nested calls require an explicit invocation stack.

Initial tunable limits to test: 64 MiB JS heap per worker runtime, 1 MiB JS stack, 5 seconds CPU/wall deadline per invocation, 16 MiB emitted result, bounded input/value depth and a total operation deadline. These are starting defaults, not measured production values. Apply cancellation/timeout interrupts across all blocks and dynamic eval; do not reset the budget to bypass limits. Reject async/module execution in the initial synchronous profile. Failed/poisoned runtimes are discarded safely. In-process VM restrictions are not an OS security boundary against engine vulnerabilities; keep the pinned engine maintainable and run adversarial tests in disposable worker processes.

## Implementation order and completion gates

1. [x] Archive native sources/JARs/native binaries and record baseline. Original Studio JARs provide binary rollback; the final source snapshot is separate. Golden fixtures cover literal/multiple-trans/typed/null/format/error behavior, mapping repeats, local isolation and Unicode.
2. [x] Pin/build x64 engine; create standalone native harness. Prove compile-only validation, bounded execution, cancellation, memory limits, eval containment, fresh contexts, host exception safety and actual cache reuse. Measure cold/warm throughput and footprint before integrating.
3. [x] Implement shared block grammar in C++ and equivalent Studio partitioning. Golden fixtures are the contract shared by both. No nesting. Reject malformed/mismatched tags with original line/column. Closing-tag recognition must respect JS strings, comments and regex literals (and any supported template literals); use a real lexical scanner, not substring/regex heuristics. Literal tags can be emitted from strings. Preserve source offsets for diagnostics and debug locations.
4. [x] Add dispatch to TokenizeTemplate/Evaluate and cached mapping plans. Add native value/global/source-row bridges. Preserve target formatting, nulls, exact literal whitespace, trans optimizations and child scope cleanup. Compile on mapping load/source changes, not each row.
5. [x] Implement the reviewed host list through explicit adapters and signature metadata. Verify read/write flush and database return behavior against reference examples. No new JDBC/Salesforce conversion work. Keep a policy manifest as the authority for runtime bindings and Studio catalog.
6. [x] Finish Studio mixed-language highlighting, completion, insertion, dependencies, validation and Test UI. Prefer a compile-only native validator library via a narrow JNI bridge for local/offline JavaScript validation with no host effects; this Java bridge is editor-only. Version/package it as x64 and test DLL loading. Do not use a second JavaScript engine with different syntax acceptance. Background/debounced validation must not freeze the UI. Server revalidates deployed source independently.
7. [x] Cover mappings, script pad and RunScript/deployment routes end to end. Store source unchanged in existing project records if length/serialization checks pass; no new entity/schema merely for language selection. Audit canonicalization must preserve meaningful JS whitespace/comments/string contents. Existing global comment removal must not damage source code.
8. [x] Run correctness/security/performance checks and document measured scope: XML/JSON/CSV parity, complete 1k/10k/100k CSV timing matrix, five repeats, output hashes, resource sampling and zero observed Tomcat traffic. Package all native consumers (including test/mapper DLLs), update installer/dependency inventory and status checklist. Studio was closed for packaging. Old trans regression oracle and explicit JavaScript feature gate pass; no Java/Tomcat evaluator fallback. Full 100k XML timing was intentionally cancelled and is not claimed as a passing scale test.

## Verification and timing matrix

- Mixed text: literal-only, trans-only unchanged, JavaScript-only, repeated/interleaved tags, adjacent and empty blocks, output setter repeated/absent, newlines and delimiter text in strings/comments/regex. Malformed/nested blocks must fail identically in Studio/server.
- Values: strings/Unicode/emoji/NUL, arrays/maps, booleans, numbers/safe-integer boundary, null/undefined, cyclic objects, dates and unsupported results. Compare source/target formatting and null suppression behavior.
- Scope: JS blocks share invocation state; rows/mappings/workers/RunScript children do not. Mixed globals, dotted names, nested mutation, branch/errors, staged commit and limits. Re-run the 32 Studio/29 native trans local-variable checks and existing recursion/concurrency/child-operation tests.
- Mapping: XML/JSON/delimited files; scalar/repeating source paths and occurrence arrays; equivalent trans/native-JS transforms must produce equivalent target files and record counts. Preserve static surrounding text byte for byte.
- Host APIs: all nine host functions, wrong types/arity, unauthorized references, Unicode files, flush, database failures and log ownership. Read-only DB fixtures or rollback-only writes; no customer data changes.
- Security: shell/Java/module/constructor/computed-name access, traversal, eval and Function, infinite loop, recursion, allocation bombs, catastrophic regex, huge result/value conversion, prototype pollution, callback timeouts and cancellation. Reject safely without taking down services or leaving Running instances orphaned.
- Performance: same fixtures through old trans path and equivalent native JS; report cold compile, warm execution, full operation time, CPU, peak memory, cache hit/miss and output hash. Use 1k/10k/100k rows, small/large payloads, at least 5 timed repeats, median and p95. Reference newer Java engine only as an optional controlled baseline, not the new runtime. Instrument calls to prove zero Tomcat requests for pure JS mappings. Separate expected connector/JDBC traffic from evaluator traffic. No claimed speedup until measured; bound cache/RSS under sustained load and ensure no material trans regression.
- Rollback: disable new tags with a clear unsupported-feature error, restore archived binaries/JARs as a coherent set; preserve source projects and database records. Existing trans-only work must continue unchanged.

## Sources

Local catalog and javap observations above are primary evidence for the supplied version. Public documentation corroborates the host set but is not a full runtime security specification:

- https://docs.jitterbit.com/design-studio/design-studio-reference/scripts/javascript/
- https://quickjs-ng.github.io/quickjs/supported_platforms/
- https://quickjs-ng.github.io/quickjs/building/
- https://bellard.org/quickjs/quickjs.html (original engine embedding concepts; verify API details against the pinned NG headers)
