package org.etlforge.ui.widget.combobox;

import com.google.common.base.Predicate;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;

final class FilteredComboPageDownAction extends FilteredComboNavigationAction {
   public FilteredComboPageDownAction(Predicate<Integer> var1) {
      super("page down", KeyStroke.getKeyStroke(34, 0), var1);
   }

   @Override
   protected int nextIndex(JComboBox var1, int var2) {
      int var3 = Math.min(var2 + var1.getMaximumRowCount(), var1.getItemCount() - 1);
      if (this.isSelectableIndex(var3)) {
         return var3;
      }

      int var4 = 1;

      while (true) {
         int var5 = var3 - var4;
         boolean var6 = var5 >= 0;
         if (var6 && this.isSelectableIndex(var5)) {
            return var5;
         }

         int var7 = var3 + var4;
         boolean var8 = var7 < var1.getItemCount();
         if (var8 && this.isSelectableIndex(var7)) {
            return var7;
         }

         if (!var6 && !var8) {
            return -1;
         }

         var4++;
      }
   }
}
