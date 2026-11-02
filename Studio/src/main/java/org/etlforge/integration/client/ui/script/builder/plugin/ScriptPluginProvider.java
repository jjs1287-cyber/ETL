package org.etlforge.integration.client.ui.script.builder.plugin;

import com.google.common.collect.ImmutableList;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;

public interface ScriptPluginProvider {
   ServerInfo getCurrentServer();

   ImmutableList<PipelinePluginManifest> getAvailablePlugins();

   void refresh();

   void addListener(ScriptPluginProviderListener var1);

   void removeListener(ScriptPluginProviderListener var1);
}
