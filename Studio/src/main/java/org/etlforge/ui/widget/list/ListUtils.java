package org.etlforge.ui.widget.list;

import com.google.common.base.Predicate;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JList;
import javax.swing.ListModel;
import org.etlforge.lang.PrimitiveConversions;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.util.KeyUtils;

public final class ListUtils {
   public static DefaultListModel createModel(Object... var0) {
      return createModel(Arrays.asList(var0));
   }

   public static DefaultListModel createModel(Iterable<?> var0) {
      DefaultListModel var1 = new DefaultListModel();

      for (Object var3 : var0) {
         var1.addElement(var3);
      }

      return var1;
   }

   public static DefaultListModel trimModel(DefaultListModel var0, Collection<?> var1) {
      Set var2 = var1 instanceof Set ? (Set)var1 : new HashSet(var1);
      DefaultListModel var3 = new DefaultListModel();
      int var4 = 0;

      for (int var5 = var0.getSize(); var4 < var5; var4++) {
         Object var6 = var0.elementAt(var4);
         if (!var2.contains(var6)) {
            var3.addElement(var6);
         }
      }

      return var3;
   }

   public static void selectObjects(JList var0, Object[] var1) {
      ArrayList var2 = new ArrayList();
      ListModel var3 = var0.getModel();

      for (Object var7 : var1) {
         int var8 = indexOf(var3, var7);
         if (var8 >= 0) {
            var2.add(var8);
         }
      }

      var0.setSelectedIndices(PrimitiveConversions.toIntArray(var2));
   }

   public static void selectAll(JList var0) {
      int var1 = var0.getModel().getSize();
      if (var1 > 0) {
         var0.setSelectionInterval(0, var1 - 1);
      }
   }

   public static void installCyclicKeyNavigation(JList var0) {
      InputMap var1 = var0.getInputMap(0);
      ActionMap var2 = var0.getActionMap();
      Object var3 = var1.get(KeyUtils.UP);
      if (var3 == null) {
         var3 = "key_up";
      }

      var2.put(var3, new ListUtils.CyclicKeyUpAction(var0));
      Object var4 = var1.get(KeyUtils.DOWN);
      if (var4 == null) {
         var4 = "key_down";
      }

      var2.put(var4, new ListUtils.CyclicKeyDownAction(var0));
   }

   public static Object[] toArray(ListModel var0, int[] var1) {
      int var2 = var1.length;
      Object[] var3 = new Object[var2];

      for (int var4 = 0; var4 < var2; var4++) {
         var3[var4] = var0.getElementAt(var1[var4]);
      }

      return var3;
   }

   private static int indexOf(ListModel var0, Object var1) {
      if (var0 instanceof DefaultListModel) {
         return ((DefaultListModel)var0).indexOf(var1);
      }

      int var2 = 0;

      for (int var3 = var0.getSize(); var2 < var3; var2++) {
         if (equals(var0.getElementAt(var2), var1)) {
            return var2;
         }
      }

      return -1;
   }

   private static boolean equals(Object var0, Object var1) {
      return var0 != null ? var0.equals(var1) : var1 == null;
   }

   public static ListFilter filterFromPredicate(final Predicate var0) {
      return var0 == null ? null : new ListFilter() {
         @Override
         public boolean test(Object var1) {
            return var0.apply(var1);
         }
      };
   }

   public static <T> Iterable<T> asIterable(ListModel var0, Class<T> var1) {
      return new ListUtils.ListModelIterable<>(var0, var1);
   }

   public static void deleteSelectedIndeces(KongaList var0) {
      IndexedSelection var1 = var0.getSelection();
      if (!var1.isEmpty()) {
         int[] var2 = var1.indeces();
         boolean var3 = var2.length == 1 && var0.getModel().getSize() > 1;
         deleteIndeces(var0.getModel(), var2);
         if (var3) {
            var0.setSelectedIndex(Math.min(var2[0], var0.getModel().getSize() - 1));
         }
      }
   }

   public static void deleteIndeces(ListModel var0, int[] var1) {
      if (var0 instanceof DefaultListModel) {
         deleteIndeces((DefaultListModel)var0, var1);
      } else {
         if (!(var0 instanceof MutableListModel)) {
            throw new UnsupportedOperationException("The list model does not support delete");
         }

         deleteIndeces((MutableListModel)var0, var1);
      }
   }

   public static void deleteIndeces(MutableListModel var0, int[] var1) {
      for (int var2 = var1.length - 1; var2 >= 0; var2--) {
         var0.removeItemAt(var1[var2]);
      }
   }

   public static void deleteIndeces(DefaultListModel var0, int[] var1) {
      for (int var2 = var1.length - 1; var2 >= 0; var2--) {
         var0.removeElementAt(var1[var2]);
      }
   }

   private ListUtils() {
   }

   private static final class CyclicKeyDownAction extends AbstractAction {
      private final JList list;

      public CyclicKeyDownAction(JList var1) {
         this.list = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = this.list.getModel().getSize();
         if (var2 != 0) {
            int var3 = this.list.getSelectedIndex();
            int var4 = var3 == var2 - 1 ? 0 : var3 + 1;
            this.list.setSelectedIndex(var4);
            this.list.ensureIndexIsVisible(var4);
         }
      }
   }

   private static final class CyclicKeyUpAction extends AbstractAction {
      private final JList list;

      public CyclicKeyUpAction(JList var1) {
         this.list = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = this.list.getModel().getSize();
         if (var2 != 0) {
            int var3 = this.list.getSelectedIndex();
            int var4 = var3 > 0 ? var3 - 1 : var2 - 1;
            this.list.setSelectedIndex(var4);
            this.list.ensureIndexIsVisible(var4);
         }
      }
   }

   private static class ListModelIterable<T> implements Iterable<T> {
      private final ListModel listModel;
      private final Class<T> valueClass;

      public ListModelIterable(ListModel var1, Class<T> var2) {
         this.listModel = var1;
         this.valueClass = var2;
      }

      @Override
      public Iterator<T> iterator() {
         return new Iterator<T>() {
            private final int size = ListModelIterable.this.listModel.getSize();
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
               if (ListModelIterable.this.listModel.getSize() != this.size) {
                  throw new ConcurrentModificationException();
               } else {
                  return this.currentIndex < this.size;
               }
            }

            @Override
            public T next() {
               if (ListModelIterable.this.listModel.getSize() != this.size) {
                  throw new ConcurrentModificationException();
               }

               if (!this.hasNext()) {
                  throw new NoSuchElementException();
               }

               Object var1 = ListModelIterable.this.listModel.getElementAt(this.currentIndex);
               this.currentIndex++;
               return ListModelIterable.this.valueClass.cast(var1);
            }

            @Override
            public void remove() {
               throw new UnsupportedOperationException();
            }
         };
      }
   }
}
