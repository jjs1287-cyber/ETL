package org.etlforge.ui.util.find;

import org.etlforge.ui.resources.UiResourceBundle;

public enum SearchDirection {
   UP(UiResourceBundle.getString("Backward")),
   DOWN(UiResourceBundle.getString("Forward"));

   private final String stringRep;

   SearchDirection(String var3) {
      this.stringRep = var3;
   }

   @Override
   public String toString() {
      return this.stringRep;
   }
}
