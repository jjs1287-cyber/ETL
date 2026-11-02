package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.deploy.DefaultDeployLauncher;
import org.etlforge.integration.client.ui.deploy.DeployLauncher;
import org.etlforge.integration.client.ui.deploy.DeployWorker;
import org.etlforge.integration.client.ui.deploy.ReadOnlyProjectMessage;
import org.etlforge.integration.client.ui.deploy.page.DeployPage;
import org.etlforge.integration.client.ui.entity.ProjectPageGroup;
import org.etlforge.integration.client.ui.interchange.deploy.DeployPageFactory;
import org.etlforge.integration.data.project.IntegrationProject;

public final class DeployProjectAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.Deploy";
   private static final String DEPLOY_PAGE_ID = "interchange-deploypage";
   private final EditorService editorService;
   private final DeployWorker deployWorker;
   private final SaveAllGate saveGate;

   public DeployProjectAction(CurrentProjectService var1, DeployWorker var2, EditorService var3, SaveAllGate var4) {
      super(var1, "action.ic.Deploy");
      this.deployWorker = var2;
      this.editorService = var3;
      this.saveGate = var4;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (DeployProjectAction.this.isProjectDeployable()) {
               DeployProjectAction.this.saveGate.saveAndRun(DeployProjectAction.this.new DeployPageLauncher());
            } else {
               ReadOnlyProjectMessage.show();
            }
         }
      });
   }

   private boolean isProjectDeployable() {
      IntegrationProject var1 = this.getProject();
      return var1.isDeployable();
   }

   private class DeployPageLauncher implements Runnable {
      private DeployPage deployPage;

      private DeployPageLauncher() {
      }

      @Override
      public void run() {
         boolean var1 = false;
         this.deployPage = this.lookForOpenPage();
         if (this.deployPage == null) {
            this.createPage();
         } else {
            var1 = true;
         }

         this.displayPage(var1);
      }

      private void createPage() {
         DeployPageFactory var1 = new DeployPageFactory(DeployProjectAction.this.getProject());
         DeployLauncher var2 = this.createLauncher();
         this.deployPage = var1.createPage(var2, "interchange-deploypage");
      }

      private DeployLauncher createLauncher() {
         DefaultDeployLauncher var1 = new DefaultDeployLauncher(DeployProjectAction.this.deployWorker, DeployProjectAction.this.getProject());
         var1.setHistory(this.getHistory());
         return var1;
      }

      private DeployHistory getHistory() {
         return DeployProjectAction.this.getManagedProject().getDeployHistory();
      }

      private DeployPage lookForOpenPage() {
         for (Editor var4 : DeployProjectAction.this.editorService.getOpenEditors()) {
            if (var4.getId().equals("interchange-deploypage")) {
               return (DeployPage)var4;
            }
         }

         return null;
      }

      private void displayPage(boolean var1) {
         if (var1) {
            this.deployPage.refresh();
         }

         DeployProjectAction.this.editorService.openEditor(this.deployPage, ProjectPageGroup.get(DeployProjectAction.this.getProject()));
      }
   }
}
