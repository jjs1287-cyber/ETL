package org.etlforge.ui.selection;

import java.util.Arrays;
import java.util.Collection;

public final class SelectionFactory {
   public static OrderedSelection newOrderedSelection(Object var0) {
      return newOrderedSelection(new Object[]{var0});
   }

   public static OrderedSelection newOrderedSelection(Object[] var0) {
      if (var0 != null && var0.length != 0) {
         return var0.length > 0 ? new SelectionFactory.OrderedSelectionImpl(var0) : OrderedSelection.EMPTY;
      } else {
         return OrderedSelection.EMPTY;
      }
   }

   public static OrderedSelection newOrderedSelection(Collection<?> var0) {
      if (var0 != null && !var0.isEmpty()) {
         return var0.isEmpty() ? OrderedSelection.EMPTY : new SelectionFactory.OrderedSelectionImpl(var0.toArray(new Object[var0.size()]));
      } else {
         return OrderedSelection.EMPTY;
      }
   }

   public static IndexedSelection newIndexedSelection(Object var0, int var1) {
      return new SelectionFactory.IndexedSelectionImpl(var0, var1);
   }

   public static IndexedSelection newIndexedSelection(Object[] var0, int[] var1) {
      return var0 != null && var0.length != 0 ? new SelectionFactory.IndexedSelectionImpl(var0, var1) : IndexedSelection.EMPTY;
   }

   private SelectionFactory() {
   }

   private static final class IndexedSelectionImpl extends SelectionFactory.OrderedSelectionImpl implements IndexedSelection {
      private static final int[] EMPTY = new int[0];
      private final int[] indeces;

      public IndexedSelectionImpl(Object var1, int var2) {
         this(new Object[]{var1}, new int[]{var2});
      }

      public IndexedSelectionImpl(Object[] var1, int[] var2) {
         super(var1);
         checkArguments(var1, var2);
         this.indeces = copy(var2);
      }

      @Override
      public int headIndex() {
         return this.isEmpty() ? -1 : this.indeces[0];
      }

      @Override
      public int[] indeces() {
         return copy(this.indeces);
      }

      private static void checkArguments(Object[] var0, int[] var1) {
         if (var0 == null) {
            if (var1 != null && var1.length > 0) {
               throw new IllegalArgumentException("items is null, so indeces must be null or empty");
            }
         } else if (var1 == null) {
            if (var0.length > 0) {
               throw new IllegalArgumentException("indeces is null, so items must be null or empty");
            }
         } else if (var0.length != var1.length) {
            throw new IllegalArgumentException("items and indeces must contain the same number of elements");
         }
      }

      private static int[] copy(int[] var0) {
         if (var0 == null) {
            return EMPTY;
         }

         int var1 = var0.length;
         if (var1 == 0) {
            return EMPTY;
         }

         int[] var2 = new int[var1];
         System.arraycopy(var0, 0, var2, 0, var1);
         return var2;
      }
   }

   private static class OrderedSelectionImpl implements OrderedSelection {
      private static final Object[] EMPTY = new Object[0];
      private final Object[] items;

      public OrderedSelectionImpl(Object[] var1) {
         this.items = copy(var1);
      }

      @Override
      public Object head() {
         return this.isEmpty() ? null : this.items[0];
      }

      @Override
      public Iterable<? extends Object> items() {
         return Arrays.asList(this.items);
      }

      @Override
      public int size() {
         return this.items.length;
      }

      @Override
      public Object[] toArray() {
         return copy(this.items);
      }

      @Override
      public boolean isEmpty() {
         return this.size() == 0;
      }

      private static Object[] copy(Object[] var0) {
         if (var0 == null) {
            return EMPTY;
         }

         int var1 = var0.length;
         if (var1 == 0) {
            return EMPTY;
         }

         Object[] var2 = new Object[var1];
         System.arraycopy(var0, 0, var2, 0, var1);
         return var2;
      }
   }
}
