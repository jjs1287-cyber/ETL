package org.etlforge.integration.data.script;

public interface SourceDataElementContainer {
   SourceDataElementContainer EMPTY = new SourceDataElementContainer() {
      @Override
      public boolean contains(String var1) {
         return false;
      }

      @Override
      public boolean isFlat() {
         return false;
      }
   };

   boolean contains(String var1);

   boolean isFlat();
}
