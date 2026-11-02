package org.etlforge.integration.uidelegate.entity.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.uidelegate.entity.list.IntegrationEntityListDecorator;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;

public class IntegrationEntityListDecorRenderer extends DefaultListCellRenderer implements ListDecor {
   private JList list;
   private Insets insets;

   @Override
   public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
      this.list = var1;
      super.getListCellRendererComponent(var1, var2, var3, var4, var5);
      ListDecorator var6 = null;
      if (var2 instanceof IntegrationEntity) {
         var6 = new IntegrationEntityListDecorator((IntegrationEntity)var2);
      } else if (var2 instanceof ListDecorator) {
         var6 = (ListDecorator)var2;
      }

      if (var6 != null) {
         var6.decorate(this, var4);
      }

      if (this.insets != null) {
         this.setBorder(new BorderBuilder(this.getBorder()).empty(this.insets));
      }

      return this;
   }

   public void setInsets(Insets var1) {
      this.insets = (Insets)var1.clone();
   }

   @Override
   public final Font getListFont() {
      return this.list != null ? this.list.getFont() : null;
   }

   @Override
   public Color getTextColor() {
      return this.list != null ? this.list.getForeground() : null;
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
