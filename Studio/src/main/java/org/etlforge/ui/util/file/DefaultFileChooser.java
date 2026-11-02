package org.etlforge.ui.util.file;

import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.widget.DefaultFileChooserFactory;
import org.etlforge.ui.widget.FileChooserFactory;
import org.etlforge.util.file.ExtensionBasedFileFilter;

public final class DefaultFileChooser implements FileChooser {
   private final JFileChooser fileChooser;
   private FileChooser.OverwriteConfirmer confirmer;

   public DefaultFileChooser() {
      this(new DefaultFileChooserFactory());
   }

   public DefaultFileChooser(FileChooserFactory var1) {
      this(var1, null);
   }

   public DefaultFileChooser(FileChooserFactory var1, FileChooser.OverwriteConfirmer var2) {
      this.confirmer = var2;
      this.fileChooser = var1.newFileChooser();
   }

   @Override
   public void setOverwriteConfirmer(FileChooser.OverwriteConfirmer var1) {
      this.confirmer = var1;
   }

   @Override
   public void setSelectedFile(File var1) {
      this.fileChooser.setSelectedFile(var1);
   }

   @Override
   public File getCurrentDirectory() {
      return this.fileChooser.getCurrentDirectory();
   }

   public void setCurrentDirectory(File var1) {
      this.fileChooser.setCurrentDirectory(var1);
   }

   @Override
   public void setSelectionMode(FileChooser.SelectionMode var1) {
      switch (var1) {
         case FILES_ONLY:
            this.fileChooser.setFileSelectionMode(0);
            break;
         case DIRECTORIES_ONLY:
            this.fileChooser.setFileSelectionMode(1);
            break;
         case FILES_AND_DIRECTORIES:
            this.fileChooser.setFileSelectionMode(2);
      }
   }

   @Override
   public FileChooser.SelectionMode getSelectionMode() {
      switch (this.fileChooser.getFileSelectionMode()) {
         case 0:
            return FileChooser.SelectionMode.FILES_ONLY;
         case 1:
            return FileChooser.SelectionMode.DIRECTORIES_ONLY;
         case 2:
            return FileChooser.SelectionMode.FILES_AND_DIRECTORIES;
         default:
            throw new AssertionError();
      }
   }

   public FileChooser.OverwriteConfirmer getOverwriteConfirmer() {
      if (this.confirmer == null) {
         this.confirmer = new DefaultFileChooser.DefaultConfirmer();
      }

      return this.confirmer;
   }

   public void setFileView(FileView var1) {
      this.fileChooser.setFileView(var1);
   }

   @Override
   public File getFileToSave(Component var1, FileFilter var2, String var3) {
      return this.getFileToSave(var1, var2, null, var3);
   }

