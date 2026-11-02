package org.etlforge.string;

import com.google.common.base.Function;

public enum StringTransforms implements Function<String, String> {
   TO_LOWER_CASE {
      public String apply(String var1) {
         return var1.toLowerCase();
      }
   },
   TRIM {
      public String apply(String var1) {
         return var1.trim();
      }
   };

   StringTransforms() {
   }
}
