package org.etlforge.ui.widget.table;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import org.etlforge.util.preference.KongaPreferences;
import org.etlforge.util.preference.PreferencesKey;

public class SortStatusPreferencesMemento implements SortStatusMemento {
   private static final String ORDER = "Order";
   private static final String INDEX = "Index";
   private final PreferencesKey preferenceKey;

   public SortStatusPreferencesMemento(Class<?> var1) {
      this(new PreferencesKey(var1, "SortStatus"));
   }

   public SortStatusPreferencesMemento(PreferencesKey var1) {
      Preconditions.checkNotNull(var1, "key");
      this.preferenceKey = var1;
   }

   @Override
   public void restore(KongaTable var1) {
      KongaPreferences var2 = this.getPreferences();
      int var3 = var2.getInt("Index", -1);
      if (var3 >= 0) {
         SortOrder var4 = this.restoreSortOrder(var2);
         if (var4 != null) {
            var1.setSortingStatus(var3, var4);
         }
      }
   }

   @Override
   public void store(KongaTable var1) {
      KongaPreferences var2 = this.getPreferences();
      RowSorter var3 = var1.getRowSorter();
      List var4 = var3.getSortKeys();
      int var5 = -1;
      SortOrder var6 = SortOrder.ASCENDING;
      if (!var4.isEmpty()) {
         SortKey var7 = (SortKey)var4.get(0);
         var5 = var7.getColumn();
         var6 = var7.getSortOrder();
      }

      var2.putInt("Index", var5);
      var2.putEnum("Order", var6);
   }

   private KongaPreferences getPreferences() {
      return KongaPreferences.getPreferences(this.preferenceKey);
   }

   private SortOrder restoreSortOrder(KongaPreferences var1) {
      return var1.getEnum("Order", SortOrder.class, null);
   }
}
