package org.etlforge.ui.util.opensave;

import com.google.common.base.Functions;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import org.etlforge.io.FileUtils;
import org.etlforge.lang.Arrays2;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.util.preference.KongaPreferences;

public class OpenAndSaveManager {
   private static final String PREF_CURRENT_DIR = "CurrentDir";
   private static final String PREF_RECENT_FILE_LIST_LENGTH = "RecentFileListLength";
   private static final String PREF_RECENT_FILE = "RecentFile";
   public static final int RECENT_FILE_LIST_DEFAULT_LENGTH = 10;
   private static final KongaPreferences uiPreferences = KongaPreferences.getPreferences(OpenAndSaveManager.class);
   private Component owner;
   private File file;
   private IFileOpener fileOpener;
   private IFileSaver fileSaver;
   private JFileChooser fileChooser;
   private FileFilter openFileFilter;
   private FileFilter[] saveFileFilters;
   private int recentFileListMaxLength = 10;
   private int numberOfRecentFiles;
   private LinkedList<OpenAndSaveManager.RecentFileMenuItem> recentFiles = new LinkedList<>();
   private final Action saveAction;
   private final Action saveAsAction;
   private final Action openAction;

   public OpenAndSaveManager(Component var1) {
      this(var1, null, null, null);
   }

   public OpenAndSaveManager(Component var1, String[] var2) {
      this(var1, null, null, var2);
   }

   public OpenAndSaveManager(Component var1, IFileOpener var2, IFileSaver var3, String[] var4) {
      this.owner = var1;
      this.fileOpener = var2;
      this.fileSaver = var3;
      this.fileChooser = new JFileChooser();
      if (var4 != null && var4.length > 0) {
         this.openFileFilter = new OpenAndSaveManager.ExtFileFilter(var4);
         this.saveFileFilters = new FileFilter[var4.length];

         for (int var5 = var4.length - 1; var5 >= 0; var5--) {
            this.saveFileFilters[var4.length - 1 - var5] = new OpenAndSaveManager.ChoosableFileFilter(var4[var5]);
         }
      }

      this.saveAction = new OpenAndSaveManager.SaveAction();
      this.saveAsAction = new OpenAndSaveManager.SaveAsAction();
      this.openAction = new OpenAndSaveManager.OpenAction();
      this.loadPreferences();
   }

   public Action getSaveAction() {
      return this.saveAction;
   }

   public Action getSaveAsAction() {
      return this.saveAsAction;
   }

   public Action getOpenAction() {
      return this.openAction;
   }

   public void setSaveIcon(Icon var1) {
      this.saveAction.putValue("SmallIcon", var1);
   }

   public void setOpenIcon(Icon var1) {
      this.openAction.putValue("SmallIcon", var1);
   }

   public void savePreferences() {
      if (uiPreferences != null) {
         uiPreferences.putFile("CurrentDir", this.fileChooser.getCurrentDirectory());
         uiPreferences.putInt("RecentFileListLength", this.recentFileListMaxLength);
         int var1 = 0;

         for (OpenAndSaveManager.RecentFileMenuItem var3 : this.recentFiles) {
            uiPreferences.putFile("RecentFile" + var1, var3.getFile());
            var1++;
         }
      }
   }

   public void setRecentFileListLength(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("length must be >= 0");
      }

