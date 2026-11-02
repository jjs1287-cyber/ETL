package org.etlforge.integration.data.entity;

import org.etlforge.system.callback.Receiver;

public interface ProcessFilterProvider {
   void addMonitor(Receiver<ProcessFilter> var1);

   void removeMonitor(Receiver<ProcessFilter> var1);
}
