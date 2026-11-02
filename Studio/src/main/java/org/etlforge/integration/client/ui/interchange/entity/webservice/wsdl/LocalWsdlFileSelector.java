package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.io.File;
import javax.swing.JComponent;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.ui.widget.ApplicationFileChooserFactory;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.util.preference.PreferencesKey;

public final class LocalWsdlFileSelector extends AbstractWsdlSelector {
   private final FileStore fileStore;
   private final FileSelectorPanel fileSelector;
   private static final String LATEST_LOCAL_WSDL = "LatestLocalWsdl";

   public LocalWsdlFileSelector(FileStore var1) {
      this.fileStore = var1;
      this.fileSelector = this.createFileSelector();
      this.layout();
   }

   public WsdlFile getSelectedFile() {
      WsdlFile var1 = null;
      File var2 = this.fileSelector.getSelectedFile();
      if (var2 != null) {
         var1 = new WsdlFile();
         var1.setLocator(var2.getAbsolutePath());
      }

      return var1;
   }

   @Override
   public boolean isValid() {
      return this.fileSelector.isInputValid();
   }

   @Override
   public void installUndoRedo(UndoRedoInstaller var1) {
   }

   @Override
   public void setActive(boolean var1) {
      this.fileSelector.setEnabled(var1);
      this.fileSelector.setActive(var1);
   }

   @Override
   public void requestFocus() {
      this.fileSelector.requestFocus();
   }

   @Override
   public WsdlProcessingJob getProcessingJob() {
      File var1 = this.fileSelector.getSelectedFile();
      return var1 != null ? new ProcessLocalWsdlJob(this.fileStore, var1.getAbsolutePath()) : null;
   }

   private FileSelectorPanel createFileSelector() {
      FileSelectorPanelFactory var1 = FileSelectorFactory.getFactory();
      DefaultFileChooser var2 = new DefaultFileChooser(ApplicationFileChooserFactory.getFactory());
      RecentFileStore var3 = this.getRecentFileStore();
      FileSelectorPanel var4 = var1.recentFilesPanel(var2, FileSelectorPanel.OPEN, PackageResources.LocalWsdlFileDefiner.BROWSE_DIALOG_TITLE, var3);
      var4.setFileFilter(new SuffixFileChooserFilter(".wsdl"));
      var4.setUseTitleLabel(false);
      var4.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            LocalWsdlFileSelector.this.fireInputChanged();
         }
      });
      return var4;
   }

   private RecentFileStore getRecentFileStore() {
      PreferencesKey var1 = new PreferencesKey(LocalWsdlFileSelector.class, "LatestLocalWsdl");
      return new PreferencesFileStore(var1, 10);
   }

   private void layout() {
      JComponent var1 = ComponentCanvasFactory.northBorder(this.fileSelector).getUi();
      this.setDisplayer(var1);
   }
}
