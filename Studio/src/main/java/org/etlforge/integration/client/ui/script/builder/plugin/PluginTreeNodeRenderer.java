package org.etlforge.integration.client.ui.script.builder.plugin;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.util.Fonts;

final class PluginTreeNodeRenderer extends DefaultTreeCellRenderer {
   @Override
   public Component getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
      super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
      if (var2 instanceof PluginTreeNode) {
         this.setFont(var1.getFont());
         this.setIcon(ClientIcons.PLUGIN_16);
      } else {
         this.setFont(getFontForNonPluginNode(var1, var2));
         this.setIcon(ClientIcons.SERVER_16);
      }

      return this;
   }

   private static Font getFontForNonPluginNode(JTree var0, Object var1) {
      Font var2 = var0.getFont();
      if (var1 == var0.getModel().getRoot()) {
         var2 = Fonts.bold(var2);
      }

      return var2;
   }
}
