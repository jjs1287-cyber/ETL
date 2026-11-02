package org.etlforge.ui.widget.table.renderer;

import com.google.common.base.Preconditions;
import java.text.NumberFormat;
import javax.swing.table.DefaultTableCellRenderer;

public final class NumberFormatRenderer extends DefaultTableCellRenderer {
   private final NumberFormat fmt;

   public static NumberFormatRenderer forInteger() {
      return new NumberFormatRenderer(NumberFormat.getIntegerInstance());
   }

   public NumberFormatRenderer(NumberFormat var1) {
      this.fmt = Preconditions.checkNotNull(var1);
      this.setHorizontalAlignment(11);
   }

   @Override
   protected void setValue(Object var1) {
      if (var1 instanceof Number) {
         super.setValue(this.fmt.format(var1));
      } else {
         super.setValue(var1);
      }
   }
}
