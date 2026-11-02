package org.etlforge.integration.activity.ui.files;

import com.google.common.base.Supplier;
import java.awt.Dimension;
import java.io.File;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.FolderSelectorPanelBuilder;
import org.etlforge.integration.activity.client.files.DownloadDestinationProvider;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.system.SystemUtils;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.util.preference.KongaPreferences;

public final class DownloadDestinationDialog implements DownloadDestinationProvider {
   @Override
   public File getDestination(OperationFile var1) {
      return UiThreadSupplier.call(new DownloadDestinationDialog.Impl(var1));
   }

   private class Impl implements Supplier<File> {
      private final OperationFile opFile;
      private FileSelectorPanel fileSelector;
      private final KongaPreferences prefs = KongaPreferences.getPreferences(DownloadDestinationDialog.class);

      public Impl(OperationFile var2) {
         this.opFile = var2;
      }

      public File get() {
         this.fileSelector = this.createFileSelector();
         boolean var1 = this.openDialog();
         if (!var1) {
            return null;
         }

         File var2 = this.fileSelector.getSelectedFile();
         if (var2 != null) {
            this.prefs.putFile("Destination", var2.getParentFile());
         }

         return var2;
      }

      private FileSelectorPanel createFileSelector() {
         FolderSelectorPanelBuilder var1 = new FolderSelectorPanelBuilder();
         var1.setMode(FileSelectorPanel.Mode.SAVE);
         File var2 = this.prefs.getFile("Destination", SystemUtils.USER_HOME);
         var1.setDefaultFolder(var2);
         var1.setDefaultFileName(this.opFile.asLocalFile().getName());
         var1.setTitle("Download File To:");
         var1.setDefaultFolder(SystemUtils.USER_HOME);
         return var1.buildPanel(ApplicationUi.getAppWin());
      }

      private boolean openDialog() {
         KongaDialog var1 = this.createDialog();
         return var1.wasOkPressed();
      }

      private KongaDialog createDialog() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder().north(this.fileSelector);
         KongaDialog var2 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Download File", true);
         var2.setFixedButtonSize(false);
         var2.standardLayout((UiProvider)var1, KongaDialog.CONTINUE_CANCEL);
         var2.getContinueButton().setText("Download File");
         var2.setContinueButtonAsDefault();
         var2.manageLocation(DownloadDestinationDialog.class);
         var2.setMinimumSize(new Dimension(500, 100));
         var2.setLocationRelativeTo(null);
         var2.setVisible(true);
         return var2;
      }
   }
}
