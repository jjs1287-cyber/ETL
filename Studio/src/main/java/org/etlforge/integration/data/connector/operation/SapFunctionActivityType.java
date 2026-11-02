package org.etlforge.integration.data.connector.operation;

import com.google.common.base.Optional;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.SapFunctionId;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.util.id.KongaGuid;

public final class SapFunctionActivityType extends PipelineActivityType {
   public static final SapFunctionActivityType TYPE = new SapFunctionActivityType();

   private SapFunctionActivityType() {
      super(11, "SAP_FUNCTION", "SAP Function", "SAP Functions");
   }

   @Override
   public PipelineActivity createEmptyActivity() {
      return new SapFunctionActivity(this.getDisplayName());
   }

   @Override
   public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
      return new SapFunctionActivity(var1, var2);
   }

   @Override
   public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
      return new SapFunctionId(var1);
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof SapFunction;
   }
}
