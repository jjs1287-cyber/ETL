package org.etlforge.integration.client.project;

import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.info.ProductNames;
import org.etlforge.integration.client.info.Versions;
import org.etlforge.util.version.UnsupportedVersionException;
import org.etlforge.util.version.Version;

final class ProjectVersionCompatibilityChecker {
   static void checkProjectVersion(String var0, ProjectFile var1) throws UnsupportedVersionException {
      checkRequiredVersion(var0, var1);
      checkPreReleaseVersion(var0, var1);
   }

   private static void checkRequiredVersion(String var0, ProjectFile var1) throws UnsupportedVersionException {
      Version var2 = var1.getMinimumVersionRequired();
      if (var2.isLaterThan(ClientInfo.getCurrentVersion())) {
         String var3 = "The project \""
            + var0
            + "\" requires version "
            + var2
            + " or later of "
            + ProductNames.CLIENT
            + ". You are currently using version "
            + ClientInfo.getCurrentVersion()
            + ".";
         throw new UnsupportedVersionException(var2, var3);
      }
   }

   private static void checkPreReleaseVersion(String var0, ProjectFile var1) throws UnsupportedVersionException {
      Version var2 = var1.getVersionCreatedIn();
      if (var2.isEarlierThan(Versions.V_1_0_0)) {
         String var3 = "'"
            + var0
            + "' was created with a pre-release version of the "
            + ProductNames.CLIENT
            + ". It cannot be opened with this version.\n\nYou can try to open the project again, in a later release, since more backward compatibility may be added.";
         throw new UnsupportedVersionException(var2, var3);
      }
   }

   private ProjectVersionCompatibilityChecker() {
   }
}
