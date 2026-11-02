package org.etlforge.integration.uidelegate.entity.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.list.IntegrationEntityListDecorator;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.widget.SelectorTable;

public final class IntegrationEntitySelectorTableRenderer extends SelectorTable.ObjectRenderer implements ListDecor {
   private JTable table;

   @Override
   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      this.table = var1;
      super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      if (var2 instanceof IntegrationEntity) {
         IntegrationEntityListDecorator var7 = new IntegrationEntityListDecorator((IntegrationEntity)var2);
         var7.decorate(this, var3);
      }

      return this;
   }

   @Override
   public final Font getListFont() {
      return this.table != null ? this.table.getFont() : null;
   }

   @Override
   public Color getTextColor() {
      return this.table != null ? this.table.getForeground() : null;
   }

   @Override
   public void setTextColor(Color var1) {
      this.setForeground(var1);
   }

   @Override
   public final void setToolTip(String var1) {
      this.setToolTipText(var1);
   }
}
