package org.etlforge.integration.client.ui.interchange.cli;

import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.deploy.DefaultDeployLauncher;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.client.ui.deploy.DeployWorker;
import org.etlforge.integration.client.ui.deploy.option.DeployOption;
import org.etlforge.integration.client.ui.interchange.core.action.SaveAllGate;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.lang.KongaStringUtils;

public abstract class AbstractDeployProjectExecutor implements CommandExecutor {
   private final CurrentProjectService currentProjectService;
   private final DeployWorker deployWorker;
   private final SaveAllGate saveGate;
   private ManagedProject project;

   protected AbstractDeployProjectExecutor(CurrentProjectService var1, DeployWorker var2, SaveAllGate var3) {
      this.currentProjectService = var1;
      this.deployWorker = var2;
      this.saveGate = var3;
   }

   @Override
   public final void execute(String[] var1) throws CommandException {
      this.project = this.currentProjectService.getManagedProject();
      if (this.project == null) {
         throw new CommandException("No project open.");
      }

      this.saveGate.saveAndRun(new AbstractDeployProjectExecutor.Runner(var1));
   }

   protected abstract DeployOption createOption(IntegrationProject var1, DeployLauncher var2);

   private final class Runner implements Runnable {
      private final String[] params;

      public Runner(String[] var2) {
         this.params = var2;
      }

      @Override
      public void run() {
         if (this.isValid()) {
            DeployOption var1 = AbstractDeployProjectExecutor.this.createOption(
               AbstractDeployProjectExecutor.this.project.getProject(), this.createDeployLauncher()
            );
            String var2 = this.getTag(this.params);
            var1.execute(var2);
         }
      }

      private boolean isValid() {
         ValidationModel var1 = AbstractDeployProjectExecutor.this.project.getValidationModel();
         if (var1.getStatus(AbstractDeployProjectExecutor.this.project.getProject(), true) == ValidationStatus.INVALID) {
            NonBlockingAlert.error("The project contains invalid items.", "Cannot deploy");
            return false;
         } else {
            return true;
         }
      }

      private DeployLauncher createDeployLauncher() {
         DefaultDeployLauncher var1 = new DefaultDeployLauncher(
            AbstractDeployProjectExecutor.this.deployWorker, AbstractDeployProjectExecutor.this.project.getProject()
         );
         var1.setHistory(AbstractDeployProjectExecutor.this.project.getDeployHistory());
         return var1;
      }

      private String getTag(String[] var1) {
         return var1.length == 0 ? null : this.removeQuotes(var1[0]);
      }

      private String removeQuotes(String var1) {
         return KongaStringUtils.removeStartAndEndIfBothPresent(var1, "\"");
      }
   }
}
