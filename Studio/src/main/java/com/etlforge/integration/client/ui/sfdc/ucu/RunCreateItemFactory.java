package com.etlforge.integration.client.ui.sfdc.ucu;

import com.etlforge.integration.client.ui.sfdc.action.RunSfdcWizardItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.SalesforceCreate;

public final class RunCreateItemFactory extends RunSfdcWizardItemFactory<SalesforceCreate> {
   public RunCreateItemFactory(ApplicationWindow var1, OperationRunnerFactory var2) {
      super("run_sfdc_create", "Run Insert", var1, SalesforceCreate.class, var2);
   }
}
