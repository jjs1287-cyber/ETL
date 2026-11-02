package org.etlforge.integration.client.ui.deploy;

import com.google.common.base.Optional;
import java.io.IOException;
import java.util.Collection;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePluginStore;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.server.ETLForgeServerEncoding;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

public final class DeployDataImpl implements DeployData {
   private static final String GENERATE_JTR_TAG = "<generate>";
   private final ManagedProject managedProject;
   private final IntegrationProject projectData;
   private final boolean partial;
   private String tag;

   public DeployDataImpl(IntegrationProject var1, boolean var2) {
      this(var1.getExtensionObject(ManagedProject.class), var1, var2);
   }

   public DeployDataImpl(ManagedProject var1, IntegrationProject var2, boolean var3) {
      this.managedProject = var1;
      this.projectData = var2;
      this.partial = var3;
   }

   public ManagedProject getManagedProject() { return this.managedProject; }

   @Override
   public FileStore getFileStore() {
      return this.managedProject.getFileStore();
   }

   @Override
   public String getInputMappingStructure(TransformationId var1) throws DataCacheException {
      InputAndOutputMappingTreeStructureCache var2 = this.managedProject.getTransformationMappingStructureCache();
      Optional var3 = var2.getInputStructure(var1);
      return this.compressJtr(var3);
   }

   @Override
   public String getOutputMappingStructure(TransformationId var1) throws DataCacheException {
      InputAndOutputMappingTreeStructureCache var2 = this.managedProject.getTransformationMappingStructureCache();
      Optional var3 = var2.getOutputStructure(var1);
      return this.compressJtr(var3);
   }

   private String compressJtr(Optional<MappingTreeStructure> var1) throws DataCacheException {
      if (var1.isPresent()) {
         try {
            return ETLForgeServerEncoding.deflateAndBase64EncodeString(((MappingTreeStructure)var1.get()).getString());
         } catch (IOException var3) {
            throw new DataCacheException(var3);
         }
      } else {
         return "<generate>";
      }
   }

   @Override
   public PermissionsManager getPermissions() {
      return this.managedProject.getPermissions();
   }

   @Override
   public AppliedPipelinePluginStore getPluginFilters() {
      return this.managedProject.getPipelinePlugins().getAppliedPlugins();
   }

   @Override
   public EntityResources getEntityResources() {
      return this.managedProject.getEntityResources();
   }

   @Override
   public IntegrationProject getProject() {
      return this.projectData;
   }

   @Override
   public boolean isPartial() {
      return this.partial;
   }

   @Override
   public Collection<IntegrationId> getDeletedItemsOfType(EntityType var1) {
      return this.managedProject.getDeletedItemsOfType(var1);
   }

   @Override
   public String getTag() {
      return this.tag;
   }

   public void setTag(String var1) {
      this.tag = var1;
   }
}
