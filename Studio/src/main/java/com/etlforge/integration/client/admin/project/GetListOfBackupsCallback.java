package com.etlforge.integration.client.admin.project;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.common.server.project.backup.ProjectBackupDescriptor;

public interface GetListOfBackupsCallback extends ServerCallback {
   void backupDescriptorsDownloaded(ProjectBackupDescriptor[] var1);
}
