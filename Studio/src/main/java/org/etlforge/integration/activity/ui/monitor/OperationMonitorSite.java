package org.etlforge.integration.activity.ui.monitor;

public interface OperationMonitorSite {
   OperationMonitorSite INACTIVE = new OperationMonitorSite() {
      @Override
      public void openActivityMonitor(long var1) {
      }

      @Override
      public void closeActivityMonitor() {
      }
   };

   void openActivityMonitor(long var1);

   void closeActivityMonitor();
}
