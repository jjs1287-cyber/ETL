package org.etlforge.integration.client.ui.deploy.history;

import java.util.List;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.util.KeyUtils;

final class DeleteSelectedDeployResultsAction extends AbstractSingleTypeSelectionAction<DeployResult> {
   private DeployHistory history;

   public DeleteSelectedDeployResultsAction(DeployHistory var1) {
      super(DeployResult.class);
      this.putValue("AcceleratorKey", KeyUtils.DELETE);
      this.setHistory(var1);
   }

   public void setHistory(DeployHistory var1) {
      this.history = var1;
      this.setEnabled(var1 != null);
   }

   protected boolean isEnabledFor(DeployResult var1) {
      return this.history != null;
   }

   @Override
   protected void run(List<DeployResult> var1) {
      for (DeployResult var3 : var1) {
         this.history.removeResult(var3);
      }
   }
}
