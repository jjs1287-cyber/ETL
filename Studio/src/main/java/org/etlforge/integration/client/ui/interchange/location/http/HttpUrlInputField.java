package org.etlforge.integration.client.ui.interchange.location.http;

import javax.swing.JTextField;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.ui.input.DefaultNonEmptyTextInputField;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.text.KongaTextField;

public final class HttpUrlInputField extends DefaultNonEmptyTextInputField {
   private TokenListCompletionSupport autoCompleteSupport;

   public HttpUrlInputField(String var1, String var2) {
      super(var1, createInputComponent(), var2);
   }

   @Override
   public void dispose() {
      super.dispose();
      this.uninstallAutoComplete();
   }

   public void setHistory(StringValueHistory var1) {
      this.uninstallAutoComplete();
      if (var1 != null) {
         this.autoCompleteSupport = new TextFieldCompletionSupport(this, var1);
      }
   }

   private void uninstallAutoComplete() {
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
         this.autoCompleteSupport = null;
      }
   }

   private static JTextField createInputComponent() {
      return new KongaTextField(45);
   }
}
