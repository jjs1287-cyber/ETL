package org.etlforge.util.string;

public enum Case {
   UPPER,
   LOWER;

   public String convert(String var1) {
      switch (this) {
         case UPPER:
            return var1 != null ? var1.toUpperCase() : null;
         case LOWER:
            return var1 != null ? var1.toLowerCase() : null;
         default:
            throw new RuntimeException("What kind of a case am I?! " + this.name());
      }
   }
}
