package org.etlforge.integration.client.ui.interchange.entity.document;

import org.etlforge.integration.client.entity.search.AbstractSearchableProperty;
import org.etlforge.integration.client.entity.search.ReplaceableKeyValueStringProperty;
import org.etlforge.integration.client.entity.search.ReplaceableProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.StringList;
import org.etlforge.integration.client.entity.search.StringListProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.integration.data.structure.text.TextStructureType;

final class TextDocumentSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public TextDocumentSearchAndReplaceProperties() {
      super(
         new TextDocumentSearchAndReplaceProperties.TextStructureTypeProperty(),
         new TextDocumentSearchAndReplaceProperties.DelimiterProperty(),
         new ReplaceableKeyValueStringProperty("String qualifier", "text_qualifier"),
         new TextDocumentSearchAndReplaceProperties.SegmentsProperty(),
         new TextDocumentSearchAndReplaceProperties.FieldsProperty()
      );
   }

   private static final class DelimiterProperty implements ReplaceableProperty<String> {
      private DelimiterProperty() {
      }

      @Override
      public String getName() {
         return "Delimiter";
      }

      @Override
      public Class<String> getValueType() {
         return String.class;
      }

      public void setValue(IntegrationEntity var1, String var2) {
         FieldDelimiter var3 = FieldDelimiter.fromString(var2);
         TextDocument var4 = (TextDocument)var1;
         var4.setDelimiter(var3);
      }

      public String getValue(IntegrationEntity var1) {
         TextDocument var2 = (TextDocument)var1;
         FieldDelimiter var3 = var2.getDelimiter();
         return var3 != null ? var3.toEscapedString() : null;
      }
   }

   private static final class FieldsProperty extends StringListProperty {
      public FieldsProperty() {
         super("Field names");
      }

      public StringList getValue(IntegrationEntity var1) {
         StringList var2 = new StringList();
         TextDocument var3 = (TextDocument)var1;
         TextStructure var4 = var3.getDataStructure();
         if (var4 instanceof ComplexTextStructure) {
            for (Segment var6 : (ComplexTextStructure)var4) {
               this.collectFields(var6, var2);
            }
         } else if (var4 instanceof SimpleTextStructure) {
            this.collectFields((SimpleTextStructure)var4, var2);
         }

         return var2;
      }

      private void collectFields(Iterable<Field> var1, StringList var2) {
         for (Field var4 : var1) {
            var2.add(var4.getName());
         }
      }
   }

   private static final class SegmentsProperty extends StringListProperty {
      public SegmentsProperty() {
         super("Segment names");
      }

      public StringList getValue(IntegrationEntity var1) {
         StringList var2 = new StringList();
         TextDocument var3 = (TextDocument)var1;
         TextStructure var4 = var3.getDataStructure();
         if (var4 instanceof ComplexTextStructure) {
            for (Segment var6 : (ComplexTextStructure)var4) {
               var2.add(var6.getName());
            }
         }

         return var2;
      }
   }

   private static final class TextStructureTypeProperty extends AbstractSearchableProperty<TextStructureType> {
      public TextStructureTypeProperty() {
         super("Simple/Complex", TextStructureType.class);
      }

      public TextStructureType getValue(IntegrationEntity var1) {
         TextDocument var2 = (TextDocument)var1;
         TextStructure var3 = var2.getDataStructure();
         return var3 instanceof SimpleTextStructure ? TextStructureType.SIMPLE : TextStructureType.COMPLEX;
      }
   }
}
