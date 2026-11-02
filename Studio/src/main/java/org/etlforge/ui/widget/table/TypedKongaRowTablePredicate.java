package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;

public final class TypedKongaRowTablePredicate<T, M extends TypedAbstractKongaTableModel<T>> extends RowFilter<M, Integer> {
   private final Predicate<? super T> condition;

   public static final <V, N extends TypedAbstractKongaTableModel<V>> TypedKongaRowTablePredicate<V, N> create(Predicate<? super V> var0) {
      return new TypedKongaRowTablePredicate<>(var0);
   }

   public TypedKongaRowTablePredicate(Predicate<? super T> var1) {
      Preconditions.checkNotNull(var1, "condition");
      this.condition = var1;
   }

   @Override
   public boolean include(Entry<? extends M, ? extends Integer> var1) {
      Integer var2 = (Integer)var1.getIdentifier();
      Object var3 = ((TypedAbstractKongaTableModel)var1.getModel()).getRowObjectAt(var2);
      return this.condition.apply((T)var3);
   }
}
