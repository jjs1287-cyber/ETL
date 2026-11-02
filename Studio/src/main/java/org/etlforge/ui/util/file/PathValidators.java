package org.etlforge.ui.util.file;

import com.google.common.base.Preconditions;

public enum PathValidators implements PathValidator {
   ALWAYS_VALID {
      @Override
      public boolean isValid(String var1) {
         Preconditions.checkNotNull(var1);
         return true;
      }
   },
   FILES_ONLY {
      @Override
      public boolean isValid(String var1) {
         return !var1.endsWith("/") && !var1.endsWith("\\");
      }
   };

   PathValidators() {
   }
}
