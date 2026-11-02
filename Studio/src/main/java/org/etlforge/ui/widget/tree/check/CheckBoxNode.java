package org.etlforge.ui.widget.tree.check;

import org.etlforge.ui.widget.tree.KongaTreeNode;

public interface CheckBoxNode extends KongaTreeNode {
   boolean isSelected();

   void setSelected(boolean var1);

   void toggle();
}
