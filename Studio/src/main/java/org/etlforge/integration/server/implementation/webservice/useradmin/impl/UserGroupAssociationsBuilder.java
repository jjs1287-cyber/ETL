package org.etlforge.integration.server.implementation.webservice.useradmin.impl;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UserGroup;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UsersAndGroups;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.User;
import org.etlforge.integration.user.UserId;

final class UserGroupAssociationsBuilder {
   private final Map<UserId, User> users = Maps.newHashMap();
   private final Map<GroupId, Group> groups = Maps.newHashMap();

   public UserGroupAssociationsBuilder(List<User> var1, List<Group> var2) {
      for (User var4 : var1) {
         this.users.put(var4.getId(), var4);
      }

      for (Group var6 : var2) {
         this.groups.put(var6.getId(), var6);
      }
   }

   public void build(UsersAndGroups var1) {
      for (UserGroup var5 : var1.getUserGroups()) {
         Group var6 = this.groups.get(new GroupId(var5.getGroupId()));
         if (var6 != null) {
            this.addMembers(var5, var6);
         }
      }
   }

   private void addMembers(UserGroup var1, Group var2) {
      for (String var6 : var1.getUserIds()) {
         User var7 = this.users.get(new UserId(var6));
         if (var7 != null) {
            var2.addMember(var7);
         }
      }
   }
}
