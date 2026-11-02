package org.etlforge.integration.data.script.de;

import org.etlforge.integration.data.entity.ProcessFilter;

public interface GlobalDataElementsListener {
   void dataElementStoreChanged();

   void dataElementStoreRebuilt();

   void processFilterChanged(ProcessFilter var1);
}
