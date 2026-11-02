package org.etlforge.integration.client.ui.interchange.entity.transformation;

import java.awt.Dimension;
import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.GenericEditor;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardDialogDisplayer;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.project.copy.EntityCopyMachine;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.ui.event.ApplicationMessageEvent;

public final class TransformationEditorWizard extends Wizard<Void> {
   private final Transformation transformation;
   private final ApplicationWindow appWin;
   private final StructureEditorStartPage page;
   private final CountDownLatch backupLatch;
   private Transformation backup;

   public TransformationEditorWizard(Transformation var1, ApplicationWindow var2) {
      this.transformation = var1;
      this.appWin = var2;
      this.page = new StructureEditorStartPage(this);
      this.backupLatch = new CountDownLatch(1);
      this.addPages();
      this.createDisplayer();
   }

   public Transformation getBackup() throws InterruptedException {
      if (!this.page.isBackupRequested()) {
         return null;
      }

      this.backupLatch.await();
      return this.backup;
   }

   public boolean isAutmaticMigrationSelected() {
      return this.page.isAutomaticMigrationSelected();
   }

   private void addPages() {
      WizardPageList var1 = new WizardPageList();
      var1.addPage(this.page);
      this.setPageChain(var1);
   }

   private void createDisplayer() {
      WizardDialogDisplayer var1 = new WizardDialogDisplayer(this.appWin.startWait());
      var1.setManageLocation(TransformationEditorWizard.class);
      var1.setMinimumSize(new Dimension(600, 600));
      var1.setWizard(this);
      this.setDisplayer(var1);
   }

   @Override
   protected boolean onWizardCancelled() {
      return true;
   }

   protected Void finishWizard() {
      if (this.page.isBackupRequested()) {
         this.doBackup();
      }

      return null;
   }

   private void doBackup() {
      EntityCopyMachine var1 = new EntityCopyMachine();
      Transformation var2 = (Transformation)var1.createAndInsertCopy(this.transformation);
      this.backup = var2;
      this.backupLatch.countDown();
      this.saveBackupToDisk(var2);
   }

   private void saveBackupToDisk(IntegrationEntity var1) {
      TransformationEditorWizard.BackupSaver var2 = new TransformationEditorWizard.BackupSaver(var1);
      var2.execute();
   }

   private final class BackupSaver extends KongaSwingWorker<Void, Void> {
      private final IntegrationEntity backup;

      public BackupSaver(IntegrationEntity var2) {
         this.backup = var2;
         this.setErrorMessage(Strings.get("StructureEditor.Wizard.SaveError"));
      }

      protected void onSuccess(Void var1) {
         this.makeEditedTransformationActive();
         this.notifyUserThatBackupWasCreated(this.backup);
         this.makeBackupPageClean();
      }

      protected Void doInBackground() throws Exception {
         IntegrationProjectLock.acquire();

         try {
            ManagedProject var1 = ProjectUtils.getManagedProject(this.backup);
            var1.saveIntegrationEntity(this.backup);
            return null;
         } finally {
            IntegrationProjectLock.release();
         }
      }

      private void makeEditedTransformationActive() {
         Editor var1 = UiCompanion.of(TransformationEditorWizard.this.transformation).findOpenPage();
         TransformationEditorWizard.this.appWin.getEditorService().openEditor(var1);
      }

      private void notifyUserThatBackupWasCreated(IntegrationEntity var1) {
         ApplicationMessageEvent var2 = new ApplicationMessageEvent(TransformationEditor.class, this.getBackupCreatedMessage(var1));
         TransformationEditorWizard.this.appWin.postApplicationMessageEvent(var2);
      }

      private String getBackupCreatedMessage(IntegrationEntity var1) {
         return Strings.format("StructureEditor.Wizard.Backup.Message", var1.getName());
      }

      private void makeBackupPageClean() {
         Editor var1 = UiCompanion.of(this.backup).findOpenPage();
         if (var1 instanceof GenericEditor) {
            ((GenericEditor)var1).setClean();
         }
      }
   }
}
