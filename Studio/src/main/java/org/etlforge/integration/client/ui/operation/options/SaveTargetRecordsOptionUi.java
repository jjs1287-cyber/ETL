package org.etlforge.integration.client.ui.operation.options;

import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.operation.options.SaveTargetRecordsOption;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnPanel;

public final class SaveTargetRecordsOptionUi implements OperationOptionUi, Disposable {
   private final DestinationSelector success;
   private final DestinationSelector failure;

   public SaveTargetRecordsOptionUi(SaveTargetRecordsOption var1, EntityExplorerSupport var2) {
      this.success = new DestinationSelector(var2, var1.getDestinationForSuccessRecords(), "success");
      this.failure = new DestinationSelector(var2, var1.getDestinationForFailureRecords(), "failure");
   }

   @Override
   public void decorate(UiCanvas var1) {
      TwoColumnPanel var2 = TwoColumnPanel.forFormEntry(5, 10);
      this.success.layoutIn(var2);
      this.failure.layoutIn(var2);
      var1.display(var2.invisible());
   }

   @Override
   public void dispose() {
      this.success.dispose();
      this.failure.dispose();
   }

   @Override
   public void requestFocus() {
      this.success.requestFocus();
   }

   public SaveTargetRecordsOption getDefinedOption() {
      return new SaveTargetRecordsOption(this.success.getDestination(), this.failure.getDestination());
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      this.success.registerAsChangeSource(var1);
      this.failure.registerAsChangeSource(var1);
   }
}
