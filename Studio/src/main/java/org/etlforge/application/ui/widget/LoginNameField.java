package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.label.KongaLabel;

public final class LoginNameField implements UiProvider {
   private final TextInputField2 field;
   private final boolean required;
   private TokenListCompletionSupport autoCompleteSupport;

   public LoginNameField(boolean var1) {
      this.field = createField(var1);
      this.required = var1;
   }

   public LoginNameField(boolean var1, String var2, String var3) {
      this.field = createField(var1, var2, var3);
      this.required = var1;
   }

   private static TextInputField2 createField(boolean var0) {
      return createField(var0, "Login", "&Login");
   }

   private static TextInputField2 createField(boolean var0, String var1, String var2) {
      String var3 = ApplicationResources.STRINGS.getString(var1);
      KongaLabel var4 = KongaLabel.withMnemonic(var2);
      TextInputField2 var5 = null;
      if (var0) {
         var5 = InputFieldFactory.getNonEmptyTextInputField(var3, var4, 20, false);
      } else {
         var5 = InputFieldFactory.getTextInputField2(var3, var4, 20, false);
      }

      return var5;
   }

   public void registerAsChangeSource(InputPanel var1) {
      if (this.required) {
         var1.addInputField(this.field, this.field, this.field);
      } else {
         var1.addInputField(this.field);
      }
   }

   public String getLoginName() {
      return this.field.getValue().trim();
   }

   public void setLoginName(String var1) {
      this.field.setValue(var1 != null ? var1.trim() : "");
   }

   public void setEnabled(boolean var1) {
      this.field.setEnabled(var1);
   }

   public void installAutoCompletion(StringValueHistory var1) {
      Preconditions.checkNotNull(var1, "history");
      this.uninstallAutoCompleteSupport();
      this.autoCompleteSupport = new TextFieldCompletionSupport(this.field, var1);
   }

   private void uninstallAutoCompleteSupport() {
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
         this.autoCompleteSupport = null;
      }
   }

   public void dispose() {
      this.field.dispose();
      this.uninstallAutoCompleteSupport();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(InputFieldFactory.getCapsule(this.field));
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   public TextInputField2 asInputField() {
      return this.field;
   }
}
