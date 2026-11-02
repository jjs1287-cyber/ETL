package com.etlforge.integration.server.implementation.webservice.admin.server.impl;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.admin.server.MigrateUsersAndGroupsCallback;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerAddress;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.ui.login.LoginDialog;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UserAdmin;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UserAdminServiceLocator;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UserGroup;
import org.etlforge.integration.server.implementation.webservice.useradmin.client.UsersAndGroups;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.User;
import org.etlforge.integration.user.UserId;
import org.etlforge.ui.UiThreadSupplier;

final class ServerAdminCppWsImpl extends CppWebServiceServerCall {
   private String userName;
   private String password;
   private ServerInfo serverInfo;

   public ServerAdminCppWsImpl(CppWebServiceConfiguration var1) {
      super(var1);
   }

   public void migrateUsersAndGroups(List<User> var1, List<Group> var2, String var3, MigrateUsersAndGroupsCallback var4) {
      try {
         UserAdmin var5 = this.getUserAdmin(var3, var4);
         if (var5 != null) {
            UsersAndGroups var6 = this.getUsersAndGroupsToUpload(var1, var2);
            var5.setUsersAndGroups(this.userName, this.password, var6);
            var4.done(this.serverInfo);
            return;
         }
      } catch (RemoteException var11) {
         var4.caught(this.convert(var11));
         return;
      } catch (IntegrationServerException var12) {
         var4.caught(new IntegrationServerException("Server exception: " + var12.getMessage(), var12));
         return;
      } finally {
         this.getConfiguration().getServerManager().getLoginService().setNextServerToLoginTo(null);
      }
   }

   private UserAdmin getUserAdmin(String var1, ServerCallback var2) throws IntegrationServerException {
      LoginCredentials var3 = this.getCredentialsForDestinationServer(var1, var2);
      if (var3 == null) {
         return null;
      }

      this.userName = var3.getUser();
      this.password = var3.getPasswordHash();
      UserAdminServiceLocator var4 = new UserAdminServiceLocator();

      try {
         StringBuilder var5 = new StringBuilder();
         ServerAddress var6 = var3.getServerAddress();
         if (!this.getConfiguration().configureWebservice(var4, "konga_useradmin", var6.toString(), var5)) {
            return null;
         }

         this.serverInfo = this.getConfiguration().getServerInfo();
         var4.setkonga_useradminEndpointAddress(var5.toString());
         return var4.getkonga_useradmin();
      } catch (ServiceException var7) {
         throw new IntegrationServerException("Failed to call web service: " + var7.getMessage(), var7);
      }
   }

   private LoginCredentials getCredentialsForDestinationServer(String var1, ServerCallback var2) {
      this.getConfiguration().getServerManager().getLoginService().setNextServerToLoginTo(var1);

      try {
         return UiThreadSupplier.call(new Supplier<LoginCredentials>() {
            public LoginCredentials get() {
               LoginDialog var1x = new LoginDialog(ServerAdminCppWsImpl.this.getConfiguration().getServerManager());
               var1x.setVisible(true);
               return var1x.wasOkPressed() ? var1x.getCredentials() : null;
            }
         });
      } catch (Exception var7) {
         var2.caught(var7.getCause());
      } finally {
         this.getConfiguration().getServerManager().getLoginService().setNextServerToLoginTo(null);
      }

      return null;
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

      ArrayList var17 = new ArrayList();

      for (Group var8 : var2) {
         List var9 = var8.getMembers();
         if (!var9.isEmpty()) {
            ServerAdminCppWsImpl.GroupUsers var10 = new ServerAdminCppWsImpl.GroupUsers();
            var10.groupId = var8.getId();
            var10.userIds = Lists.newArrayList();

            for (Object __etlItem132 : var9) {
               User var12 = (User) __etlItem132;
               var10.userIds.add(var12.getId());
            }

            var17.add(var10);
         }
      }

      var3 = var17.size();
      UserGroup[] var19 = new UserGroup[var3];

      for (int var20 = 0; var20 < var3; var20++) {
         ServerAdminCppWsImpl.GroupUsers var21 = (ServerAdminCppWsImpl.GroupUsers)var17.get(var20);
         String var22 = var21.groupId.toString();
         String[] var23 = new String[var21.userIds.size()];

         for (int var24 = 0; var24 < var21.userIds.size(); var24++) {
            var23[var24] = var21.userIds.get(var24).toString();
         }

         var19[var20] = new UserGroup(var22, var23);
      }

      return new UsersAndGroups(var4, var15, var19);
   }

   private static class GroupUsers {
      public GroupId groupId;
      public List<UserId> userIds;

      private GroupUsers() {
      }
   }
}
