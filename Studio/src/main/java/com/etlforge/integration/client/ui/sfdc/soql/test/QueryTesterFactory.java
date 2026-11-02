package com.etlforge.integration.client.ui.sfdc.soql.test;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import javax.annotation.Nullable;
import javax.swing.JFrame;
import org.etlforge.integration.salesforce.parser.SupportedQueryPolicy;
import org.etlforge.integration.salesforce.query.QueryRunner;
import org.etlforge.system.ArgCheck;

public final class QueryTesterFactory {
   private final JFrame applicationFrame;
   private int recordLimit = 10;

   public QueryTesterFactory(@Nullable JFrame var1) {
      this.applicationFrame = var1;
   }

   public void setRecordLimit(int var1) {
      this.recordLimit = ArgCheck.checkPositive(var1);
   }

   public QueryTester newTester(QueryRunner var1, SupportedQueryPolicy var2) {
      return this.newTester(Suppliers.ofInstance(var1), var2);
   }

   public QueryTester newTester(Supplier<QueryRunner> var1, SupportedQueryPolicy var2) {
      DefaultQueryResultHandler var3 = new DefaultQueryResultHandler();
      var3.setRecordLimit(this.recordLimit);
      var3.setInvoker(this.applicationFrame);
      QueryTester var4 = new QueryTester(var1, var3);
      var4.setRecordLimit(this.recordLimit);
      var4.setSupportedQueryPolicy(var2);
      return var4;
   }
}
