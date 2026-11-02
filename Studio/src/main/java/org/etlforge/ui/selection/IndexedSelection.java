package org.etlforge.ui.selection;

public interface IndexedSelection extends OrderedSelection {
   IndexedSelection EMPTY = new EmptyIndexedSelection();

   int headIndex();

   int[] indeces();
}
