package org.etlforge.integration.data.entity.operation.options;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class RunInDebugModeOption extends BooleanOperationOption {
   public RunInDebugModeOption(boolean var1) {
      super(var1);
   }

   public RunInDebugModeOption(Operation var1) {
      super(var1.getIntProperty("debug_mode", 0) == 1);
   }

   public boolean isDebugModeEnabled() {
      return this.getValue();
   }

   @Override
   public void applyTo(Operation var1) {
      var1.setProperty("debug_mode", this.isDebugModeEnabled() ? 1 : 0);
   }

   @Override
   public boolean isApplicableFor(OperationPipeline var1) {
      return true;
   }
}
