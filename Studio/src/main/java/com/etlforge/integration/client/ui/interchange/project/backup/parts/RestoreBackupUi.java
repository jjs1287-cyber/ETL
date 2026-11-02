package com.etlforge.integration.client.ui.interchange.project.backup.parts;

import com.etlforge.integration.client.ui.interchange.project.backup.AvailableBackupTransferable;
import com.etlforge.integration.client.ui.interchange.project.backup.ProjectBackupBrowser;
import com.etlforge.integration.client.ui.interchange.project.share.ExportEntityAction;
import com.etlforge.integration.client.ui.interchange.project.share.ProjectTrees;
import com.etlforge.integration.client.ui.interchange.project.share.ShareProjectDataModel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler.TransferSupport;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.KongaLayeredPane;
import org.etlforge.ui.widget.split.KongaSplitPane;

final class RestoreBackupUi implements UiProvider {
   private final BackupPartRestorer controller;
   private final ShareProjectDataModel model;
   private final SelectionAction backupSelectionAction;
   private final SelectionAction restoreEntityAction;
   private final ProjectBackupBrowser browser;
   private final ProjectTrees projectTrees;
   private final KongaLayeredPane ui;

   public RestoreBackupUi(ApplicationWindow var1, BackupPartRestorer var2, ShareProjectDataModel var3, ProjectBackupBrowser var4) {
      this.controller = var2;
      this.model = var3;
      this.backupSelectionAction = new RestoreBackupUi.SelectBackupAction();
      this.restoreEntityAction = new ExportEntityAction(var1, var3, var2.getWorker());
      this.restoreEntityAction.putValue("Name", "Restore Selected Item");
      this.browser = var4;
      this.projectTrees = this.createTrees(var1);
      this.ui = new KongaLayeredPane();
      var4.setDefaultAction(this.backupSelectionAction);
      var3.setEmptySourceTreeText("[Select a backup to restore data from]");
   }

   @Override
   public void decorate(UiCanvas var1) {
      RestoreBackupUi.Layout var2 = new RestoreBackupUi.Layout();
      this.ui.setBaseComponent(var2.getUi());
      var1.display(this.ui);
   }

   @Override
   public void requestFocus() {
      this.browser.requestFocus();
      this.browser.selectFirst();
      this.projectTrees.clearSelection();
   }

   public WaitService getWaitService() {
      return this.ui;
   }

   private ProjectTrees createTrees(ApplicationWindow var1) {
      ProjectTrees var2 = new ProjectTrees(this.model, new RestoreBackupUi.TargetTreeDropHandlerImpl());
      var2.setExportAction(this.restoreEntityAction);
      var2.setSourceDropHandler(new RestoreBackupUi.SourceTreeDropHandlerImpl());
      return var2;
   }

   private class Layout implements PropertyChangeListener {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 5);
      private final KongaSplitPane split = new KongaSplitPane(1);
      private final JPanel buttonPanel;
      private final KongaButton selectBackupButton;
      private final KongaButton restoreEntityButton;

      public Layout() {
         this.split.addPropertyChangeListener("dividerLocation", this);
         this.selectBackupButton = new KongaButton(RestoreBackupUi.this.backupSelectionAction);
         this.restoreEntityButton = new KongaButton(RestoreBackupUi.this.restoreEntityAction);
         this.buttonPanel = this.createButtonPanel();
         this.layoutSplitPane();
         this.layoutButtonPanel();
         this.layout.center(this.split).south(this.buttonPanel);
      }

      public JComponent getUi() {
         return this.layout.container();
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         this.layoutButtonPanel();
         this.getUi().revalidate();
         this.getUi().repaint();
      }

      private JPanel createButtonPanel() {
         JPanel var1 = new JPanel(null);
         var1.add(this.selectBackupButton);
         var1.add(this.restoreEntityButton);
         return var1;
      }

      private void layoutSplitPane() {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder();
         var1.center(RestoreBackupUi.this.browser).withBorder(Empty.border(10, 0, 10, 10)).deeplyNonOpaque();
         this.split.setLeftComponent(var1);
         this.split.setRightComponent(RestoreBackupUi.this.projectTrees.layout());
         this.split.setDividerLocation(300);
      }

      private void layoutButtonPanel() {
         Dimension var1 = this.split.getSize();
         if (var1.width == 0 || var1.height == 0) {
            var1 = this.split.getPreferredSize();
         }

         int var2 = var1.width;
         Dimension var3 = this.selectBackupButton.getPreferredSize();
         Dimension var4 = this.restoreEntityButton.getPreferredSize();
         int var5 = Math.max(var3.height, var4.height);
         this.buttonPanel.setPreferredSize(new Dimension(var2, var5 + 10));
         this.selectBackupButton.setBounds(0, 5, var3.width, var5);
         this.restoreEntityButton.setBounds(this.split.getDividerLocation() + this.split.getDividerSize(), 5, var4.width, var5);
      }
   }

   private class SelectBackupAction extends AbstractSelectionAction {
      private AvailableBackup backup;

      public SelectBackupAction() {
         super("Select Backup");
      }

      @Override
      protected void handleSelectionChange() {
         AvailableBackup[] var1 = RestoreBackupUi.this.browser.getSelectedBackups();
         this.backup = var1.length == 1 ? var1[0] : null;
         this.setEnabled(this.backup != null);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (this.backup != null) {
            RestoreBackupUi.this.controller.loadBackup(this.backup);
         }
      }
   }

   private class SourceTreeDropHandlerImpl implements ProjectTrees.SourceTreeDropHandler {
      private SourceTreeDropHandlerImpl() {
      }

      @Override
      public boolean canImport(TransferSupport var1) {
         return var1.isDataFlavorSupported(AvailableBackupTransferable.FLAVOR);
      }

      @Override
      public boolean importData(TransferSupport var1) {
         try {
            AvailableBackup var2 = (AvailableBackup)var1.getTransferable().getTransferData(AvailableBackupTransferable.FLAVOR);
            RestoreBackupUi.this.controller.loadBackup(var2);
            return true;
         } catch (Exception var3) {
            ErrorLog.log(RestoreBackupUi.class, "An unexpected error occurred while completing the backup drop", var3);
            return false;
         }
      }
   }

   private class TargetTreeDropHandlerImpl implements ProjectTrees.TargetTreeDropHandler {
      private TargetTreeDropHandlerImpl() {
      }

      @Override
      public boolean isValidParent(IntegrationEntity var1, IntegrationEntity var2) {
         EntityType var3 = var2.getEntityType();
         return var3 == EntityType.RootFolder ? false : this.isValidParentImpl(var1, var2);
      }

      private boolean isValidParentImpl(IntegrationEntity var1, IntegrationEntity var2) {
         return var1 instanceof Folder ? ((Folder)var1).isAllowedChild(var2) : var1 instanceof IntegrationProject;
      }

      @Override
      public boolean handleDrop(IntegrationEntity var1, IntegrationEntity var2) {
         if (var1 instanceof IntegrationProject) {
            RestoreBackupUi.this.controller.restoreProject();
            return true;
         } else if (this.isValidParent(var2, var1)) {
            RestoreBackupUi.this.controller.importItem(var1, var2);
            return true;
         } else {
            return false;
         }
      }
   }
}
