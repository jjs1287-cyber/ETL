package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.OperationFunctionLink;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.util.id.KongaGuid;

public final class MSAXFunctionActivity extends EntityActivity<MSAXFunction> {
   public MSAXFunctionActivity(String var1) {
      super(MSAXFunctionActivityType.TYPE, MSAXFunction.class, var1);
   }

   public MSAXFunctionActivity(KongaGuid var1, String var2) {
      super(MSAXFunctionActivityType.TYPE, MSAXFunction.class, var1, var2);
   }

   private MSAXFunctionActivity(MSAXFunction var1, String var2) {
      super(MSAXFunctionActivityType.TYPE, MSAXFunction.class, var1, var2);
   }

   public static MSAXFunctionActivity newInstance(MSAXFunction var0, String var1) {
      return var0 != null ? new MSAXFunctionActivity(var0, var1) : new MSAXFunctionActivity(var1);
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new OperationFunctionLink(this.getContent(), var1);
   }

   @Override
   public String getTypeName() {
      return EntityType.MSAXFunction.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.MSAXFunction.getPlural();
   }
}