      this.recentFileListMaxLength = var1;
   }

   public List<JMenuItem> getRecentFileMenuItems() {
      return new ArrayList<>(this.recentFiles);
   }

   public File getCurrentFile() {
      return this.file;
   }

   public void setCurrentFile(File var1) {
      this.file = var1;
   }

   public void setCurrentDirectory(File var1) {
      this.fileChooser.setCurrentDirectory(var1);
   }

   public File getCurrentDirectory() {
      return this.fileChooser.getCurrentDirectory();
   }

   public void setMultiSelectionEnabled(boolean var1) {
      this.fileChooser.setMultiSelectionEnabled(var1);
   }

   public boolean isMultiSelectionEnabled() {
      return this.fileChooser.isMultiSelectionEnabled();
   }

   public void setFileHidingEnabled(boolean var1) {
      this.fileChooser.setFileHidingEnabled(var1);
   }

   public boolean isFileHidingEnabled() {
      return this.fileChooser.isFileHidingEnabled();
   }

   public void setFileSelectionMode(int var1) {
      this.fileChooser.setFileSelectionMode(var1);
   }

   public int getFileSelectionMode() {
      return this.fileChooser.getFileSelectionMode();
   }

   public void setFileOpener(IFileOpener var1) {
      this.fileOpener = var1;
   }

   public void setFileSaver(IFileSaver var1) {
      this.fileSaver = var1;
   }

   public void onSave() {
      if (this.file == null) {
         this.onSaveAs();
      } else if (this.fileSaver != null) {
         this.fileSaver.saveToFile(this.file);
      }
   }

   public void onSaveAs() {
      if (this.saveFileFilters == null) {
         this.fileChooser.setFileFilter(null);
      } else {
         for (FileFilter var4 : this.saveFileFilters) {
            this.fileChooser.addChoosableFileFilter(var4);
         }
      }

      int var5 = this.fileChooser.showSaveDialog(this.owner);
      if (var5 == 0) {
         this.file = this.fileChooser.getSelectedFile();
         FileFilter var6 = this.fileChooser.getFileFilter();
         if (!var6.accept(this.file) && var6 instanceof OpenAndSaveManager.ChoosableFileFilter) {
            this.file = new File(this.file.getAbsolutePath() + "." + ((OpenAndSaveManager.ChoosableFileFilter)var6).getExtension());
         }

         if (this.fileSaver != null) {
            this.fileSaver.saveToFile(this.file);
         }
      }
   }

   public void onOpen() {
      this.fileChooser.setFileFilter(this.openFileFilter);
      int var1 = this.fileChooser.showOpenDialog(this.owner);
      if (var1 == 0) {
         this.file = this.fileChooser.getSelectedFile();
         if (this.fileOpener != null) {
            this.fileOpener.openFile(this.file);
            this.addRecentFile(this.file);
         }
      }
   }

   public void addRecentFile(File var1) {
      if (this.recentFileListMaxLength > 0) {
         this.removeFileFromList(var1);
         this.addNewFileFirst(var1);
         this.ensureWithinLimit();
         this.assignMnemonics();
         this.savePreferences();
      }
   }

   private void addNewFileFirst(File var1) {
      this.recentFiles.addFirst(new OpenAndSaveManager.RecentFileMenuItem(var1));
      this.numberOfRecentFiles++;
   }

   private void ensureWithinLimit() {
      if (this.numberOfRecentFiles > this.recentFileListMaxLength) {
         this.recentFiles.removeLast();
         this.numberOfRecentFiles--;
      }
   }

   private void removeFileFromList(File var1) {
      Iterator var2 = this.recentFiles.iterator();

      while (var2.hasNext()) {
         if (var1.equals(((OpenAndSaveManager.RecentFileMenuItem)var2.next()).getFile())) {
            var2.remove();
            this.numberOfRecentFiles--;
            break;
         }
      }
   }

   private void loadPreferences() {
      if (uiPreferences != null) {
         this.recentFileListMaxLength = uiPreferences.getInt("RecentFileListLength", this.recentFileListMaxLength);
         this.createMenuItems();
         this.assignMnemonics();
         File var1 = uiPreferences.getFile("CurrentDir", null);
         this.fileChooser.setCurrentDirectory(var1);
      }
   }

   private void createMenuItems() {
      for (int var1 = 0; var1 < this.recentFileListMaxLength; var1++) {
         File var2 = uiPreferences.getFile("RecentFile" + var1, null);
         if (var2 != null) {
            this.recentFiles.add(new OpenAndSaveManager.RecentFileMenuItem(var2));
            this.numberOfRecentFiles++;
         }
      }
   }

   private void assignMnemonics() {
      int var1 = 1;

      for (OpenAndSaveManager.RecentFileMenuItem var3 : this.recentFiles) {
         if (var1 == 10) {
            break;
         }

         var3.setText(var1 + " " + var3.getFile().getAbsolutePath());
         var3.setMnemonic(48 + var1);
         var1++;
      }
   }

   private static class ChoosableFileFilter extends FileFilter {
      private String extension;

      public ChoosableFileFilter(String var1) {
         this.extension = var1;
      }

      @Override
      public boolean accept(File var1) {
         return var1.isDirectory() ? true : this.extension.equalsIgnoreCase(FileUtils.getExtension(var1));
      }

      @Override
      public String getDescription() {
         return this.extension;
      }

      public String getExtension() {
         return this.extension;
      }
   }

   private static class ExtFileFilter extends FileFilter {
      private String[] extensions;

      public ExtFileFilter(String[] var1) {
         this.extensions = var1;
      }

      @Override
      public boolean accept(File var1) {
         if (var1.isDirectory()) {
            return true;
         }

         for (String var5 : this.extensions) {
            if (var5.equalsIgnoreCase(FileUtils.getExtension(var1))) {
               return true;
            }
         }

         return false;
      }

      @Override
      public String getDescription() {
         return Arrays2.join(this.extensions, Functions.toStringFunction(), ",");
      }
   }

   private class OpenAction extends AbstractAction {
      public OpenAction() {
         super(UiResourceBundle.getString("Open"));
         this.putValue("MnemonicKey", 79);
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(79, 2));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OpenAndSaveManager.this.onOpen();
      }
   }

   private class RecentFileMenuItem extends JMenuItem implements ActionListener {
      private File fileToOpen;

      public RecentFileMenuItem(File var2) {
         super(var2.getAbsolutePath());
         this.fileToOpen = var2;
         this.addActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.fileToOpen.exists()) {
            if (OpenAndSaveManager.this.fileOpener != null) {
               OpenAndSaveManager.this.fileOpener.openFile(this.fileToOpen);
            }
         } else {
            String var2 = UiResourceBundle.getFormattedMessage("OpenAndSaveManager.FileNotFound.Message", new Object[]{this.fileToOpen.getAbsolutePath()});
            JOptionPane.showMessageDialog(null, var2, UiResourceBundle.getString("OpenAndSaveManager.FileNotFound.Title"), 2);
         }
      }

      public File getFile() {
         return this.fileToOpen;
      }
   }

   private class SaveAction extends AbstractAction {
      public SaveAction() {
         super(UiResourceBundle.getString("Save"));
         this.putValue("MnemonicKey", 83);
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(83, 2));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OpenAndSaveManager.this.onSave();
      }
   }

   private class SaveAsAction extends AbstractAction {
      public SaveAsAction() {
         super(UiResourceBundle.getString("SaveAs"));
         this.putValue("MnemonicKey", 65);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OpenAndSaveManager.this.onSaveAs();
      }
   }
}
