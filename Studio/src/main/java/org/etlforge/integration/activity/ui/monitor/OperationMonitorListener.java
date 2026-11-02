package org.etlforge.integration.activity.ui.monitor;

import com.google.common.base.Optional;
import org.etlforge.integration.activity.client.log.LogStatus;

public interface OperationMonitorListener {
   void operationCompleted(String var1, long var2, Optional<LogStatus> var4);
}
