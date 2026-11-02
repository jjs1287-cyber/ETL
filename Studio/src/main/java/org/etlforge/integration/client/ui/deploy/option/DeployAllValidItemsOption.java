package org.etlforge.integration.client.ui.deploy.option;

import java.util.Set;
import javax.swing.JComponent;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.deploy.DeployValidItemsCollector;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.deploy.DeployDataImpl;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.util.option.OptionSelector;

public final class DeployAllValidItemsOption implements DeployOption {
   private final ManagedProject managedProject;
   private final DeployLauncher deployLauncher;
   private volatile DeployDataImpl deployData;

   public DeployAllValidItemsOption(ManagedProject var1, DeployLauncher var2) {
      this.managedProject = var1;
      this.deployLauncher = var2;
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(Strings.get("Deploy.ValidItems.Name"));
   }

   @Override
   public String getId() {
      return "DeployOption.ValidItems";
   }

   @Override
   public DeployData getDeployData() {
      return this.deployData;
   }

   @Override
   public void execute(String var1) {
      Set var2 = this.getItemsToDeploy();
      if (var2.isEmpty()) {
         Alert.error("There are no valid items to deploy.", "Nothing to deploy");
      } else {
         IntegrationProject var3 = IntegrationProject.createPartialProject(this.managedProject.getProject(), var2);
         this.deployData = new DeployDataImpl(this.managedProject, var3, true);
         this.deployData.setTag(var1);
         this.deployLauncher.startDeploy(this.deployData);
      }
   }

   private Set<IntegrationEntity> getItemsToDeploy() {
      DeployValidItemsCollector var1 = new DeployValidItemsCollector(
         this.managedProject.getProject(), this.managedProject.getDependencyStore(), this.managedProject.getValidationModel()
      );
      return var1.getItemsToDeploy();
   }

   @Override
   public void setActive(boolean var1) {
   }

   @Override
   public boolean requiresValidProject() {
      return false;
   }

   @Override
   public boolean isAdvanced() {
      return true;
   }

   @Override
   public void addAdditionalUi(JComponent var1) {
   }
}
