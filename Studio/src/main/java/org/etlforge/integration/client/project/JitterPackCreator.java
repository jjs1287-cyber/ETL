package org.etlforge.integration.client.project;

import java.io.File;
import java.util.Date;
import org.etlforge.integration.client.project.jitterpack.ExportParameters;
import org.etlforge.integration.client.project.jitterpack.Exporter;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.jitterpack.JitterPackException;
import org.etlforge.integration.client.project.jitterpack.PackingFileFilter;
import org.etlforge.util.version.UnsupportedVersionException;

final class JitterPackCreator {
   private final ProjectLoaderWorker projectLoader;
   private final Exporter exporter;

   public JitterPackCreator(ProjectLoaderWorker var1, Exporter var2) {
      this.projectLoader = var1;
      this.exporter = var2;
   }

   public JitterPack createJitterPack(String var1, File var2, File var3, boolean var4) throws InterchangeLoadingException, UnsupportedVersionException, JitterPackException {
      ManagedProjectImpl var5 = this.projectLoader.loadProjectSilently(var1, var2);

      try {
         String var6 = "Backup copy, created at " + new Date() + ".";
         ExportParameters var7 = this.getExportParameters(var2, var4);
         return this.exporter.pack(var5, var6, var3, var7);
      } finally {
         var5.close();
      }
   }

   private ExportParameters getExportParameters(File var1, boolean var2) {
      JitterPackCreator.PackingFileFilterImpl var3 = new JitterPackCreator.PackingFileFilterImpl(var1, var2);
      return new ExportParameters(var3, null, false, false);
   }

   private static class PackingFileFilterImpl implements PackingFileFilter {
      private final File projectFolder;
      private final boolean includeClientSpecificData;

      public PackingFileFilterImpl(File var1, boolean var2) {
         this.projectFolder = var1;
         this.includeClientSpecificData = var2;
      }

      @Override
      public PackingFileFilter.Result evaluateDirectory(File var1) {
         if (this.includeClientSpecificData) {
            return PackingFileFilter.Result.INCLUDE;
         }

         File var2 = ProjectFolders.getPersistFolder(this.projectFolder).asJavaFile();
         return var2.equals(var1) ? PackingFileFilter.Result.EXCLUDE : PackingFileFilter.Result.INCLUDE;
      }

      @Override
      public PackingFileFilter.Result evaluateFile(File var1) {
         return PackingFileFilter.Result.INCLUDE;
      }
   }
}
