package org.etlforge.integration.client.project;

import org.etlforge.util.version.Version;

public interface ProjectVersionHandlerFactory {
   Version getLastVersion();

   boolean handlesAllEarlierVersions();

   ProjectVersionHandler getHandler();
}
