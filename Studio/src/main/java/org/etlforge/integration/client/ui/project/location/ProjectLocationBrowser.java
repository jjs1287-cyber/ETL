package org.etlforge.integration.client.ui.project.location;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.integration.client.project.ProjectLocation;

public final class ProjectLocationBrowser {
   private static File defaultLocation;
   private static File newProjectLocation;
   private static JFileChooser fileChooser;

   public static void setDefaultProjectLocation(File var0) {
      if (var0 == null) {
         var0 = new File(SystemUtils.USER_HOME);
      }

      defaultLocation = var0;
   }

   public static File getNewProjectLocation() {
      return newProjectLocation;
   }

   public static void setNewProjectLocation(File var0) {
      newProjectLocation = var0 == null ? defaultLocation : var0;
      ProjectLocationPreference.setNewProjectLocation(newProjectLocation);
   }

   public static File browseNewProjectLocation(Component var0) {
      File var1 = ProjectLocationPreference.getNewProjectLocation();
      if (var1 == null) {
         var1 = newProjectLocation;
      }

      File var2 = browseNewProjectLocation(var1, var0);
      if (var2 != null) {
         setNewProjectLocation(var2);
      }

      return var2;
   }

   public static File browseNewProjectLocation(File var0, Component var1) {
      File var2 = null;
      JFileChooser var3 = getFileChooser();
      var3.setFileSelectionMode(1);
      if (var0 != null) {
         var3.setCurrentDirectory(var0);
      }

      int var4 = var3.showOpenDialog(var1);
      if (var4 == 0) {
         var2 = var3.getSelectedFile();
      }

      return var2;
   }

   public static ProjectLocation browseExistingProjectLocation(File var0, Component var1) {
      if (var0 == null) {
         var0 = ProjectLocationPreference.getExistingProjectLocation();
         if (var0 == null) {
            var0 = newProjectLocation;
         }
      } else {
         var0 = var0.getParentFile();
      }

      File var2 = browseExistingProject(var0, var1);
      if (var2 == null) {
         return null;
      }

      File var3 = var2.getParentFile();
      ProjectLocationPreference.setExistingProjectLocation(var3.getParentFile());
      String var4 = var2.getName();
      String var5 = StringUtils.removeEndIgnoreCase(var4, ".jip");
      return new ProjectLocation(var5, var3);
   }

   public static ProjectBrowserLocationDisplayer getNewProjectBrowser(String var0, File var1) {
      if (var1 == null) {
         var1 = getNewProjectLocation();
      }

      return new ProjectBrowserLocationDisplayer(var0, var1);
   }

   private static File browseExistingProject(File var0, Component var1) {
      ExistingProjectLocationBrowser var2 = new ExistingProjectLocationBrowser();
      return var2.selectExistingProject(var0, var1);
   }

   private static JFileChooser getFileChooser() {
      if (fileChooser == null) {
         fileChooser = ApplicationFileChooserFactory.getFactory().newFileChooser(newProjectLocation);
         fileChooser.setMultiSelectionEnabled(false);
      }

      return fileChooser;
   }

   private ProjectLocationBrowser() {
   }

   static {
      setDefaultProjectLocation(null);
      File var0 = ProjectLocationPreference.getNewProjectLocation();
      if (var0 == null) {
         var0 = defaultLocation;
      }

      newProjectLocation = var0;
   }
}
