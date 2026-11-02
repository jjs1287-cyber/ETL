package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlWriter;
import org.etlforge.io.FileUtils;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class AutoDeskGetOperation {
   public static void executeGetOperation(AutoDeskEndpoint var0, String var1, String var2, String var3) {
      AutoDeskGetOperationOptions var4 = AutoDeskGetOperationOptions.parseGetListSetting(var2);
      String var5 = FileUtils.getPostfixFilenameXml(var3, "_kv");
      String var6 = getUrl(var4, var0, var1);
      if (var4.isSigleItem()) {
         executeGetItemUrl(var0, var6, var5);
      } else if (var4.isItemsByAlias()) {
         String var7 = FileUtils.getPostfixFilenameXml(var3, "_alias");
         executeGetOperationUrl(var0, var6, var7);
         List var8 = parseAliasUrlList(true, var7);
         executeGetItemUrlList(var0, var8, var5);
      } else {
         executeGetOperationUrl(var0, var6, var5);
      }

      AutoDeskItemResponseConversion var9 = new AutoDeskItemResponseConversion();
      var9.convertFromKeyValuePairsFile(var5, var3);
   }

   static String getUrl(AutoDeskGetOperationOptions var0, AutoDeskEndpoint var1, String var2) {
      return var1.getUrl(var2, var0.toGetSettingParameters());
   }

   static void executeGetOperationUrl(AutoDeskEndpoint var0, String var1, String var2) {
      AutoDeskRestServiceHandler var4 = new AutoDeskRestServiceHandler("GET", var1);
      AutoDeskCallResponse var5 = var4.executeFile(var0.getProxySettings(), var0.autodeskSession, var2);
      var5.throwRuntimeExceptionOnBadResponse();
   }

   static void executeGetItemUrl(AutoDeskEndpoint var0, String var1, String var2) {
      ArrayList var3 = Lists.newArrayList();
      var3.add(var1);
      executeGetItemUrlList(var0, var3, var2);
   }

   static void executeGetItemUrlList(AutoDeskEndpoint var0, List<String> var1, String var2) {
      XmlWriter var3 = new XmlWriter(var2);

      try {
         var3.append("<payload><list>");
         byte var4 = 1;

         for (int var5 = 0; var5 < var1.size(); var5++) {
            String var6 = FileUtils.getPostfixFilenameXml(var2, "_" + var5);
            executeGetOperationUrl(var0, (String)var1.get(var5), var6);
            var3.appendFileUtf8(var6, var4);
         }

         var3.append("</list></payload>");
      } catch (IOException var10) {
         throw new ConnectorServerRuntimeException(var10);
      } finally {
         var3.dispose();
      }
   }

   static List<String> parseAliasUrlList(boolean var0, String var1) {
      AutoDeskGetOperation.AliasUrlListResponseHandler var2 = new AutoDeskGetOperation.AliasUrlListResponseHandler();
      KongaSaxParser var3 = new KongaSaxParser();
      var3.registerHandler(var2);

      try {
         if (var0) {
            var3.parseFile(new File(var1));
         } else {
            var3.parseString(var1);
         }

         return var2.urlList;
      } catch (SAXException var5) {
         throw new ConnectorServerRuntimeException(var5);
      } catch (IOException var6) {
         throw new ConnectorServerRuntimeException(var6);
      }
   }

   @RelativePathRoot("payload/list/data")
   private static class AliasUrlListResponseHandler {
      final List<String> urlList = Lists.newArrayList();

      private AliasUrlListResponseHandler() {
      }

      @EndElementHandler("uri")
      public void uri(String var1) {
         this.urlList.add(var1);
      }
   }
}
