package org.etlforge.integration.server.implementation.webservice.useradmin.impl;

import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.useradmin.GetUserInfoCallback;
import org.etlforge.integration.client.server.useradmin.GetUsersAndGroupsCallback;
import org.etlforge.integration.client.server.useradmin.SetUserInfoCallback;
import org.etlforge.integration.client.server.useradmin.SetUsersAndGroupsCallback;
import org.etlforge.integration.client.server.useradmin.UserAdmin;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.server.implementation.webservice.WebServiceMessages;
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UserAdminServiceLocator;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UserGroup;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UsersAndGroups;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.User;
import org.etlforge.integration.user.UserId;

public final class UserAdminWsImpl implements UserAdmin {
   private static final String SERVICE_NAME = "konga_useradmin";
   private final CppWebServiceConfiguration config;
   private final ServerManager serverManager;

   public UserAdminWsImpl(CppWebServiceConfiguration var1, ServerManager var2) {
      this.config = var1;
      this.serverManager = var2;
   }

   @Override
   public void getUsersAndGroups(GetUsersAndGroupsCallback var1, boolean var2) {
      UserAdminWsImpl.Impl var3 = new UserAdminWsImpl.Impl(this.config, this.serverManager);
      var3.getUsersAndGroups(var1, var2);
   }

   @Override
   public void setUsersAndGroups(List<User> var1, List<Group> var2, ServerGuid var3, SetUsersAndGroupsCallback var4) {
      UserAdminWsImpl.Impl var5 = new UserAdminWsImpl.Impl(this.config, this.serverManager);
      var5.setUsersAndGroups(var1, var2, var3, var4);
   }

   @Override
   public void getUserInfo(GetUserInfoCallback var1) {
      UserAdminWsImpl.Impl var2 = new UserAdminWsImpl.Impl(this.config, this.serverManager);
      var2.getUserInfo(var1);
   }

   @Override
   public void setUserInfo(User var1, SetUserInfoCallback var2) {
      UserAdminWsImpl.Impl var3 = new UserAdminWsImpl.Impl(this.config, this.serverManager);
      var3.setUserInfo(var1, var2);
   }

   private static class GroupUsers {
      public GroupId groupId;
      public List<UserId> userIds;

      private GroupUsers() {
      }
   }

   private static class Impl extends CppWebServiceServerCall {
      private final ServerManager serverManager;
      private String userName;
      private String password;
      private ServerInfo serverInfo;

      public Impl(CppWebServiceConfiguration var1, ServerManager var2) {
         super(var1);
         this.serverManager = var2;
      }

      public void setUsersAndGroups(List<User> var1, List<Group> var2, ServerGuid var3, SetUsersAndGroupsCallback var4) {
         try {
            org.etlforge.integration.server.implementation.webservice.useradmin.client.UserAdmin var5 = this.getUserAdmin(var4);
            if (var5 == null) {
               return;
            }

            if (!this.checkServer(var3)) {
               var4.cancelled();
               return;
            }

            UsersAndGroups var6 = this.getUsersAndGroupsToUpload(var1, var2);
            var5.setUsersAndGroups(this.userName, this.password, var6);
            var4.doneSettingUsersAndGroups();
         } catch (RemoteException var7) {
            var4.caught(this.convert(var7));
         } catch (IntegrationServerException var8) {
            var4.caught(new IntegrationServerException("Server exception: " + var8.getMessage(), var8));
         }
      }

