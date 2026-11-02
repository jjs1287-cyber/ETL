package com.etlforge.integration.client.ui.sfdc.query;

import com.etlforge.integration.client.ui.sfdc.action.RunSfdcWizardItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.SalesforceQuery;

public final class RunQueryItemFactory extends RunSfdcWizardItemFactory<SalesforceQuery> {
   public RunQueryItemFactory(ApplicationWindow var1, OperationRunnerFactory var2) {
      super("run_sfdc_query", "Run Query", var1, SalesforceQuery.class, var2);
   }
}
