package org.etlforge.integration.client.server.user;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public interface UserManager {
   ImmutableList<User> getAllUsers();

   ImmutableList<Group> getAllGroups();

   void updateUsersAndGroups(UserAdmin var1, boolean var2, UpdateUserManagerCallback var3);
}
