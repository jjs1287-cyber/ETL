package com.etlforge.integration.client.ui.sfdc.soql.test;

import com.google.common.base.Optional;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;

public final class DefaultQueryResultHandler implements QueryResultHandler {
   private Optional<Component> invoker = Optional.absent();
   private int recordLimit;

   public void setRecordLimit(int var1) {
      this.recordLimit = var1;
   }

   public void setInvoker(Component var1) {
      this.invoker = Optional.of(var1);
   }

   @Override
   public void reportResult(QueryResult var1) {
      if (var1.isEmpty()) {
         this.showNoMatchingRowsMessage();
      } else {
         this.showRecords(var1);
      }
   }

   private void showNoMatchingRowsMessage() {
      Window var1 = ApplicationUi.getWindowAncestor(this.invoker);
      NonBlockingAlert.info(var1, "The query is valid, but no matching records were found.", "No Matching Rows");
   }

   private void showRecords(final QueryResult var1) {
      EventQueue.invokeLater(
         new Runnable() {
            @Override
            public void run() {
               UiProvider var1x = this.createResultUi();
               Window var2 = ApplicationUi.getWindowAncestor(DefaultQueryResultHandler.this.invoker);
               KongaDialog var3 = new KongaDialog(var2, "Query Result");
               var3.standardLayout(var1x, KongaDialog.OK_ONLY);
               var3.setOkButtonAsDefault();
               var3.setFocusedComponent(var1x);
               var3.manageLocation(DefaultQueryResultHandler.class);
               var3.setVisible(true);
            }

            private UiProvider createResultUi() {
               return var1.isNested()
                  ? new QueryResultTreeTable(var1, DefaultQueryResultHandler.this.recordLimit)
                  : new QueryResultGrid(var1, DefaultQueryResultHandler.this.recordLimit);
            }
         }
      );
   }

   @Override
   public void handleError(SalesforceException var1) {
      switch (var1.getErrorCode()) {
         case MALFORMED_QUERY:
            this.showMalformedQueryMessage(var1);
         case REQUEST_CANCELED:
            break;
         default:
            this.showGeneralErrorMessage("The query could not be tested: " + var1.getMessage(), "Error");
      }
   }

   private void showMalformedQueryMessage(SalesforceException var1) {
      Window var2 = ApplicationUi.getWindowAncestor(this.invoker);
      NonBlockingAlert.error(var2, "Invalid query: " + var1.getMessage(), "Invalid Query");
   }

   private void showGeneralErrorMessage(String var1, String var2) {
      Window var3 = ApplicationUi.getWindowAncestor(this.invoker);
      NonBlockingAlert.error(var3, var1, var2);
   }

   @Override
   public void reportUnsupportedQuery(String var1) {
      this.showGeneralErrorMessage(var1, "Not Supported");
   }
}
