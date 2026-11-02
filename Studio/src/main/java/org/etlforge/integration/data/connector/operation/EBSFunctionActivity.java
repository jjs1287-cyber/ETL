package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.OperationFunctionLink;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.util.id.KongaGuid;

public final class EBSFunctionActivity extends EntityActivity<EBSFunction> {
   public EBSFunctionActivity(String var1) {
      super(EBSFunctionActivityType.TYPE, EBSFunction.class, var1);
   }

   public EBSFunctionActivity(KongaGuid var1, String var2) {
      super(EBSFunctionActivityType.TYPE, EBSFunction.class, var1, var2);
   }

   private EBSFunctionActivity(EBSFunction var1, String var2) {
      super(EBSFunctionActivityType.TYPE, EBSFunction.class, var1, var2);
   }

   public static EBSFunctionActivity newInstance(EBSFunction var0, String var1) {
      return var0 != null ? new EBSFunctionActivity(var0, var1) : new EBSFunctionActivity(var1);
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new OperationFunctionLink(this.getContent(), var1);
   }

   @Override
   public String getTypeName() {
      return EntityType.EBSFunction.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.EBSFunction.getPlural();
   }
}
