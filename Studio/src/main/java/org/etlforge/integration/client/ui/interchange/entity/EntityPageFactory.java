package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface EntityPageFactory {
   EntityPageFactory NONE = new EntityPageFactory() {
      @Override
      public IntegrationEntityPage createPage(IntegrationEntity var1) {
         return null;
      }
   };

   IntegrationEntityPage createPage(IntegrationEntity var1);
}
