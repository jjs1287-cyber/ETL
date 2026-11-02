package com.etlforge.integration.client.ui.sfdc.page.soql;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import com.etlforge.integration.client.ui.sfdc.wizard.soql.InputtedQueryStringProcessor;
import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.parser.CaseCorrector;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlErrorReporter;
import org.etlforge.lang.exception.ExceptionHandler;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public final class EditSoqlController {
   private final SoqlEditor editor;
   private final SalesforceFacade sfdcFacade;
   private final SoqlModel soqlModel;
   private Supplier<SalesforceConnector> connectorSupplier;
   private SoqlErrorReporter errorReporter = SoqlErrorReporter.STDERR;
   private WaitService waitService = WaitService.NONE;

   public EditSoqlController(SoqlModel var1, SoqlEditor var2, SalesforceFacade var3) {
      this(var1, var2, var3, null);
   }

   public EditSoqlController(SoqlModel var1, SoqlEditor var2, SalesforceFacade var3, Supplier<SalesforceConnector> var4) {
      this.soqlModel = Preconditions.checkNotNull(var1);
      this.editor = Preconditions.checkNotNull(var2);
      this.sfdcFacade = Preconditions.checkNotNull(var3);
      this.connectorSupplier = var4;
   }

   public EditSoqlController withConnectorSupplier(Supplier<SalesforceConnector> var1) {
      this.connectorSupplier = Preconditions.checkNotNull(var1);
      return this;
   }

   public EditSoqlController withErrorReporter(SoqlErrorReporter var1) {
      this.errorReporter = Preconditions.checkNotNull(var1);
      return this;
   }

   public EditSoqlController withWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
      return this;
   }

   public void edit() {
      SalesforceConnector var1 = this.connectorSupplier.get();
      if (var1 == null) {
         Alert.error(ApplicationUi.getAppWinFrame(), "Must select a Salesforce Connector.", "Cannot connect");
      } else {
         String var2 = this.soqlModel.getQueryString();
         String var3 = this.soqlModel.getObjectName();
         this.openEditor(var1, var2, var2, var3);
      }
   }

   private void openEditor(SalesforceConnector var1, String var2, String var3, String var4) {
      String var5 = this.editor.edit(var3, var4, var1);
      this.processNewSoql(var1, var2, var4, var5);
   }

   private void processNewSoql(final SalesforceConnector var1, final String var2, final String var3, final String var4) {
      if (var4 != null && !var4.equals(var2)) {
         ExceptionHandler var5 = new ExceptionHandler<Throwable>() {
            @Override
            public void caught(Throwable var1x) {
               if (var1x instanceof EditSoqlException) {
                  EditSoqlException var2x = (EditSoqlException)var1x;
                  EditSoqlController.this.errorReporter.reportError(var2x.getTitle(), var2x.getMessage());
                  EditSoqlController.this.reopenEditor(var1, var2, var3, var4);
               } else {
                  ErrorLog.attention(EditSoqlController.class, null, var1x);
               }
            }
         };
         EditSoqlController.ParsedQueryProducer var6 = new EditSoqlController.ParsedQueryProducer(var4);
         EditSoqlController.ResultConsumer var7 = new EditSoqlController.ResultConsumer(var4);
         Computation var8 = Computation.create(var6, var7);
         var8.swingWorkerBuilder().withErrorHandler(var5).withWaitService(this.waitService).buildAndExecute();
      }
   }

   private void reopenEditor(SalesforceConnector var1, String var2, String var3, String var4) {
      String var5 = var4.isEmpty() ? var2 : var4;
      this.openEditor(var1, var2, var5, var3);
   }

   private class ParsedQueryProducer implements Callable<ParsedQuery> {
      private final String soql;
      private EditSoqlException exception;

      public ParsedQueryProducer(String var2) {
         this.soql = var2;
      }

      public ParsedQuery call() throws Exception {
         EditSoqlController.this.sfdcFacade.login(EditSoqlController.this.connectorSupplier.get());

         try {
            return this.processQuery();
         } finally {
            EditSoqlController.this.sfdcFacade.close();
         }
      }

      private ParsedQuery processQuery() throws EditSoqlException {
         InputtedQueryStringProcessor var1 = this.createProcessor();
         ParsedQuery var2 = var1.process(this.soql);
         if (this.exception != null) {
            throw this.exception;
         } else {
            return var2;
         }
      }

      private InputtedQueryStringProcessor createProcessor() {
         InputtedQueryStringProcessor var1 = new InputtedQueryStringProcessor(EditSoqlController.this.soqlModel.getSupportedQueryPolicy());
         var1.setErrorReporter(new SoqlErrorReporter() {
            @Override
            public void reportError(String var1, String var2) {
               ParsedQueryProducer.this.exception = new EditSoqlException(var1, var2);
            }
         });
         CaseCorrector var2 = new CaseCorrector(EditSoqlController.this.soqlModel.getObjectName(), EditSoqlController.this.sfdcFacade.getSchemaProvider());
         var1.correctForCase(var2);
         return var1;
      }
   }

   private class ResultConsumer implements Receiver<ParsedQuery> {
      private final String soql;

      public ResultConsumer(String var2) {
         this.soql = var2;
      }

      public void handle(ParsedQuery var1) {
         EditSoqlController.this.soqlModel.setQueryInfo(this.soql, var1);
      }
   }
}
