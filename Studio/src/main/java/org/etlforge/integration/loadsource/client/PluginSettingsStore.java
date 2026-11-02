package org.etlforge.integration.loadsource.client;

import org.etlforge.integration.plugin.pipeline.PipelinePosition;

public interface PluginSettingsStore {
   PluginSettingsStore EMPTY = new PluginSettingsStore() {
      @Override
      public void store(PipelinePosition[] var1) {
      }

      @Override
      public LoadSourcePluginIdentifier[] load() {
         return new LoadSourcePluginIdentifier[0];
      }
   };

   LoadSourcePluginIdentifier[] load();

   void store(PipelinePosition[] var1);
}
