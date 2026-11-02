package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.util.Date;
import javax.swing.Icon;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.ui.admin.core.console.AdminConsolePart;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class ScheduledRunsPart implements AdminConsolePart {
   private final ScheduledRunsModel model = new ScheduledRunsModel();
   private final ScheduledRunsView view;

   public ScheduledRunsPart(ProjectAdministration var1) {
      this.view = new ScheduledRunsView(this.model, var1);
      new ScheduledRunsController(var1, this.model, this.view);
   }

   @Override
   public String getId() {
      return "ScheduledRuns";
   }

   @Override
   public Icon getIcon() {
      return null;
   }

   @Override
   public String getTitle() {
      return EnterpriseAdminResources.getString("ScheduledRuns.Title");
   }

   @Override
   public void serverConnectionChanged(ServerConnectionEvent var1) {
   }

   @Override
   public void populatePopupMenu(KongaPopupMenu var1) {
   }

   @Override
   public void dispose() {
   }

   @Override
   public void setActive(boolean var1) {
      if (this.model.getDay() == null) {
         this.model.setDay(new Date());
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.view.setCaptionBorder(Empty.border(5, 3, 0, 3));
      this.view.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.view.requestFocus();
   }
}
