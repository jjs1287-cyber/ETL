package org.etlforge.integration.data.entity.document;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.TextStructureValidator;
import org.etlforge.integration.data.structure.text.TextValidatorFactory;
import org.etlforge.integration.data.structure.text.ValidationIssue;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;

public final class TextDocumentValidator {
   private final TextDocument document;
   private final ValidationMessageCollector collector;

   public TextDocumentValidator(TextDocument var1, ValidationMessageCollector var2) {
      this.document = Preconditions.checkNotNull(var1, "doc");
      this.collector = Preconditions.checkNotNull(var2, "collector");
   }

   public void run() {
      this.validateDelimiter();
      this.validateStringQualifier();
      this.validateStructure();
   }

   private void validateDelimiter() {
      if (!this.document.isFixedField()) {
         FieldDelimiter var1 = this.document.getDelimiter();
         if (var1 == null) {
            this.fatal(DocumentMessages.getString("TextDocumentValidator.MissingDelimiter"));
         } else if (var1.equals(FieldDelimiter.NEW_LINE)) {
            this.validateUseOfNewLineAsDelimiter();
         } else if (!var1.isValid()) {
            this.fatal(DocumentMessages.getString("TextDocumentValidator.InvalidDelimiter"));
         }
      }
   }

   private void validateUseOfNewLineAsDelimiter() {
      if (!this.canHaveNewLineAsDelimiter()) {
         this.fatal(DocumentMessages.getString("TextDocumentValidator.InvalidUseOfNewLineAsDelimiter"));
      }
   }

   private boolean canHaveNewLineAsDelimiter() {
      return this.document.isSimpleDoc() && ((SimpleTextStructure)this.document.getDataStructure()).getFieldListSize() == 1;
   }

   private void validateStringQualifier() {
      String var1 = this.document.getStringQualifier();
      if (var1 != null && var1.length() > 0 && TextDocument.getQualifierFromString(var1) == 0) {
         this.fatal(DocumentMessages.getString("TextDocumentValidator.InvalidStringQualifuer"));
      }
   }

   private void validateStructure() {
      TextStructure var1 = this.document.getDataStructure();
      if (var1 == null) {
         this.fatal(DocumentMessages.getString("TextDocumentValidator.NoStructure"));
      } else {
         TextValidatorFactory var2 = new TextValidatorFactory();
         if (var1 instanceof SimpleTextStructure) {
            this.validateFields((SimpleTextStructure)var1, var2);
         } else {
            this.validateComplexStructure((ComplexTextStructure)var1, var2);
         }
      }
   }

   private void validateFields(Iterable<Field> var1, TextValidatorFactory var2) {
      TextStructureValidator var3 = var2.createFieldValidator(var1);

      for (ValidationIssue var5 : var3.validate()) {
         this.fatal(var5.getDescription());
      }

      this.validateValidationRules(var1);
   }

   private void validateValidationRules(Iterable<Field> var1) {
      for (Field var3 : var1) {
         Optional var4 = var3.getValidation();
         if (var4.isPresent() && !((FieldValidation)var4.get()).hasEffect()) {
            String var5 = "Field \"" + var3.getName() + "\": Validation is enabled, but has no effect.";
            this.collector.addMessage(var5, ValidationStatus.WARNING);
         }
      }
   }

   private void validateComplexStructure(ComplexTextStructure var1, TextValidatorFactory var2) {
      this.validateSegments(var1, var2);
      this.validateSegmentFields(var1, var2);
   }

   private void validateSegments(ComplexTextStructure var1, TextValidatorFactory var2) {
      TextStructureValidator var3 = var2.createSegmentValidator(var1);

      for (ValidationIssue var5 : var3.validate()) {
         this.fatal(var5.getDescription());
      }
   }

   private void validateSegmentFields(ComplexTextStructure var1, TextValidatorFactory var2) {
      for (Segment var4 : var1) {
         if (!var4.isRoot()) {
            if (var4.hasFields()) {
               this.validateFields(var4, var2);
            } else {
               this.fatal(DocumentMessages.getFormattedMessage("TextDocumentValidator.NoFields", new Object[]{var4.getName()}));
            }
         }
      }
   }

   private void fatal(String var1) {
      this.collector.addMessage(var1, ValidationStatus.INVALID);
   }
}
