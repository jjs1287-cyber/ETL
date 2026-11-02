package org.etlforge.integration.client.server.user;

import java.util.Collections;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.useradmin.GetUsersAndGroupsCallback;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public final class UserAndGroupRetriever {
   private final UserAdmin userAdmin;
   private final UserAndGroupRetriever.UpdateHandler updateHandler;
   private final boolean includePasswords;
   private UpdateUserManagerCallback callback;

   public UserAndGroupRetriever(UserAdmin var1, UserAndGroupRetriever.UpdateHandler var2, boolean var3) {
      this.userAdmin = var1;
      this.updateHandler = var2;
      this.includePasswords = var3;
   }

   public void fetch(UpdateUserManagerCallback var1) {
      this.callback = var1;
      Application.getWorker().submitForParallel(new UserAndGroupRetriever.FetchJob());
   }

   private class FetchJob implements ApplicationWorker.SafeRunnable, GetUsersAndGroupsCallback {
      private FetchJob() {
      }

      @Override
      public void run() {
         UserAndGroupRetriever.this.userAdmin.getUsersAndGroups(this, UserAndGroupRetriever.this.includePasswords);
      }

      @Override
      public void gotUsersAndGroups(ServerInfo var1, List<User> var2, List<Group> var3) {
         UserAndGroupRetriever.this.updateHandler.updateCompleted(var2, var3);
         if (UserAndGroupRetriever.this.callback != null) {
            List var4 = Collections.unmodifiableList(var2);
            List var5 = Collections.unmodifiableList(var3);
            UserAndGroupRetriever.this.callback.updateCompleted(var1, var4, var5);
         }
      }

      @Override
      public void cancelled() {
         if (UserAndGroupRetriever.this.callback != null) {
            UserAndGroupRetriever.this.callback.cancelled();
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (UserAndGroupRetriever.this.callback != null) {
            UserAndGroupRetriever.this.callback.failed(var1);
         }
      }
   }

   public interface UpdateHandler {
      void updateCompleted(List<User> var1, List<Group> var2);
   }
}
