package org.etlforge.application.ui.groovy;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.widget.AutoCompleterFactory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.text.contentassist.AutoCompleter;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.widget.text.KongaTextField;

final class IndividualPackageField implements UiProvider {
   private final KongaTextField packageField;
   private final ScriptArea scriptArea;

   public IndividualPackageField(ScriptArea var1) {
      this.scriptArea = var1;
      this.packageField = this.createPackageField();
      this.installAutoCompletion();
   }

   private KongaTextField createPackageField() {
      KongaTextField var1 = new KongaTextField();
      var1.addActionListener(new IndividualPackageField.InsertPackageFromFieldAction());
      var1.setLabel("Import &package (ENTER to insert):");
      var1.setSelectAllWhenFocused(true);
      return var1;
   }

   private void installAutoCompletion() {
      CompletionContext var1 = PackageNameCompletionContextBuilder.buildForField();
      AutoCompleterFactory var2 = AutoCompleterFactory.dropDownFactory();
      var2.setGrabFocus(false);
      AutoCompleter var3 = var2.createAutoCompleter(var1);
      var3.install(this.packageField);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.packageField.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.packageField.requestFocusInWindow();
   }

   public void setEnabled(boolean var1) {
      this.packageField.setEnabled(var1);
   }

   private final class InsertPackageFromFieldAction extends AbstractAction {
      private InsertPackageFromFieldAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         String var2 = IndividualPackageField.this.packageField.getText().trim();
         if (!var2.isEmpty()) {
            IndividualPackageField.this.scriptArea.insertImport(var2);
         }
      }
   }
}
