package org.etlforge.ui.text.contentassist;

import javax.swing.Icon;

public interface TokenDecorator {
   String getDisplayName(String var1);

   Icon getIcon(String var1);

   String getInfo(String var1);
}
