package org.etlforge.integration.client.ui.interchange.entity.webservice;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.wizard.EntityWizardFactory;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.webservice.page.WebServiceCallPage;
import org.etlforge.integration.client.ui.interchange.entity.webservice.page.WebServiceCallPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.webservice.wizard.WebServiceWizardFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class WebServiceCallConfig extends AbstractEntityConfig {
   public WebServiceCallConfig() {
      super(EntityType.WebServiceCall);
      this.setSearchAndReplaceMetaData(new WebServiceCallSearchAndReplaceProperties());
      this.setSearchValuesCollector(new WebServiceCallSearchValuesCollector());
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new WebServiceCallPage((WebServiceCall)var1);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new WebServiceCallPageConfig(var1, var3);
   }

   @Override
   public EntityWizardFactory getEntityWizardFactory() {
      return new WebServiceWizardFactory();
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new WebServiceCallToolTip((WebServiceCall)var1);
   }
}
