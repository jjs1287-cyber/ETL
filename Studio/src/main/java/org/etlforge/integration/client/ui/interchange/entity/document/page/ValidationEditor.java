package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FieldTableColumns;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FieldTableModel;
import org.etlforge.integration.client.ui.interchange.entity.document.validation.FieldValidationModel;
import org.etlforge.integration.client.ui.interchange.entity.document.validation.FieldValidationUi;
import org.etlforge.integration.client.ui.interchange.entity.document.validation.OperatorBox;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.ui.InvalidInputException;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.InputPanelDialogMediator;
import org.etlforge.util.string.StringLimiter;

public final class ValidationEditor {
   private final IntegrationProject project;
   private final FieldTableModel tableModel;
   private Optional<EntityExplorerSupport> projectExplorer = Optional.absent();
   private Optional<UndoableEditListener> undoRedoListener = Optional.absent();

   public ValidationEditor(IntegrationProject var1, FieldTableModel var2) {
      this.project = var1;
      this.tableModel = var2;
   }

   public IntegrationEntityLookup getEntityLookup() {
      return this.project.getItemLookup();
   }

   public void setProjectExplorer(EntityExplorerSupport var1) {
      this.projectExplorer = Optional.of(var1);
   }

   public void setUndoRedoListener(UndoableEditListener var1) {
      this.undoRedoListener = Optional.of(var1);
   }

   public FieldValidation edit(int var1) {
      Field var2 = (Field)this.tableModel.getRowObjectAt(var1);
      Optional var3 = var2.getValidation();
      FieldValidationModel var4 = new FieldValidationModel(this.project, var2);
      OperatorBox var5 = new OperatorBox(var4, this.projectExplorer);
      FieldValidationUi var6 = new FieldValidationUi(var4, var5);

      try {
         WizardStyleDialog var7 = this.createDialog(var4, var6, var2.getName());
         var7.setVisible(true);
         if (var7.wasOkPressed()) {
            this.handleInput(var1, var2, var3, var4);
            return var4.getFieldValidation().orNull();
         } else {
            return null;
         }
      } finally {
         var6.dispose();
      }
   }

   private WizardStyleDialog createDialog(final FieldValidationModel var1, FieldValidationUi var2, String var3) {
      String var4 = "Field Validation: " + StringLimiter.limit(var3, 30);
      WizardStyleDialog var5 = new WizardStyleDialog(ApplicationUi.getAppWinFrame(), var4, var2, true) {
         @Override
         protected void onOk() {
            try {
               var1.validate();
               super.onOk();
            } catch (InvalidInputException var2x) {
               NonBlockingAlert.error(this, var2x.getMessage(), "Invalid Input");
            }
         }
      };
      var5.setOkButtonAsDefault();
      var5.manageLocation(FieldValidationUi.class);
      this.installUndoRedo(var2, var5);
      return var5;
   }

   private void installUndoRedo(FieldValidationUi var1, WizardStyleDialog var2) {
      InputPanel var3 = new InputPanel();
      var1.registerAsChangeSource(var3);
      var3.setSendInputChangeEvents(true);
      var3.setSendUndoEvents(true);
      InputPanelDialogMediator.bind(var3, var2);
   }

   private void handleInput(int var1, Field var2, Optional<FieldValidation> var3, FieldValidationModel var4) {
      String var5 = null;
      String var6 = null;
      if (var2.isDate()) {
         var5 = var2.getFormat();
         var6 = var4.getDateFormat();
         if (!var6.equals(var5)) {
            this.tableModel.setUndoSupported(false);
            this.tableModel.setValueAt(var6, var1, FieldTableColumns.FORMAT);
            this.tableModel.setUndoSupported(true);
         }
      }

      Optional var7 = var4.getFieldValidation();
      if (!Objects.equal(var5, var6) || !var3.equals(var7)) {
         this.tableModel.setUndoSupported(false);
         this.tableModel.setValueAt(var7.orNull(), var1, FieldTableColumns.VALIDATION);
         this.tableModel.setUndoSupported(true);
         if (this.undoRedoListener.isPresent()) {
            UndoableEditEvent var8 = new UndoableEditEvent(this, new ValidationEditor.Edit(this.tableModel, var1, var3, var5, var7, var6));
            this.undoRedoListener.get().undoableEditHappened(var8);
         }
      }
   }

   private static class Edit extends AbstractUndoableEdit {
      private final FieldTableModel tableModel;
      private final int row;
      private final Optional<FieldValidation> oldValidation;
      private final String oldDateFormat;
      private final Optional<FieldValidation> newValidation;
      private final String newDateFormat;

      public Edit(FieldTableModel var1, int var2, Optional<FieldValidation> var3, String var4, Optional<FieldValidation> var5, String var6) {
         this.tableModel = var1;
         this.row = var2;
         this.oldValidation = var3;
         this.oldDateFormat = var4;
         this.newValidation = var5;
         this.newDateFormat = var6;
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.apply(this.oldValidation, this.oldDateFormat);
      }

      private void apply(Optional<FieldValidation> var1, String var2) {
         this.tableModel.setUndoSupported(false);
         this.tableModel.setValueAt(var1.orNull(), this.row, FieldTableColumns.VALIDATION);
         if (var2 != null) {
            this.tableModel.setValueAt(var2, this.row, FieldTableColumns.FORMAT);
         }

         this.tableModel.setUndoSupported(true);
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.apply(this.newValidation, this.newDateFormat);
      }

      @Override
      public String getPresentationName() {
         return "Validation Rule Change";
      }
   }
}
