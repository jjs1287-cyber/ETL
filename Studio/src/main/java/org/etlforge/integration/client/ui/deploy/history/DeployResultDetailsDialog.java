package org.etlforge.integration.client.ui.deploy.history;

import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.util.UiUtils;

public final class DeployResultDetailsDialog {
   public static void display(DeployResultDetailsViewer var0) {
      UiProviderDialogDisplayer.display(UiUtils.getActiveWindow(), var0, "Deploy Result Details", KongaDialog.CLOSE_ONLY, DeployResultDetailsDialog.class);
   }

   private DeployResultDetailsDialog() {
   }
}
