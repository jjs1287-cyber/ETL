package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.ubs.core.adapter.ebs.connection.EbsDesignConnection;
import com.ubs.core.adapter.ebs.interaction.EbsDesignFunction;
import com.ubs.core.adapter.ebs.interaction.EbsDesignFunctionCatalog;
import com.ubs.core.adapter.ebs.interaction.EbsDesignFunctionHelper;
import java.util.HashMap;
import java.util.Map;

public class EbsDesignWrapper {
   private final EbsDesignConnection connection;
   private EbsDesignFunctionCatalog designCatalog;

   public EbsDesignWrapper(EbsDesignConnection var1) {
      this.connection = var1;
   }

   public Map<String, String> getCatalog() throws Exception {
      HashMap var1 = new HashMap();
      EbsDesignFunctionHelper var2 = new EbsDesignFunctionHelper(this.connection);
      EbsDesignFunctionCatalog var3 = var2.getCatalog();

      for (EbsDesignFunctionCatalog var6 : var3.listFunctionCatalogs()) {
         var1.put(var6.getName(), "");
      }

      return var1;
   }

   public Map<String, String> getPrograms(String var1) throws Exception {
      HashMap var2 = new HashMap();
      EbsDesignFunctionHelper var3 = new EbsDesignFunctionHelper(this.connection);
      this.designCatalog = var3.getCatalog();
      EbsDesignFunctionCatalog var4 = this.designCatalog.getFunctionCatalog(var1);

      for (EbsDesignFunction var7 : var4.listFunctions()) {
         var2.put(var7.getName(), var7.getDescription());
      }

      return var2;
   }
}
