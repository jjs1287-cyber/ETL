package com.etlforge.integration.client.ui.sfdc.delete;

import com.etlforge.integration.client.ui.sfdc.action.RunSfdcWizardItemFactory;
import com.etlforge.integration.data.entity.SalesforceDelete;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;

public final class RunDeleteItemFactory extends RunSfdcWizardItemFactory<SalesforceDelete> {
   public RunDeleteItemFactory(ApplicationWindow var1, OperationRunnerFactory var2) {
      super("run_sfdc_delete", "Run Delete", var1, SalesforceDelete.class, var2);
   }
}
