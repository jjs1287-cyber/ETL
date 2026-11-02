package org.etlforge.ui.selection;

public interface SelectionConverter {
   SelectionConverter TRIVIAL = new SelectionConverter() {
      @Override
      public Object fromSelection(Object var1) {
         return var1;
      }

      @Override
      public Object toSelection(Object var1) {
         return var1;
      }
   };

   Object fromSelection(Object var1);

   Object toSelection(Object var1);

   abstract class Adapter implements SelectionConverter {
      @Override
      public Object fromSelection(Object var1) {
         return null;
      }

      @Override
      public Object toSelection(Object var1) {
         return null;
      }
   }
}
