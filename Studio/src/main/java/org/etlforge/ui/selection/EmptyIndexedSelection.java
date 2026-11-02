package org.etlforge.ui.selection;

public class EmptyIndexedSelection extends EmptyOrderedSelection implements IndexedSelection {
   private static final int[] INDECES = new int[0];

   @Override
   public int headIndex() {
      return -1;
   }

   @Override
   public int[] indeces() {
      return INDECES;
   }
}
