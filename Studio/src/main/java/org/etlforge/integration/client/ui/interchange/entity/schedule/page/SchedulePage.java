package org.etlforge.integration.client.ui.interchange.entity.schedule.page;

import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.data.entity.Schedule;

public final class SchedulePage extends IntegrationEntityPage {
   public SchedulePage(Schedule var1) {
      super(new IntegrationEntityPageModel(var1));
      this.setHelpId("integration.schedule.main");
   }

   protected SchedulePanel createDataPanel() {
      SchedulePanel var1 = new SchedulePanel(this.getWindow(), this.getObject());
      this.getPageModel().bindProperties(var1.getBoundProperties());
      return var1;
   }

   public Schedule getObject() {
      return (Schedule)super.getObject();
   }
}
