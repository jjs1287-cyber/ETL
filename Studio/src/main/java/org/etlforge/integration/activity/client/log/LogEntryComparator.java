package org.etlforge.integration.activity.client.log;

import com.google.common.primitives.Longs;
import java.util.Comparator;

public enum LogEntryComparator implements Comparator<OperationLogEntry> {
   LATEST_CHANGE_FIRST {
      public int compare(OperationLogEntry var1, OperationLogEntry var2) {
         return -var1.getStatusTime().compareTo(var2.getStatusTime());
      }
   },
   INSTANCE_ID {
      public int compare(OperationLogEntry var1, OperationLogEntry var2) {
         return Longs.compare(var1.getOpInstanceId(), var2.getOpInstanceId());
      }
   };

   LogEntryComparator() {
   }
}
