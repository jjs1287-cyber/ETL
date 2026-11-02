package org.etlforge.integration.client.plugin.pipeline;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface AppliedPipelinePluginStore {
   AppliedPipelinePluginStore EMPTY = new AppliedPipelinePluginStore() {
      @Override
      public AppliedPipelinePlugin[] getAllAssociations(IntegrationEntity var1) {
         return new AppliedPipelinePlugin[0];
      }

      @Override
      public void addListener(AppliedPipelinePluginStoreListener var1) {
      }

      @Override
      public void removeListener(AppliedPipelinePluginStoreListener var1) {
      }
   };

   AppliedPipelinePlugin[] getAllAssociations(IntegrationEntity var1);

   void addListener(AppliedPipelinePluginStoreListener var1);

   void removeListener(AppliedPipelinePluginStoreListener var1);
}
