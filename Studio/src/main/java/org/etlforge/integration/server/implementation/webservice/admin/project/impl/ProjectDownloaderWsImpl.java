package org.etlforge.integration.server.implementation.webservice.admin.project.impl;

import com.google.common.collect.Maps;
import com.etlforge.integration.client.admin.project.DownloadLog;
import com.etlforge.integration.client.admin.project.ProjectDownloader;
import com.etlforge.integration.client.admin.project.ProjectDownloaderCallback;
import com.etlforge.integration.data.download.ItemDownloadConfig;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.DataFormatException;
import javax.xml.rpc.ServiceException;
import org.apache.commons.io.FileUtils;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.application.filestore.FileCollisionResolver;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.project.permission.PermissionsStorageException;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.CppWebServiceServerCall;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.sfdc.SalesforceProjectFolders;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.DownloadInfo;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.DownloadDataHolder;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.RequiredFile;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.RequiredFiles;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl.CheckoutInterchangeProjectWsImplWrapper;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.zip.ZipUtils;

public final class ProjectDownloaderWsImpl extends CppWebServiceServerCall implements ProjectDownloader {
   private final ProjectManager projectManager;

   public ProjectDownloaderWsImpl(CppWebServiceConfiguration var1, ProjectManager var2) {
      super(var1);
      this.projectManager = var2;
   }

   private void addItems(IntegrationProject var1, DownloadedDataWrapper var2, EntityType var3) throws Exception {
      DownloadLog.LOG.info("Adding " + var3.getPlural() + " to the downloaded project.");

      for (IntegrationEntity var6 : var2.getIntegrationEntities(var3)) {
         this.addItem(var1, var2, var6);
      }

      DownloadLog.LOG.fine("Done adding " + var3.getPlural() + ".");
   }

   private static RootFolder getValidRootFolder(IntegrationProject var0, EntityType var1) throws Exception {
      RootFolder var2 = var0.getRootFolder(var1);
      if (var2 == null) {
         throw new Exception("This version of the ETLForge can not download projects containing " + var1.getPlural() + ".");
      } else {
         return var2;
      }
   }

   private void addItem(IntegrationProject var1, DownloadedDataWrapper var2, IntegrationEntity var3) throws Exception {
      Folder var4 = var2.getParentFolder(var3);
      if (var4 != null) {
         var4.addItem(var3);
      } else {
         getValidRootFolder(var1, var3.getEntityType()).addItem(var3);
      }
   }

   private void addFoldersToProject(IntegrationProject var1, DownloadedDataWrapper var2) throws Exception {
      DownloadLog.LOG.info("Constructing the folder structure in the downloaded project.");

      for (Folder var5 : var2.getFolders()) {
         EntityType var6 = var5.getItemType();
         Folder var7 = var2.getParentFolder(var5);
         if (var7 != null) {
            var7.addSubFolder(var5);
         } else {
            getValidRootFolder(var1, var6).addSubFolder(var5);
         }

         DownloadLog.LOG.finer(String.format("Added the %s folder %s (%s) to the project.", var5.getItemType(), var5.getName(), var5.getID().toString()));
      }

      DownloadLog.LOG.fine("Done constructing the folder structure.");
   }

   private static boolean isUrl(String var0) {
      return var0.indexOf("://") != -1;
   }

