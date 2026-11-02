package org.etlforge.integration.plugin.pipeline;

public interface PipelinePluginManifestLoader {
   PipelinePluginManifest[] loadManifests(String var1) throws PipelinePluginManifestLoadingException;
}
