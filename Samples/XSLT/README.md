# ETLForge XSLT examples

Start the updated ETLForge application and open:

`C:/ETL/Samples/XSLT/StudioProject/ETLForge XSLT Examples-9cdd4890/ETLForge XSLT Examples.jip`

Or use File → Import Backup:

`C:/ETL/Samples/XSLT/StudioProject/ETLForge XSLT Examples-9cdd4890.jpk`

Deploy the example project before running it.

- **Source to XSLT to Target** reads ExpandEmbeddedXml/input.xml and writes expanded XML into the example project's output/result.xml.
- **Run XSLT from script** reads the same source, calls the named stylesheet and writes its principal output to the operation log.
- **XSLT Transforms → Expand Embedded XML** contains the supplied XSLT 3.0 stylesheet. Edit it or use Load stylesheet.

The script editor displays named references:

```
output=RunXSLT("<TAG>XSLT Transforms/Expand Embedded XML</TAG>",FileContent);
FileContent=output[0];
```

Create another graph by selecting XSLT in New Operation, then assign its Source, XSLT and Target nodes. Both locations must be file-based.

This release supports the principal result. Includes/imports, xsl:result-document and server project download remain unfinished; use local projects and backup import/export. See C:/ETL/Reports/xslt-integration-status.md for tests and remaining work.
