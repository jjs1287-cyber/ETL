package org.etlforge.integration.server.engine.connector.msgp.adapter;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;
import org.etlforge.xml.sax.StartElementHandler;
import org.xml.sax.SAXException;

public class MsGPModuleListParser {
   public static ArrayList<MsGPModuleListParser.MsGPModuleSchema> parseModuleListResponse(String var0) {
      KongaSaxParser var1 = new KongaSaxParser();
      MsGPModuleListParser.MsGPModuleSchemaHandler var2 = new MsGPModuleListParser.MsGPModuleSchemaHandler();
      var1.registerHandler(var2);

      try {
         var1.parseString(var0);
      } catch (SAXException var4) {
         throw new ConnectorWebServiceRuntimeException(var4);
      }

      return var2.modules;
   }

   public static class MsGPModuleSchema {
      String internalname;
      String displayname;
      String msdnurl;
   }

   @RelativePathRoot("modulesResponses/modulesResponse")
   private static class MsGPModuleSchemaHandler {
      public final ArrayList<MsGPModuleListParser.MsGPModuleSchema> modules = Lists.newArrayList();
      private MsGPModuleListParser.MsGPModuleSchema module;

      private MsGPModuleSchemaHandler() {
      }

      @StartElementHandler("module/schemas/schema")
      public void startSchema() {
         this.module = new MsGPModuleListParser.MsGPModuleSchema();
      }

      @EndElementHandler("module/schemas/schema/internalname")
      public void internalname(String var1) {
         this.module.internalname = var1;
      }

      @EndElementHandler("module/schemas/schema/displayname")
      public void displayname(String var1) {
         this.module.displayname = var1;
      }

      @EndElementHandler("module/schemas/schema/msdnurl")
      public void msdnurl(String var1) {
         this.module.msdnurl = var1;
      }

      @EndElementHandler("module/schemas/schema")
      public void endSchema() {
         this.modules.add(this.module);
      }
   }
}
