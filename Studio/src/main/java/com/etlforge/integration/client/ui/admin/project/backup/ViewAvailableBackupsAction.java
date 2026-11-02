package com.etlforge.integration.client.ui.admin.project.backup;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.project.info.CloseSubViewAction;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleView;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleViewAction;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public class ViewAvailableBackupsAction extends AbstractSingleObjectSelectionAction<IntegrationProject> implements ProjectConsoleViewAction {
   private ProjectConsoleView view;

   public ViewAvailableBackupsAction() {
      super(IntegrationProject.class, ProjectAdminBackupUiResources.getString("ListBackups.Action.Name"), ClientIcons.BACKUP_16);
      this.putValue("KongaMediumIconKey", ClientIcons.BACKUP_24);
      this.putValue("SwingLargeIconKey", ClientIcons.BACKUP_32);
      this.putValue("ShortDescription", ProjectAdminBackupUiResources.getString("ListBackups.Action.ToolTip"));
   }

   protected void run(IntegrationProject var1, ActionEvent var2) {
      String var3 = AvailableProjectBackupsView.constructId(var1);
      AvailableProjectBackupsView var4 = (AvailableProjectBackupsView)this.view.getSubView(var3);
      if (var4 == null) {
         var4 = this.createView(var1);
      }

      this.view.showSubView(var4);
      this.updateBackupList(var4);
   }

   @Override
   public void setView(ProjectConsoleView var1) {
      this.view = var1;
   }

   private AvailableProjectBackupsView createView(IntegrationProject var1) {
      AvailableProjectBackupsViewBuilder var2 = new AvailableProjectBackupsViewBuilder(var1);
      var2.setWaitService(this.view.getWaitService());
      AvailableProjectBackupsView var3 = var2.createView();
      var3.setCloseAction(new CloseSubViewAction(this.view, var3));
      return var3;
   }

   private void updateBackupList(final AvailableProjectBackupsView var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            var1.refresh();
         }
      });
   }
}
