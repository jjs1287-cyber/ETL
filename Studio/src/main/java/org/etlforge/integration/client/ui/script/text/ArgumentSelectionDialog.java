package org.etlforge.integration.client.ui.script.text;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.SwingUtilities;
import org.etlforge.integration.client.ui.script.prefs.TurnOffAutomaticArgumentSuggestionChoice;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.DialogSelectionSupport;
import org.etlforge.ui.selection.SelectionSource;

public final class ArgumentSelectionDialog {
   private final KongaDialog dialog;
   private final TurnOffAutomaticArgumentSuggestionChoice turnOffAutoBehaviorChoice;
   private final UiProvider selectionUi;

   public ArgumentSelectionDialog(UiProvider var1, SelectionSource var2, String var3, Component var4) {
      this.selectionUi = var1;
      this.turnOffAutoBehaviorChoice = new TurnOffAutomaticArgumentSuggestionChoice();
      this.dialog = this.createDialog(var3, var4);
      new DialogSelectionSupport(var2, this.dialog);
   }

   private KongaDialog createDialog(String var1, Component var2) {
      KongaDialog var3 = new KongaDialog(SwingUtilities.getWindowAncestor(var2), var1) {
         @Override
         protected void onDialogClose() {
            super.onDialogClose();
            ArgumentSelectionDialog.this.turnOffAutoBehaviorChoice.apply();
         }

         @Override
         protected void onDialogOpened() {
            super.onDialogOpened();
            this.giveFocusToList();
         }

         private void giveFocusToList() {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  ArgumentSelectionDialog.this.selectionUi.requestFocus();
               }
            });
         }
      };
      this.layoutDialog(var3);
      var3.setLocationRelativeTo(null);
      var3.setOkButtonAsDefault();
      var3.getCancelButton().setText("No Thanks");
      return var3;
   }

   private void layoutDialog(KongaDialog var1) {
      UiProvider var2 = this.layoutListAndPreferenceChoice();
      var1.standardLayout(var2, KongaDialog.OK_CANCEL);
   }

   private UiProvider layoutListAndPreferenceChoice() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      var1.center(this.selectionUi).south(this.turnOffAutoBehaviorChoice);
      return var1;
   }

   public void setPreferredWidth(int var1) {
      Dimension var2 = this.dialog.getSize();
      var2.width = ArgCheck.checkPositive(var1, "width");
      this.dialog.setSize(var2);
   }

   public void show() {
      this.dialog.setVisible(true);
   }

   public boolean wasOkPressed() {
      return this.dialog.wasOkPressed();
   }
}
