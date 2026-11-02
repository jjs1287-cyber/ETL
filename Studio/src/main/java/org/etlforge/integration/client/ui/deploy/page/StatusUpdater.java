package org.etlforge.integration.client.ui.deploy.page;

import javax.swing.JLabel;
import org.etlforge.ui.util.progress.ProgressMessage;
import org.etlforge.ui.widget.LabelAdapter;

final class StatusUpdater {
   private final JLabel label;
   private final ProgressMessage message;

   public StatusUpdater(JLabel var1, String var2) {
      this.label = var1;
      this.message = new ProgressMessage(var2, new LabelAdapter(var1));
   }

   public synchronized void start() {
      this.message.setUpdateAutomatically(300);
      this.label.setIcon(null);
   }

   public synchronized void stop() {
      this.message.stopAutomaticUpdate();
   }
}
