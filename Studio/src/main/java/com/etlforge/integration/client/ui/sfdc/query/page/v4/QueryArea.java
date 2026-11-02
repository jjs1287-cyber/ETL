package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.ui.sfdc.query.v4.QueryAreaInserter;
import com.etlforge.integration.client.ui.sfdc.query.v4.QueryAreaTransferHandler;
import com.etlforge.integration.client.ui.sfdc.soql.v4.FieldListDialog;
import com.etlforge.integration.client.ui.sfdc.soql.v4.QuerySyntaxValidator;
import com.etlforge.integration.client.ui.sfdc.soql.v4.QueryValidator;
import com.etlforge.integration.client.ui.sfdc.soql.v4.SObjectSelectionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class QueryArea implements UiProvider {
   private final QueryArea.InputFieldImpl field;
   private final QueryAreaInserter inserter;
   private final QueryArea.OpenFieldDialogAction fieldsAction;
   private FieldListDialog dialog;

   public QueryArea(InputPanel var1, String var2) {
      this.field = new QueryArea.InputFieldImpl(var2);
      var1.addInputField(this.field, this.field, this.field);
      this.inserter = new QueryAreaInserter(this.field.getInputComponent(), "...");
      this.fieldsAction = new QueryArea.OpenFieldDialogAction();
      this.installDropSupport();
   }

   private void installDropSupport() {
      QueryAreaTransferHandler var1 = new QueryAreaTransferHandler(this.inserter, this.field.getInputComponent().getTransferHandler());
      this.field.getInputComponent().setTransferHandler(var1);
   }

   public void setSObject(SObject var1) {
      this.fieldsAction.setSObject(var1);
   }

   public void closeFieldSelector() {
      if (this.dialog != null) {
         this.dialog.close();
         this.dialog = null;
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.add(this.field.getLabel()).glue().add(this.fieldsAction);
      Vertical.borderLayout().of(var2, this.field.getInputComponent()).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   public void validate() throws InputFieldException {
      this.field.validateField();
   }

   public void setValidator(QueryValidator var1) {
      this.field.setValidator(var1);
   }

   public void installAutoCompletion(CompletionContext var1) {
      AutoCompleterFactory var2 = AutoCompleterFactory.codeEditorFactory();
      var2.setGrabFocus(false);
      AutoCompleter var3 = var2.createAutoCompleter(var1);
      var3.install(this.field.getInputComponent());
   }

   public void setQueryString(String var1) {
      this.field.setValue(var1);
   }

   public String getQueryString() {
      return this.field.getValue().trim();
   }

   private static class InputFieldImpl extends TextInputField2 {
      private QueryValidator validator = new QuerySyntaxValidator();
      private String errorMessage;

      public InputFieldImpl(String var1) {
         super("Query String", new KongaTextArea(var1, 6, 50), KongaLabel.withMnemonic("SO&QL string"));
      }

      public void setValidator(QueryValidator var1) {
         Preconditions.checkNotNull(var1, "validator");
         this.validator = var1;
         this.decorateField();
      }

      @Override
      public boolean isInputValid(String var1) {
         this.errorMessage = this.validator.validate(var1);
         return this.errorMessage == null;
      }

      @Override
      public String getInvalidMessage(String var1) {
         return this.errorMessage;
      }
   }

   private class OpenFieldDialogAction extends AbstractAction {
      private SObject object;

      public OpenFieldDialogAction() {
         super("Fields...");
         this.setEnabled(false);
      }

      public void setSObject(SObject var1) {
         this.object = var1;
         this.setEnabled(var1 != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (QueryArea.this.dialog == null) {
            QueryArea.this.dialog = this.createDialog();
         }

         QueryArea.this.dialog.open(QueryArea.this.field.getInputComponent());
      }

      private FieldListDialog createDialog() {
         return new FieldListDialog(this.object, new SObjectSelectionListener() {
            @Override
            public void fieldsSelected(SObject var1, List<SObject.Field> var2) {
               QueryArea.this.inserter.insertFields(var1, var2);
            }

            @Override
            public void objectSelected(SObject var1) {
            }
         });
      }
   }
}
