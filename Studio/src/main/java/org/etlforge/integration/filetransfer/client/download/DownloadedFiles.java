package org.etlforge.integration.filetransfer.client.download;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFile;

public final class DownloadedFiles {
   private final ServerInfo server;
   private final List<ServerFile> files;

   DownloadedFiles(ServerInfo var1) {
      this.server = var1;
      this.files = Lists.newArrayList();
   }

   public ServerInfo getServer() {
      return this.server;
   }

   public ServerFile[] getFiles() {
      return this.files.toArray(new ServerFile[this.files.size()]);
   }

   void addFile(ServerFile var1) {
      this.files.add(var1);
   }
}
