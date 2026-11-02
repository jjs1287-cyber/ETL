package org.etlforge.application.ui.menu;

import javax.swing.Icon;

public interface MenuDictionary {
   MenuDictionary getParent();

   String getName(String var1);

   Integer getMnemonic(String var1);

   Integer getDisplayedMnemonicIndex(String var1);

   Icon getIcon(String var1);
}
