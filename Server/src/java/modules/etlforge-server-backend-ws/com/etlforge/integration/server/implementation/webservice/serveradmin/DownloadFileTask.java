package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.io.File;
import java.rmi.RemoteException;
import org.etlforge.util.zip.ZipUtils;

final class DownloadFileTask extends Task<String> {
   private final String path;

   public DownloadFileTask(String var1, String var2, String var3) {
      super("downloadFile", var1, var2);
      this.path = var3;
   }

   protected String runTask() throws Exception {
      File var1 = new File(this.path);
      if (var1.exists() && var1.canRead()) {
         return ZipUtils.deflateAndBase64EncodeFileContents(var1);
      } else {
         throw new RemoteException("The file \"" + this.path + "\" does not exist or cannot be read.");
      }
   }
}
