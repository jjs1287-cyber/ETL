package org.etlforge.integration.design.netsuite.schema.cf;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.etlforgeconnector.XmlQName;

public class NetSuiteCustomFieldListComplexTypeName {
   static String getNewLocalName(String var0) {
      return "CustomFieldList" + var0;
   }

   static void validateType(XmlQName var0) {
      boolean var1 = "CustomFieldList".equals(var0.getLocalName()) || "SearchColumnCustomFieldList".equals(var0.getLocalName());
      if (!var1) {
         throw new ConnectorNetSuiteDefinitionError("wsdl contains unexpected type " + var0.getQName());
      }
   }
}
