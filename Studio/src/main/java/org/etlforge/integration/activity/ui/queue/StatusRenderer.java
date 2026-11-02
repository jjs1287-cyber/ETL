package org.etlforge.integration.activity.ui.queue;

import javax.swing.Icon;
import javax.swing.table.DefaultTableCellRenderer;
import org.etlforge.integration.activity.client.queue.QueueStatus;

public final class StatusRenderer extends DefaultTableCellRenderer {
   @Override
   protected void setValue(Object var1) {
      if (var1 instanceof QueueStatus) {
         Icon var2 = null;
         switch ((QueueStatus)var1) {
            case PENDING:
               var2 = PackageResources.PENDING_ICON;
               break;
            case RUNNING:
               var2 = PackageResources.RUNNING_ICON;
         }

         this.setIcon(var2);
         this.setToolTipText(((QueueStatus)var1).toString());
         this.setHorizontalAlignment(0);
      } else {
         this.setToolTipText(null);
         this.setIcon(null);
         super.setValue(var1);
      }
   }
}
