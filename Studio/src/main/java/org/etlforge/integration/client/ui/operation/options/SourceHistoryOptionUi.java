package org.etlforge.integration.client.ui.operation.options;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.entity.operation.options.SourceHistoryOption;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public final class SourceHistoryOptionUi extends BooleanOperationOptionUi {
   public SourceHistoryOptionUi(Operation var1) {
      this(var1.getOperationOptions(), var1.getPipeline());
   }

   public SourceHistoryOptionUi(OperationWrapper var1) {
      this(var1.getOptions(), var1.getPipeline());
   }

   private SourceHistoryOptionUi(OperationOptions var1, OperationPipeline var2) {
      super("Do not use &processed files");
      SourceHistoryOption var3 = var1.getSourceHistoryOption();
      this.setSelected(var3.isHistoryChecked());
      this.setEnabled(var3.isApplicableFor(var2));
   }

   public SourceHistoryOption getDefinedOption() {
      return new SourceHistoryOption(this.isSelected());
   }
}
