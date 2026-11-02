package org.etlforge.integration.activity.ui.files;

import java.awt.event.ActionEvent;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.util.clipboard.ClipboardUtils;
import org.etlforge.util.csv.Record;

final class CopyErrorAction extends AbstractSingleObjectSelectionAction<Record> {
   public CopyErrorAction() {
      super(Record.class, "Copy Error");
      this.putValue("ShortDescription", "Copy the error message to the system clipboard");
   }

   protected void run(Record var1, ActionEvent var2) {
      String var3 = var1.getValueAt(var1.size() - 1);
      ClipboardUtils.copyToSystemClipboard(var3);
   }
}
