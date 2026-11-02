package org.etlforge.application.appdir;

import java.io.File;

public abstract class ApplicationDirectoryBase {
   private final File location;

   protected ApplicationDirectoryBase(File var1, String var2) throws AppDirInitializationException {
      this.location = new File(var1, var2);
      this.init(this.location);
      this.checkReadMeFile();
   }

   private void init(File var1) throws AppDirInitializationException {
      if (var1.exists()) {
         if (!var1.isDirectory()) {
            String var2 = "Failed to create the application directory at \"" + var1.getAbsolutePath() + "\". A file with this path already exists.";
            throw new AppDirInitializationException(var2);
         }
      } else if (!var1.mkdirs()) {
         String var3 = "Failed to create the application directory at \"" + var1.getAbsolutePath() + "\". Check that the current user has write access.";
         throw new AppDirInitializationException(var3);
      }
   }

   private void checkReadMeFile() {
      ReadMeGenerator.generate(this.location);
   }

   public final File getLocation() {
      return this.location;
   }
}
