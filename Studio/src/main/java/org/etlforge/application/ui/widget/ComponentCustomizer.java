package org.etlforge.application.ui.widget;

import javax.swing.JTable;
import org.etlforge.ui.widget.table.renderer.TableRendererWithToolTip;

public final class ComponentCustomizer {
   public static void customizeTable(JTable var0) {
      TableRendererWithToolTip var1 = new TableRendererWithToolTip(var0.getDefaultRenderer(String.class)) {
         @Override
         public String getToolTipText(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
            return var2 == null ? null : var2.toString();
         }
      };
      var0.setDefaultRenderer(String.class, var1);
   }

   private ComponentCustomizer() {
   }
}
