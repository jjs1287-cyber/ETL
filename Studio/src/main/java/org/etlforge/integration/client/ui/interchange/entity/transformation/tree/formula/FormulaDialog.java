package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.help.ApplicationUiHelp;
import org.etlforge.application.ui.undo.UndoRedoControllers;
import org.etlforge.integration.client.ui.interchange.entity.transformation.prefs.CancelFormulaBuilderOnEscapePreference;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.mappings.NodeMapping;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.undo.UndoRedoController;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.widget.button.KongaButton;

final class FormulaDialog extends AbstractFormulaBuilder {
   private FormulaDialog.DialogImpl dialog;
   private final JButton clientHelpButton;
   private UndoRedoController undoRedoCtrl;
   private final JButton undoButton;
   private final JButton redoButton;

   public FormulaDialog(IntegrationProject var1, TreeMapper var2, FormulaBuilderUiFactory var3, String var4, String var5, NodeMapping var6) {
      super(var1, var2, var3, var4, var5, var6);
      this.dialog = new FormulaDialog.DialogImpl(var2, var5);
      this.clientHelpButton = this.createClientHelpButton();
      this.undoButton = new JButton();
      this.redoButton = new JButton();
      this.updateTitle();
      this.layoutDialog();
      this.dialog.setResizable(true);
      this.dialog.setOkButtonAsDefault();
      this.installHelp();
   }

   private JButton createClientHelpButton() {
      KongaButton var1 = new KongaButton("&Help", PackageResources.HELP_ICON);
      var1.setToolTipText("Display the help page for how to use the Formula Builder");
      return var1;
   }

   private void updateTitle() {
      String var1 = this.getSuggestedTitle();
      this.dialog.setTitle(var1);
   }

   private void layoutDialog() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(this.getUi());
      this.getUi().setButtonPanel(this.createDialogButtons());
      var1.withEmptyBorder(10).asContentPaneFor(this.dialog);
      this.dialog.pack();
      this.setInitialSizeAndLocation();
   }

   private JComponent createDialogButtons() {
      JButton var1 = this.dialog.createOKButton();
      JButton var2 = this.dialog.createCancelButton();
      BoxBuilder var3 = BoxBuilder.horizontal();
      KongaButton var4 = this.createTestButton();
      ImmutableList var5 = ButtonOrder.okAndCancelOrder(var1, var2);
      var3.add(this.clientHelpButton).strut(10).add(var4).glue().addAllWithSpace(10, var5);
      int var6 = SystemUtils.IS_OS_WINDOWS_VISTA ? 0 : 1;
      return var3.withEmptyBorder(10, 0, 10, var6).container();
   }

   private KongaButton createTestButton() {
      Action var1 = this.getUi().getTestAction();
      return new KongaButton(var1).setUseSmallActionIcon(true);
   }

   private void setInitialSizeAndLocation() {
      Window var1 = this.getTreeMapper().getOwner();
      byte var2 = 50;
      int var3 = var1.getWidth() - var2;
      int var4 = var1.getHeight() - var2;
      this.dialog.setSize(var3, var4);
      this.dialog.setLocationRelativeTo(var1);
   }

   @Override
   public void open() {
      this.setVisible(true);
   }

   @Override
   public void hide() {
      this.setVisible(false);
   }

   private void setVisible(boolean var1) {
      if (var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               FormulaDialog.this.getUi().requestFocus();
            }
         });
         this.dialog.setCancelOnEscape(CancelFormulaBuilderOnEscapePreference.get());
         this.undoRedoCtrl = this.installUndoRedo();
      } else {
         this.undoRedoCtrl.dispose();
      }

      this.dialog.setVisible(var1);
   }

   private UndoRedoController installUndoRedo() {
      UndoRedoController var1 = this.dialog.enableUndoRedoForTextComponents(UndoRedoControllers.textFactory());
      this.undoButton.setAction(var1.getUndoAction());
      this.undoButton.setText("");
      this.redoButton.setAction(var1.getRedoAction());
      this.redoButton.setText("");
      return var1;
   }

   @Override
   protected void mappingChanged() {
      this.updateTitle();
   }

   @Override
   protected void disposeAdditionalResources() {
      try {
         this.disposeHelp();
      } finally {
         this.dialog.dispose();
         this.dialog = null;
      }
   }

   private void installHelp() {
      ApplicationUiHelp.getHelp().enableHelpOnButton(this.clientHelpButton, "integration.transformation.formulabuilder");
   }

   private void disposeHelp() {
      ApplicationUiHelp.getHelp().disableHelpOnButton(this.clientHelpButton);
   }

   @Override
   public void showFormula() {
      this.restoreCaretPosition();
      this.getUi().showFormula();
      this.setVisible(true);
   }

   @Override
   public boolean isVisible() {
      return this.dialog.isVisible();
   }

   public void applyAndClose() {
      this.dialog.onOk();
   }

   private final class DialogImpl extends KongaDialog {
      public DialogImpl(TreeMapper var2, String var3) {
         super(var2.getOwner(), var3, ModalityType.APPLICATION_MODAL);
         this.setDefaultCloseOperation(1);
         this.setDisposeWhenClosed(false);
      }

      @Override
      public void onOk() {
         if (FormulaDialog.this.apply()) {
            FormulaDialog.this.rememberCaretPosition();
            super.onOk();
         }
      }

      @Override
      protected void onCancel() {
         FormulaDialog.this.handleCancel();
         super.onCancel();
      }
   }
}
