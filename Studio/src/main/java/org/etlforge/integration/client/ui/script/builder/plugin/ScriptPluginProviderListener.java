package org.etlforge.integration.client.ui.script.builder.plugin;

import org.etlforge.integration.client.server.ServerInfo;

public interface ScriptPluginProviderListener {
   void serverConnectionChanged(ServerInfo var1);

   void availablePluginsChanged();
}
