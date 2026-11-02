package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import javax.swing.JLabel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.integration.client.connector.serverapi.TestNetSuiteQueryApi;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchResponse;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class TestQueryResultDisplayer {
   public static void showResult(TestNetSuiteQueryApi.Result var0) {
      if (var0.isSuccess()) {
         NetSuiteSearchResponse var1 = var0.getResponse();
         TestQueryResultDisplayer.Ui var2 = new TestQueryResultDisplayer.Ui(var1);
         Alert.info(ApplicationUi.getAppWinFrame(), var2.layout(), "Query Results");
      } else {
         Alert.error(ApplicationUi.getAppWinFrame(), var0.getErrorMessage(), "Error");
      }
   }

   private TestQueryResultDisplayer() {
   }

   private static class Ui {
      private final KongaTextField totalRecords = createField("Number of records:");
      private final KongaTextField searchId = createField("Search ID:");
      private final KongaTextField pageSize = createField("Page size:");
      private final KongaTextField numberOfPages = createField("Number of pages:");
      private final KongaTextField pageIndex = createField("Page index:");

      public Ui(NetSuiteSearchResponse var1) {
         this.totalRecords.setText("" + var1.getTotalRecords());
         this.searchId.setText(var1.getSearchId());
         this.pageSize.setText("" + var1.getPageSize());
         this.numberOfPages.setText("" + var1.getTotalPages());
         this.pageIndex.setText("" + var1.getPageIndex());
      }

      private static KongaTextField createField(String var0) {
         KongaTextField var1 = new KongaTextField(50);
         var1.setEditable(false);
         var1.setLabel(var0);
         return var1;
      }

      public UiProvider layout() {
         TwoColumnPanel var1 = TwoColumnPanel.forFormEntry();
         var1.addRow(new JLabel("The query was successful. Here is a summary of the result:"));
         add(this.totalRecords, var1);
         add(this.searchId, var1);
         add(this.pageSize, var1);
         add(this.numberOfPages, var1);
         add(this.pageIndex, var1);
         return var1;
      }

      private static void add(KongaTextField var0, TwoColumnPanel var1) {
         var1.addRow(var0.getLabel(), var0);
      }
   }
}
