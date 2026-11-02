package org.etlforge.integration.client.ui.admin.component.project;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.project.info.ProjectInfoProvider;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;
import org.etlforge.integration.client.ui.project.info.ProjectsInfoDownloader;
import org.etlforge.integration.client.ui.project.info.resources.ProjectInfoUiResources;

final class DownloadProjectListAction extends AbstractAction {
   private final ProjectConsoleModel model;
   private final ProjectInfoProvider infoProvider;
   private ProjectInfoCache cache = ProjectInfoCache.EMPTY;

   public DownloadProjectListAction(ProjectConsoleModel var1, ProjectInfoProvider var2) {
      super(ProjectInfoUiResources.getString("DownloadInfoAction.Name"), ProjectInfoUiResources.getIcon("DownloadProjectInfo"));
      this.model = var1;
      this.infoProvider = var2;
      this.putValue("SwingLargeIconKey", ProjectInfoUiResources.getIcon("DownloadProjectInfo.32"));
      this.putValue("ShortDescription", ProjectInfoUiResources.getString("DownloadInfoAction.ToolTip"));
   }

   public void setCache(ProjectInfoCache var1) {
      this.cache = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (!this.model.isDownloadingInfo()) {
         ProjectsInfoDownloader var2 = new ProjectsInfoDownloader(this.model, this.infoProvider);
         var2.setCache(this.cache);
         var2.submit();
      }
   }
}
