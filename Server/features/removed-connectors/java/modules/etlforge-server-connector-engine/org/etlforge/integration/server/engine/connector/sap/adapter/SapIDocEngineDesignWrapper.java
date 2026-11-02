package org.etlforge.integration.server.engine.connector.sap.adapter;

import com.ubs.core.adapter.sap.idoc.connection.SapIDocDesignConnection;
import com.ubs.core.adapter.sap.idoc.interaction.SapIDocDesignFunction;
import com.ubs.core.adapter.sap.idoc.interaction.SapIDocDesignFunctionCatalog;
import com.ubs.core.adapter.sap.idoc.interaction.SapIDocDesignFunctionHelper;
import com.ubs.core.adapter.sap.idoc.interaction.SapIDocDesignInteraction;
import com.ubs.core.adapter.sap.idoc.schema.SapIDocSchemaManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SapIDocEngineDesignWrapper {
   private final SapIDocDesignConnection sidc;
   private final SapIDocDesignFunctionHelper sdfh;
   private SapIDocSchemaManager schemaManager;

   public SapIDocEngineDesignWrapper(SapIDocDesignConnection var1) {
      this.sidc = var1;
      this.sdfh = var1.createInteraction(new Properties()).createFuntionHelper();
   }

   public void setSchemaManager(String var1, String var2) {
      Properties var3 = new Properties();
      var3.put("idoctype", var2);
      SapIDocDesignInteraction var4 = this.sidc.createInteraction(var3);
      this.schemaManager = var4.createSchemaManager();
   }

   public String getRequestSchema() throws Exception {
      return (String)this.schemaManager.createRequestSchema().getRecord();
   }

   public String getResponseSchema() throws Exception {
      return (String)this.schemaManager.createResponseSchema().getRecord();
   }

   public String getRequestRootElementExpandedName() {
      return this.schemaManager.getRequestSchemaRootElementNameSpace();
   }

   public String getResponseRootElementExpandedName() {
      return this.schemaManager.getResponseSchemaRootElementNameSpace();
   }

   public Map<String, String> getMessageTypeList(String var1) throws Exception {
      HashMap var2 = new HashMap();
      SapIDocDesignFunctionCatalog var3 = this.sdfh.getCatalog();

      for (SapIDocDesignFunctionCatalog var6 : var3.listFunctionCatalogs()) {
         String var7 = var6.getName();
         SapIDocDesignFunction var8 = var6.listFunctions().get(0);
         String var9 = var8 != null ? var8.getDescription() : "";
         var2.put(var7, var9);
      }

      return var2;
   }

   public Map<String, String> getIDocTypeList(String var1, String var2) throws Exception {
      HashMap var3 = new HashMap();
      SapIDocDesignFunctionCatalog var4 = this.sdfh.getCatalog();
      SapIDocDesignFunctionCatalog var5 = var4.getFunctionCatalog(var1);
      if (var5 != null) {
         for (SapIDocDesignFunction var8 : var5.listFunctions()) {
            String var9 = var8.getName();
            String var10 = var8.getDescription();
            var3.put(var9, var10);
         }
      }

      return var3;
   }
}
