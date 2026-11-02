package com.etlforge.integration.client.ui.sfdc;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.widget.TextFieldCompletionSupport;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.salesforce.ServerHost;
import org.etlforge.ui.Size;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.text.contentassist.StringValueHistory;
import org.etlforge.ui.text.contentassist.TokenListCompletionSupport;
import org.etlforge.ui.widget.text.KongaTextField;

public final class ServerHostField {
   private final TextInputField field;
   private String lastNonSandboxValue = "";
   private TokenListCompletionSupport autoCompleteSupport;

   public ServerHostField(String var1, Size.Columns var2) {
      this.field = createField(var1, var2);
   }

   private static TextInputField createField(String var0, Size.Columns var1) {
      TextInputField var2 = new TextInputField("Server Host", new KongaTextField(var1), var0);
      var2.setAutoTrim(true);
      var2.setSelectAllWhenFocused(true);
      var2.setTextWhenEmpty("Leave empty for default");
      return var2;
   }

   public void setValue(ServerHost var1) {
      this.field.setValue(var1.getUrl());
   }

   public void dispose() {
      if (this.autoCompleteSupport != null) {
         this.autoCompleteSupport.dispose();
      }

      this.field.dispose();
   }

   public ServerHost getValue() {
      String var1 = this.field.getValue();
      return ServerHost.fromUrl(var1);
   }

   public void setParent(InputPanel var1) {
      var1.addInputField(this.field);
   }

   public JLabel getLabel() {
      return this.field.getLabel();
   }

   public JComponent getInputComponent() {
      return this.field.getInputComponent();
   }

   public void setUseSandbox(boolean var1) {
      if (var1) {
         this.lastNonSandboxValue = this.field.getValue();
         this.field.setValue(ServerHost.defaultHost().getSandboxUrl());
      } else {
         this.field.setValue(this.lastNonSandboxValue);
      }
   }

   public void addDocumentListener(DocumentListener var1) {
      this.field.addDocumentListener(var1);
   }

   public void removeDocumentListener(DocumentListener var1) {
      this.field.removeDocumentListener(var1);
   }

   public void requestFocus() {
      this.field.requestFocus();
      JTextComponent var1 = this.field.getInputComponent();
      int var2 = var1.getDocument().getLength();
      var1.setCaretPosition(var2);
      var1.moveCaretPosition(var2);
   }

   public void installUsernameAutoCompletion(StringValueHistory var1) {
      this.autoCompleteSupport = new TextFieldCompletionSupport(this.field, var1);
   }

   public BoundProperty asBoundProperty() {
      return BoundPropertyFactory.textProperty("server_host", this.field);
   }
}
