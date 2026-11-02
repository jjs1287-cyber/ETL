package org.etlforge.integration.data.structure.text;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.etlforge.integration.data.structure.SimpleTextStructure;

public final class FlatTextFileStructureFactory {
   private Prototype prototype = Prototype.SampleFlatFileWithHeader;
   private FieldDelimiter delimiter = FieldDelimiter.COMMA;
   private boolean inferNumericTypes = true;
   private Charset charset = Charset.defaultCharset();

   public FlatTextFileStructureFactory withCharset(Charset var1) {
      this.charset = Preconditions.checkNotNull(var1);
      return this;
   }

   public FlatTextFileStructureFactory withPrototype(Prototype var1) {
      this.prototype = Preconditions.checkNotNull(var1);
      return this;
   }

   public FlatTextFileStructureFactory withDelimiter(FieldDelimiter var1) {
      this.delimiter = Preconditions.checkNotNull(var1);
      return this;
   }

   public FlatTextFileStructureFactory doNotInferNumericTypes() {
      this.inferNumericTypes = false;
      return this;
   }

   public SimpleTextStructure createStructure(File var1) throws IOException, InvalidTemplateException {
      String var2 = "_flat_";
      SimpleTextStructure var3 = SimpleTextStructure.fromSampleDataFile(var2, this.prototype, this.delimiter, var1, this.charset);
      if (!this.inferNumericTypes) {
         this.turnNumericFieldTypesIntoStrings(var3);
      }

      return var3;
   }

   private void turnNumericFieldTypesIntoStrings(SimpleTextStructure var1) {
      for (Field var3 : var1) {
         FieldDataType var4 = var3.getDataType();
         switch (var4) {
            case Integer:
            case Long:
            case Float:
            case Double:
               var3.setDataType(FieldDataType.String);
         }
      }
   }
}
