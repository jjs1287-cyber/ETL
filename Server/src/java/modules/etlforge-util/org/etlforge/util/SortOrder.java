package org.etlforge.util;

public enum SortOrder {
   NONE("NONE", 0),
   DESCENDING("DESC", -1),
   ASCENDING("ASC", 1);

   private int m_value;
   private String m_abbreviation;

   SortOrder(String var3, int var4) {
      this.m_abbreviation = var3;
      this.m_value = var4;
   }

   public int getSortValue() {
      return this.m_value;
   }

   public String getAbbreviation() {
      return this.m_abbreviation;
   }

   public static SortOrder fromAbbreviation(String var0) {
      for (SortOrder var4 : values()) {
         if (var4.getAbbreviation().equals(var0)) {
            return var4;
         }
      }

      return null;
   }
}
