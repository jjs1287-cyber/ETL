package org.etlforge.integration.design.netsuite.definition;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;

public class NetSuiteWsdlSchemaSnippetAssumption {
   public static final void validate(String var0) {
      boolean var1 = var0.contains("<xs");
      if (var1) {
         throw new ConnectorNetSuiteDefinitionError("wsdl contains <xs. Not Implemented : snippet " + var0);
      }
   }
}
