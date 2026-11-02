package org.etlforge.integration.client.plugin.serverapi;

import org.etlforge.integration.client.server.ServerCall;

public interface PipelinePluginInfoProvider extends ServerCall {
   void downloadPluginManifests(PipelinePluginManifestDownloadCallback var1);
}
