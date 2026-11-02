package org.etlforge.integration.activity.ui.log;

import java.util.Comparator;
import javax.swing.SortOrder;
import org.etlforge.integration.activity.client.log.OperationLogEntry;

public final class SortStatusTranslator {
   public static Comparator<OperationLogEntry> toComparator(SortStatus var0) {
      if (var0.column.isNaturallyComparable()) {
         return var0.sortOrder == SortOrder.DESCENDING ? LogColumnComparator.descending(var0.column) : LogColumnComparator.ascending(var0.column);
      } else {
         return null;
      }
   }

   private SortStatusTranslator() {
   }
}
