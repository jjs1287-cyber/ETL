package org.etlforge.ui.widget.combobox;

import com.google.common.base.Predicate;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;

final class FilteredComboEndAction extends FilteredComboNavigationAction {
   public FilteredComboEndAction(Predicate<Integer> var1) {
      super("end", KeyStroke.getKeyStroke(35, 0), var1);
   }

   @Override
   protected int nextIndex(JComboBox var1, int var2) {
      for (int var3 = var1.getItemCount() - 1; var3 > var2; var3--) {
         if (this.isSelectableIndex(var3)) {
            return var3;
         }
      }

      return -1;
   }
}
