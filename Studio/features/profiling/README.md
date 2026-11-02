# Optional operation profiling

Disabled by default at the user's request. The four client/action/adapter sources are preserved here instead of being deleted. The main Studio source tree and Eclipse build path do not include this directory, so the action/factory cannot appear in the base build. No active profiling registrations were found in the supplied resource configuration. SOAP DTOs/stubs remain available in the main source tree for compatibility.

To restore:
1. Supply or rebuild the missing com.etlforge.integration.operation.profiling data, server and client implementation classes. Do not use empty compatibility stubs.
2. Put implementation source under this feature's src/main/java, or its required libraries in Studio/lib and the Eclipse build path.
3. Set feature.profiling.enabled=true in Studio/features.properties (or pass -Dfeature.profiling.enabled=true to Ant).
4. Add features/profiling/src/main/java as an Eclipse source folder when working on the feature.
5. Restore/verify menu and service registrations, compile and test operation timing/result presentation against an agent.

Original bytecode and raw recovered source remain under Studio/recovered. Other optional features are unchanged.

When changing the feature flag, run a clean build to remove stale class files and JARs. Ant's clean target removes only build/classes and dist/lib after checking their configured paths. Then rebuild, and verify the UI action is available only when the feature is enabled.
