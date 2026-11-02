package org.etlforge.application.ui.menu;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

public interface Menu {
   String getID();

   void addExtension(MenuExtensionPoint var1, MenuContribution var2);

   void removeExtension(MenuExtensionPoint var1, String var2);

   void removeAllExtensions();

   JMenu createJMenu(MenuDictionary var1);

   void populate(JMenu var1);

   void populate(JPopupMenu var1);

   boolean isSupported();
}
