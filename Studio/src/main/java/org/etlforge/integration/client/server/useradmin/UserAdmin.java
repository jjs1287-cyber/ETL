package org.etlforge.integration.client.server.useradmin;

import java.util.List;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public interface UserAdmin extends ServerCall {
   void getUsersAndGroups(GetUsersAndGroupsCallback var1, boolean var2);

   void setUsersAndGroups(List<User> var1, List<Group> var2, ServerGuid var3, SetUsersAndGroupsCallback var4);

   void getUserInfo(GetUserInfoCallback var1);

   void setUserInfo(User var1, SetUserInfoCallback var2);
}