      public void getUsersAndGroups(GetUsersAndGroupsCallback var1, boolean var2) {
         UsersAndGroups var3 = null;

         try {
            org.etlforge.integration.server.implementation.webservice.useradmin.client.UserAdmin var4 = this.getUserAdmin(var1);
            if (var4 == null) {
               return;
            }

            WsKeyValue[] var5 = new WsKeyValue[]{new WsKeyValue("IncludePasswords", var2 ? "1" : "0")};
            WsKeyValuePairs var6 = new WsKeyValuePairs(var5);
            var3 = var4.getUsersAndGroupsWithOptions(this.userName, this.password, var6);
         } catch (RemoteException var7) {
            var1.caught(this.convert(var7));
            return;
         } catch (IntegrationServerException var8) {
            var1.caught(new IntegrationServerException("Server exception: " + var8.getMessage(), var8));
            return;
         }

         List var10 = this.createUsers(var3);
         List var11 = this.createGroups(var3);
         this.addGroupMembers(var3, var10, var11);
         var1.gotUsersAndGroups(this.serverInfo, var10, var11);
      }

      public void getUserInfo(GetUserInfoCallback var1) {
         try {
            org.etlforge.integration.server.implementation.webservice.useradmin.client.UserAdmin var2 = this.getUserAdmin(var1);
            if (var2 == null) {
               return;
            }

            org.etlforge.integration.server.implementation.webservice.useradmin.client.User var3 = var2.getUserInfo(this.userName, this.password);
            var1.gotUserInfo(this.getIntegrationUser(var3));
         } catch (RemoteException var4) {
            var1.caught(this.convert(var4));
         } catch (IntegrationServerException var5) {
            var1.caught(new IntegrationServerException("Server exception: " + var5.getMessage(), var5));
         }
      }

      public void setUserInfo(User var1, SetUserInfoCallback var2) {
         org.etlforge.integration.server.implementation.webservice.useradmin.client.User var3 = new org.etlforge.integration.server.implementation.webservice.useradmin.client.User();
         var3.setUserId(var1.getId().toString());
         var3.setLogin(var1.getUserName());
         var3.setFirstName(var1.getFirstName());
         var3.setLastName(var1.getLastName());
         var3.setEmail(var1.getEmail());
         var3.setPassword(var1.getPasswordHash());

         try {
            org.etlforge.integration.server.implementation.webservice.useradmin.client.UserAdmin var4 = this.getUserAdmin(var2);
            if (var4 == null) {
               return;
            }

            var4.setUserInfo(this.userName, this.password, var3);
            var2.userInfoSet(var1);
         } catch (RemoteException var5) {
            var2.caught(this.convert(var5));
         } catch (IntegrationServerException var6) {
            var2.caught(new IntegrationServerException("Server exception: " + var6.getMessage(), var6));
         }
      }

      private org.etlforge.integration.server.implementation.webservice.useradmin.client.UserAdmin getUserAdmin(ServerCallback var1) throws IntegrationServerException {
         UserAdminServiceLocator var2 = new UserAdminServiceLocator();

         try {
            StringBuilder var3 = new StringBuilder();
            if (!this.getConfiguration().configureWebservice(var2, "konga_useradmin", var3, var1)) {
               return null;
            }

            this.userName = this.getConfiguration().getUserName();
            this.password = this.getConfiguration().getPasswordHash();
            this.serverInfo = this.getConfiguration().getServerInfo();
            var2.setkonga_useradminEndpointAddress(var3.toString());
            return var2.getkonga_useradmin();
         } catch (ServiceException var4) {
            throw new IntegrationServerException("Failed to call web service: " + var4.getMessage(), var4);
         }
      }

      private User getIntegrationUser(org.etlforge.integration.server.implementation.webservice.useradmin.client.User var1) {
         UserId var2 = new UserId(var1.getUserId());
         String var3 = var1.getLogin();
         User var4 = new User(var2, var3);
         var4.setFirstName(var1.getFirstName());
         var4.setLastName(var1.getLastName());
         var4.setEmail(var1.getEmail());
         var4.setPasswordHash(var1.getPassword());
         return var4;
      }

      private Group getIntegrationGroup(org.etlforge.integration.server.implementation.webservice.useradmin.client.Group var1) {
         GroupId var2 = new GroupId(var1.getGroupId());
         String var3 = var1.getGroupName();
         Group var4 = new Group(var2, var3);
         var4.setDescription(var1.getDescription());
         return var4;
      }

