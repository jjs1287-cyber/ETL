package com.etlforge.integration.client.ui.sfdc;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.etlforge.integration.client.ui.sfdc.wizard.prefs.DefaultDateConversionParams;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class DateOptionsEditor {
   private Optional<DateConversionParams> dateParams;
   private InputPanel inputPanel;
   private final KongaLabel label = KongaLabel.withMnemonic("&Date Options");
   private final DateOptionsEditor.EditAction editAction = new DateOptionsEditor.EditAction();
   private boolean enabled = true;

   public DateOptionsEditor() {
      this(DefaultDateConversionParams.defaultParams());
   }

   public DateOptionsEditor(Optional<DateConversionParams> var1) {
      this.dateParams = var1;
   }

   public Optional<DateConversionParams> getParams() {
      return this.enabled ? this.dateParams : Optional.absent();
   }

   public Action getEditAction() {
      return this.editAction;
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
      this.label.setEnabled(var1);
      this.editAction.setEnabled(var1);
   }

   public void layoutInPage(TwoColumnLayout var1) {
      this.editAction.putValue("Name", "Edit");
      KongaButton var2 = new KongaButton(this.editAction);
      this.label.setLabelForButton(var2);
      var1.addRow(this.label, InvisiblePanel.of(var2));
   }

   public void registerAsChangeSource(InputPanel var1) {
      this.inputPanel = var1;
   }

   private class EditAction extends AbstractAction {
      public EditAction() {
         super("&Date Options...");
      }

      @Override
      public void actionPerformed(final ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditAction.this.openDialog(var1);
            }
         });
      }

      private void openDialog(ActionEvent var1) {
         Optional var2 = DateOptionsEditor.this.dateParams;
         DateTimeOptionsDialog var3 = new DateTimeOptionsDialog(DateOptionsEditor.this.dateParams);
         Optional var4 = var3.open(UiUtils.getComponentSource(var1));
         if (!Objects.equal(var2, var4)) {
            this.applyNewParams(var2, var4);
         }
      }

      private void applyNewParams(Optional<DateConversionParams> var1, Optional<DateConversionParams> var2) {
         if (DateOptionsEditor.this.inputPanel != null) {
            DateOptionsEditor.EditImpl var3 = DateOptionsEditor.this.new EditImpl(var1, var2);
            DateOptionsEditor.this.inputPanel.undoableEditHappened(new UndoableEditEvent(DateOptionsEditor.this, var3));
            DateOptionsEditor.this.inputPanel.inputChanged(new InputChangeEvent(DateOptionsEditor.this, "Date Options"));
         }

         DateOptionsEditor.this.dateParams = var2;
      }
   }

   private class EditImpl extends AbstractUndoableEdit {
      private final Optional<DateConversionParams> oldParams;
      private final Optional<DateConversionParams> newParams;

      public EditImpl(Optional<DateConversionParams> var2, Optional<DateConversionParams> var3) {
         this.oldParams = var2;
         this.newParams = var3;
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         DateOptionsEditor.this.dateParams = this.oldParams;
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         DateOptionsEditor.this.dateParams = this.newParams;
      }

      @Override
      public String getPresentationName() {
         return "Date Options";
      }
   }
}
