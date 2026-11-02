package org.etlforge.integration.loadsource.client;

import java.io.File;
import org.etlforge.integration.loadsource.client.resources.LoadSourceResources;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.util.option.OptionSelector;

public final class ProjectFileOption implements SourceFileOption {
   private FileUi[] files;
   private final SourceFileList fileList;
   private boolean isActive = true;

   public ProjectFileOption(FileUi[] var1) {
      this.files = var1;
      this.fileList = new SourceFileList(var1);
   }

   public void setFiles(FileUi[] var1) {
      this.files = (FileUi[])var1.clone();
      this.fileList.setFiles(var1);
   }

   public void setDeleteAction(SelectionAction var1) {
      this.fileList.setDeleteAction(var1);
   }

   public File getValue() {
      FileUi var1 = this.fileList.getSelectedFile();
      return var1 != null ? var1.getFile() : null;
   }

   @Override
   public void setFinalSelectionMonitor(Receiver<Void> var1) {
      this.fileList.setFinalSelectionMonitor(var1);
   }

   @Override
   public boolean includeSelectionInProject() {
      return true;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(BorderLayoutBuilder.fillWith(this.fileList));
   }

   @Override
   public void requestFocus() {
      this.fileList.requestFocus();
   }

   @Override
   public void setActive(boolean var1) {
      this.isActive = var1;
      if (var1) {
         this.fileList.requestFocus();
      }
   }

   @Override
   public boolean isActive() {
      return this.isActive;
   }

   @Override
   public void decorate(OptionSelector var1) {
      String var2 = LoadSourceResources.getString("SourceDataFileProvider.UseExisting");
      var1.setText(var2);
      if (this.files == null || this.files.length == 0) {
         var1.setEnabled(false);
         this.setActive(false);
      }
   }
}