   @Override
   public File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3) {
      return this.getFileToSave(var1, var2, var3, null);
   }

   @Override
   public File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3, String var4, File var5) {
      return this.getFileToSaveImpl(var1, var2, var3, var4, var5);
   }

   @Override
   public File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3, String var4) {
      return this.getFileToSaveImpl(var1, var2, var3, var4, null);
   }

   private File getFileToSaveImpl(Component var1, FileFilter var2, RecentFileStore var3, String var4, File var5) {
      JFileChooser var6 = this.getFileChooser(var2, var3, var5);
      var6.setDialogTitle(var4);
      if (var6.showSaveDialog(var1) == 0) {
         File var7 = var6.getSelectedFile();
         File var8 = this.ensureFileToSaveHasCorrectExtension(var7, var2);
         if (var8 != var7) {
            var6.setSelectedFile(var8);
            var7 = var8;
         }

         if (var3 != null) {
            var3.update(var7);
         }

         if (!var7.exists() || var7.isDirectory() || this.confirmOverWrite(var7)) {
            return var7;
         }
      }

      return null;
   }

   private File ensureFileToSaveHasCorrectExtension(File var1, FileFilter var2) {
      if (!(var2 instanceof ExtensionBasedFileFilter)) {
         return var1;
      }

      if (var2.accept(var1)) {
         return var1;
      }

      ExtensionBasedFileFilter var3 = (ExtensionBasedFileFilter)var2;
      Iterator var4 = var3.getExtensions();
      if (var4.hasNext()) {
         String var5 = (String)var4.next();
         if (!var5.startsWith(".")) {
            var5 = "." + var5;
         }

         return new File(var1.getParent(), var1.getName() + var5);
      } else {
         return var1;
      }
   }

   @Override
   public File getFileToSave(Component var1, File var2, FileFilter var3, String var4) {
      JFileChooser var5 = this.getFileChooser(var2, var3);
      var5.setDialogTitle(var4);
      if (var5.showSaveDialog(var1) == 0) {
         File var6 = var5.getSelectedFile();
         if (!var6.exists() || var6.isDirectory() || this.confirmOverWrite(var6)) {
            return var6;
         }
      }

      return null;
   }

   private boolean confirmOverWrite(File var1) {
      return this.getOverwriteConfirmer().confirmOverwrite(var1);
   }

   @Override
   public File getFileToOpen(Component var1, FileFilter var2, String var3) {
      return this.getFileToOpen(var1, var2, (RecentFileStore)null, var3);
   }

   @Override
   public File getFileToOpen(Component var1, FileFilter var2, RecentFileStore var3) {
      return this.getFileToOpen(var1, var2, var3, null);
   }

   @Override
   public File getFileToOpen(Component var1, FileFilter var2, RecentFileStore var3, String var4) {
      JFileChooser var5 = this.getFileChooser(var2, var3, null);
      var5.setDialogTitle(var4);
      File var6 = null;
      if (var5.showOpenDialog(var1) == 0) {
         var6 = var5.getSelectedFile();
         if (var3 != null) {
            var3.update(var6);
         }
      }

      return var6;
   }

   private JFileChooser getFileChooser(File var1, FileFilter var2) {
      for (FileFilter var6 : this.fileChooser.getChoosableFileFilters()) {
         this.fileChooser.removeChoosableFileFilter(var6);
      }

      this.fileChooser.setAcceptAllFileFilterUsed(true);
      this.fileChooser.setFileFilter(var2);
      this.fileChooser.setSelectedFile(var1);
      return this.fileChooser;
   }

   private JFileChooser getFileChooser(FileFilter var1, RecentFileStore var2, File var3) {
      for (FileFilter var7 : this.fileChooser.getChoosableFileFilters()) {
         this.fileChooser.removeChoosableFileFilter(var7);
      }

      this.fileChooser.setAcceptAllFileFilterUsed(true);
      this.fileChooser.setFileFilter(var1);
      if (var3 != null) {
         this.fileChooser.setSelectedFile(var3);
      } else {
         this.customizeFileChooser(var2);
      }

      return this.fileChooser;
   }

   private void customizeFileChooser(RecentFileStore var1) {
      if (var1 != null) {
         FileUi var2 = var1.latestFile();
         if (var2 != null) {
            File var3 = var2.getFile();
            if (var3.exists()) {
               this.fileChooser.setSelectedFile(var3);
            } else {
               this.fileChooser.setCurrentDirectory(var3.getParentFile());
            }
         }
      }
   }

   public static class DefaultConfirmer implements FileChooser.OverwriteConfirmer {
      @Override
      public boolean confirmOverwrite(File var1) {
         Object[] var2 = new Object[]{UIManager.getString("OptionPane.yesButtonText"), UIManager.getString("OptionPane.noButtonText")};
         int var3 = JOptionPane.showOptionDialog(
            this.getDialogOwner(), this.getMessageObject(var1), UiResourceBundle.getString("Warning"), 0, 3, null, var2, var2[1]
         );
         return var3 == 0;
      }

      protected String getMessage(File var1) {
         String var2 = UiResourceBundle.getString("DefaultFileChooser.ConfirmOverwrite");
         return MessageFormat.format(var2, var1.getAbsolutePath());
      }

      protected JComponent getMessageObject(File var1) {
         return new JLabel(this.getMessage(var1));
      }

      protected Component getDialogOwner() {
         return null;
      }
   }
}
