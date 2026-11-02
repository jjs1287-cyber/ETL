# Bare-name script locals: implementation and verification plan

Requested behavior
- $NewGlobalVariable = 'hello world'; keeps existing global behavior.
- NewLocalVariable = 'hello script'; assigns a script-local variable. No new sigil or declaration keyword.
- An ordinary local must be assigned before it is read. Report a validation/syntax error with name and source location. A plain assignment target is not a read; its right-hand expression is evaluated before the target becomes initialized.
- Scope belongs to one script invocation, not one operation, thread or cached/compiled script. Child RunScript and RunOperation calls do not inherit locals. Parent locals remain intact when a synchronous child returns. Repeated, recursive and concurrent invocations have independent storage.
- Explicit RunScript arguments remain supported. They transfer values by the existing argument contract, not the parent's local namespace. Preserve _1/_2 and ArgumentList behavior. Document return values separately from local lifetime.
- Destroy locals on success, cancellation and exceptions. Do not change globals or existing source-field path syntax, and do not introduce # variables.

Evidence from local packages
- NewETL/Jitterbit Studio 11.67 compiled/lib/jitterbit-studio-core-1.0.0-SNAPSHOT.jar: javap inspection of ScriptValidator and Transform shows lde_status_map, isLocalVariableDefined, argument handling, and an error reporting that a local variable has not been initialized. Bytecode evidence is saved in C:/ETL/Reports/new-studio-local-validator-bytecode.txt and new-studio-transform-bytecode.txt. This inspection does not prove all control-flow behavior or runtime behavior of the newer agent.
- Current Studio src/main/java/org/etlforge/integration/data/script/Transform.java already has bare-name local classification and initialization checks. ScriptValidator delegates expression parsing to it.
- Native integration/konga/DataElement/Token.cpp already classifies non-global/non-source identifiers as LocalDE. LocalVariables::get currently auto-creates an element for an unknown name; definition and assignment are not separately represented there.
- Native TransformationEngine/Transform_fcn.cpp RunScript pushes a fresh local map, loads arguments, invokes the script and pops it. Pop is currently bypassed if the child throws. This must become exception-safe.

Steps
1. Freeze behavior with a table-driven baseline before edits. Run identical scripts through current Studio validation, newer Studio validation in an isolated JVM/classpath, and current native execution. Record case handling, identifiers/source-name collisions, assignment ordering, arrays, nulls, arguments and conditional/loop behavior. Do not load the two Studio versions in the same JVM. Do not claim parity until these outputs are compared.
2. Specify edge semantics from the baseline. Keep established source paths and reserved function names. Preserve existing case behavior where possible; check current Java upper-case tracking against native name storage. Explicit null or empty string counts as assigned. x=x+1, x+=1 and x++ require prior assignment. Reading an array/dictionary or writing an element must follow a documented container-initialization rule rather than silently changing existing behavior.
3. Close native validation gaps in C++, tracking initialization separately from a null value or an allocated DataElement. Validate reads and writes in actual expression order. Use a runtime check as defense for paths not statically provable. Never execute customer operations as part of syntax checking. Preserve the existing compiled-script cache while allocating invocation-local state outside it.
4. Make scope lifetime exception-safe with a native RAII guard; cover nested RunScript, operation entry, standalone script test, transformations, cancellation and recursive calls. Ensure returned values do not dangle after local storage is released. Do not infer success from absence of an exception alone.
5. Align existing Java Studio validation, highlighting and diagnostics with the native rules; add only necessary Java UI changes. Display an uninitialized-local error with line/column, retain drafts for editing, and block test/deploy of invalid scripts. Native execution must independently reject invalid input from other callers.
6. Run focused regression tests and compare baseline to final results. Archive changed sources and installed binaries/JARs, build Windows x64, and install only after tests pass. If a Studio JAR replacement is needed while Studio is open, request normal save/close; never terminate it to bypass the build guard. Document intentional stricter behavior for old scripts that relied on unassigned locals becoming null.

Control-flow decision to verify before implementation
A source-order assignment check is not proof a variable has a value on every runtime path. Example: If($condition, x='yes'); WriteToOperationLog(x); may read x without executing the assignment. Check newer Studio first, then document whether ETLForge matches its static acceptance while enforcing a runtime error, or rejects potentially unassigned reads with definite-assignment analysis. Never silently promise full flow analysis from a name table. Loops with zero iterations and short-circuit expressions need equivalent coverage.

Required test matrix
- Assigned local read; reassignment; global and local same base name.
- Read before any assignment; read before a later assignment; self-read on first assignment.
- Empty string, explicit null, zero, false, Unicode string values.
- Compound assignment, prefix/postfix increments, chained assignment and RHS evaluation order.
- Array/dictionary creation, indexed read/write and mutation isolation.
- If branches, short circuit, zero-iteration loops and assignment in nested expressions.
- Source-field identifiers and function names; case variants; strings/comments containing apparent identifiers.
- Parent/child same-name locals; child attempting to read parent's local; explicit arguments and return values.
- Recursive, asynchronous and concurrent execution; reused cached script; repeat invocation starts fresh.
- Child exception caught by parent; uncaught exception; cancellation; memory cleanup and parent-scope restoration.
- Existing globals, transformation mappings, RunScript and RunOperation regression suite.

Implementation completed on 2026-10-28. See script-local-variables.md for installed behavior, test evidence, limitations and rollback.
