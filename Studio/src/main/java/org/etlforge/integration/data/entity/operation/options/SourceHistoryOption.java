package org.etlforge.integration.data.entity.operation.options;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.SourceActivity;

public final class SourceHistoryOption extends BooleanOperationOption {
   public SourceHistoryOption(boolean var1) {
      super(var1);
   }

   public SourceHistoryOption(Operation var1) {
      super(var1.getIntProperty("ignore_history", 0) == 0);
   }

   public boolean isHistoryChecked() {
      return this.getValue();
   }

   @Override
   public boolean isApplicableFor(OperationPipeline var1) {
      if (!var1.getType().hasSourceHistory()) {
         return false;
      }

      Source var2 = getSource(var1);
      return var2 != null && var2.canHaveHistory();
   }

   @Override
   public void applyTo(Operation var1) {
      int var2 = this.isHistoryChecked() ? 0 : 1;
      var1.setProperty("ignore_history", var2);
   }

   private static Source getSource(OperationPipeline var0) {
      for (PipelineActivity var2 : var0.getActivities()) {
         if (var2 instanceof SourceActivity) {
            return ((SourceActivity)var2).getContent();
         }
      }

      return null;
   }
}
