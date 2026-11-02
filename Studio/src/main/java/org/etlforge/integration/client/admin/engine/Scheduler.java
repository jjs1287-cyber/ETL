package org.etlforge.integration.client.admin.engine;

import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.GetStatusCallback;
import org.etlforge.integration.client.admin.StartCallback;
import org.etlforge.integration.client.admin.StopCallback;

public final class Scheduler extends ServerEngine {
   public Scheduler() {
      super("Scheduler");
   }

   @Override
   public void start(Admin var1, StartCallback var2) {
      var1.startScheduler(var2);
   }

   @Override
   public void stop(Admin var1, StopCallback var2) {
      var1.stopScheduler(var2);
   }

   @Override
   public void requestStatus(Admin var1, Admin.StatusDetail var2, GetStatusCallback var3) {
      var1.getSchedulerStatus(var3, var2);
   }
}
