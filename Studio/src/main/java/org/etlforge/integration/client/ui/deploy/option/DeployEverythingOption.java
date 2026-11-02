package org.etlforge.integration.client.ui.deploy.option;

import javax.swing.JComponent;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.ui.deploy.DeployDataImpl;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.util.option.OptionSelector;

public final class DeployEverythingOption implements DeployOption {
   private final IntegrationProject project;
   private final DeployLauncher deployLauncher;
   private volatile DeployDataImpl deployData;

   public DeployEverythingOption(IntegrationProject var1, DeployLauncher var2) {
      this.project = var1;
      this.deployLauncher = var2;
   }

   @Override
   public String getId() {
      return "DeployOption.Everything";
   }

   @Override
   public DeployData getDeployData() {
      return this.deployData;
   }

   @Override
   public boolean isAdvanced() {
      return false;
   }

   @Override
   public void setActive(boolean var1) {
   }

   @Override
   public void addAdditionalUi(JComponent var1) {
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(Strings.get("Deploy.Everything.Name"));
   }

   @Override
   public void execute(String var1) {
      this.project.markAsDeployDirty();
      this.deployData = new DeployDataImpl(this.project, false);
      this.deployData.setTag(var1);
      this.deployLauncher.startDeploy(this.deployData);
   }

   @Override
   public boolean requiresValidProject() {
      return true;
   }
}
