package org.etlforge.integration.client.server;

import org.etlforge.application.VersionFactory;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.info.Versions;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.UnsupportedVersionException;
import org.etlforge.util.version.Version;

class ServerVersionCompatibilityChecker {
   private static final Version FIRST_RELEASE = Versions.V_1_0_0;
   private final Version requiredVersion;

   public ServerVersionCompatibilityChecker(Version var1) {
      this.requiredVersion = var1;
   }

   void checkServerVersion(String var1, String var2) throws UnsupportedVersionException, IllegalVersionException {
      Version var3 = VersionFactory.fromString(var2);
      if (var3.isEarlierThan(FIRST_RELEASE)) {
         throwPre_1_0_Error(var1, var3);
      }

      if (var3.isEarlierThan(this.requiredVersion)) {
         String var4 = String.format(
            "The current version of the %s requires a minimum server version of %s. The server %s is running version %s. All connections are refused.",
            ApplicationConfiguration.getProductName(),
            this.requiredVersion,
            var1,
            var3
         );
         throw new UnsupportedVersionException(var3, var4);
      }
   }

   private static void throwPre_1_0_Error(String var0, Version var1) throws UnsupportedVersionException {
      String var2 = String.format(
         "%s is running a pre-release version of the ETLForge Integration Server. It is not compatible with this version of the client, and all connections are refused.\n\nServer version: %s\nClient version: %s",
         var0,
         var1,
         ClientInfo.getCurrentVersion()
      );
      throw new UnsupportedVersionException(var1, var2);
   }
}
