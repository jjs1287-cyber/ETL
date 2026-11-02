package org.etlforge.integration.client.ui.admin.engine;

import java.awt.event.ActionEvent;
import java.util.EnumSet;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.ServerStatus;
import org.etlforge.integration.client.admin.engine.ServerEngine;
import org.etlforge.ui.wait.WaitService;

public abstract class EngineControlAction extends AbstractAction {
   private final ServerEngine engine;
   private final Admin serverApi;
   private WaitService waitService;

   public EngineControlAction(ServerEngine var1, Admin var2, String var3) {
      super(var3);
      this.engine = var1;
      this.serverApi = var2;
   }

   public final void setIcon(Icon var1) {
      this.putValue("SmallIcon", var1);
   }

   public final void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public final void actionPerformed(ActionEvent var1) {
      this.beforeCall(this.engine);
      UiJob var2 = this.createJob(this.engine, this.serverApi);
      this.runJob(var2);
   }

   protected void beforeCall(ServerEngine var1) {
   }

   protected abstract UiJob createJob(ServerEngine var1, Admin var2);

   private void runJob(UiJob var1) {
      if (this.waitService != null) {
         var1.waitWhileRunning(this.waitService, 125);
      }

      var1.submit();
   }

   protected void updateAccordingToCurrentEngineStatus(EnumSet<ServerStatus> var1) {
   }
}
