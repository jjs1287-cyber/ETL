package com.etlforge.integration.client.ui.sfdc.soql.test;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import java.awt.EventQueue;
import javax.annotation.Nullable;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.dataelement.DataElementResolver;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.parser.ParsedQuery;
import org.etlforge.integration.salesforce.parser.SoqlParser;
import org.etlforge.integration.salesforce.parser.SoqlParserFactory;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.salesforce.query.QueryRunner;
import org.etlforge.system.ArgCheck;
import org.etlforge.ui.wait.WaitService;

public final class QueryTester {
   private final Supplier<QueryRunner> queryRunnerFactory;
   private final QueryResultHandler resultHandler;
   private SupportedQueryPolicy supportedQueryPolicy = SupportedQueryPolicy.NO_RESTRICTIONS;
   private WaitService waitService = WaitService.NONE;
   private int recordLimit = 10;

   public QueryTester(QueryRunner var1, QueryResultHandler var2) {
      this(Suppliers.ofInstance(Preconditions.checkNotNull(var1, "runner")), var2);
   }

   public QueryTester(Supplier<QueryRunner> var1, QueryResultHandler var2) {
      this.queryRunnerFactory = Preconditions.checkNotNull(var1, "queryRunnerFactory");
      this.resultHandler = Preconditions.checkNotNull(var2, "resultHandler");
   }

   public void setRecordLimit(int var1) {
      this.recordLimit = ArgCheck.checkPositive(var1);
   }

   public void setSupportedQueryPolicy(SupportedQueryPolicy var1) {
      this.supportedQueryPolicy = Preconditions.checkNotNull(var1);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   public void testQuery(String var1, boolean var2) {
      String var3 = this.checkQuery(var1);
      if (var3 != null) {
         QueryTester.Worker var4 = new QueryTester.Worker(var3, var2, this.recordLimit);
         var4.setUseProgressDialog(0);
         var4.submit(this.waitService, 0);
      }
   }

   @Nullable
   private String checkQuery(String var1) {
      ArgCheck.checkNotBlank(var1);
      String var2 = DataElementResolver.resolveDefaultValues(var1);
      return this.isSupported(var2) ? var2 : null;
   }

   private boolean isSupported(String var1) {
      SoqlParser var2 = SoqlParserFactory.newParser();
      ParsedQuery var3 = var2.parse(var1);
      boolean var4 = this.supportedQueryPolicy.isSupported(var3);
      if (!var4) {
         String var5 = this.supportedQueryPolicy.whyNot();
         this.reportUnsupportedQuery(var5);
      }

      return var4;
   }

   private void reportUnsupportedQuery(final String var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            QueryTester.this.resultHandler.reportUnsupportedQuery(var1);
         }
      });
   }

   private class Worker extends UiJob {
      private final String queryString;
      private final boolean includeDeletedRows;
      private final int recordLimit;

      public Worker(String var2, boolean var3, int var4) {
         super("Testing the query");
         this.queryString = var2;
         this.includeDeletedRows = var3;
         this.recordLimit = var4;
         this.setBlockUntilDone(true);
         this.setUncaughtExceptionHandler(LoggingExceptionHandler.attention(QueryTester.class));
         this.setMessage(this.getName());
      }

      @Override
      protected void runImpl() throws InterruptedException {
         try {
            String var1 = this.constructTestQuery();
            QueryRunner var2 = QueryTester.this.queryRunnerFactory.get();
            var2.setIncludeDeletedRows(this.includeDeletedRows);
            QueryResult var3 = var2.query(var1);
            this.showResult(var3);
         } catch (SalesforceException var4) {
            this.handleQueryError(var4);
         }
      }

      private String constructTestQuery() {
         TestQueryConstructor var1 = new TestQueryConstructor();
         var1.setLimit(this.recordLimit);
         return var1.getTestableQuery(this.queryString);
      }

      private void handleQueryError(final SalesforceException var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               QueryTester.this.resultHandler.handleError(var1);
            }
         });
      }

      private void showResult(final QueryResult var1) {
         if (!this.isCancelled()) {
            this.setCanBeCancelled(false);
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  QueryTester.this.resultHandler.reportResult(var1);
               }
            });
         }
      }
   }
}
