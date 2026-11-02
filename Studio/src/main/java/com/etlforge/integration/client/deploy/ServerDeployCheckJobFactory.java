package com.etlforge.integration.client.deploy;

import com.etlforge.integration.client.deploy.api.ServerDeployCheckResult;
import com.etlforge.integration.client.deploy.ui.OverwritingOtherUsersEditsWarning;
import com.etlforge.integration.client.deploy.ui.SharedItemsWarning;
import com.etlforge.integration.client.deploy.ui.prefs.WarnBeforeOverwritingOtherUsersEditsPreference;
import com.etlforge.integration.client.deploy.ui.prefs.WarnWhenDeployingSharedItemsPreference;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.project.deploy.PreDeployJob;
import org.etlforge.integration.client.project.deploy.PreDeployJobFactory;
import org.etlforge.integration.client.project.deploy.PreDeployStatus;
import org.etlforge.system.callback.Receiver;

public final class ServerDeployCheckJobFactory implements PreDeployJobFactory {
   @Override
   public PreDeployJob getJob(DeployData var1) {
      return new ServerDeployCheckJobFactory.JobImpl();
   }

   private static final class JobImpl implements PreDeployJob {
      private JobImpl() {
      }

      @Override
      public PreDeployStatus run(DeployData var1) {
         return this.isCheckNecessary() ? this.doCheck(var1) : PreDeployStatus.CONTINUE;
      }

      private boolean isCheckNecessary() {
         return WarnBeforeOverwritingOtherUsersEditsPreference.get() || WarnWhenDeployingSharedItemsPreference.get();
      }

      private PreDeployStatus doCheck(DeployData var1) {
         ServerDeployCheckJobFactory.JobImpl.ResultHandler var2 = new ServerDeployCheckJobFactory.JobImpl.ResultHandler(var1);
         RunServerDeployCheckJob var3 = new RunServerDeployCheckJob(var1, var2);
         var3.submit();
         return var2.getStatus();
      }

      private static final class ResultHandler implements Receiver<ServerDeployCheckResult> {
         private final DeployData data;
         private PreDeployStatus status;
         private final CountDownLatch latch;

         public ResultHandler(DeployData var1) {
            this.data = var1;
            this.status = PreDeployStatus.CONTINUE;
            this.latch = new CountDownLatch(1);
         }

         public void handle(ServerDeployCheckResult var1) {
            try {
               this.status = this.checkResult(var1);
            } catch (Exception var6) {
               var6.printStackTrace();
            } finally {
               this.latch.countDown();
            }
         }

         public PreDeployStatus getStatus() {
            try {
               this.latch.await();
               return this.status;
            } catch (InterruptedException var2) {
               Thread.currentThread().interrupt();
               return PreDeployStatus.ABORT;
            }
         }

         private PreDeployStatus checkResult(ServerDeployCheckResult var1) {
            if (var1 == null) {
               return PreDeployStatus.ABORT;
            } else {
               return var1.isEmpty() ? PreDeployStatus.CONTINUE : this.askTheUser(var1);
            }
         }

         private PreDeployStatus askTheUser(ServerDeployCheckResult var1) {
            PreDeployStatus var2 = this.checkOtherUsersEdits(var1);
            if (var2 == PreDeployStatus.CONTINUE) {
               var2 = this.checkSharedItems(var1);
            }

            return var2;
         }

         private PreDeployStatus checkOtherUsersEdits(ServerDeployCheckResult var1) {
            List var2 = var1.getLastUpdatedInfo();
            if (!var2.isEmpty()) {
               OverwritingOtherUsersEditsWarning var3 = new OverwritingOtherUsersEditsWarning(this.data.getProject(), var2);
               if (!var3.isGreenlighted()) {
                  return PreDeployStatus.ABORT;
               }
            }

            return PreDeployStatus.CONTINUE;
         }

         private PreDeployStatus checkSharedItems(ServerDeployCheckResult var1) {
            List var2 = var1.getSharedItems();
            if (!var2.isEmpty()) {
               SharedItemsWarning var3 = new SharedItemsWarning(this.data.getProject(), var2);
               if (!var3.isGreenlighted()) {
                  return PreDeployStatus.ABORT;
               }
            }

            return PreDeployStatus.CONTINUE;
         }
      }
   }
}
