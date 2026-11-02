package com.etlforge.integration.client.ui.project.download.console;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.admin.project.ProjectDownloader;
import com.etlforge.integration.client.admin.project.ProjectDownloaderCallback;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.info.ProjectInfoProvider;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleModel;

public final class DownloadProjectConsoleFactory {
   private final ProjectManager projectManager;
   private final ServerManager serverManager;
   private final ProjectInfoProvider infoProvider;
   private final ProjectDownloader downloader;

   public DownloadProjectConsoleFactory(ProjectManager var1, ServerManager var2, ProjectInfoProvider var3, ProjectDownloader var4) {
      this.projectManager = var1;
      this.serverManager = var2;
      this.infoProvider = var3;
      this.downloader = var4;
   }

   public DownloadProjectConsole newConsole(ProjectConsoleModel var1, ProjectDownloaderCallback var2, Supplier<Boolean> var3) {
      DownloadProjectController var4 = this.createController(var1, var2, var3);
      DownloadProjectConsole var5 = this.createConsole(var1);
      var5.setController(var4);
      var4.setConsole(var5);
      return var5;
   }

   private DownloadProjectController createController(ProjectConsoleModel var1, ProjectDownloaderCallback var2, Supplier<Boolean> var3) {
      DownloadProjectController var4 = new DownloadProjectController(var1, this.serverManager, this.infoProvider, this.downloader, var3);
      var4.addCallback(var2);
      return var4;
   }

   private DownloadProjectConsole createConsole(ProjectConsoleModel var1) {
      DownloadProjectConsole var2 = new DownloadProjectConsole(var1);
      var2.setDefaultDownloadLocation(this.projectManager.getProjectDirectory().getLocation());
      return var2;
   }
}
