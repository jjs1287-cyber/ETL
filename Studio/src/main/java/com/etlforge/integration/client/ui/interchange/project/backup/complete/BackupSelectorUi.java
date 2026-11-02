package com.etlforge.integration.client.ui.interchange.project.backup.complete;

import com.etlforge.integration.client.project.restore.ProjectBackupRestorer;
import com.etlforge.integration.client.project.restore.RestoreProjectBackupException;
import com.etlforge.integration.client.ui.interchange.project.backup.ProjectBackupBrowser;
import com.etlforge.integration.client.ui.interchange.project.backup.ProjectBackupListBrowser;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.Action;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.widget.FileSelectorFactory;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.system.callback.NoReceiver;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.file.FileSelectorPanelFactory;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.LabelDecorator;

public final class BackupSelectorUi implements UiProvider {
   private final ProjectManager projectManager;
   private final ProjectBackupBrowser browser;
   private final FileSelectorPanel destinationSelector;
   private final BackupSelectorUi.RestoreAction restoreAction;
   private Action cancelAction;
   private WaitService waitService = WaitService.NONE;
   private Receiver<ProjectLocation> projectHandler;
   private final Object lock = new Object();

   public BackupSelectorUi(ProjectManager var1) {
      this.projectManager = var1;
      this.browser = new ProjectBackupListBrowser();
      this.browser.setInspectBackupAction(new BackupSelectorUi.ViewProjectDetailsAction());
      this.projectHandler = NoReceiver.create();
      this.destinationSelector = this.createDestinationPanel();
      this.restoreAction = new BackupSelectorUi.RestoreAction();
      this.restoreAction.setSelectionSource(this.browser);
      this.destinationSelector.addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            BackupSelectorUi.this.restoreAction.update();
         }
      });
      this.browser.setDefaultAction(this.restoreAction);
   }

   public void setProjectHandler(Receiver<ProjectLocation> var1) {
      synchronized (this.lock) {
         this.projectHandler = var1;
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.center(this.layoutContents()).south(this.layoutButtons());
      var2.decorate(var1);
   }

   private UiProvider layoutContents() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10);
      var1.center(this.browser).south(this.destinationSelector);
      var1.withBorder(new BorderBuilder().etched().empty(10));
      return var1;
   }

   private UiProvider layoutButtons() {
      BoxBuilder var1 = BoxBuilder.horizontal();
      var1.glue().addAllWithSpace(10, this.restoreAction, this.cancelAction);
      return var1;
   }

   @Override
   public void requestFocus() {
      this.browser.requestFocus();
   }

   public Action getRestoreAction() {
      return this.restoreAction;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void retrieveAvailableBackups(ProjectLocation var1) {
      this.updateDestinationPanelWithProjectLocation(var1);
      BackupSelectorUi.AvailableBackupsRetriever var2 = new BackupSelectorUi.AvailableBackupsRetriever(var1);
      KongaSwingWorker.runWhileWaiting(var2, this.waitService);
   }

   private void updateDestinationPanelWithProjectLocation(ProjectLocation var1) {
      File var2 = this.destinationSelector.getSelectedFile();
      if (var2 == null) {
         this.destinationSelector.setSelectedFile(var1.getLocation());
      }
   }

   public void setCancelAction(Action var1) {
      this.cancelAction = var1;
   }

   private FileSelectorPanel createDestinationPanel() {
      FileSelectorPanelFactory var1 = FileSelectorFactory.getFactory();
      FileSelectorPanel var2 = var1.recentFilesPanel(ApplicationFileChooser.getInstance(null), FileSelectorPanel.OPEN, "Restore to:", RecentFileStore.EMPTY);
      var2.setSelectionMode(FileChooser.DIRECTORIES_ONLY);
      LabelDecorator var3 = TextStyles.DefaultText.asLabelDecorator(ApplicationResources.ICONS.getIcon("FolderClosed.16"));
      var2.decorateTitle(var3);
      return var2;
   }

   private class AvailableBackupsRetriever implements ApplicationWorker.SafeRunnable {
      private final ProjectLocation location;

      public AvailableBackupsRetriever(ProjectLocation var2) {
         this.location = var2;
      }

      @Override
      public void run() {
         try {
            IntegrationProjectId var1 = this.location.getProjectId();
            AvailableBackup[] var2 = BackupSelectorUi.this.projectManager.getBackupService().getAvailableBackups(var1);
            this.publishResult(var2);
         } catch (Exception var3) {
            ErrorLog.attention(BackupSelectorUi.class, "An error occurred while trying to locate available backups", var3);
         }
      }

      private void publishResult(AvailableBackup[] var1) {
         if (var1.length > 0) {
            BackupSelectorUi.this.browser.setBackups(var1);
         } else {
            Alert.error("There are no available backups for the project \"" + this.location.getProjectName() + "\".", "No Backups Available");
         }
      }
   }

   private class RestoreAction extends AbstractSingleObjectSelectionAction<AvailableBackup> {
      public RestoreAction() {
         super(AvailableBackup.class, "&Restore");
         this.putValue("ShortDescription", "Restore the selected backup.");
      }

      public void update() {
         this.setEnabled(!this.isSelectionEmpty() && BackupSelectorUi.this.destinationSelector.isInputValid());
      }

      protected boolean isEnabledFor(AvailableBackup var1) {
         return BackupSelectorUi.this.destinationSelector.isInputValid();
      }

      protected void run(AvailableBackup var1, ActionEvent var2) {
         File var3 = BackupSelectorUi.this.destinationSelector.getSelectedFile();
         if (var3 != null) {
            BackupSelectorUi.RestoreWorker var4 = BackupSelectorUi.this.new RestoreWorker(var1, var3);
            KongaSwingWorker.runWhileWaiting(var4, BackupSelectorUi.this.waitService);
         }
      }
   }

   private class RestoreWorker implements ApplicationWorker.SafeRunnable {
      private final AvailableBackup backup;
      private final File destination;

      public RestoreWorker(AvailableBackup var2, File var3) {
         this.backup = var2;
         this.destination = var3;
      }

      @Override
      public void run() {
         try {
            ProjectBackupRestorer var1 = new ProjectBackupRestorer(this.backup, this.destination);
            ProjectLocation var2 = var1.restore();
            this.notifyCallback(var2);
         } catch (RestoreProjectBackupException var3) {
            ErrorLog.attention(BackupSelectorUi.class, var3.getMessage(), var3);
         }
      }

      private void notifyCallback(ProjectLocation var1) {
         synchronized (BackupSelectorUi.this.lock) {
            BackupSelectorUi.this.projectHandler.handle(var1);
         }
      }
   }

   private class ViewProjectDetailsAction extends AbstractSingleObjectSelectionAction<AvailableBackup> {
      public ViewProjectDetailsAction() {
         super(AvailableBackup.class);
         ActionDecorators.VIEW_DETAILS.decorate(this);
         this.putValue("ShortDescription", "View details about the selected backup");
         this.setSelectionSource(BackupSelectorUi.this.browser);
      }

      protected void run(AvailableBackup var1, ActionEvent var2) {
         BackupDetailsViewer var3 = new BackupDetailsViewer(BackupSelectorUi.this.projectManager, var1);
         var3.display();
      }
   }
}
