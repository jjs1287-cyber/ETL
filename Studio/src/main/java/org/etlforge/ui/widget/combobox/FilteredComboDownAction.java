package org.etlforge.ui.widget.combobox;

import com.google.common.base.Predicate;
import javax.swing.JComboBox;
import org.etlforge.ui.util.KeyUtils;

final class FilteredComboDownAction extends FilteredComboNavigationAction {
   public FilteredComboDownAction(Predicate<Integer> var1) {
      super("down", KeyUtils.DOWN, var1);
   }

   @Override
   protected int nextIndex(JComboBox var1, int var2) {
      for (int var3 = var2 + 1; var3 < var1.getItemCount(); var3++) {
         if (this.isSelectableIndex(var3)) {
            return var3;
         }
      }

      return -1;
   }
}
