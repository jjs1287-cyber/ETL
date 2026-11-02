package org.etlforge.integration.client.ui.interchange.location.local;

import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.data.location.file.PathValidator;
import org.etlforge.ui.Size;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class FolderPathField extends TextInputField2 {
   private final PathValidator validator = new PathValidator();
   private TokenListCompletionSupport autoCompleteSupport;

   public FolderPathField(String var1, String var2, Size.Columns var3) {
      super(var1, new KongaTextField(var3), KongaLabel.withMnemonic(var2));
      this.setAutoTrim(true);
   }

   public void setAllowDriveLetter(boolean var1) {
      this.validator.setAllowDriveLetter(var1);
   }

   public void setAllowFileshares(boolean var1) {
      this.validator.setAllowFileshares(var1);
   }

   @Override
   public boolean isInputValid(String var1) {
      this.validator.setRequired(this.isRequired());
      return this.validator.isValid(var1);
   }

   @Override
   public String getInvalidMessage(String var1) {
      return this.validator.getLastError();
   }

   public void installAutoCompletion(StringValueHistory var1) {
      this.autoCompleteSupport = new TextFieldCompletionSupport(this, var1);
   }

   @Override
   public void dispose() {
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }

      super.dispose();
   }

   public void addToParent(InputPanel var1) {
      var1.addInputField(this, this, this);
   }
}
