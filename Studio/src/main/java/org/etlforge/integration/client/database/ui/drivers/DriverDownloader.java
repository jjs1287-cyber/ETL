package org.etlforge.integration.client.database.ui.drivers;

import java.util.EnumSet;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.database.driver.DriverType;

public interface DriverDownloader {
   void downloadDrivers(ServerInfo var1, EnumSet<DriverType> var2);
}
