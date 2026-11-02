package com.etlforge.integration.client.admin.project;

import java.io.File;
import org.etlforge.integration.client.server.ServerCallback;

public interface DownloadBackupCallback extends ServerCallback {
   void backupDownloaded(File var1);
}
