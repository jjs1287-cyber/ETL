package org.etlforge.integration.server.implementation.webservice.filemanagement.impl;

import org.etlforge.integration.filetransfer.serverapi.FileDownloadDependency;

final class FileDownloadDependencyImpl implements FileDownloadDependency {
   private final String path;
   private final String md5;

   public FileDownloadDependencyImpl(String var1, String var2) {
      this.path = var1;
      this.md5 = var2;
   }

   @Override
   public String getMD5() {
      return this.md5;
   }

   @Override
   public String getPath() {
      return this.path;
   }
}
