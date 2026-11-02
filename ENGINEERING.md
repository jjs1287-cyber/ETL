# ETLForge implementation decisions

New functionality should be implemented in C++ by default. Use Java only where a native implementation is impractical or would be substantially slower; document the reason.

Do not migrate JDBC or Salesforce implementations as part of the native JSON work.

Native JSON conversion must be compared with the existing Java implementation, including schema-dependent array handling, scalar types, nulls, field-name sanitization and numeric behavior. Preserve the Java production route until compatibility has been demonstrated and runtime integration has been verified.
