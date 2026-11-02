package org.etlforge.integration.client.ui.interchange.location.panel;

import com.google.common.base.Preconditions;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.integration.data.location.file.FileFilter;
import org.etlforge.integration.data.location.file.TargetFileName;
import org.etlforge.integration.data.validation.ValidationIssue;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputFieldValidator;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.FileDropStrategy;
import org.etlforge.ui.widget.text.FileDropSupport;

final class FileFilterField {
   private final TextInputField2 field;

   public FileFilterField(InputPanel var1, String var2, SourceTarget var3, int var4) {
      Preconditions.checkNotNull(var3, "st");
      this.field = this.createField(var2, var3, var4);
      this.field.setAutoTrim(true);
      FileNameFieldNavigationActions.install(this.field.getInputComponent());
      var1.addInputField(this.field, this.getValidator(var3), this.field);
   }

   private TextInputField2 createField(String var1, SourceTarget var2, int var3) {
      if (var2 == SourceTarget.Source) {
         String var7 = Strings.get("File.Filter.Name");
         TextInputField2 var5 = InputFieldFactory.getNonEmptyTextInputField(var7, KongaLabel.withMnemonic(var1), var3);
         JTextComponent var6 = var5.getInputComponent();
         FileDropSupport.install(var6, FileDropStrategy.SET_NAME);
         return var5;
      } else {
         String var4 = Strings.get("File.Name.Name");
         return new FileFilterField.TargetField(var4, var1, var3);
      }
   }

   public String getValue() {
      return this.field.getValue();
   }

   public void setValue(String var1) {
      this.field.setValue(var1.trim());
   }

   public void reset(FileLocation var1) {
      this.field.setValue(var1.getFileFilter().getFilter());
   }

   public InputField asInputFieldForLayout() {
      return this.field;
   }

   public void setTextWhenEmpty(String var1) {
      this.field.setTextWhenEmpty(var1);
   }

   public UiProvider asUiProvider() {
      JComponent var1 = InputFieldFactory.getCapsule(this.field);
      return UiProviderAdapter.of(var1);
   }

   private InputFieldValidator getValidator(SourceTarget var1) {
      return var1 == SourceTarget.Source ? new FileFilterField.SourceFileFilterValidator() : this.field;
   }

   private static class SourceFileFilterValidator implements InputFieldValidator {
      private SourceFileFilterValidator() {
      }

      @Override
      public final boolean isFieldRequired(InputField var1) {
         return true;
      }

      @Override
      public final boolean isFieldValid(InputField var1) {
         return this.getValidationIssues(var1).isEmpty();
      }

      @Override
      public final void validateField(InputField var1) throws InputFieldException {
         Set var2 = this.getValidationIssues(var1);
         if (!var2.isEmpty()) {
            StringBuilder var3 = new StringBuilder("<html>");

            for (Object __etlItem98 : var2) {
               ValidationIssue var5 = (ValidationIssue) __etlItem98;
               var3.append(var5.getMessage()).append("<br>");
            }

            var3.append("</html>");
            throw new InputFieldException(var1, var3.toString());
         }
      }

      private Set<ValidationIssue> getValidationIssues(InputField var1) {
         FileFilter var2 = new FileFilter(((TextInputField)var1).getValue().trim());
         return var2.validate();
      }
   }

   private static class TargetField extends TargetFileNameInputField {
      public TargetField(String var1, String var2, int var3) {
         super(var1, var2, var3);
      }

      @Override
      protected Set<ValidationIssue> getValidationIssues(String var1) {
         return new TargetFileName(var1).validate();
      }
   }
}
