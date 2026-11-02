package org.etlforge.integration.client.ui.interchange.core.action;

import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.ui.entity.tree.EntityCollectionTreeFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.tree.KongaTree;

public final class DeleteConfirmer {
   public static boolean confirm() {
      Answer var0 = Ask.yesOrNo(PackageResources.DeleteConfirmer.MESSAGE, PackageResources.DeleteConfirmer.TITLE);
      return var0.isYes();
   }

   public static boolean confirm(List<IntegrationEntity> var0) {
      if (!ConfirmDeletePreference.get()) {
         return true;
      }

      JFrame var1 = UiUtils.getActiveFrame();
      DeleteConfirmer.Ui var2 = new DeleteConfirmer.Ui(var0);
      KongaDialog var3 = UiProviderDialogDisplayer.createDialog(var1, var2, PackageResources.DeleteConfirmer.TITLE, KongaDialog.OK_CANCEL, null);
      var3.getOKButton().setText("Delete");
      var3.getRootPane().setDefaultButton(var3.getOKButton());
      var3.setVisible(true);
      var2.updatePreferences();
      return var3.wasOkPressed();
   }

   private DeleteConfirmer() {
   }

   private static class Ui implements UiProvider {
      private final KongaTree tree;
      private final JCheckBox doNotShowAgainCheck;

      public Ui(List<IntegrationEntity> var1) {
         this.tree = this.createTree(var1);
         this.doNotShowAgainCheck = new KongaCheckBox("&Do not show again");
      }

      @Override
      public void decorate(UiCanvas var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
         var2.northToSouth(PackageResources.DeleteConfirmer.MESSAGE, this.tree, this.doNotShowAgainCheck);
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.tree.requestFocusInWindow();
      }

      public void updatePreferences() {
         if (this.doNotShowAgainCheck.isSelected()) {
            ConfirmDeletePreference.set(false);
         }
      }

      private KongaTree createTree(List<IntegrationEntity> var1) {
         EntityCollectionTreeFactory var2 = new EntityCollectionTreeFactory();
         return var2.newTree(var1);
      }
   }
}
