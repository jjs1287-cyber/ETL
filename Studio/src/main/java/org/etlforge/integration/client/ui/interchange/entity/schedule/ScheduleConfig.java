package org.etlforge.integration.client.ui.interchange.entity.schedule;

import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.schedule.page.SchedulePage;
import org.etlforge.integration.client.ui.interchange.entity.schedule.page.SchedulePageConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class ScheduleConfig extends AbstractEntityConfig {
   private ApplicationAction testAction;

   public ScheduleConfig() {
      super(EntityType.Schedule);
      this.setSearchAndReplaceMetaData(new ScheduleSearchAndReplaceProperties());
   }

   public void setTestAction(ApplicationAction var1) {
      this.testAction = var1;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new SchedulePage((Schedule)var1);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      SchedulePageConfig var4 = new SchedulePageConfig(var1, var3);
      var4.setTestAction(this.testAction);
      return var4;
   }
}
