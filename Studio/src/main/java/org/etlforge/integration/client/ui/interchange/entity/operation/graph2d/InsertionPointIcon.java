package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.resources.GraphResources;
import org.etlforge.ui.graph2d.Graph2D;
import org.etlforge.ui.graph2d.dnd.DropStatus;

public class InsertionPointIcon implements Icon {
   private static final Icon NORMAL_ICON = GraphResources.ICONS.getIcon("InsertionPoint.Yellow");
   private static final Icon DROP_ALLOWED_ICON = GraphResources.ICONS.getIcon("InsertionPoint.Green");
   private static final Icon DROP_DISALLOWED_ICON = GraphResources.ICONS.getIcon("InsertionPoint.Red");
   private final Graph2D graph;
   private Icon current;

   public InsertionPointIcon(Graph2D var1) {
      this.graph = var1;
      this.current = NORMAL_ICON;
   }

   public void setDropStatus(DropStatus var1) {
      Icon var2 = this.getIconForStatus(var1);
      if (var2 != this.current) {
         this.current = var2;
         this.graph.repaint();
      }
   }

   @Override
   public int getIconHeight() {
      return this.current.getIconHeight();
   }

   @Override
   public int getIconWidth() {
      return this.current.getIconWidth();
   }

   @Override
   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      this.current.paintIcon(var1, var2, var3, var4);
   }

   private Icon getIconForStatus(DropStatus var1) {
      switch (var1) {
         case ALLOWED:
            return DROP_ALLOWED_ICON;
         case DISALLOWED:
            return DROP_DISALLOWED_ICON;
         default:
            return NORMAL_ICON;
      }
   }
}
