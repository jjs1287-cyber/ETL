package org.etlforge.integration.server.engine.connector.sap.adapter;

import com.ubs.core.adapter.exception.AdapterSchemaException;
import com.ubs.core.adapter.sap.rfc.connection.SapDesignConnection;
import com.ubs.core.adapter.sap.rfc.interaction.SapDesignFunction;
import com.ubs.core.adapter.sap.rfc.interaction.SapDesignFunctionHelper;
import com.ubs.core.adapter.sap.rfc.interaction.SapDesignInteraction;
import com.ubs.core.adapter.sap.rfc.interaction.SapFunctionSearchFilter;
import com.ubs.core.adapter.sap.rfc.schema.SapSchemaManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SapEngineDesignWrapper {
   private final SapDesignConnection sidc;
   private SapSchemaManager schemaManager = null;

   public SapEngineDesignWrapper(SapDesignConnection var1) {
      this.sidc = var1;
   }

   public void setSchemaManager(String var1) {
      Properties var2 = new Properties();
      var2.put("function", var1);
      SapDesignInteraction var3 = this.sidc.createInteraction(var2);
      this.schemaManager = var3.createSchemaManager();
   }

   public String getRequestSchema() throws AdapterSchemaException {
      return (String)this.schemaManager.createRequestSchema().getRecord();
   }

   public String getResponseSchema() throws AdapterSchemaException {
      return (String)this.schemaManager.createResponseSchema().getRecord();
   }

   public String getRequestRootElementExpandedName() {
      return this.schemaManager.getRequestSchemaRootElementNameSpace();
   }

   public String getResponseRootElementExpandedName() {
      return this.schemaManager.getResponseSchemaRootElementNameSpace();
   }

   public Map<String, String> getObjectList(boolean var1, boolean var2, String var3) throws Exception {
      HashMap var4 = new HashMap();
      if (this.sidc != null) {
         ArrayList var5 = null;
         SapDesignInteraction var6 = this.sidc.createInteraction(new Properties());
         SapDesignFunctionHelper var7 = var6.createFuntionHelper();
         SapFunctionSearchFilter var8 = new SapFunctionSearchFilter(var3);
         if (var1) {
            var5 = var7.searchFunctions(SapDesignFunctionHelper.RFC, var8);
         } else if (var2) {
            var5 = var7.searchFunctions(SapDesignFunctionHelper.BAPI, var8);
         }

         if (var5 != null) {
            for (Object __etlItem60 : var5) {
               SapDesignFunction var10 = (SapDesignFunction) __etlItem60;
               var4.put(var10.getName(), var10.getDescription());
            }
         }
      }

      return var4;
   }
}
