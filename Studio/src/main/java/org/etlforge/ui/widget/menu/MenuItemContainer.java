package org.etlforge.ui.widget.menu;

import javax.swing.JMenuItem;

public interface MenuItemContainer {
   void removeAll();

   void add(JMenuItem var1);

   void addSeparator();
}
