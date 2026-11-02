package com.etlforge.integration.client.ui.sfdc.ucu;

import com.etlforge.integration.client.ui.sfdc.action.RunSfdcWizardItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.SalesforceUpdate;

public final class RunUpdateItemFactory extends RunSfdcWizardItemFactory<SalesforceUpdate> {
   public RunUpdateItemFactory(ApplicationWindow var1, OperationRunnerFactory var2) {
      super("run_sfdc_update", "Run Update", var1, SalesforceUpdate.class, var2);
   }
}
