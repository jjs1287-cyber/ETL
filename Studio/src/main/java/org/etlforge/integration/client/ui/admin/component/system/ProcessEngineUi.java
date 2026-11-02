package org.etlforge.integration.client.ui.admin.component.system;

import javax.swing.Icon;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.engine.ProcessEngine;
import org.etlforge.integration.client.ui.admin.engine.AbstractEngineUi;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;

public final class ProcessEngineUi extends AbstractEngineUi {
   public ProcessEngineUi(Admin var1) {
      super(new ProcessEngine(), var1);
   }

   @Override
   public String getId() {
      return "ProcessEngineConsole";
   }

   @Override
   public String getTitle() {
      return "Process &Engine";
   }

   @Override
   public Icon getIcon() {
      return AdminViewResources.ICONS.getIcon("System.Engine");
   }

   @Override
   protected String getStatusPanelTitle() {
      return AdminViewResources.getString("System.StatusPanel.Title");
   }

   @Override
   protected Icon getStatusPanelIcon() {
      return AdminViewResources.ICONS.getIcon("System.StatusPanel");
   }

   @Override
   protected String getActionPanelTitle() {
      return AdminViewResources.getString("System.ActionPanel.Title");
   }

   @Override
   protected Icon getActionPanelIcon() {
      return AdminViewResources.ICONS.getIcon("System.ActionPanel");
   }

   @Override
   protected Icon getStartEngineIcon() {
      return AdminViewResources.ICONS.getIcon("System.ActionPanel.Stop");
   }

   @Override
   protected Icon getStopEngineIcon() {
      return AdminViewResources.ICONS.getIcon("System.ActionPanel.Start");
   }
}
