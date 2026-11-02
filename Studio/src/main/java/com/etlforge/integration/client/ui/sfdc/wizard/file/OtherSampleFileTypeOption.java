package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Optional;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.resources.UiResourceBundle;

final class OtherSampleFileTypeOption implements SampleFileTypeOption {
   @Override
   public MnemonicString getText() {
      return UiResourceBundle.MNEMONIC_FACTORY.fromString("Select from e&xisting " + EntityType.Document.getDisplayName() + ", or create by hand");
   }

   @Override
   public boolean isValid() {
      return true;
   }

   @Override
   public SampleFileType getFileType() {
      return new OtherSampleFileType();
   }

   @Override
   public boolean isGeneratedAutomatically() {
      return false;
   }

   @Override
   public Optional<UiProvider> getUi() {
      return Optional.absent();
   }
}
