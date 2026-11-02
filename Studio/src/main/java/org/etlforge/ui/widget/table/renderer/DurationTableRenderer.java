package org.etlforge.ui.widget.table.renderer;

import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.util.time.Duration;

public class DurationTableRenderer extends DefaultTableCellRenderer {
   private final boolean includeMilliSeconds;

   public DurationTableRenderer() {
      this(false);
   }

   public DurationTableRenderer(boolean var1) {
      this.includeMilliSeconds = var1;
   }

   @Override
   protected void setValue(Object var1) {
      if (var1 instanceof Duration) {
         String var2 = ((Duration)var1).toString(this.includeMilliSeconds);
         super.setValue(var2);
      } else {
         super.setValue(var1);
      }
   }
}
