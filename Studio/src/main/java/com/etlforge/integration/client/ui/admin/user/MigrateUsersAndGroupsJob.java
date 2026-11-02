package com.etlforge.integration.client.ui.admin.user;

import com.etlforge.integration.client.admin.server.MigrateUsersAndGroupsCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

final class MigrateUsersAndGroupsJob extends UiJob {
   private final ServerAdministration serverApi;
   private final List<User> users;
   private final List<Group> groups;
   private final String destinationServer;
   private final MigrateUsersAndGroupsCallback callback;
   private final CountDownLatch latch;
   private boolean success;

   public MigrateUsersAndGroupsJob(ServerAdministration var1, List<User> var2, List<Group> var3, String var4) {
      super(EnterpriseAdminResources.getString("MigrateUsersAndGroupsJob.Name"));
      this.serverApi = var1;
      this.users = var2;
      this.groups = var3;
      this.destinationServer = var4;
      this.callback = new MigrateUsersAndGroupsJob.CallbackImpl();
      this.latch = new CountDownLatch(1);
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.callback);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.serverApi.migrateUsersAndGroups(this.users, this.groups, this.destinationServer, this.callback);
   }

   public boolean waitForSuccess() {
      try {
         this.latch.await();
         return this.success;
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         return false;
      }
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private class CallbackImpl implements MigrateUsersAndGroupsCallback {
      private CallbackImpl() {
      }

      @Override
      public void done(ServerInfo var1) {
         MigrateUsersAndGroupsJob.this.success = true;
         MigrateUsersAndGroupsJob.this.latch.countDown();
      }

      @Override
      public void cancelled() {
         MigrateUsersAndGroupsJob.this.latch.countDown();
      }

      @Override
      public void caught(Throwable var1) {
         MigrateUsersAndGroupsJob.this.latch.countDown();
         ErrorLog.attention(
            MigrateUsersAndGroupsJob.class, EnterpriseAdminResources.getString("MigrateUsersAndGroupsJob.Error") + ": " + var1.getMessage(), var1
         );
      }
   }
}
