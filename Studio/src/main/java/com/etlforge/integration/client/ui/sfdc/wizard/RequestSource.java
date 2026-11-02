package com.etlforge.integration.client.ui.sfdc.wizard;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceModifier;
import org.etlforge.integration.data.entity.Transformation;

public interface RequestSource {
   void applyTo(Transformation var1);

   void applyTo(SalesforceModifier var1);

   Iterable<IntegrationEntity> getItemsCreatedByWizard();
}
