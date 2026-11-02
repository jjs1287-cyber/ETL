package org.etlforge.integration.client.ui.deploy.option;

import javax.swing.JComponent;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.ui.deploy.DeployDataImpl;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.util.option.OptionSelector;

public final class DeployDirtyItemsOption implements DeployOption {
   private final IntegrationProject project;
   private final DeployLauncher deployLauncher;
   private volatile DeployDataImpl deployData;

   public DeployDirtyItemsOption(IntegrationProject var1, DeployLauncher var2) {
      this.project = var1;
      this.deployLauncher = var2;
   }

   @Override
   public String getId() {
      return "DeployOption.DirtyItems";
   }

   @Override
   public DeployData getDeployData() {
      return this.deployData;
   }

   @Override
   public boolean isAdvanced() {
      return true;
   }

   @Override
   public void setActive(boolean var1) {
   }

   @Override
   public void addAdditionalUi(JComponent var1) {
   }

   @Override
   public void decorate(OptionSelector var1) {
      var1.setText(Strings.get("Deploy.DirtyItems.Name"));
   }

   @Override
   public void execute(String var1) {
      this.deployData = new DeployDataImpl(this.project, false);
      this.deployData.setTag(var1);
      if (this.isDeployDirty(this.deployData)) {
         this.deployLauncher.startDeploy(this.deployData);
      } else {
         Alert.info(Strings.get("Deploy.DirtyItems.NothingToDeploy"), Strings.get("Deploy.DirtyItems.NothingToDeploy.Title"));
      }
   }

   @Override
   public boolean requiresValidProject() {
      return true;
   }

   private boolean isDeployDirty(DeployData var1) {
      return var1.getProject().doesAtLeastOneItemNeedDeploy() || this.atLeastOneItemIsDeleted(var1);
   }

   private boolean atLeastOneItemIsDeleted(DeployData var1) {
      for (EntityType var5 : EntityType.values()) {
         if (!var1.getDeletedItemsOfType(var5).isEmpty()) {
            return true;
         }
      }

      return false;
   }
}
