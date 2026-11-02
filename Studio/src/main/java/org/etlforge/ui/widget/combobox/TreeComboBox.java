package org.etlforge.ui.widget.combobox;

import com.google.common.base.Predicate;
import javax.swing.ComboBoxModel;
import org.etlforge.system.ArgCheck;

public class TreeComboBox extends KongaComboBox {
   public TreeComboBox(TreeComboBoxModel var1) {
      super(var1);
      this.installNavigationActions();
   }

   private void installNavigationActions() {
      Predicate var1 = this.getKeyNavigationFilter();
      FilteredComboNavigationAction.installAll(this, var1);
   }

   private Predicate<Integer> getKeyNavigationFilter() {
      return new Predicate<Integer>() {
         public boolean apply(Integer var1) {
            return TreeComboBox.this.isSelectableIndex(var1);
         }
      };
   }

   @Override
   public void setModel(ComboBoxModel var1) {
      super.setModel(ArgCheck.checkInstance(var1, TreeComboBoxModel.class));
   }

   public TreeComboBoxModel getModel() {
      return (TreeComboBoxModel)super.getModel();
   }

   public final boolean isSelectable(Object var1) {
      return this.getModel().isSelectable(var1);
   }

   public final boolean isSelectableIndex(int var1) {
      return this.isSelectable(this.getItemAt(var1));
   }
}
