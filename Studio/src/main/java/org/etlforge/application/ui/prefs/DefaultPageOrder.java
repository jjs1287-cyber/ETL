package org.etlforge.application.ui.prefs;

import java.io.Serializable;
import java.util.Comparator;

public final class DefaultPageOrder implements Comparator<String>, Serializable {
   private static final String GENERAL = "General";

   public int compare(String var1, String var2) {
      if ("General".equalsIgnoreCase(var1)) {
         return "General".equalsIgnoreCase(var2) ? 0 : -1;
      } else {
         return "General".equals(var2) ? 1 : var1.compareToIgnoreCase(var2);
      }
   }
}
