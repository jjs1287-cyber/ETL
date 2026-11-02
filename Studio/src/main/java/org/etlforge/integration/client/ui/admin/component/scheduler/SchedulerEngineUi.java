package org.etlforge.integration.client.ui.admin.component.scheduler;

import javax.swing.Icon;
import org.etlforge.integration.client.admin.Admin;
import org.etlforge.integration.client.admin.engine.Scheduler;
import org.etlforge.integration.client.ui.admin.engine.AbstractEngineUi;

public final class SchedulerEngineUi extends AbstractEngineUi {
   public SchedulerEngineUi(Admin var1) {
      super(new Scheduler(), var1);
   }

   @Override
   public String getId() {
      return "SchedulerEngineConsole";
   }

   @Override
   public String getTitle() {
      return "Schedule &Engine";
   }

   @Override
   protected String getStatusPanelTitle() {
      return Strings.get("Scheduler.StatusPanel.Title");
   }

   @Override
   protected Icon getStatusPanelIcon() {
      return Icons.get("Scheduler.StatusPanel");
   }

   @Override
   protected String getActionPanelTitle() {
      return Strings.get("Scheduler.ActionPanel.Title");
   }

   @Override
   protected Icon getActionPanelIcon() {
      return Icons.get("Scheduler.ActionPanel");
   }

   @Override
   protected Icon getStartEngineIcon() {
      return Icons.get("Scheduler.ActionPanel.Start");
   }

   @Override
   protected Icon getStopEngineIcon() {
      return Icons.get("Scheduler.ActionPanel.Stop");
   }
}
