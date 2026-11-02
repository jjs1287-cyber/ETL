package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.NetSuiteUpsertId;

public final class NetSuiteUpsert extends NetSuiteFunction {
   @IdProperty
   public static final String NetSuite_UPSERT_ID = "netsuiteupsert_id";
   @NameProperty
   public static final String NetSuite_UPSERT_NAME = "netsuiteupsert_name";

   public NetSuiteUpsert(String var1) {
      super(null, var1, EntityType.NetSuiteUpsert);
   }

   public NetSuiteUpsert(NetSuiteUpsertId var1, String var2) {
      super(var1, var2, EntityType.NetSuiteUpsert);
   }

   public NetSuiteUpsertId getID() {
      return (NetSuiteUpsertId)super.getID();
   }

   @Override
   public NetSuiteFunction.FunctionType getFunctionType() {
      return NetSuiteFunction.FunctionType.UPSERT;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("netsuiteupsert_id", "netsuiteupsert_name");
   }
}
