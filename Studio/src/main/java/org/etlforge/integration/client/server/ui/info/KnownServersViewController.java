package org.etlforge.integration.client.server.ui.info;

import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInformationStore;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.server.ui.login.LoginServiceUi;
import org.etlforge.system.callback.Receiver;

public final class KnownServersViewController {
   private final ServerManager serverManager;
   private final KnownServersViewModel model;

   public KnownServersViewController(ServerManager var1, KnownServersViewModel var2) {
      this.serverManager = var1;
      this.model = var2;
   }

   public void updateListOfServers() {
      Computation var1 = this.createUpdateListOfServersJob();
      this.runJobInBackground(var1);
   }

   private void runJobInBackground(Computation<?> var1) {
      var1.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, this.getClass()).buildAndExecute();
   }

   private Computation<List<KnownServer>> createUpdateListOfServersJob() {
      Callable var1 = new Callable<List<KnownServer>>() {
         public List<KnownServer> call() throws Exception {
            return KnownServersViewController.this.serverManager.getRegisteredServers();
         }
      };
      Receiver var2 = new Receiver<List<KnownServer>>() {
         public void handle(List<KnownServer> var1) {
            KnownServersViewController.this.model.setKnownServers(var1);
         }
      };
      return Computation.create(var1, var2);
   }

   public void removeServers(List<KnownServer> var1) {
      Computation var2 = this.createRemoveServersJob(var1);
      this.runJobInBackground(var2);
   }

   private Computation<Void> createRemoveServersJob(final List<KnownServer> var1) {
      Callable var2 = new Callable<Void>() {
         public Void call() throws Exception {
            ServerInformationStore var1x = KnownServersViewController.this.serverManager.getServerInfoStore();

            for (KnownServer var3 : var1) {
               var1x.removeServerRegistration(var3.getServerInfo().getGuid());
            }

            return null;
         }
      };
      Receiver var3 = new Receiver<Void>() {
         public void handle(Void var1x) {
            KnownServersViewController.this.model.removeServers(var1);
         }
      };
      return Computation.create(var2, var3);
   }

   public void login(KnownServer var1) {
      LoginService var2 = this.serverManager.getLoginService();
      var2.setNextServerToLoginTo(var1.getServerInfo().getName());
      LoginServiceUi var3 = new LoginServiceUi(this.serverManager);
      var3.login(null);
   }
}
