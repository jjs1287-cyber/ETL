package org.etlforge.integration.client.connect.wizard;

import java.awt.EventQueue;
import java.util.List;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.ui.wait.WaitLock;

public abstract class AbstractConnectWizard<S extends DataStructure, T extends DataStructure> extends ApplicationWindowWizard<Operation> {
   private final String name;
   private final IntegrationProject project;
   private final Operation operation;
   private final Folders folders;
   private volatile Transformation transformation;
   private final ConnectWizardPageChain<S, T> pages;

   protected AbstractConnectWizard(String var1, ApplicationWindow var2, Operation var3, EntityExplorerSupport var4) {
      super(var2);
      this.name = var1;
      this.project = var4.getProject();
      this.operation = var3;
      this.folders = new Folders(var4.getProject(), var1);
      this.pages = this.createPages(this.folders, var4);
      this.setFinishWizardOnEventThread(false);
   }

   protected abstract ConnectWizardPageChain<S, T> createPages(Folders var1, EntityExplorerSupport var2);

   public final String getName() {
      return this.name;
   }

   @Override
   public final void start() {
      final WaitLock var1 = this.getWaitService().startWait();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AbstractConnectWizard.this.startImpl(var1);
         }
      });
   }

   private void startImpl(WaitLock var1) {
      try {
         this.setPageChain(this.pages);
         this.getDisplayer().setSection("Source");
         super.start();
      } finally {
         var1.release();
      }
   }

   protected final Operation finishWizard() throws WizardPageException {
      this.configureOperation();
      return this.operation;
   }

   private void configureOperation() {
      OperationBuilder var1 = this.createOperationBuilder(this.project, this.operation, this.folders, this.pages);
      var1.build();
      this.transformation = var1.getTransformation();
   }

   protected abstract OperationBuilder<S, T> createOperationBuilder(IntegrationProject var1, Operation var2, Folders var3, ConnectWizardPageChain<S, T> var4);

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }

   public final Operation getOperation() {
      return this.operation;
   }

   public final Transformation getTransformation() {
      return this.transformation;
   }

   public final Folders getFolders() {
      return this.folders;
   }

   public final List<Folder> getCreatedFolders() {
      return this.folders.getCreatedFolders();
   }

   public final boolean isAutoMapSelected() {
      return this.pages.isAutoMapSelected();
   }

   @Override
   public final boolean canFinish() {
      WizardPage var1 = this.getCurrentPage();
      if (var1 != null && var1.canContinue()) {
         WizardPageChain var2 = this.getPageChain();
         return var2.canFinish(var1);
      } else {
         return false;
      }
   }
}
