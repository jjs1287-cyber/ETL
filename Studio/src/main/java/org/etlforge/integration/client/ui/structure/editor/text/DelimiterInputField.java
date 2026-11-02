package org.etlforge.integration.client.ui.structure.editor.text;

import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.text.KongaTextField;

public final class DelimiterInputField extends TextInputField2 {
   public DelimiterInputField(TextDocument var1) {
      this(var1.getDelimiter());
   }

   public DelimiterInputField(FieldDelimiter var1) {
      super(PackageResources.DelimiterField.NAME, createTextField(), new JLabel(PackageResources.DelimiterField.NAME));
      String var2;
      if (var1 == null) {
         var2 = TextDocument.DEFAULT_DELIMITER.toString();
      } else {
         var2 = var1.toEscapedString();
      }

      this.setValue(var2);
   }

   @Override
   public String getValue() {
      String var1 = super.getValue();
      if (!var1.equals(" ")) {
         var1 = var1.trim();
      }

      return var1;
   }

   public FieldDelimiter getDelimiter() {
      return FieldDelimiter.fromString(this.getValue());
   }

   @Override
   public boolean isInputValid(String var1) {
      return this.getDelimiter().isValid();
   }

   @Override
   public String getInvalidMessage(String var1) {
      return var1.length() == 0 ? PackageResources.DelimiterField.MISSING_DELIMITER : PackageResources.DelimiterField.INVALID_DELIMITER;
   }

   private static JTextComponent createTextField() {
      return new KongaTextField(2);
   }
}
