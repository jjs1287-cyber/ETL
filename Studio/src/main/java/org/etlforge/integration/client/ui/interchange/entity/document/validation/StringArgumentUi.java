package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.StringArgument;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.DropDownAutoCompleter;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.KongaTextField;

final class StringArgumentUi extends ArgumentUi {
   private final TextInputField field = new TextInputField("Argument", new KongaTextField(20));
   private final StringArgumentUi.ModelBinding modelBinding;
   private InputPanel inputPanel;
   private AutoCompleter autoCompleter;

   public StringArgumentUi(FieldValidationModel var1) {
      this.modelBinding = new StringArgumentUi.ModelBinding(var1);
      this.modelBinding.syncUi();
   }

   public void setAutoCompletionContext(CompletionContext var1) {
      if (this.autoCompleter != null) {
         this.autoCompleter.uninstall();
      }

      if (var1 != null) {
         this.autoCompleter = new DropDownAutoCompleter(var1);
         this.autoCompleter.install(this.field.getInputComponent());
      }
   }

   @Override
   public void setEnabled(boolean var1) {
      this.field.setEnabled(var1);
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      this.inputPanel = var1;
      var1.addInputField(this.field);
   }

   @Override
   public void dispose() {
      this.modelBinding.dispose();
      if (this.inputPanel != null) {
         this.inputPanel.removeInputField(this.field);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.field.getInputComponent());
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   private class ModelBinding extends AbstractBinding<OperatorArgument> {
      private final DocumentListener uiListener = new DocumentContentChangeListener() {
         @Override
         public void contentChanged(DocumentEvent var1) {
            if (ModelBinding.this.isUiToModelEnabled()) {
               ModelBinding.this.syncModel();
            }
         }
      };

      public ModelBinding(FieldValidationModel var2) {
         super(var2, "argument");
         StringArgumentUi.this.field.addDocumentListener(this.uiListener);
      }

      @Override
      protected void disposeUiListener() {
         StringArgumentUi.this.field.removeDocumentListener(this.uiListener);
      }

      protected StringArgument getValueFromUi() {
         return StringArgument.of(StringArgumentUi.this.field.getValue());
      }

      protected void updateUiWithValue(OperatorArgument var1) {
         if (var1 instanceof StringArgument) {
            StringArgumentUi.this.field.setValue(((StringArgument)var1).getValue());
         } else {
            StringArgumentUi.this.field.setValue("");
         }
      }
   }
}
