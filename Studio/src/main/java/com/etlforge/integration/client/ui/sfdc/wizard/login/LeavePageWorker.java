package com.etlforge.integration.client.ui.sfdc.wizard.login;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.ErrorCode;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.ui.UiThreadSupplier;

final class LeavePageWorker {
   private final SalesforceWizard wizard;
   private final ConnectorSelector ui;
   private final LastConnectorMemento lastConnectorMemento;

   public LeavePageWorker(SalesforceWizard var1, ConnectorSelector var2, LastConnectorMemento var3) {
      this.wizard = var1;
      this.ui = var2;
      this.lastConnectorMemento = var3;
   }

   public boolean leavePage() {
      SalesforceConnector var1 = UiThreadSupplier.call(new Supplier<SalesforceConnector>() {
         public SalesforceConnector get() {
            return LeavePageWorker.this.ui.allowsRemoval() ? LeavePageWorker.this.ui.getSelectedConnector() : null;
         }
      });
      if (var1 != null) {
         try {
            this.wizard.loginToSalesforce(var1);
            this.lastConnectorMemento.store(var1);
            return true;
         } catch (SalesforceException var3) {
            if (var3.getErrorCode() != ErrorCode.REQUEST_CANCELED) {
               NonBlockingAlert.error(var3.getMessage(), "Error");
            }

            this.reOpenEditorOnConnectionFailure(var1);
         }
      }

      return false;
   }

   private void reOpenEditorOnConnectionFailure(final SalesforceConnector var1) {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            LeavePageWorker.this.ui.editConnector(var1);
            return null;
         }
      });
   }
}
