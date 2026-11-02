package org.etlforge.integration.client.plugin.serverapi;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.io.KongaIoUtils;

final class MockPipelinePluginInfoProvider implements PipelinePluginInfoProvider {
   @Override
   public void downloadPluginManifests(PipelinePluginManifestDownloadCallback var1) {
      Random var2 = new Random();
      if (var2.nextInt(100) < 1) {
         var1.caught(new IntegrationServerException("This is a mock error."));
      } else {
         try {
            PluginAsXml[] var3 = new PluginAsXml[]{this.loadMockPluginDescription()};
            ServerInfo var4 = this.getMockServer();
            var1.pluginInfoDownloaded(var4, var3);
         } catch (Exception var5) {
            var1.caught(var5);
         }
      }
   }

   private DefaultPluginAsXml loadMockPluginDescription() throws IOException, URISyntaxException {
      URL var1 = this.getClass().getResource("/org/etlforge/integration/client/server/pluginmanagement/pipeline/plugin.xml");
      URI var2 = var1.toURI();
      File var3 = new File(var2);
      return new DefaultPluginAsXml(KongaIoUtils.read(var3));
   }

   private ServerInfo getMockServer() {
      return new ServerInfo(new ServerGuid(UUID.randomUUID().toString()), "Sandbox Server", VersionFactory.newVersion(2, 0, 0));
   }
}
