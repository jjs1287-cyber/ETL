package org.etlforge.integration.client.applications.deployer;

import com.google.common.base.Optional;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.DeployResultVisitor;
import org.etlforge.integration.client.deploy.serverapi.EntityWarning;
import org.etlforge.integration.client.deploy.serverapi.IntegrationProjectDeployer;
import org.etlforge.integration.client.deploy.serverapi.JitterPackServerBackup;
import org.etlforge.integration.client.deploy.serverapi.UploadJitterPackCallback;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePluginStore;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.jitterpack.Importer;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.server.IntegrationServer;
import org.etlforge.integration.client.server.ETLForgeServerEncoding;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

final class DeployWorker {
   private final ProjectManager projectManager;
   private final Unpacker unpacker;
   private File tempDir;
   private final ConsoleWriter consoleWriter;
   private boolean backupEnabled;

   public DeployWorker(ProjectManager var1, Importer var2, ConsoleWriter var3) {
      this.projectManager = var1;
      this.consoleWriter = var3;
      this.tempDir = this.createTempDir();
      this.unpacker = new Unpacker(var1, var2, this.tempDir, var3);
   }

   public final void setBackupEnabled(boolean var1) {
      this.backupEnabled = var1;
   }

   public void dispose() {
      if (this.tempDir != null) {
         try {
            FileUtils.deleteDirectory(this.tempDir);
            this.tempDir = null;
         } catch (IOException var2) {
            System.err.println("Failed to delete the folder " + this.tempDir.getAbsolutePath());
         }
      }
   }

   private File createTempDir() {
      try {
         File var1 = File.createTempFile("etlforge_unpack", "", null);
         if (var1.delete() && var1.mkdirs()) {
            return var1;
         } else {
            throw new IOException("Failed to create the temp tmp.");
         }
      } catch (IOException var2) {
         this.consoleWriter.log(var2);
         System.exit(1);
         return null;
      }
   }

   public void deploy(JitterPackCollector var1) {
      JitterPack[] var2 = var1.collect();
      if (var2.length == 0) {
         this.consoleWriter.logError("No Jitterpaks to deploy. Exiting.");
      } else {
         for (JitterPack var6 : var2) {
            this.deploy(var6);
         }
      }
   }

   private void deploy(JitterPack var1) {
      IntegrationProject var2 = this.unpacker.unpack(var1);

      try {
         if (var2 != null) {
            boolean var3 = this.deploy(var2);
            if (var3 && this.backupEnabled) {
               this.sendBackup(var1, var2);
            }
         }
      } finally {
         this.closeProject();
      }
   }

   private boolean deploy(IntegrationProject var1) {
      this.consoleWriter.log("Deploying the project " + var1.getName() + "...");
      var1.markAsDeployDirty();
      DeployData var2 = this.convertProjectToDeployData(var1);
      DeployWorker.DeployCallbackImpl var3 = new DeployWorker.DeployCallbackImpl(var1, this.consoleWriter);
      IntegrationServer var4 = IntegrationServer.getInstance();
      IntegrationProjectDeployer var5 = var4.getServerCall(IntegrationProjectDeployer.class);
      var5.deployProject(var2, var3);
      return var3.waitUntilDone();
   }

   private DeployData convertProjectToDeployData(final IntegrationProject var1) {
      final ManagedProject var2 = var1.getExtensionObject(ManagedProject.class);
      return new DeployData() {
         private static final String GENERATE_JTR_TAG = "<generate>";

         @Override
         public FileStore getFileStore() {
            return var2.getFileStore();
         }

         @Override
         public String getInputMappingStructure(TransformationId var1x) throws DataCacheException {
            InputAndOutputMappingTreeStructureCache var2x = var2.getTransformationMappingStructureCache();
            Optional var3 = var2x.getInputStructure(var1x);
            return this.compressJtr(var3);
         }

         @Override
         public String getOutputMappingStructure(TransformationId var1x) throws DataCacheException {
            InputAndOutputMappingTreeStructureCache var2x = var2.getTransformationMappingStructureCache();
            Optional var3 = var2x.getOutputStructure(var1x);
            return this.compressJtr(var3);
         }

         private String compressJtr(Optional<MappingTreeStructure> var1x) throws DataCacheException {
            if (var1x.isPresent()) {
               try {
                  return ETLForgeServerEncoding.deflateAndBase64EncodeString(((MappingTreeStructure)var1x.get()).getString());
               } catch (IOException var3) {
                  throw new DataCacheException(var3);
               }
            } else {
               return "<generate>";
            }
         }

         @Override
         public PermissionsManager getPermissions() {
            return var2.getPermissions();
         }

         @Override
         public AppliedPipelinePluginStore getPluginFilters() {
            return var2.getPipelinePlugins().getAppliedPlugins();
         }

         @Override
         public EntityResources getEntityResources() {
            return var2.getEntityResources();
         }

         @Override
         public IntegrationProject getProject() {
            return var1;
         }

         @Override
         public boolean isPartial() {
            return false;
         }

         @Override
         public Collection<IntegrationId> getDeletedItemsOfType(EntityType var1x) {
            return var2.getDeletedItemsOfType(var1x);
         }

         @Override
         public String getTag() {
            return null;
         }
      };
   }

