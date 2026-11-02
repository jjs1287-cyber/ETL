package org.etlforge.integration.client.ui.interchange.location.panel;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.location.LineBasedSourceData;
import org.etlforge.integration.data.location.file.IgnoredLines;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.lang.IntParser;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputFieldValidator;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.text.IntegerDocument;
import org.etlforge.ui.widget.text.KongaTextField;

public final class IgnoreLinesPanel extends InputPanel {
   private final IgnoreLinesPanel.Field firstField = this.createField("File.IgnoreFirst", new IgnoreLinesPanel.IgnoreFirstValidator());
   private final IgnoreLinesPanel.Field lastField = this.createField("File.IgnoreLast", new IgnoreLinesPanel.IgnoreLastValidator());

   public IgnoreLinesPanel() {
      this.doLayout();
   }

   private IgnoreLinesPanel.Field createField(String var1, InputFieldValidator var2) {
      String var3 = Strings.get(var1 + ".Name");
      String var4 = Strings.get(var1 + ".Label");
      IgnoreLinesPanel.Field var5 = new IgnoreLinesPanel.Field(var3, var4, var2);
      this.addInputField(var5, var5, var5);
      return var5;
   }

   private void doLayout() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.addAllWithSpace(20, getFieldForLayout(this.firstField), getFieldForLayout(this.lastField));
      var1.withBorder(StyleSheet.getDefaultTitleBorder(Strings.get("File.IgnoreLines")));
      this.wrap(var1);
   }

   private static JComponent getFieldForLayout(InputField var0) {
      return InputFieldFactory.getCapsule(var0, 0);
   }

   public void apply(LineBasedSourceData var1) {
      IgnoredLines var2 = new IgnoredLines(this.firstField.getIntValue(), this.lastField.getIntValue());
      var1.setLinesToIgnore(var2);
   }

   public void reset(LineBasedSourceData var1) {
      IgnoredLines var2 = var1.getLinesToIgnore();
      this.firstField.setValue(var2.getFirst());
      this.lastField.setValue(var2.getLast());
   }

   private static class Field extends TextInputField2 {
      private final InputFieldValidator validator;

      public Field(String var1, String var2, InputFieldValidator var3) {
         super(var1, createTextComponent(), new JLabel(var2));
         this.validator = var3;
         this.setRequired(false);
         this.setAutoTrim(true);
      }

      private static JTextComponent createTextComponent() {
         KongaTextField var0 = new KongaTextField(4);
         var0.setDocument(new IntegerDocument());
         var0.setSelectAllWhenFocused(true);
         return var0;
      }

      @Override
      public boolean isFieldValid(InputField var1) {
         return this.validator.isFieldValid(var1);
      }

      @Override
      public void validateField(InputField var1) throws InputFieldException {
         this.validator.validateField(var1);
      }

      @Override
      public String getInvalidMessage(String var1) {
         try {
            this.validateField(this);
            return null;
         } catch (InputFieldException var3) {
            return var3.getMessage();
         }
      }

      @Override
      public boolean isInputValid(String var1) {
         throw new UnsupportedOperationException();
      }

      public int getIntValue() {
         String var1 = this.getValue();
         return IntParser.parse(var1, 0);
      }
   }

   private static class IgnoreFirstValidator extends IgnoreLinesPanel.ValidatorBase {
      private IgnoreFirstValidator() {
      }

      @Override
      protected ValidationIssue getValidationIssue(int var1) {
         return new IgnoredLines(var1, 0).validateFirst();
      }
   }

   private static class IgnoreLastValidator extends IgnoreLinesPanel.ValidatorBase {
      private IgnoreLastValidator() {
      }

      @Override
      protected ValidationIssue getValidationIssue(int var1) {
         return new IgnoredLines(0, var1).validateLast();
      }
   }

   private abstract static class ValidatorBase implements InputFieldValidator {
      private ValidatorBase() {
      }

      @Override
      public final boolean isFieldRequired(InputField var1) {
         return true;
      }

      @Override
      public final boolean isFieldValid(InputField var1) {
         return this.getValidationIssue(this.getValue(var1)) == null;
      }

      @Override
      public final void validateField(InputField var1) throws InputFieldException {
         ValidationIssue var2 = this.getValidationIssue(this.getValue(var1));
         if (var2 != null) {
            throw new InputFieldException(var1, var2.getMessage());
         }
      }

      private int getValue(InputField var1) {
         return ((IgnoreLinesPanel.Field)var1).getIntValue();
      }

      protected abstract ValidationIssue getValidationIssue(int var1);
   }
}
