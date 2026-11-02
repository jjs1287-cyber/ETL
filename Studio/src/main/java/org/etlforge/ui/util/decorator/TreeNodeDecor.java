package org.etlforge.ui.util.decorator;

import javax.swing.Icon;
import javax.swing.JTree;
import org.etlforge.ui.widget.text.TextDecorator;

public interface TreeNodeDecor {
   JTree getTree();

   void setIcon(Icon var1);

   void setText(String var1);

   void setToolTip(String var1);

   void setTextStyle(TextDecorator var1);

   void setEnabled(boolean var1);

   boolean isSelected();

   boolean isExpanded();
}
