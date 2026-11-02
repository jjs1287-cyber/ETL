package org.etlforge.ui.text.contentassist;

public interface StringValueHistory {
   StringValueHistory EMPTY = new StringValueHistory() {
      @Override
      public String[] values() {
         return new String[0];
      }

      @Override
      public boolean delete(String var1) {
         return false;
      }
   };

   String[] values();

   boolean delete(String var1);
}
