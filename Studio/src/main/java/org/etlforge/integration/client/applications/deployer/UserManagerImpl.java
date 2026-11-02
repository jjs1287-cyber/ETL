package org.etlforge.integration.client.applications.deployer;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.client.server.user.UpdateUserManagerCallback;
import org.etlforge.integration.client.server.user.UserManager;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public final class UserManagerImpl implements UserManager {
   @Override
   public ImmutableList<User> getAllUsers() {
      return ImmutableList.of();
   }

   @Override
   public ImmutableList<Group> getAllGroups() {
      return ImmutableList.of();
   }

   @Override
   public void updateUsersAndGroups(UserAdmin var1, boolean var2, UpdateUserManagerCallback var3) {
      var3.cancelled();
   }
}
