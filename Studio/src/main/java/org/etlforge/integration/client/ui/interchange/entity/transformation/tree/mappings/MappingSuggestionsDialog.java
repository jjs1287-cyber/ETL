package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings;

import java.awt.Window;
import javax.swing.SwingUtilities;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.dialog.KongaDialog;

public final class MappingSuggestionsDialog {
   private final MappingSuggestionUi ui;
   private final SuggestionsController controller;
   private final SuggestionsModel model;
   private Receiver<Node> externalCallback;

   public MappingSuggestionsDialog(SuggestionsModel var1) {
      this.model = var1;
      this.ui = new MappingSuggestionUi(var1);
      this.controller = new SuggestionsController(var1);
   }

   public void setCallback(Receiver<Node> var1) {
      this.externalCallback = var1;
   }

   public void open() {
      KongaDialog var1 = this.createDialog();
      this.installCallback(var1);
      var1.setVisible(true);
      this.controller.stopSearch();
   }

   private KongaDialog createDialog() {
      Window var1 = SwingUtilities.getWindowAncestor(this.model.getOpposingTree());
      KongaDialog var2 = new KongaDialog(var1, "Mapping Suggestions") {
         @Override
         protected void onDialogOpened() {
            MappingSuggestionsDialog.this.controller.startSearch();
            MappingSuggestionsDialog.this.ui.requestFocus();
         }
      };
      var2.standardLayout(this.ui, KongaDialog.CLOSE_ONLY);
      var2.manageLocation(this.getClass());
      return var2;
   }

   private void installCallback(KongaDialog var1) {
      MappingSuggestionsDialog.Callback var2 = new MappingSuggestionsDialog.Callback(this.externalCallback, var1);
      this.ui.setCallback(var2);
   }

   private static class Callback implements Receiver<Node> {
      private final Receiver<Node> external;
      private final KongaDialog dialog;

      public Callback(Receiver<Node> var1, KongaDialog var2) {
         this.external = var1;
         this.dialog = var2;
      }

      public void handle(Node var1) {
         this.dialog.dispose();
         this.external.handle(var1);
      }
   }
}
