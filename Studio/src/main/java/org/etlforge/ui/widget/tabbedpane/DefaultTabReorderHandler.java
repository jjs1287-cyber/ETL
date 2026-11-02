package org.etlforge.ui.widget.tabbedpane;

import java.awt.Component;
import javax.swing.Icon;

public class DefaultTabReorderHandler implements TabReorderHandler {
   @Override
   public void handle(KongaTabbedPane var1, int var2, int var3) {
      String var4 = var1.getTitleAt(var2);
      Icon var5 = var1.getIconAt(var2);
      String var6 = var1.getToolTipAt(var2);
      Component var7 = var1.getComponentAt(var2);
      var1.removeTabAt(var2);
      var1.insertTab(var4, var5, var7, var6, var3);
      var1.setSelectedIndex(var3);
   }
}
