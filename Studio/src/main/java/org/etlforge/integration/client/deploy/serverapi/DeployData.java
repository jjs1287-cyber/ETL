package org.etlforge.integration.client.deploy.serverapi;

import java.util.Collection;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePluginStore;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.project.IntegrationProject;

public interface DeployData {
   IntegrationProject getProject();

   FileStore getFileStore();

   String getInputMappingStructure(TransformationId var1) throws DataCacheException;

   String getOutputMappingStructure(TransformationId var1) throws DataCacheException;

   PermissionsManager getPermissions();

   Collection<IntegrationId> getDeletedItemsOfType(EntityType var1);

   AppliedPipelinePluginStore getPluginFilters();

   EntityResources getEntityResources();

   boolean isPartial();

   String getTag();
}
