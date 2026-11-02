package org.etlforge.integration.activity.ui.log;

import javax.swing.Icon;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.integration.activity.client.log.LogStatus;

public final class LogStatusTableRenderer extends DefaultTableCellRenderer {
   @Override
   protected void setValue(Object var1) {
      if (var1 instanceof LogStatus) {
         LogStatus var2 = (LogStatus)var1;
         Icon var3 = PackageResources.getStatusIcon(var2);
         this.setIcon(var3);
         this.setText(var3 == null ? var2.toString() : null);
         this.setToolTipText(var2.toString());
         this.setHorizontalAlignment(0);
      } else {
         this.setToolTipText(null);
         this.setIcon(null);
         super.setValue(var1);
      }
   }
}
