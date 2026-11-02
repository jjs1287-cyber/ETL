package org.etlforge.ui.property;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.IndexedSelectionBuilder;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.list.ListUtils;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class ListBinding<T> extends AbstractBinding<List<T>> {
   private Object itemWhenEmpty;
   private final KongaList ui;
   private ListBinding.ListFunction<? super T> listFunction;
   private Comparator<? super T> listOrder;
   private boolean useFilteredModel;
   private ListBinding.SelectionRetentionMode selectionRetentionMode = ListBinding.SelectionRetentionMode.NONE;

   public static <K> ListBinding.ListFunction<K> identityFunction() {
      return new ListBinding.ListFunction<K>() {
         @Override
         public Object toListItem(K var1) {
            return var1;
         }

         @Override
         public void prepare() {
         }
      };
   }

   public ListBinding(AbstractPropertyBasedModel var1, String var2, KongaList var3) {
      this(var1.getProperty(var2), var3);
   }

   public ListBinding(Property<List<T>> var1, KongaList var2) {
      super(var1);
      this.ui = var2;
      this.listFunction = identityFunction();
   }

   public void setListItemFunction(ListBinding.ListFunction<? super T> var1) {
      this.listFunction = var1;
   }

   public void setListOrder(Comparator<? super T> var1) {
      this.listOrder = var1;
   }

   public void setItemWhenEmpty(Object var1) {
      this.itemWhenEmpty = var1;
   }

   public void setSelectionRetentionMode(ListBinding.SelectionRetentionMode var1) {
      this.selectionRetentionMode = Preconditions.checkNotNull(var1);
   }

   public void setUseFilteredModel(boolean var1) {
      this.useFilteredModel = var1;
   }

   protected void updateUiWithValue(List<T> var1) {
      ListModel var2 = this.ui.getModel();
      IndexedSelection var3 = this.ui.getSelection();
      ListModel var4 = this.createModel(var1);
      this.ui.setModel(var4);
      IndexedSelection var5 = this.selectionRetentionMode.apply(var2, var3, this.ui.getModel());
      this.ui.setSelection(var5);
   }

   private ListModel createModel(List<T> var1) {
      DefaultListModel var2 = new DefaultListModel();
      if (var1.isEmpty()) {
         if (this.itemWhenEmpty != null) {
            var2.addElement(this.itemWhenEmpty);
         }

         return var2;
      } else {
         this.listFunction.prepare();

         for (Object var5 : this.getListOrder(var1)) {
            Object var6 = this.listFunction.toListItem((T)var5);
            if (var6 instanceof Iterable) {
               for (Object var8 : (Iterable)var6) {
                  var2.addElement(var8);
               }
            } else {
               var2.addElement(var6);
            }
         }

         return this.useFilteredModel ? this.filteredModel(var2) : var2;
      }
   }

   private Iterable<? extends T> getListOrder(Iterable<? extends T> var1) {
      if (this.listOrder == null) {
         return var1;
      }

      ArrayList var2 = Lists.newArrayList(var1);
      Collections.sort(var2, this.listOrder);
      return var2;
   }

   private ListModel filteredModel(DefaultListModel var1) {
      FilteredListModel var2 = new FilteredListModel(var1);
      ListModel var3 = this.ui.getModel();
      if (var3 instanceof FilteredListModel) {
         for (ListFilter var5 : ((FilteredListModel)var3).getFilters()) {
            var2.addFilter(var5);
         }
      }

      return var2;
   }

   public interface ListFunction<V> {
      void prepare();

      Object toListItem(V var1);
   }

   public enum SelectionRetentionMode {
      NONE {
         @Override
         IndexedSelection apply(ListModel var1, IndexedSelection var2, ListModel var3) {
            return IndexedSelection.EMPTY;
         }
      },
      VALUES {
         @Override
         IndexedSelection apply(ListModel var1, IndexedSelection var2, ListModel var3) {
            if (!var2.isEmpty() && var3.getSize() != 0) {
               ArrayList var4 = Lists.newArrayList(ListUtils.asIterable(var3, Object.class));
               IndexedSelectionBuilder var5 = new IndexedSelectionBuilder();

               for (Object var7 : var2.items()) {
                  int var8 = var4.indexOf(var7);
                  if (var8 >= 0) {
                     var5.add(var7, var8);
                  }
               }

               return var5.build();
            } else {
               return IndexedSelection.EMPTY;
            }
         }
      },
      INDECES {
         @Override
         IndexedSelection apply(ListModel var1, IndexedSelection var2, ListModel var3) {
            if (!var2.isEmpty() && var3.getSize() != 0) {
               int[] var4 = var2.indeces();
               switch (var4.length) {
                  case 0:
                     return IndexedSelection.EMPTY;
                  case 1:
                     return this.restoreSingleSelection(var1, var4[0], var3);
                  default:
                     return this.restoreMultiSelection(var1, var4, var3);
               }
            } else {
               return IndexedSelection.EMPTY;
            }
         }

         private IndexedSelection restoreSingleSelection(ListModel var1, int var2, ListModel var3) {
            int var4 = var3.getSize();
            int var5 = -1;
            if (var2 < var4) {
               var5 = var2;
            } else if (var2 == var1.getSize() - 1) {
               var5 = var3.getSize() - 1;
            }

            if (var5 == -1) {
               return IndexedSelection.EMPTY;
            }

            Object var6 = var3.getElementAt(var5);
            return SelectionFactory.newIndexedSelection(var6, var5);
         }

         private IndexedSelection restoreMultiSelection(ListModel var1, int[] var2, ListModel var3) {
            Arrays.sort(var2);
            int var4 = var2[var2.length - 1];
            if (var4 >= var3.getSize()) {
               int var10 = var2[0];
               int var11 = var10 < var3.getSize() ? var10 : var3.getSize() - 1;
               return SelectionFactory.newIndexedSelection(var3.getElementAt(var11), var11);
            }

            ArrayList var5 = new ArrayList();

            for (int var9 : var2) {
               var5.add(var3.getElementAt(var9));
            }

            return SelectionFactory.newIndexedSelection(var5.toArray(), var2);
         }
      };

      SelectionRetentionMode() {
      }

      abstract IndexedSelection apply(ListModel var1, IndexedSelection var2, ListModel var3);
   }
}
