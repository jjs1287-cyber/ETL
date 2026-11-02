package org.etlforge.integration.client.ui.deploy;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

public final class DeployWarningsDialog {
   public static void display(DeployResult var0, IntegrationEntityLookup var1) {
      DeployWarningsPresenter var2 = new DeployWarningsPresenter(var0, var1);
      KongaDialog var3 = createDialog(var2);
      var3.setVisible(true);
   }

   private static KongaDialog createDialog(UiProvider var0) {
      KongaDialog var1 = new KongaDialog(getOwner(), Strings.get("Deploy.Result.Dialog.Title"), true);
      layoutDialog(var1, var0);
      var1.setCloseButtonAsDefault();
      var1.pack();
      var1.manageLocation(DeployWarningsDialog.class);
      return var1;
   }

   private static JFrame getOwner() {
      JFrame var0 = ApplicationUi.getAppWinFrame();
      if (var0 == null) {
         var0 = UiUtils.getActiveFrame();
      }

      return var0;
   }

   private static void layoutDialog(KongaDialog var0, UiProvider var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.center(var1).south(createButtonPanel(var0));
      var2.withBorder(Empty.border(10));
      var2.asContentPaneFor(var0);
   }

   private static JComponent createButtonPanel(KongaDialog var0) {
      JPanel var1 = var0.getButtonPanel(KongaDialog.CLOSE_ONLY, 0);
      var0.setFocusedComponent(var0.getCloseButton());
      var1.setBorder(Empty.border(10, 10, 20, 10));
      return var1;
   }

   private DeployWarningsDialog() {
   }
}
