package org.etlforge.application.ui.look;

import java.awt.Color;
import javax.swing.JTable;
import org.etlforge.ui.style.AbstractTableStyle;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.AlternateRowRenderer;

abstract class WindowsVistaBaseTableStyle extends AbstractTableStyle {
   private final double rowHeightFactor;

   protected WindowsVistaBaseTableStyle(double var1) {
      this.rowHeightFactor = var1;
      this.setGridMode(TableGridMode.HORIZONTAL);
      this.addRendererPreparer(new AlternateRowRenderer(new Color[]{Color.WHITE, new Color(250, 254, 255)}));
   }

   @Override
   public void makeOver(JTable var1) {
      this.setRowHeight(var1, this.rowHeightFactor);
      this.setGridColor(var1, new Color(185, 211, 238));
      super.makeOver(var1);
   }
}
