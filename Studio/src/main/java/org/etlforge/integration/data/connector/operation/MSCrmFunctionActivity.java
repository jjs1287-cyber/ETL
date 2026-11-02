package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.OperationFunctionLink;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.util.id.KongaGuid;

public final class MSCrmFunctionActivity extends EntityActivity<MSCrmFunction> {
   public MSCrmFunctionActivity(KongaGuid var1, String var2) {
      super(MSCrmFunctionActivityType.TYPE, MSCrmFunction.class, var1, var2);
   }

   public MSCrmFunctionActivity(MSCrmFunction var1, String var2) {
      super(MSCrmFunctionActivityType.TYPE, MSCrmFunction.class, var1, var2);
   }

   public MSCrmFunctionActivity(String var1) {
      super(MSCrmFunctionActivityType.TYPE, MSCrmFunction.class, var1);
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new OperationFunctionLink(this.getContent(), var1);
   }

   @Override
   public String getTypeName() {
      return EntityType.MSCrmFunction.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.MSCrmFunction.getPlural();
   }
}
