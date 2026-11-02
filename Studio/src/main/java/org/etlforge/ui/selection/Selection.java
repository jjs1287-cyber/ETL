package org.etlforge.ui.selection;

import java.util.ArrayList;

public interface Selection {
   Selection EMPTY = new Selection() {
      @Override
      public boolean isEmpty() {
         return true;
      }

      @Override
      public Iterable<? extends Object> items() {
         return new ArrayList<>();
      }
   };

   boolean isEmpty();

   Iterable<? extends Object> items();
}
