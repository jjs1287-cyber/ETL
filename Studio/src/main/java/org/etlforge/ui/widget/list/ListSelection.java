package org.etlforge.ui.widget.list;

import javax.swing.JList;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.IndexedSelectionBuilder;
import org.etlforge.ui.selection.SelectionConverter;

public final class ListSelection implements IndexedSelection {
   private final IndexedSelection impl;

   public ListSelection(JList var1) {
      this(var1, SelectionConverter.TRIVIAL);
   }

   public ListSelection(JList var1, SelectionConverter var2) {
      this.impl = this.createSelection(var1, var2);
   }

   @Override
   public int headIndex() {
      return this.impl.headIndex();
   }

   @Override
   public int[] indeces() {
      return this.impl.indeces();
   }

   @Override
   public Object head() {
      return this.impl.head();
   }

   @Override
   public Iterable<? extends Object> items() {
      return this.impl.items();
   }

   @Override
   public int size() {
      return this.impl.size();
   }

   @Override
   public Object[] toArray() {
      return this.impl.toArray();
   }

   @Override
   public boolean isEmpty() {
      return this.impl.isEmpty();
   }

   private IndexedSelection createSelection(JList var1, SelectionConverter var2) {
      Object[] var3 = var1.getSelectedValues();
      int[] var4 = var1.getSelectedIndices();
      IndexedSelectionBuilder var5 = new IndexedSelectionBuilder();
      int var6 = 0;

      for (int var7 = var3.length; var6 < var7; var6++) {
         Object var8 = var2.fromSelection(var3[var6]);
         if (var8 != null) {
            var5.add(var8, var4[var6]);
         }
      }

      return var5.build();
   }
}
