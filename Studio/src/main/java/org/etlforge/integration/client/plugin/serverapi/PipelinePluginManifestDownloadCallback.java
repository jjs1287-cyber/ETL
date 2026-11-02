package org.etlforge.integration.client.plugin.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;

public interface PipelinePluginManifestDownloadCallback extends ServerCallback {
   void pluginInfoDownloaded(ServerInfo var1, PluginAsXml[] var2);
}
