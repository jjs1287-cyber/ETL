package org.etlforge.integration.client.ui.admin.engine;

import java.util.EnumSet;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.integration.client.admin.engine.ServerEngine;
import org.etlforge.integration.client.admin.engine.StopEngineJob;

public final class StopEngineAction extends EngineControlAction {
   public StopEngineAction(ServerEngine var1, Admin var2) {
      super(var1, var2, "Stop " + var1.getName());
   }

   @Override
   protected UiJob createJob(ServerEngine var1, Admin var2) {
      return new StopEngineJob(var1, var2);
   }

   @Override
   protected void updateAccordingToCurrentEngineStatus(EnumSet<ServerStatus> var1) {
      this.setEnabled(var1.contains(ServerStatus.RUNNING) && !var1.contains(ServerStatus.STOPPING));
   }
}