   private void sendBackup(JitterPack var1, IntegrationProject var2) {
      this.consoleWriter.log("Sending a backup of the project " + var2.getName() + " to the server...");
      JitterPackServerBackup var3 = new JitterPackServerBackup(var2.getID(), var1.getFile(), null);
      DeployWorker.UploadBackupCallbackImpl var4 = new DeployWorker.UploadBackupCallbackImpl(this.consoleWriter);
      IntegrationServer var5 = IntegrationServer.getInstance();
      IntegrationProjectDeployer var6 = var5.getServerCall(IntegrationProjectDeployer.class);
      var6.uploadJitterPack(var3, var4);
      var4.waitUntilDone();
   }

   private void closeProject() {
      try {
         this.projectManager.closeCurrentProject();
      } catch (CloseProjectVetoException var2) {
         var2.printStackTrace();
      }
   }

   private static final class DeployCallbackImpl implements DeployCallback {
      private final IntegrationProject project;
      private final CountDownLatch latch;
      private final ConsoleWriter consoleWriter;
      private boolean success;

      public DeployCallbackImpl(IntegrationProject var1, ConsoleWriter var2) {
         this.project = var1;
         this.consoleWriter = var2;
         this.latch = new CountDownLatch(1);
      }

      public boolean waitUntilDone() {
         try {
            this.latch.await();
            return this.success;
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            this.consoleWriter.log(var2);
            return false;
         }
      }

      @Override
      public void deployComplete(DeployData var1, DeployResult var2) {
         try {
            DeployWorker.DeployCallbackImpl.ProjectUpdater var3 = new DeployWorker.DeployCallbackImpl.ProjectUpdater();
            var3.visit(var2);
         } finally {
            this.latch.countDown();
         }
      }

      @Override
      public void cancelled() {
         this.caught(new Exception("Canceled"));
      }

      @Override
      public void caught(Throwable var1) {
         this.consoleWriter.logError("Failed to deploy the jitterpak: " + var1.getMessage());
         this.latch.countDown();
      }

      private class ProjectUpdater extends DeployResultVisitor<Void> {
         private ProjectUpdater() {
         }

         protected Void onSuccess(DeployResult var1) {
            DeployCallbackImpl.this.consoleWriter.log("The project was deployed: " + DeployCallbackImpl.this.project.getName());
            this.postDeployProcessing();
            return null;
         }

         protected Void onWarnings(DeployResult var1) {
            DeployCallbackImpl.this.consoleWriter.log("The project was deployed with warnings: " + DeployCallbackImpl.this.project.getName());
            this.writeWarningsToConsole(var1);
            this.postDeployProcessing();
            return null;
         }

         private void writeWarningsToConsole(DeployResult var1) {
            for (String var3 : var1.getGlobalWarnings()) {
               DeployCallbackImpl.this.consoleWriter.log("Warning: " + var3);
            }

            IntegrationEntityLookup var8 = DeployCallbackImpl.this.project.getItemLookup();

            for (EntityWarning var4 : var1.getEntityWarnings()) {
               IntegrationEntityId var5 = var4.getIntegrationEntityId();
               IntegrationEntity var6 = var8.getEntity(var5);
               String var7;
               if (var6 == null) {
                  var7 = "[" + var5.getEntityType() + "] " + var5;
               } else {
                  var7 = var6.toString();
               }

               DeployCallbackImpl.this.consoleWriter.log("Warning for " + var7 + ": " + var4.getWarning());
            }
         }

         private void postDeployProcessing() {
            IntegrationProjectLock.acquire();

            try {
               ManagedProject var1 = DeployCallbackImpl.this.project.getExtensionObject(ManagedProject.class);
               var1.projectWasDeployed();
            } catch (InterchangeSavingException var5) {
               DeployCallbackImpl.this.consoleWriter.log(var5);
            } finally {
               IntegrationProjectLock.release();
               DeployCallbackImpl.this.success = true;
            }
         }

         protected Void onFailure(DeployResult var1) {
            DeployCallbackImpl.this.consoleWriter.logError(var1.getCause().getMessage());
            return null;
         }
      }
   }

   private static class UploadBackupCallbackImpl implements UploadJitterPackCallback {
      private final ConsoleWriter consoleWriter;
      private final CountDownLatch latch = new CountDownLatch(1);

      public UploadBackupCallbackImpl(ConsoleWriter var1) {
         this.consoleWriter = var1;
      }

      public void waitUntilDone() {
         try {
            this.latch.await();
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            this.consoleWriter.log(var2);
         }
      }

      @Override
      public void caught(Throwable var1) {
         this.consoleWriter.logError("Failed to send backup to the server: " + var1.getMessage());
         this.latch.countDown();
      }

      @Override
      public void cancelled() {
         this.consoleWriter.log("The backup was canceled.");
         this.latch.countDown();
      }

      @Override
      public void uploadComplete() {
         this.consoleWriter.log("The backup was sent successfully.");
         this.latch.countDown();
      }
   }
}
