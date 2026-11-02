package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.NetSuiteUpdateId;

public final class NetSuiteUpdate extends NetSuiteFunction {
   @IdProperty
   public static final String NetSuite_UPDATE_ID = "netsuiteupdate_id";
   @NameProperty
   public static final String NetSuite_UPDATE_NAME = "netsuiteupdate_name";

   public NetSuiteUpdate(String var1) {
      super(null, var1, EntityType.NetSuiteUpdate);
   }

   public NetSuiteUpdate(NetSuiteUpdateId var1, String var2) {
      super(var1, var2, EntityType.NetSuiteUpdate);
   }

   public NetSuiteUpdateId getID() {
      return (NetSuiteUpdateId)super.getID();
   }

   @Override
   public NetSuiteFunction.FunctionType getFunctionType() {
      return NetSuiteFunction.FunctionType.UPDATE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("netsuiteupdate_id", "netsuiteupdate_name");
   }
}
