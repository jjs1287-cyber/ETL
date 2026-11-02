package org.etlforge.integration.client.project.info;

import org.etlforge.integration.client.server.ServerCall;

public interface ProjectInfoProvider extends ServerCall {
   void downloadProjectInfo(DownloadProjectInfoCallback var1);
}
