package org.etlforge.integration.server.netsuite.config;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.xml.sax.SAXException;

public class NetSuiteConfigParser {
   static NetSuiteConfigSetting parseString(String var0) {
      return parse(false, var0);
   }

   static NetSuiteConfigSetting parse(boolean var0, String var1) {
      try {
         return parseImpl(var0, var1);
      } catch (SAXException var3) {
         throw new ConnectorNetSuiteDefinitionError("failed to parse search configuration file", var3);
      } catch (IOException var4) {
         throw new ConnectorNetSuiteDefinitionError("failed to read search configuration file", var4);
      }
   }

   static NetSuiteConfigSetting parseImpl(boolean var0, String var1) throws SAXException, IOException {
      String var2 = "config/";
      String var3 = var2 + "searchconfig" + "/";
      String var4 = var3 + "customfieldtypes" + "/";
      String var5 = var3 + "customfieldobjects" + "/";
      String var6 = var3 + "itemsearch" + "/";
      String var7 = var3 + "transactionsearch" + "/";
      NetSuiteCustomFieldTypeHandler var8 = new NetSuiteCustomFieldTypeHandler(var4);
      NetSuiteCustomFieldObjectHandler var9 = new NetSuiteCustomFieldObjectHandler(var5);
      NetSuiteCustomSearchSubtypeHandler var10 = new NetSuiteCustomSearchSubtypeHandler(var6);
      NetSuiteCustomSearchSubtypeHandler var11 = new NetSuiteCustomSearchSubtypeHandler(var7);
      NetSuiteConfigParser.Handler var12 = new NetSuiteConfigParser.Handler(var2);
      KongaSaxParser var13 = new KongaSaxParser();
      var13.registerHandler(var12);
      var13.registerHandler(var8);
      var13.registerHandler(var9);
      var13.registerHandler(var10);
      var13.registerHandler(var11);
      if (var0) {
         var13.parseFile(new File(var1));
      } else {
         var13.parseString(var1);
      }

      return new NetSuiteConfigSetting(var12.executionCacheSize, var12.designCacheSize, var8.list, var9.list, var10.list, var11.list);
   }

   private static final class Handler {
      private int executionCacheSize;
      private int designCacheSize;
      @RelativePathRoot
      public final String pathRoot;

      Handler(String var1) {
         this.pathRoot = var1 + "cachesize" + "/";
      }

      @EndElementHandler("execution")
      public void endExecution(String var1) {
         this.executionCacheSize = convert(var1);
      }

      @EndElementHandler("design")
      public void endDesign(String var1) {
         this.designCacheSize = convert(var1);
      }

      private static int convert(String var0) {
         if (Strings.isNullOrEmpty(var0)) {
            return 0;
         }

         String var1 = var0.trim();
         if (Strings.isNullOrEmpty(var1)) {
            return 0;
         }

         try {
            return Integer.parseInt(var1);
         } catch (Throwable var3) {
            return -1;
         }
      }
   }
}
