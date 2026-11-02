package org.etlforge.integration.client.server;

import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.server.user.UpdateUserManagerCallback;
import org.etlforge.integration.client.server.user.UserManager;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public final class UpdateUsersAndGroupsJob {
   private final UserManager userManager;
   private final UserAdmin serverApi;

   public UpdateUsersAndGroupsJob(UserManager var1, UserAdmin var2) {
      this.userManager = var1;
      this.serverApi = var2;
   }

   public void execute() {
      this.userManager.updateUsersAndGroups(this.serverApi, false, new UpdateUsersAndGroupsJob.CallbackImpl());
   }

   private static class CallbackImpl implements UpdateUserManagerCallback {
      private CallbackImpl() {
      }

      @Override
      public void failed(Throwable var1) {
         ErrorLog.log(UpdateUsersAndGroupsJob.class, ServerMessages.getString("UpdateUsersAndGroupsJob.Error"), var1);
      }

      @Override
      public void updateCompleted(ServerInfo var1, List<User> var2, List<Group> var3) {
      }

      @Override
      public void cancelled() {
      }
   }
}
