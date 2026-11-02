package org.etlforge.integration.filetransfer.serverapi;

public interface FileDownloadDependency {
   String getPath();

   String getMD5();
}
