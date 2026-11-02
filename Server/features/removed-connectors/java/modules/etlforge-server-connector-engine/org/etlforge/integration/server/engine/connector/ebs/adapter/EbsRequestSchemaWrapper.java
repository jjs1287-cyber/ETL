package org.etlforge.integration.server.engine.connector.ebs.adapter;

import com.ubs.core.adapter.ebs.connection.EbsDesignConnection;
import com.ubs.core.adapter.ebs.schema.EbsSchemaManager;
import com.ubs.core.adapter.exception.AdapterSchemaException;
import java.util.Properties;

public class EbsRequestSchemaWrapper {
   final EbsSchemaManager schemaManager;

   public EbsRequestSchemaWrapper(EbsDesignConnection var1, String var2) {
      Properties var3 = new Properties();
      var3.put("programname", var2);
      this.schemaManager = new EbsSchemaManager(var1, var3);
   }

   public String getRequestSchema() throws AdapterSchemaException {
      return (String)this.schemaManager.createRequestSchema().getRecord();
   }

   public String getRequestRootElementExpandedName() {
      return this.schemaManager.getRequestSchemaRootElementNameSpace();
   }
}
