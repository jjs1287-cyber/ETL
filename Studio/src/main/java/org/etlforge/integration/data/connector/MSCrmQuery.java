package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.MSCrmQueryId;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.system.ArgCheck;

public final class MSCrmQuery extends MSCrmFunction {
   @IdProperty
   public static final String MSCRM_QUERY_ID = "mscrmquery_id";
   @NameProperty
   public static final String MSCRM_QUERY_NAME = "mscrmquery_name";

   public MSCrmQuery(String var1) {
      super(var1, EntityType.MSCrmQuery);
   }

   public MSCrmQuery(MSCrmQueryId var1, String var2) {
      super(var1, var2, EntityType.MSCrmQuery);
   }

   @Override
   public FunctionType getFunctionType() {
      return FunctionType.RETRIEVE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("mscrmquery_id", "mscrmquery_name");
   }

   public void setQueryXml(String var1) {
      this.setStringProperty("query_xml", ArgCheck.checkNotEmpty(var1));
   }

   public String getQueryXml() {
      synchronized (this.getDataLock()) {
         return this.getProperty("query_xml");
      }
   }

   @Override
   public boolean hasRequestTransformation() {
      return false;
   }
}
