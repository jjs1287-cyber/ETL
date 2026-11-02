package org.etlforge.integration.activity.ui.files;

import java.util.Collection;
import javax.swing.Action;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.activity.client.files.ResultFileSummary;
import org.etlforge.integration.common.server.file.FileCategory;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.csv.CsvFileTableUi;
import org.etlforge.ui.selection.SelectionSource;

final class ResultFileSummaryView implements UiProvider {
   private final CsvFileTableUi table;

   public ResultFileSummaryView(OperationFile var1, ResultFileSummary var2) {
      if (var1.getCategory() == FileCategory.SFDC_SUCCESS) {
         var2.appendEmptyColumn();
      }

      this.table = this.createTable(var2);
   }

   private CsvFileTableUi createTable(ResultFileSummary var1) {
      String var2 = var1.isComplete() ? "Result:" : "Showing the first " + var1.getNumberOfRows() + " records:";
      return new CsvFileTableUi(var1.getCsvFileRepresentation()).withTextStyle(TextStyles.DefaultMonoSpaced).withCaption(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }

   public SelectionSource asSelectionSource() {
      return this.table;
   }

   public void installContextMenu(Collection<? extends Action> var1) {
      this.table.installContextMenu(var1);
   }
}
