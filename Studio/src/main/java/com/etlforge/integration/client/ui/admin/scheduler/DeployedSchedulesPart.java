package com.etlforge.integration.client.ui.admin.scheduler;

import com.google.common.base.Predicate;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import javax.swing.Icon;
import javax.swing.JPanel;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.project.info.ProjectInfoProvider;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.component.project.ProjectConsoleActions;
import org.etlforge.integration.client.ui.admin.component.project.ProjectConsoleController;
import org.etlforge.integration.client.ui.admin.core.console.AdminConsolePart;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleTreeView;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleView;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.popup.KongaPopupMenu;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class DeployedSchedulesPart implements AdminConsolePart {
   private final ProjectConsoleView console;
   private final ProjectConsoleController controller;

   public DeployedSchedulesPart(ApplicationWindow var1, ProjectConsoleActions var2, ServerConnectionService var3, ProjectInfoProvider var4) {
      this(var1, var2, var3, var4, ProjectInfoCache.EMPTY);
   }

   public DeployedSchedulesPart(
      ApplicationWindow var1, ProjectConsoleActions var2, ServerConnectionService var3, ProjectInfoProvider var4, ProjectInfoCache var5
   ) {
      this.console = this.createConsole(var1);
      this.controller = new ProjectConsoleController(this.console, var2, var3, var4);
      this.controller.setConsoleActionFilter("schedule");
      this.controller.setDownloadActionName(EnterpriseAdminResources.getString("DeployedSchedules.Download.Name"));
      this.controller.setDownloadActionDescription(EnterpriseAdminResources.getString("DeployedSchedules.Download.ToolTip"));
      this.controller.setCache(var5);
      this.controller.activate();
   }

   @Override
   public String getId() {
      return "DeployedSchedules";
   }

   @Override
   public void serverConnectionChanged(ServerConnectionEvent var1) {
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public String getTitle() {
      return EnterpriseAdminResources.getString("DeployedSchedules.Title");
   }

   @Override
   public void populatePopupMenu(KongaPopupMenu var1) {
   }

   @Override
   public void decorate(UiCanvas var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(this.console);
      var2.setOpaque(false);
      var2.setBorder(Empty.border(10));
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.console.requestFocus();
   }

   @Override
   public void dispose() {
      this.console.dispose();
      this.controller.dispose();
   }

   @Override
   public void setActive(boolean var1) {
   }

   private ProjectConsoleView createConsole(ApplicationWindow var1) {
      ProjectConsoleTreeView var2 = new ProjectConsoleTreeView(var1);
      var2.setFilterControl(new DeployedSchedulesTreeFilter());
      var2.setExpansionFilter(new DeployedSchedulesPart.ExpansionFilter());
      String var3 = EnterpriseAdminResources.getMnemonicString("DeployedSchedules.Title").getText();
      var2.setTitle(var3);
      return var2;
   }

   private static class ExpansionFilter implements Predicate<KongaTreeNode> {
      private ExpansionFilter() {
      }

      public boolean apply(KongaTreeNode var1) {
         Object var2 = var1.getUserObject();
         if (var2 instanceof IntegrationProject) {
            return true;
         } else if (var2 instanceof RootFolder) {
            EntityType var3 = ((RootFolder)var2).getItemType();
            return var3 == EntityType.Schedule;
         } else {
            return false;
         }
      }
   }
}
