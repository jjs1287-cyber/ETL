package com.etlforge.integration.client.ui.interchange.launcher;

import com.etlforge.integration.client.ui.interchange.project.backup.complete.BackupSelector;
import com.etlforge.integration.client.ui.interchange.resources.EnterpriseInterchangeResources;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.project.recent.RecentProjectsViewer;
import org.etlforge.integration.client.ui.project.recent.RecentProjectsViewerAction;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class RestoreFromBackupAction extends AbstractSingleObjectSelectionAction<ProjectLocation> implements RecentProjectsViewerAction {
   private final BackupSelector selector;
   private final Receiver<ProjectLocation> loader;

   public RestoreFromBackupAction(BackupSelector var1, Receiver<ProjectLocation> var2) {
      super(ProjectLocation.class, EnterpriseInterchangeResources.getString("Launcher.RestoreBackup.Name"), ClientIcons.BACKUP_16);
      this.putValue("ShortDescription", EnterpriseInterchangeResources.getString("Launcher.RestoreBackup.ToolTip"));
      this.selector = var1;
      this.loader = var2;
   }

   @Override
   public void setViewer(RecentProjectsViewer var1) {
      this.setSelectionSource(var1);
   }

   protected void run(final ProjectLocation var1, ActionEvent var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RestoreFromBackupAction.this.selectAndRestoreBackup(var1);
         }
      });
   }

   private void selectAndRestoreBackup(ProjectLocation var1) {
      ProjectLocation var2 = this.selector.selectBackup(var1);
      if (var2 != null) {
         this.loader.handle(var2);
      }
   }
}
