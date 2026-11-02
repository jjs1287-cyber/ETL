package com.etlforge.integration.client.admin.project;

import java.io.File;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

public interface ProjectDownloader extends ServerCall {
   void downloadProject(IntegrationProjectId var1, File var2, ProjectDownloaderCallback var3);
}
