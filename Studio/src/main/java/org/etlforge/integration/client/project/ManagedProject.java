package org.etlforge.integration.client.project;

import com.google.common.eventbus.EventBus;
import java.io.File;
import java.util.Collection;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.deploy.history.DeployHistory;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.project.jitterpack.ExportPreparationException;
import org.etlforge.integration.client.project.jitterpack.PostExportManager;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.DependencyStore;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.TrashCan;
import org.etlforge.integration.data.project.validation.ValidationModel;

public interface ManagedProject {
   IntegrationProject getProject();

   ProjectFile getProjectFile();

   File getLocation();

   void saveToDisk() throws InterchangeSavingException;

   void saveProjectRelationships() throws InterchangeSavingException;

   void saveIntegrationEntity(IntegrationEntity var1) throws InterchangeSavingException;

   DependencyStore getDependencyStore();

   void rebuildDependencies();

   ProjectUiState getUiState();

   void saveUiState() throws InterchangeSavingException;

   void renameProject(String var1);

   TrashCan getTrashCan();

   FileStore getFileStore();

   ProjectPersistor getProjectPersistor();

   InputAndOutputMappingTreeStructureCache getTransformationMappingStructureCache();

   InputAndOutputMappingTreeStructureCache getWebServiceMappingStructureCache();

   InputAndOutputMappingTreeStructureCache getConnectorCallMappingStructureCache();

   PostExportManager prepareForExport(boolean var1) throws ExportPreparationException;

   ProjectPluginInfo getPipelinePlugins();

   EntityResources getEntityResources();

   ProjectTestHistory getTestHistory();

   DeployHistory getDeployHistory();

   PermissionsManager getPermissions();

   Collection<IntegrationId> getDeletedItemsOfType(EntityType var1);

   void savePluginInfo() throws InterchangeSavingException;

   ValidationModel getValidationModel();

   EventBus getEventBus();

   boolean isReadOnly();

   void projectWasDeployed() throws InterchangeSavingException;

   void partialProjectWasDeployed(IntegrationProject var1) throws InterchangeSavingException;

   void emptyTrashCan();

   void eraseDeletedItemsHistory();
}
