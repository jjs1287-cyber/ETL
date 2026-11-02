package org.etlforge.ui.selection;

public interface OrderedSelection extends Selection {
   OrderedSelection EMPTY = new EmptyOrderedSelection();

   int size();

   Object head();

   Object[] toArray();
}
