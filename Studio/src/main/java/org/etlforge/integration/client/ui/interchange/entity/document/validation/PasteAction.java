package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import com.google.common.base.Optional;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.system.Disposable;
import org.etlforge.system.Disposables;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.util.UiUtils;

final class PasteAction extends AbstractAction implements Disposable {
   private final FieldValidationModel model;
   private Optional<InputPanel> inputPanel = Optional.absent();
   private Optional<Disposable> clipboardListener = Optional.absent();

   public PasteAction(FieldValidationModel var1) {
      super("&Paste from clipboard");
      this.model = var1;
      this.putValue("ShortDescription", "Pastes a copied FieldValidation definition from the clipboard");
   }

   public void listenToClipboard() {
      Disposables.dispose(this.clipboardListener);
      Disposable var1 = ClipboardSerializer.registerPasteAvailableCallback(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            ActionUtils.setEnabledSafely(PasteAction.this, var1);
         }
      });
      this.clipboardListener = Optional.of(var1);
   }

   @Override
   public void dispose() {
      Disposables.dispose(this.clipboardListener);
   }

   public void registerAsChangeSource(InputPanel var1) {
      this.inputPanel = Optional.of(var1);
   }

   @Override
   public void actionPerformed(final ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PasteAction.this.paste(var1);
         }
      });
   }

   private void paste(ActionEvent var1) {
      Optional var2 = ClipboardSerializer.fromClipboard();
      if (var2.isPresent()) {
         this.startCompoundEdit();
         Optional var3 = this.model.getFieldValidation();
         FieldDataType var4 = this.model.getDataType();
         FieldValidation var5 = ((FieldValidation)var2.get()).transformToType(var4);
         this.model.setFieldValidation(var5);
         this.endCompoundEdit(var3, var5);
      } else {
         Optional var6 = UiUtils.getComponentSource(var1);
         Alert.info((Component)var6.orNull(), "The clipboard does not contain a field validation definition.", "Not Available");
      }
   }

   private void startCompoundEdit() {
      if (this.inputPanel.isPresent()) {
         this.inputPanel.get().setSendUndoEvents(false);
      }
   }

   private void endCompoundEdit(Optional<FieldValidation> var1, FieldValidation var2) {
      if (this.inputPanel.isPresent()) {
         this.inputPanel.get().setSendUndoEvents(true);
         this.inputPanel.get().undoableEditHappened(new UndoableEditEvent(this, new PasteAction.Edit(var1, var2)));
      }
   }

   private class Edit extends AbstractUndoableEdit {
      private final Optional<FieldValidation> oldValue;
      private final Optional<FieldValidation> newValue;

      public Edit(Optional<FieldValidation> var2, FieldValidation var3) {
         this.oldValue = var2;
         this.newValue = Optional.of(var3);
      }

      @Override
      public void undo() throws CannotUndoException {
         super.undo();
         this.setValue(this.oldValue);
      }

      private void setValue(Optional<FieldValidation> var1) {
         PasteAction.this.inputPanel.get().setSendInputChangeEvents(false);
         PasteAction.this.model.setFieldValidation(var1);
         PasteAction.this.inputPanel.get().setSendInputChangeEvents(true);
      }

      @Override
      public void redo() throws CannotRedoException {
         super.redo();
         this.setValue(this.newValue);
      }

      @Override
      public String getPresentationName() {
         return "Validation";
      }
   }
}
