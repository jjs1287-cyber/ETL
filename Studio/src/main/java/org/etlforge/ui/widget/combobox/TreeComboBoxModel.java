package org.etlforge.ui.widget.combobox;

import javax.swing.ComboBoxModel;

public interface TreeComboBoxModel extends ComboBoxModel {
   int getIndentLevel(Object var1);

   boolean isSelectable(Object var1);
}
