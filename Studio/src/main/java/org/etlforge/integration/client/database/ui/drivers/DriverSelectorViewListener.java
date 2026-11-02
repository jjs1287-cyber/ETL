package org.etlforge.integration.client.database.ui.drivers;

import java.util.EnumSet;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;

public interface DriverSelectorViewListener {
   void serverSelectionChanged(ServerInfo var1);

   void driverSelectionChanged(DriverDescriptor var1);

   void driverTypeSelectionChanged(EnumSet<DriverType> var1);

   void finalSelectionWasMade(DriverDescriptor var1);

   abstract class Adapter implements DriverSelectorViewListener {
      @Override
      public void driverSelectionChanged(DriverDescriptor var1) {
      }

      @Override
      public void driverTypeSelectionChanged(EnumSet<DriverType> var1) {
      }

      @Override
      public void finalSelectionWasMade(DriverDescriptor var1) {
      }

      @Override
      public void serverSelectionChanged(ServerInfo var1) {
      }
   }
}
