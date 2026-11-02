package org.etlforge.ui.widget.table;

import javax.swing.JTable;

public enum TableGridMode {
   BOTH,
   HORIZONTAL,
   VERTICAL,
   NONE;

   public void applyTo(JTable var1) {
      switch (this) {
         case BOTH:
            var1.setShowGrid(true);
            break;
         case HORIZONTAL:
            var1.setShowHorizontalLines(true);
            var1.setShowVerticalLines(false);
            break;
         case VERTICAL:
            var1.setShowHorizontalLines(false);
            var1.setShowVerticalLines(true);
            break;
         case NONE:
            var1.setShowGrid(false);
      }
   }
}
