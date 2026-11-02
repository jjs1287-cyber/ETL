package org.etlforge.integration.client.ui.structure.editor.db.rels;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.database.ui.TableIcon;
import org.etlforge.ui.graphics.IconRow;

final class TableJoinIcon implements Icon {
   private final Icon impl;

   public TableJoinIcon() {
      TableIcon var1 = new TableIcon();
      Icon var2 = ApplicationResources.ICONS.getIcon("ArrowRight.16");
      this.impl = new IconRow(var1, var2, var1);
   }

   @Override
   public int getIconHeight() {
      return this.impl.getIconHeight();
   }

   @Override
   public int getIconWidth() {
      return this.impl.getIconWidth();
   }

   @Override
   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      this.impl.paintIcon(var1, var2, var3, var4);
   }
}
