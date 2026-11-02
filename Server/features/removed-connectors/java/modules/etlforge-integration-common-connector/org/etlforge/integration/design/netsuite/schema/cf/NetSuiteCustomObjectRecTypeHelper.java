package org.etlforge.integration.design.netsuite.schema.cf;

import org.apache.commons.lang.StringEscapeUtils;

public class NetSuiteCustomObjectRecTypeHelper {
   private static final String _recType = "recType";
   private static final String recTypePrefix = "<element name=\"recType\"><complexType><attribute name=\"internalId\" type=\"xsd:string\" fixed=\"";
   private static final String recTypePostfix = "\"></attribute></complexType></element>";

   public static final String getCustomObjectRecType(String var0) {
      return "<element name=\"recType\"><complexType><attribute name=\"internalId\" type=\"xsd:string\" fixed=\""
         + StringEscapeUtils.escapeXml(var0)
         + "\"></attribute></complexType></element>";
   }
}
