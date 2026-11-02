package org.etlforge.integration.client.project.info;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.data.project.IntegrationProject;

public interface DownloadProjectInfoCallback extends ServerCallback {
   void projectInfoDownloaded(List<IntegrationProject> var1);
}
