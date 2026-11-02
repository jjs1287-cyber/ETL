package org.etlforge.integration.client.plugin.pipeline;

import org.etlforge.integration.data.entity.IntegrationEntity;

public interface AppliedPipelinePluginStoreListener {
   void assignedPluginsChange(AppliedPipelinePluginStore var1, IntegrationEntity var2);
}
