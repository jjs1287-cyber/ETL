package org.etlforge.ui.action;

import javax.swing.Action;

public interface ToggleAction extends Action {
   String ON_OFF = "ToggleAction#ON_OFF";

   boolean isSelected();

   void setSelected(boolean var1);
}
