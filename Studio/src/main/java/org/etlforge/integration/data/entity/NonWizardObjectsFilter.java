package org.etlforge.integration.data.entity;

import org.etlforge.integration.data.functors.EntityFilter;

public final class NonWizardObjectsFilter implements EntityFilter {
   @Override
   public boolean apply(IntegrationEntity var1) {
      return !SalesforceUtils.isSalesforceWizardObject(var1);
   }
}
