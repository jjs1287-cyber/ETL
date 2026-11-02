package org.etlforge.integration.client.server;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.log.ApplicationLog;
import org.etlforge.integration.common.server.ServerGuid;

public class DefaultServerDirectory implements ServerDirectory {
   public static final String NAME = "servers";
   private final File location;
   private ServerInformationStore infoStore;

   public DefaultServerDirectory(File var1) {
      this.location = new File(var1, "servers");
   }

   @Override
   public final File getLocation() {
      return this.location;
   }

   @Override
   public final synchronized ServerInformationStore getInformationStore() {
      if (this.infoStore == null) {
         this.infoStore = new DefaultServerDirectory.ServerInfoStore();
      }

      return this.infoStore;
   }

   @Override
   public final File getLocation(ServerGuid var1) {
      return new File(this.getLocation(), var1.toString());
   }

   private void registerServer(ServerInfo var1, String var2, String var3, boolean var4) throws Exception {
      File var5 = this.getFolderForServer(var1.getGuid());
      if (var5.exists()) {
         KnownServerFileStorage var6 = new KnownServerFileStorage(var5, var1);
         var6.updateOnLogin(var2, var3, var4);
      }
   }

   private void removeDuplicateServers(ServerInfo var1) {
      String var2 = var1.getName();
      ServerGuid var3 = var1.getGuid();
      ServerInformationStore var4 = this.getInformationStore();

      for (KnownServer var6 : var4.getRegisteredServers()) {
         ServerInfo var7 = var6.getServerInfo();
         if (var7.getName().equals(var2) && !var7.getGuid().equals(var3)) {
            var4.removeServerRegistration(var7.getGuid());
         }
      }
   }

   protected File getFolderForServer(ServerGuid var1) throws Exception {
      File var2 = new File(this.getLocation(), var1.toString());
      if (var2.exists()) {
         if (!var2.isDirectory()) {
            throw new Exception("Failed to created the requested ServerDirectory: the path \"" + var2.getAbsolutePath() + "\" is a file.");
         }
      } else if (!var2.mkdirs()) {
         throw new Exception("Failed to created the requested ServerDirectory: the path \"" + var2.getAbsolutePath() + "\" could not be created on disk.");
      }

      return var2;
   }

   private static final class ServerDirectoryFilter implements FileFilter {
      private ServerDirectoryFilter() {
      }

      @Override
      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            try {
               new ServerGuid(var1.getName());
               return true;
            } catch (IllegalArgumentException var3) {
            }
         }

         return false;
      }
   }

   private class ServerInfoStore implements ServerInformationStore {
      private ServerInfoStore() {
      }

      @Override
      public List<KnownServer> getRegisteredServers() {
         return this.getRegisteredServersImpl();
      }

      private List<KnownServer> getRegisteredServersImpl() {
         ArrayList var1 = new ArrayList();
         File[] var2 = DefaultServerDirectory.this.getLocation().listFiles(new DefaultServerDirectory.ServerDirectoryFilter());

         for (File var6 : var2) {
            KnownServerFileStorage var7 = new KnownServerFileStorage(var6);
            var1.add(var7.buildKnownServer());
         }

         return var1;
      }

      @Override
      public boolean removeServerRegistration(ServerGuid var1) {
         try {
            File var2 = DefaultServerDirectory.this.getFolderForServer(var1);
            FileUtils.deleteDirectory(var2);
            return true;
         } catch (Exception var3) {
            this.logFailedAttemptToRemoveServer(var1, var3);
            return false;
         }
      }

      private void logFailedAttemptToRemoveServer(ServerGuid var1, Exception var2) {
         ApplicationLog.getMainLogger().warning("Failed to remove the server " + var1 + " from the ServerDirectory: " + var2.getMessage());
         var2.printStackTrace();
      }

      @Override
      public void storeNewLogin(ServerInfo var1, String var2, String var3, boolean var4) {
         try {
            DefaultServerDirectory.this.registerServer(var1, var2, var3, var4);
            DefaultServerDirectory.this.removeDuplicateServers(var1);
         } catch (Exception var6) {
            ErrorLog.log(this, "Failed to register the server " + var1.getName() + " with the client.", var6);
         }
      }
   }
}
