package org.etlforge.ui.widget.combobox;

import com.google.common.base.Objects;
import java.util.Collection;
import javax.swing.ComboBoxModel;
import org.etlforge.ui.widget.list.FilteredListModel;
import org.etlforge.ui.widget.list.ListUtils;

public final class FilteredComboBoxModel extends FilteredListModel implements ComboBoxModel {
   private Object selected;

   public FilteredComboBoxModel(Collection<?> var1) {
      this(var1.toArray());
   }

   public FilteredComboBoxModel(Object[] var1) {
      super(ListUtils.createModel(var1));
   }

   @Override
   public void setSelectedItem(Object var1) {
      if (!Objects.equal(this.selected, var1)) {
         this.selected = var1;
         this.fireContentsChanged(this, -1, -1);
      }
   }

   @Override
   public Object getSelectedItem() {
      return this.selected;
   }
}
