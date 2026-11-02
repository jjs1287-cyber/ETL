package com.etlforge.integration.client.ui.sfdc.soql.test;

import org.etlforge.application.ui.look.TableStyles;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.csv.CsvFileTableUi;
import org.etlforge.util.csv.CsvFile;

public final class QueryResultGrid implements UiProvider {
   private final CsvFileTableUi grid;

   public QueryResultGrid(QueryResult var1, int var2) {
      CsvFile var3 = var1.asCsvFile();
      this.grid = new CsvFileTableUi(var3).withTableStyle(TableStyles.normal()).withTextStyle(TextStyles.DefaultMonoSpaced);
      if (var1.getNumberOfRecords() == var2) {
         String var4 = "Only the first " + (var2 == 1 ? "row is" : var2 + " rows are") + " displayed.";
         this.grid.withCaption(var4);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.grid.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.grid.requestFocus();
   }
}
