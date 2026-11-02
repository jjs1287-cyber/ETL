package org.etlforge.integration.server.engine.connector.msax.rest;

import java.io.IOException;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlWriter;

public class MsAXRequestBodyExecution {
   public static void executeOperationRequestBody(String var0, String var1, String var2, String var3, String var4, String var5) {
      XmlWriter var6 = new XmlWriter(var5);

      try {
         var6.append(XmlElementUtils.startElementTag(var0));
         var6.append("<operation>");
         StringBuilder var7 = new StringBuilder();
         XmlElementUtils.elementValue(var7, "wsdlendpoint", var1);
         XmlElementUtils.elementValue(var7, "servicename", var2);
         XmlElementUtils.elementValue(var7, "name", var3);
         var6.append(var7.toString());
         var6.appendFileUtf8(var4);
         var6.append("</operation>");
         var6.append(XmlElementUtils.endElementTag(var0));
      } catch (IOException var11) {
         throw new ConnectorServerRuntimeException(var11);
      } finally {
         var6.dispose();
      }
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/MSAX/Data/";
      String var2 = var1 + "SalesOrder.xml";
      String var3 = var1 + "SalesOrder_Body.xml";
      executeOperationRequestBody("restServiceAction", null, null, null, var2, var3);
   }
}
