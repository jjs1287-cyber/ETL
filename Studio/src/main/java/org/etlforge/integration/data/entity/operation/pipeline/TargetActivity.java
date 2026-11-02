package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.link.PropertyBasedEntityLink;
import org.etlforge.util.id.KongaGuid;

public final class TargetActivity extends EntityActivity<Target> {
   public TargetActivity(String var1) {
      super(PipelineActivityType.TARGET, Target.class, var1);
   }

   public TargetActivity(KongaGuid var1, String var2) {
      super(PipelineActivityType.TARGET, Target.class, var1, var2);
   }

   private TargetActivity(Target var1, String var2) {
      super(PipelineActivityType.TARGET, Target.class, var1, var2);
   }

   public static TargetActivity newInstance(Target var0, String var1) {
      return var0 != null ? new TargetActivity(var0, var1) : new TargetActivity(var1);
   }

   public TargetActivity clone() {
      return (TargetActivity)super.clone();
   }

   @Override
   protected EntityLink createOperationLink(Operation var1) {
      return new PropertyBasedEntityLink(this.getContent(), var1, "target_id");
   }

   @Override
   public String getTypeName() {
      return EntityType.Target.getDisplayName();
   }

   @Override
   public String getTypeNamePlural() {
      return EntityType.Target.getPlural();
   }
}
