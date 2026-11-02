package org.etlforge.integration.client.plugin.pipeline.xml;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.plugin.PluginSpecVersionExtractor;
import org.etlforge.integration.client.plugin.pipeline.PipelinePluginSpecVersionExtractor;
import org.etlforge.integration.client.plugin.serverapi.PluginAsXml;
import org.etlforge.integration.plugin.PluginSpecificationException;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifestLoader;
import org.etlforge.util.version.Version;

public final class XmlToManifestTranslator {
   public PipelinePluginManifest[] translate(PluginAsXml var1) throws PluginSpecificationException {
      return this.translate(var1.getXmlRepresentation());
   }

   public PipelinePluginManifest[] translate(String var1) throws PluginSpecificationException {
      Preconditions.checkNotNull(var1, "xml");
      PipelinePluginManifestLoader var2 = this.getLoader(var1);
      return var2.loadManifests(var1);
   }

   private PipelinePluginManifestLoader getLoader(String var1) throws PluginSpecificationException {
      PipelinePluginManifestLoaderFactory var2 = this.getLoaderFactory(var1);
      return var2.newLoader();
   }

   private PipelinePluginManifestLoaderFactory getLoaderFactory(String var1) throws PluginSpecificationException {
      Version var2 = this.getPipelinePluginSpecVersion(var1);
      return PipelinePluginManifestLoaderFactory.getFactory(var2);
   }

   private Version getPipelinePluginSpecVersion(String var1) throws PluginSpecificationException {
      Version var2 = this.getPluginSpecVersion(var1);
      PipelinePluginSpecVersionExtractor var3 = PipelinePluginSpecVersionExtractor.getExtractor(var2);
      return var3.getVersion(var1);
   }

   private Version getPluginSpecVersion(String var1) throws PluginSpecificationException {
      PluginSpecVersionExtractor var2 = PluginSpecVersionExtractor.getExtractor();
      return var2.getVersion(var1);
   }
}
