package org.etlforge.integration.activity.client.log;

import com.google.common.base.Preconditions;
import org.etlforge.util.clipboard.ClipboardUtils;

public final class OperationLogEntryToClipBoard {
   private final OperationLogEntryTextRepresentation textRepresenter;

   public OperationLogEntryToClipBoard(OperationLogEntryTextRepresentation var1) {
      Preconditions.checkNotNull(var1, "textRepresenter");
      this.textRepresenter = var1;
   }

   public void copy(OperationLogEntry var1) {
      Preconditions.checkNotNull(var1, "logEntry");
      String var2 = this.textRepresenter.getText(var1);
      ClipboardUtils.copyToSystemClipboard(var2);
   }
}
