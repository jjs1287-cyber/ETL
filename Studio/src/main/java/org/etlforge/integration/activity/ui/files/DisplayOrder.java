package org.etlforge.integration.activity.ui.files;

import com.google.common.primitives.Longs;
import java.util.Comparator;
import java.util.Date;
import org.etlforge.integration.activity.client.files.SingleRunResult;

final class DisplayOrder implements Comparator<SingleRunResult> {
   public int compare(SingleRunResult var1, SingleRunResult var2) {
      Date var3 = var1.getLogEntry().getTimeCompleted();
      Date var4 = var2.getLogEntry().getTimeCompleted();
      return var3 != null && var4 != null ? var4.compareTo(var3) : Longs.compare(var2.getLogEntry().getOpInstanceId(), var1.getLogEntry().getOpInstanceId());
   }
}
