package org.etlforge.integration.server;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.server.accesscontrol.AccessController;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.accesscontrol.User;
import org.etlforge.integration.server.db.ServerDbInfo;
import org.etlforge.integration.server.db.trandb.AccessControllerFactory;
import org.etlforge.integration.server.db.trandb.TranDb;
import org.etlforge.integration.server.db.trandb.UsersTab;
import org.etlforge.integration.server.idmappings.GuidToIdDbMapper;
import org.etlforge.integration.server.idmappings.GuidToIdMapper;
import org.etlforge.system.Disposable;

public final class ExternalApiSupport implements Disposable {
   private Connection tranDbConn;
   private final User user;
   private final String md5Pwd;
   private final boolean admin;
   private AccessController ac;

   public static ExternalApiSupport forCall(String var0, String var1) throws ServerAccessException {
      Connection var2 = openConnectionToBackend();
      boolean var3 = false;

      try {
         UsersTab var4 = new UsersTab(var2);
         User var5 = var4.getUser(var0, var1);
         if (var5 == null) {
            throw new ServerAccessException("Invalid user name and/or password.");
         }

         boolean var6 = TranDb.isValidAdminUser(var2, var5.getName(), var1);
         ExternalApiSupport var7 = new ExternalApiSupport(var2, var5, var1, var6);
         var3 = true;
         return var7;
      } catch (SQLException var12) {
         throw new ServerAccessException("Failed to validate the connecting user.", var12);
      } finally {
         if (!var3) {
            KongaDbUtils.close(var2);
         }
      }
   }

   private static Connection openConnectionToBackend() throws ServerAccessException {
      try {
         return ServerDbInfo.getTranDbConnectionInfo().getConnection();
      } catch (SQLException var1) {
         throw new ServerAccessException("Failed to connect to the server database.", var1);
      }
   }

   public static void safeDispose(ExternalApiSupport var0) {
      if (var0 != null) {
         var0.dispose();
      }
   }

   private ExternalApiSupport(Connection var1, User var2, String var3, boolean var4) {
      this.tranDbConn = var1;
      this.user = var2;
      this.md5Pwd = var3;
      this.admin = var4;
   }

   public User getUser() {
      return this.user;
   }

   public boolean isAdmin() {
      return this.admin;
   }

   public boolean isSameCredentials(String var1, String var2) {
      return this.user.getName().equalsIgnoreCase(var1) && StringUtils.equals(this.md5Pwd, var2);
   }

   public synchronized AccessController getAccessController() throws ServerAccessException {
      if (this.ac == null) {
         if (this.tranDbConn == null) {
            this.tranDbConn = openConnectionToBackend();
         }

         this.ac = new AccessControllerFactory(this.tranDbConn).newController();
      }

      return this.ac;
   }

   public synchronized void closeConnectionToBackendDatabase() {
      KongaDbUtils.close(this.tranDbConn);
      this.tranDbConn = null;
      this.ac = null;
   }

   public boolean hasPermission(IntegrationEntityId var1, Permission var2) throws ServerAccessException {
      return this.admin || this.getAccessController().hasPermission(this.user, var1, var2);
   }

   public void verifyPermission(IntegrationEntityId var1, Permission var2) throws ServerAccessException {
      if (!this.hasPermission(var1, var2)) {
         throw new ServerAccessException("The user " + this.user.getName() + " does not have " + var2 + " permission to the" + var1.getEntityType());
      }
   }

   public synchronized Connection getTranDbConnection() throws ServerAccessException {
      if (this.tranDbConn == null) {
         this.tranDbConn = openConnectionToBackend();
      }

      return this.tranDbConn;
   }

   public synchronized GuidToIdMapper getIdMapper() throws ServerAccessException {
      return new GuidToIdDbMapper(this.getTranDbConnection());
   }

   @Override
   public void dispose() {
      this.closeConnectionToBackendDatabase();
   }
}
