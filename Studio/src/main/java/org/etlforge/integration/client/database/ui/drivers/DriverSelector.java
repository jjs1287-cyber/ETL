package org.etlforge.integration.client.database.ui.drivers;

import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.system.callback.Receiver;

public interface DriverSelector {
   void selectDriver(Receiver<DriverDescriptor> var1);

   void closeIfOpen();
}
