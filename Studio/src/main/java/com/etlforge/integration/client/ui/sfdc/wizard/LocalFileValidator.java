package com.etlforge.integration.client.ui.sfdc.wizard;

import java.io.File;

public interface LocalFileValidator {
   LocalFileValidator OK = new LocalFileValidator() {
      @Override
      public String validate(File var1) {
         return null;
      }
   };

   String validate(File var1);
}
