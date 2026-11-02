package org.etlforge.application.ui.look;

import java.awt.Color;
import javax.swing.JTable;
import org.etlforge.ui.style.AbstractTableStyle;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.AlternateRowRenderer;

abstract class NimbusBaseTableStyle extends AbstractTableStyle {
   private final double rowHeightFactor;

   protected NimbusBaseTableStyle(double var1) {
      this.rowHeightFactor = var1;
      this.addRendererPreparer(new AlternateRowRenderer(new Color[]{Color.WHITE, new Color(250, 254, 255)}));
   }

   @Override
   public void makeOver(JTable var1) {
      this.setRowHeight(var1, this.rowHeightFactor);
      super.makeOver(var1);
   }

   @Override
   public void setGridMode(TableGridMode var1) {
      super.setGridMode(TableGridMode.NONE);
   }
}
