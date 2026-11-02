package org.etlforge.application.ui.action;

import javax.swing.Icon;
import javax.swing.KeyStroke;
import org.etlforge.ui.MnemonicString;

public interface ActionDictionary {
   ActionDictionary getParent();

   MnemonicString getName(String var1);

   String getToolTip(String var1);

   Icon getSmallIcon(String var1);

   Icon getMediumIcon(String var1);

   Icon getLargeIcon(String var1);

   KeyStroke getAccelerator(String var1);
}
