package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.client.sfdc.data.RetryPolicy;
import com.etlforge.integration.client.sfdc.data.RetryPolicyMapper;
import com.etlforge.integration.client.ui.sfdc.wizard.RetryPolicyUi;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.undo.UndoRedoInstallerFactory;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.event.UndoableEditListeners;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class DefaultRetryPolicyEditor implements RetryPolicyEditor {
   private RetryPolicy policy;
   private final UndoableEditListeners undoRedoListeners;

   public DefaultRetryPolicyEditor(SalesforceWizardEntity var1) {
      this.policy = this.getOriginalPolicy(var1);
      this.undoRedoListeners = new UndoableEditListeners();
   }

   private RetryPolicy getOriginalPolicy(SalesforceWizardEntity var1) {
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      Transformation var3 = var2.getRequestTransformation();
      return var3 == null ? RetryPolicy.NONE : RetryPolicyMapper.fromTransformation(var3);
   }

   @Override
   public RetryPolicy getPolicy() {
      return this.policy;
   }

   @Override
   public void addUndoableEditListener(UndoableEditListener var1) {
      this.undoRedoListeners.add(var1);
   }

   @Override
   public void removeUndoableEditListener(UndoableEditListener var1) {
      this.undoRedoListeners.remove(var1);
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      KongaLabel var2 = KongaLabel.withMnemonic("Retry &Policy");
      KongaButton var3 = new KongaButton(new DefaultRetryPolicyEditor.EditAction());
      var2.setLabelForButton(var3);
      var1.addRow(var2, InvisiblePanel.of(var3));
   }

   private class Edit extends AbstractUndoableEdit {
      private final RetryPolicy oldValue;
      private final RetryPolicy newValue;

      public Edit(RetryPolicy var2, RetryPolicy var3) {
         this.oldValue = var2;
         this.newValue = var3;
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         DefaultRetryPolicyEditor.this.policy = this.newValue;
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         DefaultRetryPolicyEditor.this.policy = this.oldValue;
      }

      @Override
      public String getPresentationName() {
         return "Retry Policy Change";
      }
   }

   private class EditAction extends AbstractAction {
      private KongaDialog dialog;
      private RetryPolicyUi ui;

      public EditAction() {
         super("Edit");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditAction.this.openDialog();
            }
         });
      }

      private void openDialog() {
         this.ui = new RetryPolicyUi();
         this.ui.setRetryPolicy(DefaultRetryPolicyEditor.this.policy);
         this.dialog = this.createDialog();
         this.bindOkButtonToValidState();
         this.dialog.setVisible(true);
         if (this.dialog.wasOkPressed() && this.ui.isInputValid()) {
            this.handleNewPolicy();
         }

         this.ui = null;
         this.dialog = null;
      }

      private KongaDialog createDialog() {
         JComponent var1 = WizardStyleDialog.wrapUi(this.ui);
         WizardStyleDialog var2 = new WizardStyleDialog(UiUtils.getActiveWindow(), "Retry Policy", var1);
         var2.setFocusedComponent(this.ui);
         var2.setResizable(false);
         var2.manageLocation(DefaultRetryPolicyEditor.class);
         var2.setOkButtonAsDefault();
         this.installUndoRedo(var2);
         return var2;
      }

      private void installUndoRedo(KongaDialog var1) {
         UndoRedoInstaller var2 = UndoRedoInstallerFactory.newInstaller();
         var2.installIntoComponentTree(var1.getContentPane());
      }

      private void bindOkButtonToValidState() {
         this.ui.addChangeListener(new InputChangeListener() {
            @Override
            public void inputChanged(InputChangeEvent var1) {
               EditAction.this.updateOkButton();
            }
         });
         this.updateOkButton();
      }

      private void updateOkButton() {
         boolean var1 = this.ui.isInputValid();
         this.dialog.getOKButton().setEnabled(var1);
      }

      private void handleNewPolicy() {
         RetryPolicy var1 = DefaultRetryPolicyEditor.this.policy;
         DefaultRetryPolicyEditor.this.policy = this.ui.getRetryPolicy();
         if (!var1.equals(DefaultRetryPolicyEditor.this.policy)) {
            this.sendEdit(var1);
         }
      }

      private void sendEdit(RetryPolicy var1) {
         DefaultRetryPolicyEditor.Edit var2 = DefaultRetryPolicyEditor.this.new Edit(var1, DefaultRetryPolicyEditor.this.policy);
         UndoableEditEvent var3 = new UndoableEditEvent(DefaultRetryPolicyEditor.this, var2);
         DefaultRetryPolicyEditor.this.undoRedoListeners.call().undoableEditHappened(var3);
      }
   }
}
