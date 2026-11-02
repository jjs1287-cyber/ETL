package org.etlforge.integration.activity.ui.files;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.util.clipboard.ClipboardUtils;
import org.etlforge.util.csv.Record;

final class CopyRecordsAction extends AbstractSingleTypeSelectionAction<Record> {
   public CopyRecordsAction() {
      super(Record.class, "Copy Record");
      this.putValue("ShortDescription", "Copy the selected rows to the system clipboard");
   }

   @Override
   protected boolean isEnabledFor(List<Record> var1) {
      String var2 = "Copy " + (var1.size() == 1 ? "Record" : "Records");
      this.putValue("Name", var2);
      return !var1.isEmpty();
   }

   @Override
   protected void run(List<Record> var1) {
      String var2 = Joiner.on(SystemUtils.LINE_SEPARATOR).join(Iterables.transform(var1, new Function<Record, String>() {
         public String apply(Record var1) {
            return CopyRecordsAction.this.recordToString(var1);
         }
      }));
      ClipboardUtils.copyToSystemClipboard(var2);
   }

   private String recordToString(Record var1) {
      return Joiner.on(",").join(Iterables.transform(var1.getValues(), new Function<String, String>() {
         public String apply(String var1) {
            return StringEscapeUtils.escapeCsv(var1);
         }
      }));
   }
}
