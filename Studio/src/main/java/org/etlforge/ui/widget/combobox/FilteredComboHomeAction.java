package org.etlforge.ui.widget.combobox;

import com.google.common.base.Predicate;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;

final class FilteredComboHomeAction extends FilteredComboNavigationAction {
   public FilteredComboHomeAction(Predicate<Integer> var1) {
      super("home", KeyStroke.getKeyStroke(36, 0), var1);
   }

   @Override
   protected int nextIndex(JComboBox var1, int var2) {
      for (int var3 = 0; var3 < var2; var3++) {
         if (this.isSelectableIndex(var3)) {
            return var3;
         }
      }

      return -1;
   }
}
