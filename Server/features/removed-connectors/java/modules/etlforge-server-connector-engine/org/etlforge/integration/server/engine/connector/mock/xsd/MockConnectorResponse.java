package org.etlforge.integration.server.engine.connector.mock.xsd;

import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.mock.ConnectorEndpointMock;

public class MockConnectorResponse {
   public static String buildResponse(ConnectorEndpointMock var0, List<String> var1, Map<String, String> var2) {
      StringBuilder var3 = new StringBuilder();
      XmlElementUtils.startElementTag(var3, "mockresponse");
      buildItems(var0.toMap(), "endpoints", var3);
      buildItems(toMap(var1), "selectedobjects", var3);
      buildItems(var2, "items", var3);
      XmlElementUtils.endElementTag(var3, "mockresponse");
      return var3.toString();
   }

   static void buildItems(Map<String, String> var0, String var1, StringBuilder var2) {
      XmlElementUtils.startElementTag(var2, var1);

      for (Entry var4 : var0.entrySet()) {
         String var5 = "item";
         XmlElementUtils.startElementTag(var2, var5);
         XmlElementUtils.elementValue(var2, "key", (String)var4.getKey());
         XmlElementUtils.elementValue(var2, "value", (String)var4.getValue());
         XmlElementUtils.endElementTag(var2, var5);
      }

      XmlElementUtils.endElementTag(var2, var1);
   }

   static Map<String, String> toMap(List<String> var0) {
      HashMap var1 = Maps.newHashMap();

      for (int var2 = 0; var2 < var0.size(); var2++) {
         String var3 = "K" + var2;
         String var4 = (String)var0.get(var2);
         var1.put(var3, var4);
      }

      return var1;
   }
}
