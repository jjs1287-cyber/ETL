package com.etlforge.integration.client.ui.sfdc.page.cli;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;

public final class ReviewRequestExecutor extends AbstractReviewImplementingItemExecutor {
   public ReviewRequestExecutor(SalesforceWizardEntity var1) {
      super(var1);
   }

   @Override
   protected IntegrationEntity locateItem(WizardItemBrowser var1) {
      return var1.getRequestTransformation();
   }
}
