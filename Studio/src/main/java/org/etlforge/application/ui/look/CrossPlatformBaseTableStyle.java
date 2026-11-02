package org.etlforge.application.ui.look;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.JTable;
import org.etlforge.ui.style.AbstractTableStyle;
import org.etlforge.ui.widget.table.TableGridMode;
import org.etlforge.ui.widget.table.renderer.AlternateRowRenderer;

abstract class CrossPlatformBaseTableStyle extends AbstractTableStyle {
   private final double rowHeightFactor;
   private final Insets headerPadding;
   private final float headerFontSizeDelta;

   protected CrossPlatformBaseTableStyle(double var1, Insets var3, float var4) {
      this.rowHeightFactor = var1;
      this.headerPadding = var3;
      this.headerFontSizeDelta = var4;
      this.setGridMode(TableGridMode.HORIZONTAL);
      this.addRendererPreparer(new AlternateRowRenderer(new Color[]{Color.WHITE, new Color(250, 254, 255)}));
   }

   @Override
   public void makeOver(JTable var1) {
      this.setHeaderRenderer(var1, this.headerPadding, this.headerFontSizeDelta);
      this.setRowHeight(var1, this.rowHeightFactor);
      this.setGridColor(var1, new Color(185, 211, 238));
      super.makeOver(var1);
   }
}
