package org.etlforge.integration.client.ui.operation.options;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.entity.operation.options.RunInDebugModeOption;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class DebugModeOptionUi extends BooleanOperationOptionUi {
   public DebugModeOptionUi(Operation var1) {
      this(var1.getOperationOptions(), var1.getPipeline());
   }

   public DebugModeOptionUi(OperationWrapper var1) {
      this(var1.getOptions(), var1.getPipeline());
   }

   private DebugModeOptionUi(OperationOptions var1, OperationPipeline var2) {
      super("Run In &Debug Mode");
      RunInDebugModeOption var3 = var1.getDebugOption();
      this.setSelected(var3.isDebugModeEnabled());
      this.setEnabled(var3.isApplicableFor(var2));
   }

   public RunInDebugModeOption getDefinedOption() {
      return new RunInDebugModeOption(this.isSelected());
   }
}
