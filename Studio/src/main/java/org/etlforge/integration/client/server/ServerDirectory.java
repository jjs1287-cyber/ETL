package org.etlforge.integration.client.server;

import java.io.File;
import org.etlforge.integration.common.server.ServerGuid;

public interface ServerDirectory {
   File getLocation();

   File getLocation(ServerGuid var1);

   ServerInformationStore getInformationStore();
}
