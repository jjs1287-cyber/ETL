package org.etlforge.integration.data.connector;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.id.NetSuiteDeleteId;

public final class NetSuiteDelete extends NetSuiteFunction {
   @IdProperty
   public static final String NetSuite_DELETE_ID = "netsuitedelete_id";
   @NameProperty
   public static final String NetSuite_DELETE_NAME = "netsuitedelete_name";

   public NetSuiteDelete(String var1) {
      super(null, var1, EntityType.NetSuiteDelete);
   }

   public NetSuiteDelete(NetSuiteDeleteId var1, String var2) {
      super(var1, var2, EntityType.NetSuiteDelete);
   }

   public NetSuiteDeleteId getID() {
      return (NetSuiteDeleteId)super.getID();
   }

   @Override
   public NetSuiteFunction.FunctionType getFunctionType() {
      return NetSuiteFunction.FunctionType.DELETE;
   }

   @Override
   public String[] getPropertyKeys() {
      return this.getPropertyKeys("netsuitedelete_id", "netsuitedelete_name");
   }
}
