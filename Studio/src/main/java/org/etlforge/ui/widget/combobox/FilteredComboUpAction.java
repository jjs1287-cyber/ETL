package org.etlforge.ui.widget.combobox;

import com.google.common.base.Predicate;
import javax.swing.JComboBox;
import org.etlforge.ui.util.KeyUtils;

final class FilteredComboUpAction extends FilteredComboNavigationAction {
   public FilteredComboUpAction(Predicate<Integer> var1) {
      super("up", KeyUtils.UP, var1);
   }

   @Override
   protected int nextIndex(JComboBox var1, int var2) {
      for (int var3 = var2 - 1; var3 >= 0; var3--) {
         if (this.isSelectableIndex(var3)) {
            return var3;
         }
      }

      return -1;
   }
}
