package org.etlforge.integration.client.ui.interchange.entity.message;

import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.message.page.EmailMessagePage;
import org.etlforge.integration.client.ui.interchange.entity.message.page.EmailMessagePageConfig;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class EmailMessageConfig extends AbstractEntityConfig {
   private ApplicationAction testAction;

   public EmailMessageConfig() {
      super(EntityType.EmailMessage);
      this.setSearchValuesCollector(new EmailSearchValuesCollector());
      this.setSearchAndReplaceMetaData(new EmailSearchAndReplaceProperties());
   }

   public void setTestAction(ApplicationAction var1) {
      this.testAction = var1;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new EmailMessagePage((EmailMessage)var1);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new EmailMessagePageConfig(var1, var3, this.testAction);
   }
}
