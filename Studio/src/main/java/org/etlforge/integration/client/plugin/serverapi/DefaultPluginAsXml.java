package org.etlforge.integration.client.plugin.serverapi;

public final class DefaultPluginAsXml implements PluginAsXml {
   private final String xmlRep;

   public DefaultPluginAsXml(String var1) {
      this.xmlRep = var1;
   }

   @Override
   public String getXmlRepresentation() {
      return this.xmlRep;
   }
}
