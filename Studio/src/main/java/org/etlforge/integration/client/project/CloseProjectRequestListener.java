package org.etlforge.integration.client.project;

import org.etlforge.integration.data.project.IntegrationProject;

public interface CloseProjectRequestListener {
   void closeProjectRequested(IntegrationProject var1) throws CloseProjectVetoException;
}
