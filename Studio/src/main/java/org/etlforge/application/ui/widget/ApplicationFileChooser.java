package org.etlforge.application.ui.widget;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.IdentityHashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import org.etlforge.application.ui.ApplicationUiUtils;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.RecentFileStore;

public final class ApplicationFileChooser implements FileChooser {
   private static final Map<ApplicationWindow, ApplicationFileChooser> INSTANCES = new IdentityHashMap<>();
   private final Window window;
   private final DefaultFileChooser delegate;

   private ApplicationFileChooser(ApplicationWindow var1) {
      this(var1.getFrame());
      INSTANCES.put(var1, this);
   }

   public ApplicationFileChooser(Window var1) {
      this.window = var1;
      this.delegate = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory(), new ApplicationFileChooser.ConfirmerImpl(var1));
   }

   public static synchronized FileChooser getInstance(ApplicationWindow var0) {
      return var0 != null ? new ApplicationFileChooser(var0) : new ApplicationFileChooser(UiUtils.getActiveFrame());
   }

   private Component getParent(Component var1) {
      return var1 == null ? this.window : var1;
   }

   @Override
   public File getCurrentDirectory() {
      this.checkThread("getCurrentDirectory");
      return this.delegate.getCurrentDirectory();
   }

   @Override
   public void setSelectedFile(File var1) {
      this.checkThread("setSelectedFile");
      this.delegate.setSelectedFile(var1);
   }

   @Override
   public void setSelectionMode(FileChooser.SelectionMode var1) {
      this.checkThread("setSelectionMode");
      this.delegate.setSelectionMode(var1);
   }

   @Override
   public FileChooser.SelectionMode getSelectionMode() {
      this.checkThread("getSelectionMode");
      return this.delegate.getSelectionMode();
   }

   @Override
   public File getFileToOpen(Component var1, FileFilter var2, RecentFileStore var3, String var4) {
      this.checkThread("getFileToOpen");
      return this.delegate.getFileToOpen(this.getParent(var1), var2, var3, var4);
   }

   @Override
   public File getFileToOpen(Component var1, FileFilter var2, RecentFileStore var3) {
      this.checkThread("getFileToOpen");
      return this.delegate.getFileToOpen(this.getParent(var1), var2, var3);
   }

   @Override
   public File getFileToOpen(Component var1, FileFilter var2, String var3) {
      this.checkThread("getFileToOpen");
      return this.delegate.getFileToOpen(this.getParent(var1), var2, var3);
   }

   @Override
   public File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3, String var4) {
      this.checkThread("getFileToSave");
      return this.delegate.getFileToSave(this.getParent(var1), var2, var3, var4);
   }

   @Override
   public File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3) {
      this.checkThread("getFileToSave");
      return this.delegate.getFileToSave(this.getParent(var1), var2, var3);
   }

   @Override
   public File getFileToSave(Component var1, FileFilter var2, String var3) {
      this.checkThread("getFileToSave");
      return this.delegate.getFileToSave(this.getParent(var1), var2, var3);
   }

   @Override
   public File getFileToSave(Component var1, File var2, FileFilter var3, String var4) {
      this.checkThread("getFileToSave");
      return this.delegate.getFileToSave(this.getParent(var1), var2, var3, var4);
   }

   @Override
   public File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3, String var4, File var5) {
      this.checkThread("getFileToSave");
      return this.delegate.getFileToSave(this.getParent(var1), var2, var3, var4, var5);
   }

   @Override
   public void setOverwriteConfirmer(FileChooser.OverwriteConfirmer var1) {
   }

   private void checkThread(String var1) {
      ApplicationUiUtils.logIfNotOnEDT(this.getClass(), var1);
   }

   private static class ConfirmerImpl extends DefaultFileChooser.DefaultConfirmer {
      private final Component parent;

      public ConfirmerImpl(Window var1) {
         this.parent = var1;
      }

      @Override
      protected JComponent getMessageObject(File var1) {
         return new JLabel(this.getMessage(var1));
      }

      @Override
      protected Component getDialogOwner() {
         return this.parent;
      }
   }
}
