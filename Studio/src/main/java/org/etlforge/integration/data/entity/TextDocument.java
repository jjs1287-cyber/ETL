package org.etlforge.integration.data.entity;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.document.PatchForBug1969251;
import org.etlforge.integration.data.entity.document.TextDocumentLinkCollector;
import org.etlforge.integration.data.entity.document.TextDocumentReferenceReplacer;
import org.etlforge.integration.data.entity.document.TextDocumentValidator;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.QualifierMode;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.integration.data.structure.text.validation.NumberOfFieldsValidation;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.persist.Persistor;

public final class TextDocument extends IntegrationEntity {
   public static final FieldDelimiter DEFAULT_DELIMITER = FieldDelimiter.COMMA;
   public static final String DEFAULT_STRING_QUALIFIER = "\"";
   public static final int c_unknown_doc = 0;
   public static final int c_flat_fix_doc = 1;
   public static final int c_flat_del_doc = 2;
   public static final int c_complex_fix_doc = 3;
   public static final int c_complex_del_doc = 4;
   @IdProperty
   public static final String DOCUMENT_ID = "document_id";
   public static final String FIELD_ID = "field_id";
   public static final String PARENT_ID = "parent_id";
   @NameProperty
   public static final String DOCUMENT_NAME = "document_name";
   public static final String FORMAT_TYPE_ID = "format_type_id";
   public static final String DELIMITER = "delimiter";
   public static final String TEXT_QUALIFIER = "text_qualifier";
   public static final String QUALIFIER_MODE = "qualifier_mode";
   public static final String STRUCTURE = "document_structure";
   public static final String EDIFACT = "edifact";
   public static final String NUMBER_OF_COLUMNS_VALIDATION = "number_of_columns_validation";
   private TextStructure textStructure;

   public static String[] getDocumentDefinitionsColumnNames() {
      return new String[]{
         "document_id",
         "field_id",
         "parent_id",
         "field_name",
         "sequence",
         "var_type_id",
         "start_pos",
         "end_pos",
         "required",
         "default_value",
         "format",
         "uni_id"
      };
   }

   public static String[] getDocumentFieldValidationColumnNames() {
      return new String[]{
         "document_id",
         "field_id",
         "parent_id",
         "notempty",
         "fieldlength",
         "trimtolength",
         "ignorecase",
         "fieldoperator",
         "operatorargument",
         "actiononinvalid"
      };
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{
         "document_id",
         "document_name",
         "format_type_id",
         "delimiter",
         "text_qualifier",
         "qualifier_mode",
         "edifact",
         "number_of_columns_validation",
         "salesforce_wizard_object"
      };
   }

   public TextDocument(String var1) {
      this(var1, null, 0);
   }

   public TextDocument(TextDocumentId var1, String var2) {
      this(var2, var1, null, 0);
   }

   protected TextDocument(String var1, TextDocumentId var2, int var3) {
      this(var1, var2, var3, null);
   }

   public TextDocument(String var1, TextDocumentId var2, TextStructure var3, int var4) {
      super(EntityType.Document, var2, var1);
      this.setProperty("document_name", var1);
      this.setProperty("format_type_id", var4);
      this.setProperty("document_id", this.getID().toString());
      this.textStructure = var3;
      this.updateTextStructureDelimiterAndQualifier();
   }

   public TextDocument(String var1, TextDocumentId var2, int var3, List<String> var4) {
      super(EntityType.Document, var2, var1);
      this.setProperty("document_name", var1);
      this.setProperty("format_type_id", var3);
      this.setProperty("document_id", this.getID().toString());
      if (var3 != 0) {
         this.createTextStructure(var4, null);
      }
   }

   public void setStructure(TextStructure var1) {
      Preconditions.checkNotNull(var1, "structure");
      if (var1 != this.textStructure) {
         synchronized (this.getDataLock()) {
            this.textStructure = var1;
            this.updateTextStructureDelimiterAndQualifier();
            this.updateDocType(var1);
            if (!(var1 instanceof SimpleTextStructure)) {
               this.setProperty("number_of_columns_validation", (String)null);
            }
         }

         this.firePropertyChange("document_structure", null, var1);
      }
   }

