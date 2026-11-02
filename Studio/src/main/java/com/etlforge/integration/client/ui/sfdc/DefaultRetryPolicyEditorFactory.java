package com.etlforge.integration.client.ui.sfdc;

import org.etlforge.integration.data.entity.SalesforceWizardEntity;

public final class DefaultRetryPolicyEditorFactory implements RetryPolicyEditorFactory {
   @Override
   public RetryPolicyEditor newEditor(SalesforceWizardEntity var1) {
      return new DefaultRetryPolicyEditor(var1);
   }
}
