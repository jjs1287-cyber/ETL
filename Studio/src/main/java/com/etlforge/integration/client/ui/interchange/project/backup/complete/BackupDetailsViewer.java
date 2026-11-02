package com.etlforge.integration.client.ui.interchange.project.backup.complete;

import java.text.DateFormat;
import javax.swing.JPanel;
import org.etlforge.application.Application;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.widget.tree.TreePanel;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTree;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModel;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeModelFactory;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.LayeredKongaDialog;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;

public final class BackupDetailsViewer {
   private final ProjectManager projectManager;
   private final AvailableBackup backup;
   private final IntegrationEntityTree projectTree;
   private final TreePanel treePanel;

   public BackupDetailsViewer(ProjectManager var1, AvailableBackup var2) {
      this.backup = var2;
      this.projectManager = var1;
      this.projectTree = new IntegrationEntityTree(new IntegrationEntityTreeModel("Wait while loading the backup..."));
      this.treePanel = new TreePanel(getTitle(var2), this.projectTree);
      this.treePanel.setRootVisible(true);
   }

   public void display() {
      KongaDialog var1 = this.createDialog();
      var1.setVisible(true);
   }

   private static String getTitle(AvailableBackup var0) {
      String var1 = var0.getLocation().getProjectName();
      DateFormat var2 = DateFormat.getDateTimeInstance(2, 3);
      return var1 + " [Created: " + var2.format(var0.getTimestamp()) + "]";
   }

   private KongaDialog createDialog() {
      LayeredKongaDialog var1 = new LayeredKongaDialog(UiUtils.getActiveWindow(), "Backup Details") {
         @Override
         protected void onDialogOpened() {
            super.onDialogOpened();
            BackupDetailsViewer.Worker var1x = BackupDetailsViewer.this.new Worker(this, BackupDetailsViewer.this.backup, this.startWait());
            var1x.execute();
         }
      };
      var1.getDefaultLayout().withVGap(5);
      var1.getDefaultLayout().north("Backup contents:");
      var1.getDefaultLayout().center(this.treePanel);
      JPanel var2 = var1.getButtonPanel(KongaDialog.CLOSE_ONLY, 10);
      var2.setBorder(Empty.border(5, 0, 10, 0));
      var1.getDefaultLayout().south(var2);
      var1.getDefaultLayout().withBorder(Empty.border(10));
      var1.pack();
      var1.manageLocation(BackupDetailsViewer.class);
      return var1;
   }

   private class Worker extends KongaSwingWorker<IntegrationEntityTreeModel, Void> {
      private final AvailableBackup backup;
      private final KongaDialog dialog;
      private final WaitLock waitLock;

      public Worker(KongaDialog var2, AvailableBackup var3, WaitLock var4) {
         this.dialog = var2;
         this.backup = var3;
         this.waitLock = var4;
      }

      protected IntegrationEntityTreeModel doInBackground() throws Exception {
         IntegrationProjectLock.acquire();

         try {
            ProjectLocation var1 = this.backup.getLocation();
            IntegrationProject var2 = BackupDetailsViewer.this.projectManager.inspectProjectData(var1.getProjectName(), var1.getLocation());
            return this.createTreeModel(var2);
         } finally {
            IntegrationProjectLock.release();
         }
      }

      private IntegrationEntityTreeModel createTreeModel(IntegrationProject var1) {
         IntegrationEntityTreeModelFactory var2 = new IntegrationEntityTreeModelFactory(Application.getBean("ic.EntityTypeCategories", Iterable.class));
         return var2.newModel(var1);
      }

      @Override
      protected void finished() {
         this.waitLock.release();
      }

      protected void onSuccess(IntegrationEntityTreeModel var1) {
         BackupDetailsViewer.this.projectTree.setModel(var1);
         BackupDetailsViewer.this.projectTree.expandAll();
      }

      @Override
      protected void onFailure(Throwable var1) {
         this.dialog.dispose();
      }
   }
}