   private void updateDocType(TextStructure var1) {
      int var2 = 0;
      if (var1 instanceof SimpleTextStructure) {
         var2 = var1.isFixField() ? 1 : 2;
      } else {
         var2 = var1.isFixField() ? 3 : 4;
      }

      this.setProperty("format_type_id", var2);
   }

   public TextDocumentId getID() {
      return (TextDocumentId)super.getID();
   }

   @Override
   public void setName(String var1) throws IllegalNameException {
      synchronized (this.getDataLock()) {
         super.setName(var1);
         this.setProperty("document_name", var1);
         if (this.textStructure != null) {
            this.textStructure.setName(var1);
         }
      }
   }

   public TextStructure createTextStructure() {
      return this.createTextStructure(null, null);
   }

   public TextStructure createTextStructure(List<String> var1, List<FieldValidation> var2) {
      synchronized (this.getDataLock()) {
         if (this.isUnknownDoc()) {
            return null;
         }

         if (this.isSimpleDoc()) {
            this.textStructure = new SimpleTextStructure(this.getName(), this.isFixedField(), var1, var2);
         } else {
            this.textStructure = new ComplexTextStructure(this.getName(), this.isFixedField(), var1);
         }

         this.updateTextStructureDelimiterAndQualifier();
         this.setDeployDirty(true);
         return this.textStructure;
      }
   }

   private void updateTextStructureDelimiterAndQualifier() {
      if (this.textStructure != null) {
         this.textStructure.setDelimiter(this.getDelimiter());
         this.textStructure.setStringQualifier(this.getStringQualifier());
      }
   }

   public TextStructure getDataStructure() {
      synchronized (this.getDataLock()) {
         return this.textStructure;
      }
   }

   public boolean isUnknownDoc() {
      synchronized (this.getDataLock()) {
         return this.getDocType() == 0;
      }
   }

