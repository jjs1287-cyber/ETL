package org.etlforge.integration.data.connector.operation;

import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.OperationFunctionLink;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.util.id.KongaGuid;

public final class SapFunctionActivity extends EntityActivity<SapFunction> {
   public SapFunctionActivity(String var1) {
      super(SapFunctionActivityType.TYPE, SapFunction.class, var1);
   }

   public SapFunctionActivity(KongaGuid var1, String var2) {
      super(SapFunctionActivityType.TYPE, SapFunction.class, var1, var2);
   }

   private SapFunctionActivity(SapFunction var1, String var2) {
      super(SapFunctionActivityType.TYPE, SapFunction.class, var1, var2);
   }

   public static SapFunctionActivity newInstance(SapFunction var0, String var1) {
      return var0 != null ? new SapFunctionActivity(var0, var1) : new SapFunctionActivity(var1);
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new OperationFunctionLink(this.getContent(), var1);
   }

   @Override
   public String getTypeName() {
      return EntityType.SapFunction.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.SapFunction.getPlural();
   }
}
