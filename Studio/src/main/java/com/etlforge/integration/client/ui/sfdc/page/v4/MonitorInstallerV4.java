package com.etlforge.integration.client.ui.sfdc.page.v4;

import org.etlforge.integration.activity.serverapi.log.OperationLogProvider;
import org.etlforge.integration.activity.serverapi.queue.OperationQueueProvider;
import org.etlforge.integration.activity.ui.monitor.OperationMonitor;
import org.etlforge.integration.activity.ui.monitor.OperationMonitorApis;
import org.etlforge.integration.client.server.IntegrationServer;

public final class MonitorInstallerV4 {
   public static void installMonitor(AbstractSalesforceItemPageV4<?> var0) {
      IntegrationServer var1 = IntegrationServer.getInstance();
      OperationMonitorApis var2 = new OperationMonitorApis(var1.getServerCall(OperationLogProvider.class), var1.getServerCall(OperationQueueProvider.class));
      OperationMonitor var3 = new OperationMonitor(var2);
      var0.installMonitoring(var3);
   }

   private MonitorInstallerV4() {
   }
}
