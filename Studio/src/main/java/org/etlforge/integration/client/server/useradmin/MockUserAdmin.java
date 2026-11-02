package org.etlforge.integration.client.server.useradmin;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.User;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class MockUserAdmin implements UserAdmin {
   private static MockUserAdmin.UserData data;

   @Override
   public void getUsersAndGroups(GetUsersAndGroupsCallback var1, boolean var2) {
      if (simulateWait(var1)) {
         MockUserAdmin.UserData var3 = getData();
         var1.gotUsersAndGroups(null, var3.getUsers(), var3.getGroups());
      }
   }

   @Override
   public void setUsersAndGroups(List<User> var1, List<Group> var2, ServerGuid var3, SetUsersAndGroupsCallback var4) {
      if (simulateWait(var4)) {
         MockUserAdmin.UserData var5 = getData();
         var5.update(var1, var2);
         var4.doneSettingUsersAndGroups();
      }
   }

   @Override
   public void getUserInfo(GetUserInfoCallback var1) {
      if (simulateWait(var1)) {
         var1.caught(new NotImplementedYetException());
      }
   }

   @Override
   public void setUserInfo(User var1, SetUserInfoCallback var2) {
      if (simulateWait(var2)) {
         var2.caught(new NotImplementedYetException());
      }
   }

   private static boolean simulateWait(ServerCallback var0) {
      try {
         Thread.sleep(2000L);
         return true;
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
         var0.cancelled();
         return false;
      }
   }

   private static synchronized MockUserAdmin.UserData getData() {
      if (data == null) {
         data = new MockUserAdmin.UserData();
      }

      return data;
   }

   private static class UserData {
      private List<User> users = Lists.newArrayList();
      private List<Group> groups = Lists.newArrayList();

      public UserData() {
         this.createDummyData();
      }

      public void update(List<User> var1, List<Group> var2) {
         this.users = Lists.newArrayList(var1);
         this.groups = Lists.newArrayList(var2);
      }

      public List<User> getUsers() {
         return Lists.newArrayList(this.users);
      }

      public List<Group> getGroups() {
         return Lists.newArrayList(this.groups);
      }

      private void createDummyData() {
         User var1 = new User("Admin") {
            @Override
            public boolean isAdmin() {
               return true;
            }
         };
         User var2 = new User("Torgil");
         User var3 = new User("Ludvig");
         Collections.addAll(this.users, var1, var2, var3);
         Group var4 = new Group(new GroupId("14ec289d-1c2d-486e-98fb-16d5edbc5fbb"), "Administrators");
         var4.addMember(var1);
         Group var5 = new Group("Users");
         var5.addMember(var2);
         var5.addMember(var3);
         Collections.addAll(this.groups, var4, var5);
      }
   }
}
