package com.etlforge.integration.server.implementation.webservice.admin.server.impl;

import com.google.common.base.Preconditions;
import com.etlforge.integration.client.admin.server.ServerConfigurationFile;
import java.util.zip.DataFormatException;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.util.zip.ZipUtils;

final class ServerConfigurationFileImpl implements ServerConfigurationFile {
   private final ServerInfo serverInfo;
   private final String content;

   public ServerConfigurationFileImpl(ServerInfo var1, String var2) throws DataFormatException {
      Preconditions.checkNotNull(var1, "serverInfo");
      this.serverInfo = var1;
      this.content = ZipUtils.base64DecodeAndInflateString(var2);
   }

   @Override
   public String getContents() {
      return this.content;
   }

   @Override
   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }
}
