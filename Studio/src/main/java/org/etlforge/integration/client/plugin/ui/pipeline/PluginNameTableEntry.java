package org.etlforge.integration.client.plugin.ui.pipeline;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JLabel;
import org.etlforge.integration.client.ui.icons.ClientIcons;

final class PluginNameTableEntry {
   private static final Icon OPTIONAL = ClientIcons.PLUGIN_16;
   private static final Icon GLOBAL = ClientIcons.PLUGIN_16;
   private final String name;
   private final boolean global;

   public PluginNameTableEntry(PluginTableItem var1) {
      this.name = var1.getDisplayName();
      this.global = var1.isGlobal();
   }

   @Override
   public String toString() {
      return this.name;
   }

   public void decorate(JLabel var1, boolean var2) {
      var1.setText(this.name);
      if (this.global) {
         var1.setIcon(GLOBAL);
         if (!var2) {
            var1.setForeground(Color.BLUE);
         }
      } else {
         var1.setIcon(OPTIONAL);
         if (!var2) {
            var1.setForeground(javax.swing.UIManager.getColor("Label.foreground"));
         }
      }
   }
}

