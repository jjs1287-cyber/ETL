package com.etlforge.integration.client.ui.sfdc;

import java.awt.EventQueue;
import javax.swing.JFrame;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.salesforce.PoolingConnectionFactory;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.ui.Size;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class PrintPooledConnectionsReportCommandExecutor implements CommandExecutor {
   private final PoolingConnectionFactory pool;

   public PrintPooledConnectionsReportCommandExecutor(SalesforceConnectionFactory var1) {
      this.pool = var1 instanceof PoolingConnectionFactory ? (PoolingConnectionFactory)var1 : null;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      if (this.pool == null) {
         throw new CommandException("Not using a connection pool.");
      }

      String var2 = this.pool.getPooledConnectionsReport();
      ApplicationLog.getMainLogger().info(var2);
      this.viewReport(var2);
   }

   private void viewReport(final String var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            KongaTextArea var1x = KongaTextArea.viewer(var1, Size.rows(6), Size.columns(80));
            var1x.setLineWrap(false);
            JFrame var2 = ApplicationUi.getAppWinFrame();
            Alert.info(var2, var1x, "Connection Pool Status");
         }
      });
   }
}