   private boolean downloadFiles(FileStore var1, DownloadedDataWrapper var2, ProjectDownloaderCallback var3) throws RemoteException, ServiceException, DataFormatException, FileStoreException {
      DownloadLog.LOG.info("Starting to download required files.");
      CheckoutInterchangeProjectWsImplWrapper var4 = new CheckoutInterchangeProjectWsImplWrapper(
         this.getConfiguration(), this.projectManager.getSupportedEntityTypes()
      );
      Collection var5 = var2.getFilesToDownload();
      HashMap var6 = Maps.newHashMap();

      for (Object __etlItem118 : var5) {
         FileToDownload var8 = (FileToDownload) __etlItem118;
         String var9 = var8.getClientPath();
         DownloadLog.LOG.fine(String.format("Processing the file %s for the %s with ID %s.", var9, var8.getEntityType(), var8.getIntegrationEntityId()));
         boolean var10 = isUrl(var9);
         if (var10 && var6.get(var9) != null) {
            DownloadLog.LOG.fine("The file has already been handled.");
         } else {
            if (!var10) {
               FileStoreFile var11 = var1.getFile(var9);
               if (var11 != null) {
                  DownloadLog.LOG.fine("The file has already been added to the project file store.");
                  continue;
               }
            }

            DownloadLog.LOG.fine("Requesting the file from the server.");
            RequiredFiles var25 = var4.downloadFiles(var8.getEntityType().getServerId(), var8.getIntegrationEntityId().toString(), var3);
            if (var25 == null) {
               return true;
            }

            RequiredFile[] var12 = var25.getFiles();
            if (var12 != null) {
               for (RequiredFile var16 : var12) {
                  if (var16 != null) {
                     var9 = var16.getClientPath();
                     DownloadLog.LOG.fine("Got the file " + var9 + " from the server.");
                     if (isUrl(var9)) {
                        var6.put(var9, "1");
                     } else {
                        String var17 = var16.getZippedBase64EncodedContents();
                        String var18 = ZipUtils.base64DecodeAndInflateString(var17);
                        var1.addFile(var9, var18, FileCollisionResolver.REPLACE_EXISTING);
                        String[] var19 = var16.getDependencies();
                        if (var19 != null && var19.length != 0) {
                           for (String var23 : var19) {
                              var1.addDependency(var9, var23);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      DownloadLog.LOG.fine("Done downloading required files.");
      return false;
   }

   @Override
   public void downloadProject(IntegrationProjectId var1, File var2, ProjectDownloaderCallback var3) {
      this.logDownloadRequest(var1, var2);
      CheckoutInterchangeProjectWsImplWrapper var4 = new CheckoutInterchangeProjectWsImplWrapper(
         this.getConfiguration(), this.projectManager.getSupportedEntityTypes()
      );
      var4.setLogger(DownloadLog.LOG);
      DownloadDataHolder var5 = this.downloadProjectDefinitions(var4, var1, var3);
      if (var5 != null) {
         DownloadedDataWrapper var6 = new DownloadedDataWrapper(var5, this.projectManager.getSupportedEntityTypes(), DownloadLog.LOG);
         IntegrationProject var7 = var6.getIntegrationProject();
         var7.flagAsTemporaryProject();
         String var8 = var4.getServerName();
         DownloadInfo var9 = new DownloadInfo(var8, new Date());
         var7.setDownloadInfo(var9);

         try {
            this.addFoldersToProject(var7, var6);
            this.addNonFoldersToProject(var6, var7);
         } catch (Exception var16) {
            var3.caught(var16);
            return;
         }

         this.doPostProjectAssemblyProcessing(var7);
         IntegrationProjectLock.acquire();
         ManagedProject var10 = null;

         try {
            var10 = this.createManagedProject(var7);
            var3.getTracker().downloadingRequiredFiles();
            if (!this.downloadFiles(var10.getFileStore(), var6, var3)) {
               this.addPermissions(var6, var10);
               this.addPlugins(var6, var10);
               this.addJtrFiles(var6, var10);
               this.addEntityResources(var6, var10);
               this.saveProjectToDisk(var7, var10);
               File var11 = this.copyProjectToFinalLocation(var2, var7, var10);
               DownloadLog.LOG.info("========== The project has been downloaded. Notifying the callback.\n\n");
               var3.downloadCompleted(new ProjectLocation(var7.getName(), var11));
               return;
            }
         } catch (Exception var17) {
            DownloadLog.LOG.throwing(this.getClass().getName(), "downloadProject", var17);
            var3.caught(var17);
            return;
         } finally {
            this.doCleanup(var10);
         }
      }
   }

   private void saveProjectToDisk(IntegrationProject var1, ManagedProject var2) throws InterchangeSavingException {
      DownloadLog.LOG.info("Saving the downloaded project to a temporary location: " + var2.getLocation().getAbsolutePath());
      this.setDeployClean(var1);
      var2.saveToDisk();
      DownloadLog.LOG.fine("The project was successfully saved to disk.");
   }

   private void addPermissions(DownloadedDataWrapper var1, ManagedProject var2) throws PermissionsStorageException {
      DownloadLog.LOG.info("Setting the user and group permissions on the downloaded project.");
      PermissionsManager var3 = var2.getPermissions();
      var1.setPermissions(var3);
      var3.save();
      DownloadLog.LOG.fine("Done setting permissions.");
   }

   private void addPlugins(DownloadedDataWrapper var1, ManagedProject var2) {
      DownloadLog.LOG.info("Restoring plugin settings.");
      ProjectPluginInfo var3 = var2.getPipelinePlugins();
      MutableAppliedPipelinePluginStore var4 = var3.getAppliedPlugins();
      var1.setPluginAssociations(var4);
      DownloadLog.LOG.fine("Done restoring plugin settings.");
   }

   private void addJtrFiles(DownloadedDataWrapper var1, ManagedProject var2) throws DataCacheException, IOException {
      DownloadLog.LOG.info("Writing JTR files to the project cache.");
      var1.addJtrFiles(var2);
      DownloadLog.LOG.fine("Done writing JTR files.");
   }

   private void addEntityResources(DownloadedDataWrapper var1, ManagedProject var2) {
      DownloadLog.LOG.info("Adding entity resources to the downloaded project.");
      EntityResources var3 = var2.getEntityResources();
      var1.addEntityResources(var3);
      DownloadLog.LOG.fine("Done adding entity resources.");
   }

   private File copyProjectToFinalLocation(File var1, IntegrationProject var2, ManagedProject var3) throws IOException {
      File var4 = new File(var1, var2.getName());
      this.copyDownloadedData(var3.getLocation(), var4);
      return var4;
   }

   private void copyDownloadedData(File var1, File var2) throws IOException {
      DownloadLog.LOG.info("Copying the downloaded project to its final location: " + var2.getAbsolutePath());
      if (var2.isDirectory()) {
         try {
            FileUtils.cleanDirectory(var2);
         } catch (IOException var4) {
            ErrorLog.log(
               ProjectDownloaderWsImpl.class,
               "Failed to clean the folder \"" + var2.getAbsolutePath() + "\" before moving the downloaded project data there.",
               var4
            );
         }
      }

      FileUtils.copyDirectory(var1, var2);
      DownloadLog.LOG.fine("The project has been copied to its final location.");
   }

   private void doPostProjectAssemblyProcessing(IntegrationProject var1) {
      SalesforceProjectFolders.correctETLForgeConnectFolderNamesInDownloadedProject(var1);
      var1.refreshItemLookup();
      ExtensionObjectService var2 = ExtensionObjectService.getService();

      for (EntityType var6 : EntityType.values()) {
         ItemDownloadConfig var7 = var2.getExtensionObject(var6, ItemDownloadConfig.class);
         if (var7 != null) {
            var7.doPostProjectAssemblyProcessing(var1);
         }
      }
   }

   private void addNonFoldersToProject(DownloadedDataWrapper var1, IntegrationProject var2) throws Exception {
      this.addItems(var2, var1, EntityType.Source);
      this.addItems(var2, var1, EntityType.Target);
      this.addItems(var2, var1, EntityType.Schedule);
      this.addItems(var2, var1, EntityType.EmailMessage);
      this.addItems(var2, var1, EntityType.Script);
      this.addItems(var2, var1, EntityType.Document);
      this.addItems(var2, var1, EntityType.WebServiceCall);
      this.addItems(var2, var1, EntityType.SapEndpoint);
      this.addItems(var2, var1, EntityType.SapFunction);
      this.addItems(var2, var1, EntityType.MSAXEndpoint);
      this.addItems(var2, var1, EntityType.MSAXFunction);
      this.addItems(var2, var1, EntityType.EBSEndpoint);
      this.addItems(var2, var1, EntityType.EBSFunction);
      this.addItems(var2, var1, EntityType.MSCrmEndpoint);
      this.addItems(var2, var1, EntityType.MSCrmCreate);
      this.addItems(var2, var1, EntityType.MSCrmUpdate);
      this.addItems(var2, var1, EntityType.MSCrmUpsert);
      this.addItems(var2, var1, EntityType.MSCrmDelete);
      this.addItems(var2, var1, EntityType.MSCrmQuery);
      this.addItems(var2, var1, EntityType.MSGPEndpoint);
      this.addItems(var2, var1, EntityType.MSGPCreate);
      this.addItems(var2, var1, EntityType.MSGPUpdate);
      this.addItems(var2, var1, EntityType.MSGPDelete);
      this.addItems(var2, var1, EntityType.AutoDeskEndpoint);
      this.addItems(var2, var1, EntityType.AutoDeskGet);
      this.addItems(var2, var1, EntityType.AutoDeskCreate);
      this.addItems(var2, var1, EntityType.AutoDeskUpdate);
      this.addItems(var2, var1, EntityType.AutoDeskUpsert);
      this.addItems(var2, var1, EntityType.AutoDeskDelete);
      this.addItems(var2, var1, EntityType.NetSuiteEndpoint);
      this.addItems(var2, var1, EntityType.NetSuiteCreate);
      this.addItems(var2, var1, EntityType.NetSuiteUpdate);
      this.addItems(var2, var1, EntityType.NetSuiteUpsert);
      this.addItems(var2, var1, EntityType.NetSuiteDelete);
      this.addItems(var2, var1, EntityType.NetSuiteQuery);
      this.addItems(var2, var1, EntityType.Transformation);
      this.addItems(var2, var1, EntityType.SalesforceConnector);
      this.addItems(var2, var1, EntityType.SalesforceBulkLoad);
      this.addItems(var2, var1, EntityType.Operation);
      this.addItems(var2, var1, EntityType.HttpEndpoint);
      this.addItems(var2, var1, EntityType.SalesforceQuery);
      this.addItems(var2, var1, EntityType.SalesforceUpsert);
      this.addItems(var2, var1, EntityType.SalesforceCreate);
      this.addItems(var2, var1, EntityType.SalesforceUpdate);
      this.addItems(var2, var1, EntityType.SalesforceDelete);
   }

   private void logDownloadRequest(IntegrationProjectId var1, File var2) {
      DownloadLog.LOG
         .info(
            String.format(
               "========== Received request to download the project with GUID %s. The project will be downloaded to the folder: %s.",
               var1,
               var2.getAbsolutePath()
            )
         );
   }

   private DownloadDataHolder downloadProjectDefinitions(
      CheckoutInterchangeProjectWsImplWrapper var1, IntegrationProjectId var2, ProjectDownloaderCallback var3
   ) {
      try {
         var3.getTracker().downloadingProjectDefinitions();
         DownloadDataHolder var4 = var1.downloadProject(var2.toString(), var3);
         DownloadLog.LOG.info("The project definitions have been downloaded from the server.");
         return var4;
      } catch (RemoteException var5) {
         this.logErrorAndNotifyCallback("downloadProjectDefinitions", var5, var3);
         return null;
      } catch (ServiceException var6) {
         this.logErrorAndNotifyCallback("downloadProjectDefinitions", var6, var3);
         return null;
      }
   }

   private void logErrorAndNotifyCallback(String var1, Exception var2, ProjectDownloaderCallback var3) {
      DownloadLog.LOG.throwing(this.getClass().getName(), var1, var2);
      var3.caught(var2);
   }

   private void doCleanup(ManagedProject var1) {
      try {
         if (var1 != null) {
            this.closeTemporaryProject(var1);
            this.deleteTemporaryProject(var1);
         }
      } finally {
         IntegrationProjectLock.release();
      }
   }

   private void closeTemporaryProject(ManagedProject var1) {
      try {
         this.projectManager.closeProject(var1);
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   private void deleteTemporaryProject(ManagedProject var1) {
      try {
         File var2 = var1.getLocation();
         FileUtils.deleteDirectory(var2);
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   private void setDeployClean(IntegrationProject var1) {
      var1.visitAllEntities(new Receiver<IntegrationEntity>() {
         public void handle(IntegrationEntity var1) {
            var1.setDeployDirty(false);
            var1.setDeployed(true);
         }
      });
   }

   private ManagedProject createManagedProject(IntegrationProject var1) throws InterchangeSavingException {
      File var2 = ApplicationTempFolder.newLikelyUniqueFolder("P", true);
      return this.projectManager.createManagedProject(var1, var2);
   }
}

