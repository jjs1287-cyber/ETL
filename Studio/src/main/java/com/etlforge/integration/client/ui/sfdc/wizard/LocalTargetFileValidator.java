package com.etlforge.integration.client.ui.sfdc.wizard;

import java.io.File;

public final class LocalTargetFileValidator implements LocalFileValidator {
   @Override
   public String validate(File var1) {
      return var1.isDirectory() ? "The selected file is a directory." : null;
   }
}
