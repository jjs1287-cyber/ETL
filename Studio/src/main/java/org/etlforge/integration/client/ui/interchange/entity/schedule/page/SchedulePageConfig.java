package org.etlforge.integration.client.ui.interchange.entity.schedule.page;

import javax.swing.JButton;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.cli.ui.ActionCommand;
import org.etlforge.integration.client.ui.interchange.component.page.entity.ActiveObjectPageAction;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SchedulePageConfig extends DefaultPageConfig {
   private ApplicationAction testAction;

   public SchedulePageConfig(ApplicationWindow var1, ProjectPersistor var2) {
      super(var1, var2);
   }

   public void setTestAction(ApplicationAction var1) {
      this.testAction = var1;
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      this.installTestAction(var1);
   }

   private void installTestAction(IntegrationEntityPage var1) {
      this.addToolbarButton(this.testScheduleButton(var1));
      this.addCommand(new ActionCommand("test", this.testAction));
   }

   private JButton testScheduleButton(IntegrationEntityPage var1) {
      ActiveObjectPageAction var2 = this.createActiveObjectAction(var1, this.testAction);
      return this.createToolbarButton(var2);
   }
}
