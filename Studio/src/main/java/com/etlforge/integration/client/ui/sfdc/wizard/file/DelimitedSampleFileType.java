package com.etlforge.integration.client.ui.sfdc.wizard.file;

import java.io.IOException;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.InvalidTemplateException;
import org.etlforge.integration.data.structure.text.Prototype;

final class DelimitedSampleFileType implements SampleFileType {
   private final FieldDelimiter delimiter;
   private final boolean withHeader;

   public DelimitedSampleFileType(FieldDelimiter var1, boolean var2) {
      this.delimiter = var1;
      this.withHeader = var2;
   }

   @Override
   public SourceFileStructure createFileStructure(SampleFile var1, String var2) throws IOException, InvalidTemplateException {
      SimpleTextStructure var3 = this.createTextStructure(var1);
      TextDocument var4 = this.createDocument(var2, var3);
      return new SourceFileStructure(var4, this.withHeader);
   }

   private SimpleTextStructure createTextStructure(SampleFile var1) throws IOException, InvalidTemplateException {
      Prototype var2 = this.withHeader ? Prototype.SampleFlatFileWithHeader : Prototype.SampleFlatFile;
      return var1.asTextStructure(var2, this.delimiter);
   }

   private TextDocument createDocument(String var1, SimpleTextStructure var2) {
      TextDocument var3 = new TextDocument(var1);
      var3.setStructure(var2);
      var3.setDelimiter(this.delimiter);
      var3.setPartOfSalesforceWizard(true);
      return var3;
   }
}
