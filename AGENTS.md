# ETLForge development

Implement new product functionality in C++ by default. Use Java only when a native implementation is impractical or would be substantially slower, and document the reason for the exception.

For the current native JSON migration, leave JDBC database and Salesforce functionality unchanged. Compare native conversion against the existing Java converters before changing runtime routing.

# Text encoding

Use UTF-8 for text exchanged between ETLForge components and for generated text files. Decode external text once at the source boundary using a Unicode BOM, XML declaration or explicit source charset. Reject malformed/ambiguous input instead of silently replacing characters or guessing a legacy code page. Preserve original input files and binary/archive bytes.

Do not add platform-default charset conversions or UTF-8 to ANSI to UTF-8 round-trips. UTF-16 representations required by Java, Xerces, Windows wide APIs, COM and Unicode driver bindings are legitimate boundary conversions. New Windows filesystem code must use wide APIs after strict UTF-8 path decoding.

Character-width text fields must not split UTF-8 sequences. Keep external byte-oriented protocol requirements explicit. Cover multilingual text, supplementary characters, malformed input and streaming boundaries when changing encoding code.
