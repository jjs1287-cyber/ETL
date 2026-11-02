package org.etlforge.integration.client.project.jitterpack;

import org.etlforge.integration.data.project.IntegrationProject;

public interface UnpackCallback {
   void unpackingCompleted(IntegrationProject var1);

   void unpackingFailed(String var1, Throwable var2);

   void unpackingCancelled(String var1);
}
