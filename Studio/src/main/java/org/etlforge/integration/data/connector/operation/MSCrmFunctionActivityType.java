package org.etlforge.integration.data.connector.operation;

import com.google.common.base.Optional;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.util.id.KongaGuid;

public final class MSCrmFunctionActivityType extends PipelineActivityType {
   public static final MSCrmFunctionActivityType TYPE = new MSCrmFunctionActivityType();

   private MSCrmFunctionActivityType() {
      super(12, "MSCRM_FUNCTION", "MS CRM Function", "MS CRM Functions");
   }

   @Override
   public PipelineActivity createEmptyActivity() {
      return new MSCrmFunctionActivity(this.getDisplayName());
   }

   @Override
   public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
      return new MSCrmFunctionActivity(var1, var2);
   }

   @Override
   public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
      EntityType var3 = EntityType.fromId((Integer)var2.get());
      return var3.getIntegrationId(var1);
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof MSCrmFunction;
   }
}