   public FieldDelimiter getDelimiter() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("delimiter");
         return var2 != null ? FieldDelimiter.fromString(var2) : null;
      }
   }

   public void setDelimiter(FieldDelimiter var1) {
      synchronized (this.getDataLock()) {
         this.setStringProperty("delimiter", var1.toString());
         this.updateTextStructureDelimiterAndQualifier();
      }
   }

   public String getStringQualifier() {
      return this.getProperty("text_qualifier", "\"");
   }

   public void setStringQualifier(String var1) {
      synchronized (this.getDataLock()) {
         this.setStringProperty("text_qualifier", var1);
         this.updateTextStructureDelimiterAndQualifier();
      }
   }

   public QualifierMode getQualifierMode() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("qualifier_mode", null);
         if (var2 != null) {
            QualifierMode var10000;
            try {
               var10000 = QualifierMode.valueOf(var2);
            } catch (Exception var5) {
               var5.printStackTrace();
               return QualifierMode.WHEN_NEEDED;
            }

            return var10000;
         }
      }

      return QualifierMode.WHEN_NEEDED;
   }

   public void setQualifierMode(QualifierMode var1) {
      this.setStringProperty("qualifier_mode", var1.name());
   }

   public int getDocType() {
      return this.getIntProperty("format_type_id", 0);
   }

   public void setDocType(int var1) {
      this.setDocType(var1, true);
   }

   public void setDocType(int var1, boolean var2) {
      synchronized (this.getDataLock()) {
         if (this.isUnknownDoc()) {
            this.setProperty("format_type_id", var1);
            if (var2) {
               this.createTextStructure();
            }
         }
      }
   }

   public boolean isSimpleDoc() {
      synchronized (this.getDataLock()) {
         int var2 = this.getDocType();
         return var2 == 1 || var2 == 2;
      }
   }

   public boolean isFixedField() {
      synchronized (this.getDataLock()) {
         int var2 = this.getDocType();
         return var2 == 1 || var2 == 3;
      }
   }

   @Override
   public StringKeyValuePairs getCopyOfKeyValues() {
      synchronized (this.getDataLock()) {
         StringKeyValuePairs var2 = new StringKeyValuePairs();
         var2.putAll(super.getCopyOfKeyValues());
         var2.put("document_id", this.getID().toString());
         var2.put("document_name", this.getName());
         return var2;
      }
   }

   public TextDocument clone() {
      TextDocument var1 = (TextDocument)super.clone();
      synchronized (this.getDataLock()) {
         if (this.textStructure != null) {
            var1.textStructure = this.textStructure.clone();
         }

         return var1;
      }
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      new TextDocumentValidator(this, var1).run();
   }

   public static char getDelimiterFromString(String var0) {
      if (var0.length() == 1) {
         return var0.charAt(0);
      }

      if (var0.length() > 1 && var0.charAt(0) == '\\') {
         switch (var0.charAt(1)) {
            case '1':
               return '\u0001';
            case 't':
               return '\t';
            default:
               return '\u0000';
         }
      } else {
         return '\u0000';
      }
   }

   public static char getQualifierFromString(String var0) {
      return getDelimiterFromString(var0);
   }

   public boolean isPartOfSalesforceWizard() {
      return SalesforceUtils.isSalesforceWizardObject(this);
   }

   public void setPartOfSalesforceWizard(boolean var1) {
      SalesforceUtils.setSalesforceWizardObject(this, var1);
   }

   public void documentStructureChanged() {
      this.setDeployDirty(true);
      this.firePropertyChange("structure", "0", "1");
   }

   @Override
   protected Set<String> getKeysToSkipWhenPersisting() {
      return Collections.singleton("delimiter");
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      FieldDelimiter var2 = this.getDelimiter();
      if (var2 != null) {
         var2.writeTo(var1.createChild("Delimiter"));
      }

      if (this.isFixedField()) {
         new PatchForBug1969251(this).apply();
      }

      this.textStructure.writeTo(var1.createChild("Structure"));
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      Persistor var2 = var1.getFirstChild("Delimiter");
      if (var2 != null) {
         this.setDelimiter(FieldDelimiter.fromPersistor(var2));
      }

      if (this.isSimpleDoc()) {
         this.textStructure = new SimpleTextStructure("", false);
      } else {
         this.textStructure = new ComplexTextStructure("", false);
      }

      this.textStructure.restoreFrom(var1.getFirstChild("Structure"));
      this.updateTextStructureDelimiterAndQualifier();
   }

   @Override
   public void importAdditionalDataFrom(IntegrationEntity var1) {
      this.textStructure = ((TextDocument)var1).textStructure.clone();
   }

   public boolean isEdifact() {
      return this.getBooleanProperty("edifact", false);
   }

   public void setEdifact(boolean var1) {
      this.setBooleanProperty("edifact", var1);
   }

   public boolean isSupportingEdifact() {
      return !this.isSimpleDoc() && !this.isFixedField();
   }

   public Optional<NumberOfFieldsValidation> getNumberOfFieldsValidation() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("number_of_columns_validation");
         return StringUtils.isNotEmpty(var2) ? Optional.of(NumberOfFieldsValidation.fromStorageForm(var2)) : Optional.absent();
      }
   }

   public void setNumberOfFieldsValidation(Optional<NumberOfFieldsValidation> var1) {
      Preconditions.checkState(this.isSimpleDoc(), "NumberOfFieldsValidation is only supported for Simple Text Structures");
      String var3 = var1.isPresent() ? ((NumberOfFieldsValidation)var1.get()).toStorageForm() : null;
      Optional var2;
      synchronized (this.getDataLock()) {
         var2 = this.getNumberOfFieldsValidation();
         this.setProperty("number_of_columns_validation", var3);
      }

      this.firePropertyChange("number_of_columns_validation", var2, var1);
   }

   public void setNumberOfFieldsValidation(NumberOfFieldsValidation var1) {
      this.setNumberOfFieldsValidation(Optional.fromNullable(var1));
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      TextDocumentLinkCollector var2 = new TextDocumentLinkCollector(this, var1);
      return var2.collect();
   }

   @Override
   public boolean isDeepCopyable() {
      return true;
   }

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      synchronized (this.getDataLock()) {
         TextDocumentReferenceReplacer var3 = new TextDocumentReferenceReplacer(var1);
         var3.replaceReferences(this);
      }
   }
}
