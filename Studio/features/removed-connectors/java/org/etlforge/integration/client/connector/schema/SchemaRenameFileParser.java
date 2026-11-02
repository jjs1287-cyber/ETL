package org.etlforge.integration.client.connector.schema;

import java.io.File;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.client.connector.serverapi.MultipleSchemas;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.XsdNamingStrategy;
import org.etlforge.integration.design.util.SchemaRenameParser;
import org.etlforge.xml.XmlFileWriter;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;

public class SchemaRenameFileParser {
   private final SchemaRenameStrategyFile rename;

   public SchemaRenameFileParser(AbstractConnectorCall var1, XsdNamingStrategy var2, MultipleSchemas var3, MultipleSchemasProcessor.ProcessedFiles var4) {
      this.rename = new SchemaRenameStrategyFile(var1, var2, var3, var4);
   }

   public void process(File var1) throws Exception {
      Document var2 = DomParser.parseFile(var1, true);
      this.rename.setFile(var1);
      SchemaRenameParser var3 = new SchemaRenameParser(this.rename, var2);
      boolean var4 = var3.process();
      if (var4) {
         XmlFileWriter var5 = new XmlFileWriter();
         var5.write(var2, var1);
      }
   }

   public Set<Entry<String, String>> newReferences() {
      return this.rename.newReferences();
   }
}
