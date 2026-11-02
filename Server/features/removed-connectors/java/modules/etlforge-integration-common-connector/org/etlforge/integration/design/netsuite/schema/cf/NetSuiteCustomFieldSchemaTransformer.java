package org.etlforge.integration.design.netsuite.schema.cf;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldSchema;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlSchemaSnippetAssumption;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.SAXException;

public class NetSuiteCustomFieldSchemaTransformer {
   private final String schemaXml;
   private final NetSuiteCustomFieldSchemaHandler handler;

   NetSuiteCustomFieldSchemaTransformer(boolean var1, String var2, String var3, ConnectorCustomFieldSchema var4) {
      this.schemaXml = var3;
      this.handler = new NetSuiteCustomFieldSchemaHandler(var1, var2, var4);
   }

   public String getModifiedSchema() {
      NetSuiteWsdlSchemaSnippetAssumption.validate(this.schemaXml);

      try {
         SaxParsers.parseString(this.schemaXml, this.handler, false);
         return this.handler.sb.toString();
      } catch (SAXException var2) {
         throw new ConnectorNetSuiteDefinitionError(var2);
      }
   }
}
