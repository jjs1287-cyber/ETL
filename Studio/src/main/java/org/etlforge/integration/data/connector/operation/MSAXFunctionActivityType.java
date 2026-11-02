package org.etlforge.integration.data.connector.operation;

import com.google.common.base.Optional;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.MSAXFunctionId;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.util.id.KongaGuid;

public final class MSAXFunctionActivityType extends PipelineActivityType {
   public static final MSAXFunctionActivityType TYPE = new MSAXFunctionActivityType();

   private MSAXFunctionActivityType() {
      super(15, "MSAX_FUNCTION", "MSAX Function", "MSAX Functions");
   }

   @Override
   public PipelineActivity createEmptyActivity() {
      return new MSAXFunctionActivity(this.getDisplayName());
   }

   @Override
   public PipelineActivity restoreEmptyActivity(KongaGuid var1, String var2) {
      return new MSAXFunctionActivity(var1, var2);
   }

   @Override
   public IntegrationEntityId createEntityId(String var1, Optional<Integer> var2) {
      return new MSAXFunctionId(var1);
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof MSAXFunction;
   }
}
