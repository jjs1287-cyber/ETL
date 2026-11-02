package org.etlforge.integration.data.connector.operation;

import com.google.common.base.Optional;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.util.id.KongaGuid;

public final class EBSFunctionActivityType extends PipelineActivityType {
   public static final EBSFunctionActivityType TYPE = new EBSFunctionActivityType();

   private EBSFunctionActivityType() {
      super(16, "EBS_FUNCTION", "EBS Program", "EBS Programs");
   }

   @Override
   public PipelineActivity createEmptyActivity() {
      return new EBSFunctionActivity(this.getDisplayName());
   }

   @Override
   public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
      return new EBSFunctionActivity(var1, var2);
   }

   @Override
   public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
      EntityType var3 = EntityType.fromId((Integer)var2.get());
      return var3.getIntegrationId(var1);
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof EBSFunction;
   }
}
