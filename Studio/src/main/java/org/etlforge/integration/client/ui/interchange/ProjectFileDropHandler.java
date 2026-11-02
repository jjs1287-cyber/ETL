package org.etlforge.integration.client.ui.interchange;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowFileDropHandler;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.ImporterFactory;
import org.etlforge.integration.client.ui.jitterpack.PushedJitterPackHandler;
import org.etlforge.integration.client.ui.project.loading.ProjectLoader;
import org.etlforge.io.FileUtils;
import org.etlforge.io.Folder;

public final class ProjectFileDropHandler implements WindowFileDropHandler {
   private static final String PROJECT_FILE_EXTENSION = "jip";
   private static final String JITTERPACK_FILE_EXTENSION = "jpk";
   private final ApplicationWindow window;
   private final ProjectManager projectManager;
   private final ImporterFactory importerFactory;
   private static final FileFilter PROJECT_FILTER = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isFile() && FileUtils.getExtension(var1).equalsIgnoreCase("jip");
      }
   };
   private static final FileFilter JITTERPAK_FILTER = new FileFilter() {
      @Override
      public boolean accept(File var1) {
         return var1.isFile() && FileUtils.getExtension(var1).equalsIgnoreCase("jpk");
      }
   };

   public ProjectFileDropHandler(ApplicationWindow var1, ProjectManager var2, ImporterFactory var3) {
      this.window = var1;
      this.projectManager = var2;
      this.importerFactory = var3;
   }

   @Override
   public boolean canImport(List<File> var1) {
      return getApplicableFile(var1) != null;
   }

   @Override
   public boolean doImport(List<File> var1) {
      File var2 = getApplicableFile(var1);
      if (var2 == null) {
         return false;
      } else {
         String var3 = FileUtils.getExtension(var2).toLowerCase();
         if (var3.equals("jpk")) {
            return this.handleJitterpakDrop(var2);
         } else {
            return var3.equals("jip") ? this.handleProjectFileDrop(var2) : false;
         }
      }
   }

   @Override
   public boolean isAvailable() {
      return true;
   }

   private boolean handleJitterpakDrop(File var1) {
      PushedJitterPackHandler var2 = new PushedJitterPackHandler(this.window, this.projectManager);
      var2.handlePotentialJitterpack(var1, this.importerFactory);
      return true;
   }

   private boolean handleProjectFileDrop(File var1) {
      ProjectLocation var2 = this.getProjectLocation(var1);
      ProjectLoader var3 = new ProjectLoader(this.projectManager);
      var3.setWaitService(this.window);
      var3.loadAndDisplay(var2);
      return true;
   }

   private ProjectLocation getProjectLocation(File var1) {
      String var2 = var1.getName();
      String var3 = var2.substring(0, var2.length() - 4);
      return new ProjectLocation(var3, var1.getParentFile());
   }

   private static File getApplicableFile(List<File> var0) {
      if (var0.size() == 1) {
         File var1 = (File)var0.get(0);
         if (var1.isDirectory()) {
            return getApplicableChildFile(new Folder(var1));
         }

         String var2 = FileUtils.getExtension(var1).toLowerCase();
         if (var2.equals("jpk") || var2.equals("jip")) {
            return var1;
         }
      }

      return null;
   }

   private static File getApplicableChildFile(Folder var0) {
      File var1 = getChildFile(var0, JITTERPAK_FILTER);
      if (var1 == null) {
         var1 = getChildFile(var0, PROJECT_FILTER);
         if (var1 == null) {
            for (Folder var5 : var0.listSubFolders()) {
               var1 = getApplicableChildFile(var5);
               if (var1 != null) {
                  break;
               }
            }
         }
      }

      return var1;
   }

   private static File getChildFile(Folder var0, FileFilter var1) {
      File[] var2 = var0.listFiles(var1);
      return var2.length > 0 ? var2[0] : null;
   }
}
