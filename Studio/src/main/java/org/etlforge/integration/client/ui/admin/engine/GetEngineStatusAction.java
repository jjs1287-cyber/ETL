package org.etlforge.integration.client.ui.admin.engine;

import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.engine.GetEngineStatusJob;
import org.etlforge.integration.client.admin.engine.ServerEngine;
import org.etlforge.integration.client.ui.admin.resources.AdminIcons;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;

public final class GetEngineStatusAction extends EngineControlAction {
   private final Admin.StatusDetail detail;
   private Receiver<Admin.StatusDetail> callback;

   public GetEngineStatusAction(ServerEngine var1, Admin var2, Admin.StatusDetail var3) {
      super(var1, var2, Strings.get("GetEngineStatus.Label." + var3.name()));
      this.detail = var3;
      this.callback = NoReceiver.create();
      this.setIcon(AdminIcons.STATUS_16);
   }

   public void setCallback(Receiver<Admin.StatusDetail> var1) {
      this.callback = var1;
   }

   @Override
   protected void beforeCall(ServerEngine var1) {
      var1.setDetails("");
      this.callback.handle(this.detail);
   }

   @Override
   protected UiJob createJob(ServerEngine var1, Admin var2) {
      return new GetEngineStatusJob(var1, var2, this.detail);
   }
}
