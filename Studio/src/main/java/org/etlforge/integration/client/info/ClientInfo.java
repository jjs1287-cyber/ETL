package org.etlforge.integration.client.info;

import com.google.common.base.Supplier;
import java.util.Date;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.update.BranchId;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.version.Version;

public final class ClientInfo {
   public static final String DATA_LOADER_APPDIR_NAME = ".etlforge_dataloader";

   public static Version getCurrentVersion() {
      return BuildProperties.getCurrentVersion();
   }

   public static Date getBuildTime() {
      return BuildProperties.getBuildTime();
   }

   public static boolean isDataLoader() {
                                                                                             
      return false;
   }

   public static String getDataLoaderServer() {
      return ApplicationConfiguration.getString("DataLoader.Server", "localhost");
   }

   public static Supplier<BranchId> getBranchIdSupplierForUpdateService() {
      return new Supplier<BranchId>() {
         public BranchId get() {
            String var1 = ApplicationConfiguration.getString("UpdateService.AppID", null);
            var1 = ClientInfo.getPlatformDependentAppIdForUpdateService(var1);
            return new BranchId(var1, ClientInfo.getCurrentVersion());
         }
      };
   }

   public static String getPlatformDependentAppIdForUpdateService(String var0) {
      String var1;
      if (SystemUtils.IS_OS_WINDOWS) {
         var1 = "windows";
      } else if (SystemUtils.IS_OS_MAC) {
         var1 = "mac";
      } else {
         if (!SystemUtils.IS_OS_LINUX) {
            throw new RuntimeException("Unexpected platform: " + SystemUtils.OS_NAME);
         }

         var1 = "linux";
      }

      return var0 + "." + var1;
   }

   public static ClientEdition getEdition() {
      return EnumWithIdLookup.lookupOr(ApplicationConfiguration.getString("Edition", ""), ClientEdition.UNKNOWN);
   }

   private ClientInfo() {
   }

   static {
      BuildProperties.init();
   }
}
