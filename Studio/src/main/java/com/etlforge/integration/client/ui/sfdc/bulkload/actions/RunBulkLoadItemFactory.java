package com.etlforge.integration.client.ui.sfdc.bulkload.actions;

import com.google.common.base.Function;
import com.etlforge.integration.client.ui.sfdc.action.RunSfdcWizardItemFactory;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.operation.runner.OperationRunnerFactory;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.salesforce.bulkload.BulkAction;

public final class RunBulkLoadItemFactory extends RunSfdcWizardItemFactory<SalesforceBulkLoad> {
   public RunBulkLoadItemFactory(ApplicationWindow var1, OperationRunnerFactory var2) {
      super("run_sfdc_bulkload", "Run Bulk Process", var1, SalesforceBulkLoad.class, var2);
      this.setNameFunction(new Function<SalesforceBulkLoad, String>() {
         public String apply(SalesforceBulkLoad var1) {
            BulkAction var2x = var1.getAction();
            return "Run Bulk " + var2x.toSfdcMethod().getDisplayName();
         }
      });
   }
}
