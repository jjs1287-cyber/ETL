package com.etlforge.integration.client.sfdc.data;

import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;

public interface ObjectSummary<T extends IntegrationEntity & SalesforceWizardEntity> {
   T getWizardObject();

   List<IntegrationEntity> getItemsCreatedByWizard();

   void setNewWizardName(String var1);

   void insertIntoProject();
}
