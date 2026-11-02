package org.etlforge.ui.widget.text;

import javax.swing.Action;
import javax.swing.JMenuItem;

public interface KongaTextComponent {
   void disableContextMenu();

   void addActionToContextMenu(Action var1);

   void addToContextMenu(JMenuItem var1);

   void setSelectAllWhenFocused(boolean var1);

   boolean isEmpty();
}
