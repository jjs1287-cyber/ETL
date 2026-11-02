package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.OperationFunctionLink;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.util.id.KongaGuid;

public final class NetSuiteFunctionActivity extends EntityActivity<NetSuiteFunction> {
   public NetSuiteFunctionActivity(KongaGuid var1, String var2) {
      super(NetSuiteFunctionActivityType.TYPE, NetSuiteFunction.class, var1, var2);
   }

   public NetSuiteFunctionActivity(NetSuiteFunction var1, String var2) {
      super(NetSuiteFunctionActivityType.TYPE, NetSuiteFunction.class, var1, var2);
   }

   public NetSuiteFunctionActivity(String var1) {
      super(NetSuiteFunctionActivityType.TYPE, NetSuiteFunction.class, var1);
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new OperationFunctionLink(this.getContent(), var1);
   }

   @Override
   public String getTypeName() {
      return EntityType.NetSuiteFunction.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.NetSuiteFunction.getPlural();
   }
}
