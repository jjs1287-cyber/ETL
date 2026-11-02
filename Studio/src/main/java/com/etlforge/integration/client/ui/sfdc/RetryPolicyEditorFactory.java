package com.etlforge.integration.client.ui.sfdc;

import org.etlforge.integration.data.entity.SalesforceWizardEntity;

public interface RetryPolicyEditorFactory {
   RetryPolicyEditor newEditor(SalesforceWizardEntity var1);
}
