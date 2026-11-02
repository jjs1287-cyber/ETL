# Native JavaScript verification

Implementation and installed-package checks began 2026-10-28; closeout 2026-10-29. Native C++ execution and the Studio compile-only JNI validator use pinned QuickJS-NG 0.17.0. See [the scripting guide](native-javascript-blocks.md) for the supported profile, configuration, host functions and rollback.

## Correctness and regression results

- 166 native checks: block grammar, values, Unicode, globals, isolation, bounded bytecode cache, host error handling, eval restrictions, loops, regex, heap/stack/result limits and cancellation.
- 13 installed Studio validator checks; 18 installed editor/dependency checks; three background-validation checks. These load the installed JARs and discover the installed x64 DLL without a test override.
- 21 live native JavaScript checks and 15 live host/safety checks, including all nine host functions, read-only database SELECTs, file flush/content parity, staged connector configuration, concurrent isolation, timeout failure status and recovery.
- Existing trans regression suites: 29 live cases and 32 Studio validation cases pass. Child calls, caught errors, recursive scopes, synchronous/asynchronous child operations and 12 concurrent parent/child requests pass. Native XSLT Studio Test smoke check passes.
- XML, JSON and CSV mappings match their equivalent trans outputs byte-for-byte. XML records are independently checked for count, IDs and multilingual names. The scale CSV test checks every expected record, count and full output SHA-256 on every repeat.
- The runtime disable switch was tested with an owned queued JavaScript operation: status 6 and an explicit disabled-feature message (instance 270). The original configuration bytes were restored afterward, leaving JavaScript enabled. Small XML/JSON/CSV mapping fixtures are rerun after restoration.

Owned fixtures use project GUID prefix `42470000-0000-4000-8000` and `C:\ETL\Samples\NativeJavaScriptMapping`. Customer records were not changed. Test database commands are read-only SELECTs. Queued script exceptions now finalize as failed rather than falsely successful; a failed timeout is followed by a successful execution to check recovery.

## Full operation timings

Five repeats per size, two mapped fields per record, multilingual UTF-8 text. Times include SOAP submission, worker startup and polling PostgreSQL for a verified terminal status. p95 uses nearest rank; with five observations this is the slowest observation. Measurements are from the development Windows machine, not a controlled production capacity test.

| CSV records | trans median / p95 | native JavaScript median / p95 |
|---:|---:|---:|
| 1,000 | 1.563 / 2.039 s | 2.432 / 2.531 s |
| 10,000 | 1.734 / 2.082 s | 6.296 / 7.963 s |
| 100,000 | 2.992 / 3.414 s | 44.830 / 46.074 s |

All 15 comparisons passed; hashes are identical across all five repeats at each size. Native JavaScript adds language support without evaluator IPC, but is substantially slower than trans for this simple field-copy workload. Keep simple trans mappings on their existing fast path. No Java-engine performance comparison or JavaScript speedup is claimed.

XML also passed five comparisons each at 1,000 and 10,000 rows. The 100,000-row XML trans baseline was still running after several minutes and was cancelled through the operation cancellation API (owned instance 231, final cancelled status 1). It is not a successful 100,000-row XML comparison or a timing result. The complete scale matrix uses CSV to avoid making existing XML mapping overhead dominate the experiment. A Studio rebuild overlapped that abandoned XML baseline; it did not overlap the final CSV timing matrix.

## Tomcat and resource measurements

The Tomcat access-log offset was captured before the pure XML/JSON/CSV verification and complete CSV scale matrix, then read after a 12-second flush allowance. Appended bytes: **0**. The backend stayed running throughout; no evaluator requests were observed. This applies to pure mappings, not database connectors that legitimately retain their backend.

One-second process sampling during those mappings observed a maximum worker peak working set of **31,932,416 bytes (30.45 MiB)**. Summed sampled worker CPU maxima were **143.02 seconds**, a lower bound because short-lived processes/final CPU intervals can fall between samples. The sampler is not proof of total machine memory or a precise per-operation CPU breakdown.

The isolated engine microbenchmark measured five 1,000-iteration batches: compile-only fresh-realm batches approximately 170–183 ms; cached-code execution with fresh realms approximately 151–165 ms. These are different operations, so their ratio is not a compile-cache speedup measurement. Peak working set was 5,595,136 bytes (5.34 MiB). Cache reuse and realm isolation are additionally asserted in correctness tests; cache entries are bounded in code.

## Build and installer footprint

Native staging build/tests and Studio packaging succeeded. Three native consumers increased by approximately 1.08 MB each; the other four staged consumers were unchanged in size. Studio's x64 validator DLL is 1,117,184 bytes. Exact sizes are in `C:\ETL\Reports\javascript-binary-sizes.csv`. These are uncompressed deltas, not final installer sizes.

QuickJS-NG is statically linked into the server evaluator. Ship Studio's validator DLL, updated JARs, and both product license directories. The dependency lock records the source commit and local file hashes without claiming publisher-signature verification. Development sources, test fixtures, reports and rollback archives need not be included in the runtime installer.

## Evidence files

- `C:\ETL\Reports\javascript-final-native.txt`
- `C:\ETL\Reports\javascript-installed-MixedJavaScriptValidatorTest.txt`
- `C:\ETL\Reports\javascript-installed-MixedJavaScriptUiTest.txt`
- `C:\ETL\Reports\javascript-installed-JavaScriptBackgroundValidationTest.txt`
- `C:\ETL\Reports\javascript-final-MixedJavaScriptRuntimeTest.txt`
- `C:\ETL\Reports\javascript-final-NativeJavaScriptHostLiveTest.txt`
- `C:\ETL\Reports\javascript-final-StudioNativeXsltTestSmoke.txt`
- `C:\ETL\Reports\javascript-feature-gate.txt`, `javascript-final-mappings.txt`
- `C:\ETL\Reports\javascript-trans-after.txt`, `javascript-trans-isolation.txt`, `javascript-trans-validator.txt`
- `C:\ETL\Reports\javascript-mapping-benchmark.csv` and `.txt`
- `C:\ETL\Reports\javascript-xml-benchmark.csv` and `.txt` (smaller completed XML comparisons)
- `C:\ETL\Reports\javascript-tomcat-during-mappings.txt` (empty access-log delta)
- `C:\ETL\Reports\javascript-worker-memory.csv`, `javascript-summary.json`
- `C:\ETL\Reports\javascript-engine-benchmark.txt`, `javascript-source-snapshot.csv`
- `C:\ETL\Reports\javascript-install.txt`, `javascript-studio-package.txt`

The original implementation plan remains as a design record. Explicit supported-profile limitations and blocking native connector timeout behavior are described in the guide; they are not silently replaced by Tomcat fallbacks.
