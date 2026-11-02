package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.NetSuiteCreateId;

public final class NetSuiteCreate extends NetSuiteFunction {
   @IdProperty
   public static final String NetSuite_CREATE_ID = "netsuitecreate_id";
   @NameProperty
   public static final String NetSuite_CREATE_NAME = "netsuitecreate_name";

   public NetSuiteCreate(String var1) {
      super(null, var1, EntityType.NetSuiteCreate);
   }

   public NetSuiteCreate(NetSuiteCreateId var1, String var2) {
      super(var1, var2, EntityType.NetSuiteCreate);
   }

   public NetSuiteCreateId getID() {
      return (NetSuiteCreateId)super.getID();
   }

   @Override
   public NetSuiteFunction.FunctionType getFunctionType() {
      return NetSuiteFunction.FunctionType.CREATE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("netsuitecreate_id", "netsuitecreate_name");
   }
}
