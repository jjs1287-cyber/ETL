package org.etlforge.integration.client.admin;

import org.etlforge.integration.client.server.ServerCall;

public interface Admin extends ServerCall {
   void startProcessEngine(StartCallback var1);

   void stopProcessEngine(StopCallback var1);

   void isProcessEngineRunning(IsRunningCallback var1);

   void getProcessEngineStatus(GetStatusCallback var1, Admin.StatusDetail var2);

   void startScheduler(StartCallback var1);

   void stopScheduler(StopCallback var1);

   void isSchedulerRunning(IsRunningCallback var1);

   void getSchedulerStatus(GetStatusCallback var1, Admin.StatusDetail var2);

   void startJmsEngine(StartCallback var1);

   void stopJmsEngine(StopCallback var1);

   void isJmsEngineRunning(IsRunningCallback var1);

   void getJmsEngineStatus(GetStatusCallback var1, Admin.StatusDetail var2);

   enum StatusDetail {
      SHORT(0),
      NORMAL(1),
      FULL(-1);

      private final int serverCode;

      StatusDetail(int var3) {
         this.serverCode = var3;
      }

      public int getServerCode() {
         return this.serverCode;
      }
   }
}
