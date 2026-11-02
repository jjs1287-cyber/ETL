package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.OperationFunctionLink;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.util.id.KongaGuid;

public final class AutoDeskFunctionActivity extends EntityActivity<AutoDeskFunction> {
   public AutoDeskFunctionActivity(KongaGuid var1, String var2) {
      super(AutoDeskFunctionActivityType.TYPE, AutoDeskFunction.class, var1, var2);
   }

   public AutoDeskFunctionActivity(AutoDeskFunction var1, String var2) {
      super(AutoDeskFunctionActivityType.TYPE, AutoDeskFunction.class, var1, var2);
   }

   public AutoDeskFunctionActivity(String var1) {
      super(AutoDeskFunctionActivityType.TYPE, AutoDeskFunction.class, var1);
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new OperationFunctionLink(this.getContent(), var1);
   }

   @Override
   public String getTypeName() {
      return EntityType.AutoDeskFunction.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.AutoDeskFunction.getPlural();
   }
}
