package org.etlforge.integration.client.ui.structure.editor.xml;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;

public final class StructureFileSelectionModel extends AbstractPropertyBasedModel {
   public static final String INPUT_MODE = "mode";
   public static final String FOLDERS = "folders";
   public static final String SELECTED_FOLDER = "selectedFolder";
   public static final String AVAILABLE_FILES = "availableFiles";
   public static final String SELECTED_FILE = "selectedFile";
   public static final String LOCAL_FILE = "localFile";
   public static final String BUSY = "busy";
   private final EnumSet<FileInputMode> inputModes;
   private final Property<FileInputMode> inputMode = this.newProperty("mode", FileInputMode.EXISTING);
   private final ListProperty<ServerFileFolder> folders = this.newListProperty("folders");
   private final Property<ServerFileFolder> selectedFolder = this.newProperty("selectedFolder");
   private final ListProperty<ServerFile> availableFiles = this.newListProperty("availableFiles");
   private final Property<ServerFile> selectedFile = this.newProperty("selectedFile");
   private final Property<File> localFile = this.newProperty("localFile");
   private final Property<Boolean> busy = this.newProperty("busy", Boolean.FALSE);

   public static StructureFileSelectionModel forXml() {
      return new StructureFileSelectionModel(EnumSet.allOf(FileInputMode.class));
   }

   public static StructureFileSelectionModel forJson() {
      return new StructureFileSelectionModel(EnumSet.of(FileInputMode.EXISTING, FileInputMode.FROM_SAMPLE_FILE));
   }

   private StructureFileSelectionModel(EnumSet<FileInputMode> var1) {
      this.inputModes = var1;
      this.selectedFolder.addListener(new StructureFileSelectionModel.SelectedFolderListener());
   }

   public EnumSet<FileInputMode> getAllInputModes() {
      return EnumSet.copyOf(this.inputModes);
   }

   public FileInputMode getInputMode() {
      return this.inputMode.get();
   }

   public Property<FileInputMode> getInputModeProperty() {
      return this.inputMode;
   }

   public void setInputMode(FileInputMode var1) {
      this.inputMode.set(var1);
   }

   public List<ServerFileFolder> getFolders() {
      return this.folders.get();
   }

   public ListProperty<ServerFileFolder> getFoldersProperty() {
      return this.folders;
   }

   public Property<ServerFileFolder> getSelectedFolderProperty() {
      return this.selectedFolder;
   }

   public void clearAvailableFiles() {
      this.availableFiles.clear();
   }

   public ListProperty<ServerFile> getAvailableFilesProperty() {
      return this.availableFiles;
   }

   public Set<ServerFile> getAllFiles() {
      HashSet var1 = Sets.newHashSet();

      for (ServerFileFolder var3 : this.folders.get()) {
         Iterables.addAll(var1, var3.allFiles());
      }

      return var1;
   }

   public void setAvailableFolders(Collection<ServerFileFolder> var1) {
      ServerFileFolder var2 = this.selectedFolder.get();
      ServerFile var3 = this.selectedFile.get();
      this.selectedFolder.set(null);
      this.selectedFile.set(null);
      this.folders.setIterable(var1);
      this.restoreFolderSelection(var2, var1);
      this.restoreFileSelection(var3);
   }

   private void restoreFolderSelection(ServerFileFolder var1, Collection<ServerFileFolder> var2) {
      if (!var2.isEmpty()) {
         if (var1 != null) {
            for (ServerFileFolder var4 : var2) {
               if (var1.getName().equals(var4.getName())) {
                  this.selectedFolder.set(var4);
                  return;
               }
            }
         }

         this.selectedFolder.set(Iterables.get(var2, 0));
      }
   }

   private void restoreFileSelection(ServerFile var1) {
      if (var1 != null) {
         ServerFileFolder var2 = this.selectedFolder.get();
         if (var2 != null) {
            for (ServerFile var4 : var2.allFiles()) {
               if (var4.getFilePath().equals(var1.getFilePath())) {
                  this.setSelectedServerFile(var4);
                  return;
               }
            }
         }
      }
   }

   public Property<ServerFile> getSelectedFileProperty() {
      return this.selectedFile;
   }

   public ServerFile getSelectedServerFile() {
      return this.selectedFile.get();
   }

   public void setSelectedServerFile(ServerFile var1) {
      this.selectedFile.set(var1);
   }

   public void setSelectedServerFileFromPath(String var1) {
      if (var1 == null) {
         this.setSelectedServerFile(null);
      } else {
         for (ServerFile var3 : this.getAllFiles()) {
            if (var1.equals(var3.getFilePath())) {
               this.setSelectedServerFile(var3);
               return;
            }
         }
      }
   }

   public File getLocalFile() {
      return this.localFile.get();
   }

   public Property<File> getLocalFileProperty() {
      return this.localFile;
   }

   public boolean isBusy() {
      return this.busy.get();
   }

   public void setBusy(boolean var1) {
      this.busy.set(var1);
   }

   public Binding bindBusyState(WaitService var1) {
      WaitStateBinding var2 = new WaitStateBinding(this.busy, var1);
      var2.syncUi();
      return var2;
   }

   private class SelectedFolderListener implements PropertyChangeListener {
      private SelectedFolderListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         ServerFileFolder var2 = (ServerFileFolder)var1.getNewValue();
         if (var2 == null) {
            StructureFileSelectionModel.this.availableFiles.clear();
         } else {
            StructureFileSelectionModel.this.availableFiles.setIterable(var2.allFiles());
         }
      }
   }
}