      private void addGroupMembers(UsersAndGroups var1, List<User> var2, List<Group> var3) {
         new UserGroupAssociationsBuilder(var2, var3).build(var1);
      }

      private List<Group> createGroups(UsersAndGroups var1) {
         ArrayList var2 = Lists.newArrayList();

         for (org.etlforge.integration.server.implementation.webservice.useradmin.client.Group var6 : var1.getGroups()) {
            var2.add(this.getIntegrationGroup(var6));
         }

         return var2;
      }

      private List<User> createUsers(UsersAndGroups var1) {
         ArrayList var2 = Lists.newArrayList();

         for (org.etlforge.integration.server.implementation.webservice.useradmin.client.User var6 : var1.getUsers()) {
            var2.add(this.getIntegrationUser(var6));
         }

         return var2;
      }

      private boolean checkServer(ServerGuid var1) {
         if (var1 != null && !var1.equals(this.serverInfo.getGuid())) {
            String var2 = WebServiceMessages.getMessage("UserAdmin.Upload.ServerMismatch");
            KnownServer var3 = this.serverManager.getKnownServer(var1);
            if (var3 != null) {
               var2 = var2 + " " + WebServiceMessages.getFormattedMessage("UserAdmin.Upload.Login", new Object[]{var3.getServerInfo().getName()});
            }

            Alert.error(var2, WebServiceMessages.getMessage("Title.Error"));
            return false;
         } else {
            return true;
         }
      }

      private UsersAndGroups getUsersAndGroupsToUpload(List<User> var1, List<Group> var2) {
         int var3 = var1.size();
         org.etlforge.integration.server.implementation.webservice.useradmin.client.User[] var4 = new org.etlforge.integration.server.implementation.webservice.useradmin.client.User[var3];

         for (int var5 = 0; var5 < var3; var5++) {
            User var6 = (User)var1.get(var5);
            var4[var5] = new org.etlforge.integration.server.implementation.webservice.useradmin.client.User(
               var6.getId().toString(), var6.getFirstName(), var6.getLastName(), var6.getUserName(), var6.getPasswordHash(), var6.getEmail()
            );
         }

         var3 = var2.size();
         org.etlforge.integration.server.implementation.webservice.useradmin.client.Group[] var15 = new org.etlforge.integration.server.implementation.webservice.useradmin.client.Group[var3];

         for (int var16 = 0; var16 < var3; var16++) {
            Group var7 = (Group)var2.get(var16);
            var15[var16] = new org.etlforge.integration.server.implementation.webservice.useradmin.client.Group(
               var7.getId().toString(), var7.getName(), var7.getDescription()
            );
         }

         ArrayList var17 = Lists.newArrayList();

         for (Group var8 : var2) {
            List var9 = var8.getMembers();
            if (!var9.isEmpty()) {
               UserAdminWsImpl.GroupUsers var10 = new UserAdminWsImpl.GroupUsers();
               var10.groupId = var8.getId();
               var10.userIds = Lists.newArrayList();

               for (Object __etlItem281 : var9) {
                  User var12 = (User) __etlItem281;
                  var10.userIds.add(var12.getId());
               }

               var17.add(var10);
            }
         }

         var3 = var17.size();
         UserGroup[] var19 = new UserGroup[var3];

         for (int var20 = 0; var20 < var3; var20++) {
            UserAdminWsImpl.GroupUsers var21 = (UserAdminWsImpl.GroupUsers)var17.get(var20);
            String var22 = var21.groupId.toString();
            String[] var23 = new String[var21.userIds.size()];

            for (int var24 = 0; var24 < var21.userIds.size(); var24++) {
               var23[var24] = var21.userIds.get(var24).toString();
            }

            var19[var20] = new UserGroup(var22, var23);
         }

         return new UsersAndGroups(var4, var15, var19);
      }
   }
}
