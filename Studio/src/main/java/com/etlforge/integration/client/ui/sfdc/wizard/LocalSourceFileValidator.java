package com.etlforge.integration.client.ui.sfdc.wizard;

import java.io.File;

public final class LocalSourceFileValidator implements LocalFileValidator {
   @Override
   public String validate(File var1) {
      if (!var1.exists()) {
         return "The selected file does not exist.";
      } else if (var1.isDirectory()) {
         return "The selected file is a directory.";
      } else {
         return var1.length() == 0L ? "The selected file is empty." : null;
      }
   }
}
