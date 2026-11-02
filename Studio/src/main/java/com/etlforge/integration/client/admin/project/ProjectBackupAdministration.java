package com.etlforge.integration.client.admin.project;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;

public interface ProjectBackupAdministration extends ServerCall {
   void getListOfBackups(IntegrationProjectId var1, GetListOfBackupsCallback var2);

   void downloadBackup(ProjectBackupDescriptor var1, DownloadBackupCallback var2);
}
