package org.etlforge.integration.client.ui.project.location;

import com.google.common.base.Predicate;
import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.integration.client.project.ProjectFile;
import org.etlforge.integration.client.project.ProjectFolderDetector;
import org.etlforge.integration.client.ui.project.browser.ProjectFileView;
import org.etlforge.ui.widget.KongaFileChooser;

final class ExistingProjectLocationBrowser {
   private static final boolean USE_NEW_FILE_VIEW = true;
   private static final FileFilter PROJECT_FILE_FILTER = new ProjectFile.Filter();
   private final ProjectFolderDetector detector = new ProjectFolderDetector();

   public File selectExistingProject(File var1, Component var2) {
      File var3 = null;
      JFileChooser var4 = this.createFileChooser(var1);
      int var5 = var4.showOpenDialog(var2);
      if (var5 == 0) {
         File var6 = var4.getSelectedFile();
         if (var6 != null && var6.isDirectory()) {
            var3 = this.detector.getProjectFile(var6);
            assert var3 != null;
         } else {
            var3 = var6;
         }
      }

      return var3;
   }

   private JFileChooser createFileChooser(File var1) {
      JFileChooser var2 = ApplicationFileChooserFactory.getFactory().newFileChooser();
      var2.setMultiSelectionEnabled(false);
      if (var2 instanceof KongaFileChooser) {
         ((KongaFileChooser)var2).setApprovalFilter(new ExistingProjectLocationBrowser.ApprovalFilter(var2));
      }

      ProjectFileView var3 = new ProjectFileView(this.detector);
      var2.setFileView(var3);
      var2.setFileSelectionMode(2);
      var2.setFileFilter(PROJECT_FILE_FILTER);
      if (var1 != null) {
         var2.setCurrentDirectory(var1);
      }

      return var2;
   }

   private class ApprovalFilter implements Predicate<File> {
      private final JFileChooser fc;

      public ApprovalFilter(JFileChooser var2) {
         this.fc = var2;
      }

      public boolean apply(File var1) {
         if (var1 != null && var1.isDirectory()) {
            if (ExistingProjectLocationBrowser.this.detector.getProjectFile(var1) != null) {
               return true;
            }

            this.fc.setCurrentDirectory(var1);
            return false;
         } else {
            return true;
         }
      }
   }
}
