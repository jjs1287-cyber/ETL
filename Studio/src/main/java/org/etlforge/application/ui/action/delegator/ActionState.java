package org.etlforge.application.ui.action.delegator;

import javax.swing.Icon;

public interface ActionState {
   void setName(String var1);

   void setToolTip(String var1);

   void setIcon(Icon var1);

   void setEnabled(boolean var1);

   void setSelected(boolean var1);
}
