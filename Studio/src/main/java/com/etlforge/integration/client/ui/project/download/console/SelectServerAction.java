package com.etlforge.integration.client.ui.project.download.console;

import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.icons.ClientIcons;

final class SelectServerAction extends AbstractAction {
   private DownloadProjectController controller;

   public SelectServerAction() {
      super(ProjectDownloadResources.getString("Console.Server.Name"), ClientIcons.SERVER_16);
      this.putValue("ShortDescription", ProjectDownloadResources.getString("Console.Server.ToolTip"));
      this.setEnabled(false);
   }

   public void setController(DownloadProjectController var1) {
      this.controller = var1;
      this.setEnabled(var1 != null);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.controller != null) {
         this.controller.login();
      }
   }
}
