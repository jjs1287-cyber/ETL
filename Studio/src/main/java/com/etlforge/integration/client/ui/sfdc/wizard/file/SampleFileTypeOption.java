package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Optional;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiProvider;

interface SampleFileTypeOption {
   MnemonicString getText();

   boolean isValid();

   boolean isGeneratedAutomatically();

   SampleFileType getFileType();

   Optional<UiProvider> getUi();
}
