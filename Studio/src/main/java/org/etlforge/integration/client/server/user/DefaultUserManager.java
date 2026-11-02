package org.etlforge.integration.client.server.user;

import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public final class DefaultUserManager implements UserManager {
   private ImmutableList<User> users = ImmutableList.of();
   private ImmutableList<Group> groups = ImmutableList.of();
   private final Object lock = new Object();

   @Override
   public ImmutableList<User> getAllUsers() {
      synchronized (this.lock) {
         return this.users;
      }
   }

   @Override
   public ImmutableList<Group> getAllGroups() {
      synchronized (this.lock) {
         return this.groups;
      }
   }

   @Override
   public void updateUsersAndGroups(UserAdmin var1, boolean var2, UpdateUserManagerCallback var3) {
      UserAndGroupRetriever var4 = new UserAndGroupRetriever(var1, new DefaultUserManager.UpdateCallback(), var2);
      var4.fetch(var3);
   }

   private class UpdateCallback implements UserAndGroupRetriever.UpdateHandler {
      private UpdateCallback() {
      }

      @Override
      public void updateCompleted(List<User> var1, List<Group> var2) {
         synchronized (DefaultUserManager.this.lock) {
            DefaultUserManager.this.users = ImmutableList.copyOf(var1);
            DefaultUserManager.this.groups = ImmutableList.copyOf(var2);
         }
      }
   }
}
