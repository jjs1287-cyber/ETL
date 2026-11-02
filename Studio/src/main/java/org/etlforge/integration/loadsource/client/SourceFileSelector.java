package org.etlforge.integration.loadsource.client;

import java.io.File;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.option.InputOptionPanel;

final class SourceFileSelector implements UiProvider, Disposable {
   private final SourceFileOption[] options;
   private final InputOptionPanel<File> fileInputPanel;

   public SourceFileSelector(SourceFileOption[] var1) {
      this.options = var1;
      this.fileInputPanel = new InputOptionPanel<>(var1);
   }

   @Override
   public void dispose() {
      this.fileInputPanel.dispose();
   }

   public File getSelectedFile() {
      return this.fileInputPanel.getValue();
   }

   public void setSelectedOption(SourceFileOption var1) {
      this.fileInputPanel.setSelectedOption(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.fileInputPanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.fileInputPanel.requestFocus();
   }

   public void setFinalSelectionMonitor(Receiver<Void> var1) {
      for (SourceFileOption var5 : this.options) {
         var5.setFinalSelectionMonitor(var1);
      }
   }
}
