package org.etlforge.integration.server.engine.connector.msax.rest;

import org.etlforge.integration.design.util.XmlElementUtils;

public class MsAxRequestBodyDesign {
   public static String testConnectionRequestBody(String var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      XmlElementUtils.startElementTag(var2, var0);
      XmlElementUtils.elementValue(var2, "wsdlendpoint", var1);
      XmlElementUtils.endElementTag(var2, var0);
      return var2.toString();
   }

   public static String operationRequestBody(String var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      XmlElementUtils.startElementTag(var2, var0);
      XmlElementUtils.elementValue(var2, "wsdlendpoint", var1);
      XmlElementUtils.endElementTag(var2, var0);
      return var2.toString();
   }

   public static String operationSchemaRequestBody(String var0, boolean var1, String var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder();
      XmlElementUtils.startElementTag(var5, var0);
      XmlElementUtils.startElementTag(var5, "operation");
      XmlElementUtils.elementValue(var5, "wsdlendpoint", var2);
      XmlElementUtils.elementValue(var5, "servicename", var3);
      XmlElementUtils.elementValue(var5, "name", var4);
      XmlElementUtils.elementValue(var5, "refresh", var1 ? "true" : "false");
      XmlElementUtils.endElementTag(var5, "operation");
      XmlElementUtils.endElementTag(var5, var0);
      return var5.toString();
   }
}
