package org.etlforge.integration.client.ui.interchange.deploy;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.client.ui.deploy.page.DeployPage;
import org.etlforge.integration.data.project.IntegrationProject;

public final class DeployPageFactory {
   private final IntegrationProject project;

   public DeployPageFactory(IntegrationProject var1) {
      Preconditions.checkNotNull(var1, "project");
      this.project = var1;
   }

   public DeployPage createPage(DeployLauncher var1, String var2) {
      ManagedProject var3 = this.project.getExtensionObject(ManagedProject.class);
      DeployPageFactory.LauncherWrapper var4 = new DeployPageFactory.LauncherWrapper(var1);
      DeployPage var5 = new DeployPage(var2, var3, var4);
      var4.setPage(var5);
      return var5;
   }

   private static final class LauncherWrapper implements DeployLauncher {
      private final DeployLauncher impl;
      private DeployPage page;

      public LauncherWrapper(DeployLauncher var1) {
         this.impl = var1;
      }

      public void setPage(DeployPage var1) {
         this.page = var1;
      }

      @Override
      public void startDeploy(DeployData var1) {
         this.impl.startDeploy(var1);
         this.page.close();
      }
   }
}
