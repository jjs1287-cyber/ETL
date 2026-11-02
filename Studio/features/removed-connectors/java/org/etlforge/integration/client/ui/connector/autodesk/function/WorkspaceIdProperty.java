package org.etlforge.integration.client.ui.connector.autodesk.function;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.annotation.Nullable;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.etlforge.application.ui.widget.TextFieldInput;
import org.etlforge.application.ui.widget.TextFieldInputValidator;
import org.etlforge.integration.client.ui.connector.FunctionProperty;
import org.etlforge.integration.client.ui.connector.function.ConnectorCallPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.AbstractEntityPageSaveJob;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

final class WorkspaceIdProperty implements FunctionProperty {
   private final AutoDeskFunction entity;
   private String currentValue;
   private final KongaTextField valueField;
   private final WorkspaceIdProperty.EditAction editAction = new WorkspaceIdProperty.EditAction();
   private ConnectorCallPage page;

   public WorkspaceIdProperty(AutoDeskFunction var1) {
      this.entity = Preconditions.checkNotNull(var1);
      this.currentValue = var1.getWorkspaceId();
      this.valueField = new KongaTextField(this.currentValue, 4);
      this.valueField.setEditable(false);
   }

   @Override
   public void associateWithPage(ConnectorCallPage var1) {
      this.page = Preconditions.checkNotNull(var1);
      this.editAction.setEnabled(true);
   }

   @Override
   public void layoutIn(TwoColumnLayout var1) {
      KongaLabel var2 = KongaLabel.withMnemonic("Workspace &ID");
      KongaButton var3 = new KongaButton(this.editAction);
      var2.setLabelForButton(var3);
      JPanel var4 = Horizontal.box().addAllWithSpace(5, this.valueField, var3).invisible();
      var1.addRow(var2, var4);
   }

   private void updateCurrentValue(String var1) {
      this.currentValue = var1;
      this.valueField.setText(var1);
   }

   private class ApplyNewIdJob extends AbstractEntityPageSaveJob<String> {
      public ApplyNewIdJob(String var2, String var3) {
         super("edit-workspace-id", var2, var3);
         this.setDescription("Workspace ID Change");
         this.setUndoRedoStateReceiver(new Receiver<String>() {
            public void handle(String var1) {
               WorkspaceIdProperty.this.updateCurrentValue(var1);
            }
         });
      }

      protected void apply(String var1) {
         WorkspaceIdProperty.this.entity.setWorkspaceId(var1);
      }
   }

   private class EditAction extends AbstractAction {
      public EditAction() {
         super("Edit...");
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditAction.this.edit(var1);
            }
         });
      }

      private void edit(ActionEvent var1) {
         String var2 = this.askForValue();
         if (var2 != null && !var2.equals(WorkspaceIdProperty.this.currentValue)) {
            WorkspaceIdProperty.ApplyNewIdJob var3 = WorkspaceIdProperty.this.new ApplyNewIdJob(WorkspaceIdProperty.this.currentValue, var2);
            WorkspaceIdProperty.this.page.addSaveJob(var3);
            WorkspaceIdProperty.this.updateCurrentValue(var2);
         }
      }

      @Nullable
      private String askForValue() {
         TextFieldInput var1 = this.createInputField();
         return var1.showInDialog(null);
      }

      private TextFieldInput createInputField() {
         TextFieldInput var1 = new TextFieldInput("Workspace ID", "Workspace ID");
         var1.setInitialValue(WorkspaceIdProperty.this.currentValue);
         var1.setAutoTrimEnabled(true);
         var1.setSelectAllWhenFocused(true);
         var1.setTextWhenEmpty("Enter the workspace ID");
         var1.setValidator(new TextFieldInputValidator() {
            @Override
            public boolean validate(String var1) {
               if (var1.isEmpty()) {
                  return false;
               }

               try {
                  Integer.parseInt(var1);
                  return true;
               } catch (NumberFormatException var3) {
                  return false;
               }
            }

            @Override
            public String getErrorMessage(String var1) {
               return var1.isEmpty() ? "Must provide a workspace ID" : "Invalid workspace ID";
            }
         });
         return var1;
      }
   }
}
