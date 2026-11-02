package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Optional;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.resources.UiResourceBundle;

final class CsvSampleFileTypeOption implements SampleFileTypeOption {
   private final boolean withHeader;

   public CsvSampleFileTypeOption(boolean var1) {
      this.withHeader = var1;
   }

   @Override
   public MnemonicString getText() {
      String var1 = this.withHeader ? "&CSV file, with header" : "CS&V file, without header";
      return UiResourceBundle.MNEMONIC_FACTORY.fromString(var1);
   }

   @Override
   public boolean isValid() {
      return true;
   }

   @Override
   public Optional<UiProvider> getUi() {
      return Optional.absent();
   }

   @Override
   public SampleFileType getFileType() {
      return new DelimitedSampleFileType(FieldDelimiter.COMMA, this.withHeader);
   }

   @Override
   public boolean isGeneratedAutomatically() {
      return true;
   }
}
