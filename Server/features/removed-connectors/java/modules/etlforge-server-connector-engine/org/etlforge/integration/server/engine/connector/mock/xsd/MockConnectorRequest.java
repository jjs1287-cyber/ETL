package org.etlforge.integration.server.engine.connector.mock.xsd;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class MockConnectorRequest {
   static Map<String, String> parseRequestFile(String var0) {
      return parseRequest(true, var0);
   }

   public static Map<String, String> parseRequestXmlString(String var0) {
      return parseRequest(false, var0);
   }

   private static Map<String, String> parseRequest(boolean var0, String var1) {
      if (var1 != null && !var1.isEmpty()) {
         MockConnectorRequest.MockRequestHandler var2 = new MockConnectorRequest.MockRequestHandler();
         KongaSaxParser var3 = new KongaSaxParser();
         var3.registerHandler(var2);

         try {
            if (var0) {
               var3.parseFile(new File(var1));
            } else {
               var3.parseString(var1);
            }

            return var2.items;
         } catch (SAXException var5) {
            throw new ConnectorServerRuntimeException(var5);
         } catch (IOException var6) {
            throw new ConnectorServerRuntimeException(var6);
         }
      } else {
         return Maps.newHashMap();
      }
   }

   static String requestSample() {
      StringBuilder var0 = new StringBuilder();
      XmlElementUtils.startElementTag(var0, "mockrequest");
      HashMap var1 = Maps.newHashMap();
      var1.put("K1", "V1");
      var1.put("K2", "V2");
      MockConnectorResponse.buildItems(var1, "items", var0);
      XmlElementUtils.endElementTag(var0, "mockrequest");
      return var0.toString();
   }

   public static void main(String[] var0) {
      String var1 = requestSample();
      Map var2 = parseRequestXmlString(var1);
      String var3 = var2.toString();
   }

   @RelativePathRoot("mockrequest/items/item")
   private static class MockRequestHandler {
      Map<String, String> items = Maps.newHashMap();
      String key = "NoKey";
      String value = "NoValue";

      private MockRequestHandler() {
      }

      @EndElementHandler("key")
      public void key(String var1) {
         this.key = var1;
      }

      @EndElementHandler("value")
      public void val(String var1) {
         this.value = var1;
      }

      @EndElementHandler("/")
      public void item() {
         this.items.put(this.key, this.value);
         this.key = "NoKey";
         this.value = "NoValue";
      }
   }
}
