package org.etlforge.integration.server.implementation.webservice.pluginmanagement.impl;

import org.etlforge.integration.client.plugin.serverapi.PluginAsXml;
import org.etlforge.integration.server.implementation.webservice.pluginmanagement.client.FilterAsXml;

final class PluginAsXmlImpl implements PluginAsXml {
   private final FilterAsXml plugin;

   public PluginAsXmlImpl(FilterAsXml var1) {
      this.plugin = var1;
   }

   @Override
   public String getXmlRepresentation() {
      return this.plugin.getXmlRepresentation();
   }
}
