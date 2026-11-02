package org.etlforge.application.ui.prefs;

import java.util.Arrays;

final class DuplicateNodeException extends RuntimeException {
   public DuplicateNodeException(String[] var1) {
      super("Duplicate preferences node path: " + Arrays.toString(var1));
   }
}
