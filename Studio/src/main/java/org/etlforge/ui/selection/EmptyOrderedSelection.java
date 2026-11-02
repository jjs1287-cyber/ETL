package org.etlforge.ui.selection;

import java.util.ArrayList;

public class EmptyOrderedSelection implements OrderedSelection {
   private static final Object[] AS_ARRAY = new Object[0];

   @Override
   public Object head() {
      return null;
   }

   @Override
   public int size() {
      return 0;
   }

   @Override
   public Object[] toArray() {
      return AS_ARRAY;
   }

   @Override
   public Iterable<Object> items() {
      return new ArrayList<>();
   }

   @Override
   public boolean isEmpty() {
      return true;
   }
}
