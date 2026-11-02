package org.etlforge.integration.client.runtime.appdir;

import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;

final class PackageResources {
   private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org.etlforge.integration.client.runtime.appdir.resources.AppDirResources");

   private PackageResources() {
   }

   static String getCreateSubDirFailed(File var0, String var1) {
      return getFormattedMessage("Error.CreateSubFolder", new Object[]{var0.getAbsolutePath(), var1});
   }

   static String getString(String var0) {
      return BUNDLE.getString(var0);
   }

   static String getFormattedMessage(String var0, Object[] var1) {
      return MessageFormat.format(getString(var0), var1);
   }

   static class ProjectDir {
      static final String NEW_PROJECT_NAME_BASE = PackageResources.getString("ProjectDir.NewProjectNameBase");

      static String getNewProjectName(int var0) {
         return var0 == 0 ? NEW_PROJECT_NAME_BASE : PackageResources.getFormattedMessage("ProjectDir.NewProjectName", new Object[]{var0});
      }
   }

   static class Reasons {
      static final String PATH_IS_FILE = PackageResources.getString("Reason.PathIsFile");
      static final String CREATE_DIR_FAILED = PackageResources.getString("Reason.CreateDirectoryFailed");
   }

   static class ServerDir {
      static class Errors {
         static final String SERVER_REGISTRATION_FAILED = PackageResources.getString("ServerDir.Error.Registration");
      }
   }
}
