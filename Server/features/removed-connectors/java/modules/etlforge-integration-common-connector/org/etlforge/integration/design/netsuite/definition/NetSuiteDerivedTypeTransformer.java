package org.etlforge.integration.design.netsuite.definition;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.SAXException;

public class NetSuiteDerivedTypeTransformer {
   private final NetSuiteDerivedTypeDefaultHandler handler;

   public NetSuiteDerivedTypeTransformer(NetSuiteDerivedTypeDefaultHandler var1) {
      this.handler = var1;
   }

   public String transform(XsdGlobalElementName var1, String var2) {
      NetSuiteWsdlSchemaSnippetAssumption.validate(var2);

      try {
         return this.transformImpl(var1, var2);
      } catch (SAXException var4) {
         throw new ConnectorNetSuiteDefinitionError(var4);
      }
   }

   protected String transformImpl(XsdGlobalElementName var1, String var2) throws SAXException {
      this.handler.init();
      this.handler.setObjectName(var1.getLocalName());
      SaxParsers.parseString(var2, this.handler, false);
      return this.handler.sb.toString();
   }
}
