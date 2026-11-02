package org.etlforge.integration.client.ui.project.copy;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.project.copy.DeepCopyResult;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;

final class ResultUi {
   private final DeepCopyResult result;
   private WaitService waitService = WaitService.NONE;

   public ResultUi(DeepCopyResult var1) {
      this.result = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void show() {
      SummaryTable var1 = this.createTable();
      String var2 = this.createTitle();
      KongaDialog var3 = this.createDialog(var1, var2);
      var3.setVisible(true);
      var3.removeFromLocationManager();
      if (var3.wasCancelled()) {
         this.undo();
      }
   }

   private SummaryTable createTable() {
      SummaryTable var1 = new SummaryTable(this.result);
      var1.setCaption("The following items were copied:");
      return var1;
   }

   private String createTitle() {
      int var1 = this.result.getNumberOfCopiedItems();
      return var1 == 1 ? "One Item Copied" : var1 + " Items Copied";
   }

   private KongaDialog createDialog(UiProvider var1, String var2) {
      WizardStyleDialog var3 = new WizardStyleDialog(UiUtils.getActiveWindow(), var2, var1, null, true);
      var3.getDefaultLayout().south(this.createDialogButtons(var3));
      var3.setFocusedComponent(var1);
      var3.manageLocation(ResultUi.class);
      var3.setOkButtonAsDefault();
      return var3;
   }

   private UiProvider createDialogButtons(KongaDialog var1) {
      JButton var2 = var1.createOKButton();
      return Horizontal.box()
         .nonOpaque()
         .addAllWithSpace(10, new ResultUi.UndoAction(var1), new ResultUi.ReplaceAction(var1))
         .glue()
         .add(var2)
         .withEmptyBorder(10);
   }

   private void undo() {
      DeepCopyUndoer var1 = new DeepCopyUndoer(this.result);
      var1.setWaitService(this.waitService);
      var1.undo();
   }

   private final class ReplaceAction extends AbstractAction {
      private final KongaDialog dialog;

      public ReplaceAction(KongaDialog var2) {
         super("&Replace Originals");
         this.dialog = var2;
         this.putValue("ShortDescription", "Replace the original items with their copies");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.dialog.setVisible(false);
         DeepCopyReplacer var2 = new DeepCopyReplacer(ResultUi.this.result);
         var2.setWaitService(ResultUi.this.waitService);
         var2.replaceOriginals();
      }
   }

   private final class UndoAction extends AbstractAction {
      private final KongaDialog dialog;

      public UndoAction(KongaDialog var2) {
         super("That Is &Not What I Wanted");
         this.dialog = var2;
         this.putValue("ShortDescription", "Removes the copies from the project");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.dialog.setVisible(false);
         ResultUi.this.undo();
      }
   }
}
