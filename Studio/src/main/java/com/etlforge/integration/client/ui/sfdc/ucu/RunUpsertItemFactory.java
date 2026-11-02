package com.etlforge.integration.client.ui.sfdc.ucu;

import com.etlforge.integration.client.ui.sfdc.action.RunSfdcWizardItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.SalesforceUpsert;

public final class RunUpsertItemFactory extends RunSfdcWizardItemFactory<SalesforceUpsert> {
   public RunUpsertItemFactory(ApplicationWindow var1, OperationRunnerFactory var2) {
      super("run_sfdc_upsert", "Run Upsert", var1, SalesforceUpsert.class, var2);
   }
}
