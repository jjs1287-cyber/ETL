package com.etlforge.integration.client.ui.admin.project.backup;

import javax.swing.Action;
import javax.swing.Icon;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleSubView;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.ComponentPainter;
import org.etlforge.ui.widget.OverlayMessagePainter;

public final class AvailableProjectBackupsView implements ProjectConsoleSubView {
   private final IntegrationProject project;
   private final ProjectBackupDescriptorTable table;
   private Action refreshAction;
   private SelectionAction downloadBackupAction;
   private Action closeAction;

   public static String constructId(IntegrationProject var0) {
      return "Backups." + var0.getID();
   }

   public AvailableProjectBackupsView(IntegrationProject var1) {
      this.project = var1;
      this.table = new ProjectBackupDescriptorTable();
      this.table.setEmptyTablePainter(this.createEmptyTablePainter());
   }

   @Override
   public String getId() {
      return constructId(this.project);
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public String getTitle() {
      return ProjectAdminBackupUiResources.getFormattedMessage("BackupView.Title", this.project.getName());
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.getControlPanel()).center(this.table);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public void setBackups(ProjectBackupDescriptor[] var1) {
      this.table.setBackups(var1);
   }

   public void refresh() {
      if (this.refreshAction != null) {
         ActionUtils.run(this.refreshAction, this, "refresh-backup-list");
      }
   }

   public void setRefreshAction(Action var1) {
      this.refreshAction = var1;
   }

   public void setDownloadSelectedBackupAction(SelectionAction var1) {
      var1.setSelectionSource(this.table);
      this.downloadBackupAction = var1;
   }

   public void setCloseAction(Action var1) {
      this.closeAction = var1;
   }

   private ComponentPainter createEmptyTablePainter() {
      OverlayMessagePainter var1 = new OverlayMessagePainter(ProjectAdminBackupUiResources.getString("BackupView.Empty"));
      var1.setTextStyle(TextStyles.AdminHintBold);
      return var1;
   }

   private Object getControlPanel() {
      BoxBuilder var1 = Horizontal.box();
      var1.addAllWithSpace(10, this.refreshAction, this.downloadBackupAction);
      if (this.closeAction != null) {
         var1.glue().add(this.closeAction);
      }

      return var1.withBorder(new BorderBuilder().etched().empty(5));
   }
}
