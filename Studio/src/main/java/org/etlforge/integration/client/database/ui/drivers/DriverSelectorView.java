package org.etlforge.integration.client.database.ui.drivers;

import java.util.EnumSet;
import javax.swing.Action;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.database.driver.DriverDescriptor;
import org.etlforge.integration.database.driver.DriverType;
import org.etlforge.ui.UiProvider;

public interface DriverSelectorView extends UiProvider {
   DriverDescriptor getSelectedDriver();

   void setAvailableDrivers(Iterable<DriverDescriptor> var1);

   ServerInfo getSelectedServer();

   void setSelectedServer(ServerInfo var1);

   void setKnownServers(Iterable<KnownServer> var1);

   void setDriverTypes(EnumSet<DriverType> var1);

   EnumSet<DriverType> getDriverTypes();

   void setRefreshAction(Action var1);

   void addListener(DriverSelectorViewListener var1);

   void removeListener(DriverSelectorViewListener var1);
}
