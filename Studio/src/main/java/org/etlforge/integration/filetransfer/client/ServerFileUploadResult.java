package org.etlforge.integration.filetransfer.client;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.server.ServerInfo;

public final class ServerFileUploadResult {
   private final ServerInfo server;
   private ServerFile file;
   private List<String> missingFiles;
   private List<ServerFile> existingDependencies;

   public ServerFileUploadResult(ServerInfo var1, ServerFile var2) {
      this.file = var2;
      this.server = var1;
      this.missingFiles = Lists.newArrayList();
      this.existingDependencies = Lists.newArrayList();
   }

   public ServerFileUploadResult(ServerInfo var1, ServerFile var2, String[] var3, List<ServerFile> var4) {
      this.server = var1;
      this.file = var2;
      ArrayList var5 = Lists.newArrayList();
      if (var3 != null) {
         for (String var9 : var3) {
            var5.add(var9);
         }
      }

      this.missingFiles = var5;
      this.existingDependencies = var4;
   }

   public ServerInfo getServer() {
      return this.server;
   }

   public ServerFile getServerFile() {
      return this.file;
   }

   public List<String> getMissingFiles() {
      return this.missingFiles;
   }

   public List<ServerFile> getExistingDependencies() {
      return this.existingDependencies;
   }
}
