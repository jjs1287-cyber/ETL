package org.etlforge.integration.server.implementation.webservice.interchange.checkout.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.rpc.ServiceException;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.client.project.info.DownloadProjectInfoCallback;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.entity.id.FolderId;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.sfdc.SalesforceProjectFolders;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.de.DefaultGlobalDataElements;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.CheckoutInterchangeData;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.CheckoutInterchangeDataServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.DownloadDataHolder;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.InterchangeDataHolder;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.InterchangeDataRow;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.InterchangeDataTable;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.RequiredFiles;

public final class CheckoutInterchangeProjectWsImplWrapper {
   private static final String SERVICE_NAME = "konga_checkout_interchange_data";
   private String serverName;
   private String user;
   private String password;
   private final WebServiceConfiguration wsConfiguration;
   private final Iterable<EntityType> supportedTypes;
   private Logger logger = Logger.getAnonymousLogger();
   private final Map<IntegrationProjectId, IntegrationProject> m_integrationProjectMap = Maps.newHashMap();
   private final Map<FolderId, Folder> m_folderIdFolderMap = Maps.newHashMap();
   private final Map<IntegrationEntityId, List<Folder>> m_itemIdParentFolderListMap = Maps.newHashMap();

   public CheckoutInterchangeProjectWsImplWrapper(WebServiceConfiguration var1, Iterable<EntityType> var2) {
      this.wsConfiguration = var1;
      this.supportedTypes = var2;
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   private CheckoutInterchangeData getCheckoutInterchangeData(ServerCallback var1) throws ServiceException {
      CheckoutInterchangeDataServiceLocator var2 = new CheckoutInterchangeDataServiceLocator();
      StringBuilder var3 = new StringBuilder();
      if (!this.wsConfiguration.configureWebservice(var2, "konga_checkout_interchange_data", var3, var1)) {
         this.logger.info("Aborted the download process since login failed or was canceled");
         return null;
      } else {
         this.user = this.wsConfiguration.getUserName();
         this.password = this.wsConfiguration.getPasswordHash();
         this.serverName = this.wsConfiguration.getServerName();
         this.logger.info("Connected as " + this.user + "@" + this.serverName + ". URL = " + var3);
         var2.setkonga_checkout_interchange_dataEndpointAddress(var3.toString());
         return var2.getkonga_checkout_interchange_data();
      }
   }

   public String getServerName() {
      return this.serverName;
   }

   public DownloadDataHolder downloadProject(String var1, ServerCallback var2) throws RemoteException, ServiceException {
      CheckoutInterchangeData var3 = this.getCheckoutInterchangeData(var2);
      if (var3 == null) {
         return null;
      }

      this.logger.info("Asking the server for project definitions");
      return var3.downloadIntegrationProject(this.user, this.password, var1);
   }

   public RequiredFiles downloadFiles(int var1, String var2, ServerCallback var3) throws ServiceException, RemoteException {
      CheckoutInterchangeData var4 = this.getCheckoutInterchangeData(var3);
      if (var4 == null) {
         return null;
      } else if (Thread.currentThread().isInterrupted()) {
         var3.cancelled();
         return null;
      } else {
         RequiredFiles var5 = var4.downloadRequiredFiles(this.user, this.password, var1, var2);
         if (Thread.currentThread().isInterrupted()) {
            var3.cancelled();
            return null;
         } else {
            return var5;
         }
      }
   }

   public List<IntegrationProject> getProjectTree(DownloadProjectInfoCallback var1) throws RemoteException, ServiceException {
      CheckoutInterchangeData var2 = this.getCheckoutInterchangeData(var1);
      if (var2 == null) {
         return null;
      } else if (Thread.currentThread().isInterrupted()) {
         var1.cancelled();
         return null;
      } else {
         InterchangeDataHolder var3 = var2.getOperationTree(this.user, this.password);
         if (Thread.currentThread().isInterrupted()) {
            var1.cancelled();
            return null;
         } else {
            this.buildProjectTreeCheckoutMaps(var3.getInterchangeProjectsTab());
            this.addFolders(var3);
            this.buildFolderItemStructure(var3);
            this.addEntities(var3.getSchedulesTab(), new DownloadScheduleHandler());
            this.addEntities(var3.getOperationsTab(), new DownloadOperationHandler());
            this.correctETLForgeConnectFolderNames();
            if (Thread.currentThread().isInterrupted()) {
               var1.cancelled();
               return null;
            } else {
               return Lists.newArrayList(this.m_integrationProjectMap.values());
            }
         }
      }
   }

   private void correctETLForgeConnectFolderNames() {
      for (IntegrationProject var2 : this.m_integrationProjectMap.values()) {
         SalesforceProjectFolders.correctETLForgeConnectFolderNamesInDownloadedProject(var2);
      }
   }

   private void buildProjectTreeCheckoutMaps(InterchangeDataTable var1) {
      if (var1 != null) {
         InterchangeDataParser var2 = new InterchangeDataParser(var1);
         InterchangeDataRow[] var3 = var1.getRows();
         int var4 = var3.length;

         for (int var5 = 0; var5 < var4; var5++) {
            StringKeyValuePairs var6 = var2.parse(var3[var5]);
            String var7 = var6.getString("interchangeproject_name");
            String var8 = var6.getString("interchangeproject_id");
            IntegrationProjectId var9 = (IntegrationProjectId)EntityType.Project.getIntegrationId(var8);
            IntegrationProject var10 = IntegrationProject.createExistingProjectShell(var7, var9, this.supportedTypes);
            var10.setGlobalDataElements(new DefaultGlobalDataElements());
            this.m_integrationProjectMap.put(var9, var10);
         }
      }
   }

   private RootFolder getRootFolder(EntityType var1, IntegrationProject var2) {
      return var2 != null ? var2.getRootFolder(var1) : null;
   }

   private FolderId getFolderId(String var1) {
      return var1.length() == 0 ? null : (FolderId)EntityType.Folder.getIntegrationId(var1);
   }

   private void addFolders(InterchangeDataHolder var1) {
      InterchangeDataTable var2 = var1.getFoldersTab();
      if (var2 != null) {
         InterchangeDataParser var3 = new InterchangeDataParser(var2);
         InterchangeDataRow[] var4 = var2.getRows();
         int var5 = 0;
         EntityType var6 = null;
         ArrayList var7 = Lists.newArrayList();

         for (InterchangeDataRow var11 : var4) {
            StringKeyValuePairs var12 = var3.parse(var11);
            int var13 = var12.getInt("contentsentitytype_id");
            if (var13 != var5) {
               var5 = var13;
               var6 = EntityType.fromId(var5);
            }

            FolderId var14 = this.getFolderId(var12.get("folder_id"));
            FolderId var15 = this.getFolderId(var12.get("parentfolder_id"));
            String var16 = var12.get("folder_name");
            Folder var17 = new Folder(var14, var16, var6);
            this.m_folderIdFolderMap.put(var14, var17);
            IntegrationProject var18 = null;
            String var19 = var12.getString("interchangeproject_id");
            if (var19 != null) {
               IntegrationProjectId var20 = (IntegrationProjectId)EntityType.Project.getIntegrationId(var19);
               var18 = this.m_integrationProjectMap.get(var20);
            }

            if (var15 == null) {
               RootFolder var29 = this.getRootFolder(var6, var18);
               if (var29 == null) {
                  System.out.println("Failed to find RootFolder for Folder " + var17.getName());
               } else {
                  var29.addSubFolder(var17);
               }
            } else {
               Folder var30 = this.m_folderIdFolderMap.get(var15);
               if (var30 == null) {
                  var7.add(new CheckoutInterchangeProjectWsImplWrapper.OrphanFolder(var17, var15));
               } else {
                  var30.addSubFolder(var17);
               }
            }
         }

         int var21 = var7.size();

         while (var21 > 0) {
            int var22 = var21;
            int var23 = 0;

            while (var23 < var21) {
               Folder var25 = ((CheckoutInterchangeProjectWsImplWrapper.OrphanFolder)var7.get(var23)).getFolder();
               FolderId var27 = ((CheckoutInterchangeProjectWsImplWrapper.OrphanFolder)var7.get(var23)).getParentFolderId();
               Folder var28 = this.m_folderIdFolderMap.get(var27);
               if (var28 != null) {
                  var28.addSubFolder(var25);
                  var7.remove(var23);
                  var21 = var7.size();
               } else {
                  var23++;
               }
            }

            if (var22 == var21) {
               System.err.println("Warning: The following folders could not be assigned to another folder:");

               for (Object __etlItem231 : var7) {
                  CheckoutInterchangeProjectWsImplWrapper.OrphanFolder var26 = (CheckoutInterchangeProjectWsImplWrapper.OrphanFolder) __etlItem231;
                  System.err.println(var26.getFolder().getName() + " with ParentFolder_ID = " + var26.getParentFolderId().toString());
               }
            }
         }
      }
   }

   private void addEntityToFolder(IntegrationEntityId var1, Folder var2) {
      List var3 = (List)this.m_itemIdParentFolderListMap.get(var1);
      if (var3 == null) {
         var3 = Lists.newArrayList();
         this.m_itemIdParentFolderListMap.put(var1, var3);
         var3.add(var2);
      }
   }

   private void buildFolderItemStructure(InterchangeDataHolder var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("dataHolder is null.");
      }

      InterchangeDataTable var2 = var1.getFolderItemTab();
      if (var2 != null) {
         InterchangeDataParser var3 = new InterchangeDataParser(var2);
         InterchangeDataRow[] var4 = var2.getRows();

         for (InterchangeDataRow var8 : var4) {
            StringKeyValuePairs var9 = var3.parse(var8);
            FolderId var10 = this.getFolderId(var9.get("parentfolder_id"));
            if (var10 != null) {
               Folder var11 = this.m_folderIdFolderMap.get(var10);
               if (var11 != null) {
                  int var12 = var9.getInt("entitytype_id");
                  EntityType var13 = EntityType.fromId(var12);
                  IntegrationEntityId var14 = var13.getIntegrationId(var9.get("item_id"));
                  this.addEntityToFolder(var14, var11);
               }
            }
         }
      }
   }

   private void addEntities(InterchangeDataTable var1, DownloadEntityHandler var2) {
      if (var1 != null) {
         InterchangeDataParser var3 = new InterchangeDataParser(var1);
         InterchangeDataRow[] var4 = var1.getRows();

         for (InterchangeDataRow var8 : var4) {
            StringKeyValuePairs var9 = var3.parse(var8);
            IntegrationEntity var10 = null;

            try {
               var10 = var2.createEntity(var9);
               if (var10 == null) {
                  continue;
               }
            } catch (ItemCheckoutException var14) {
               System.err.println("Warning: " + var14.getMessage());
               continue;
            }

            String var11 = var9.getString("interchangeproject_id");
            if (var11 != null) {
               IntegrationProjectId var12 = (IntegrationProjectId)EntityType.Project.getIntegrationId(var11);
               IntegrationProject var13 = this.m_integrationProjectMap.get(var12);
               if (var13 == null) {
                  System.err
                     .println(var10 + " belongs to the project with GUID " + var12 + " but the user does not have access to the project itself. Skipping it.");
               } else {
                  this.addEntityToFolderOrProject(var10, var13);
               }
            }
         }
      }
   }

   private void addEntityToFolderOrProject(IntegrationEntity var1, IntegrationProject var2) {
      List var3 = this.m_itemIdParentFolderListMap.get(var1.getID());
      if (var3 != null && !var3.isEmpty()) {
         Folder var4 = (Folder)var3.get(0);
         var4.addItem(var1);
         var3.remove(0);
      } else {
         var2.getRootFolder(var1.getEntityType()).addItem(var1);
      }
   }

   private static final class OrphanFolder {
      private final Folder m_folder;
      private final FolderId m_parentFolderId;

      OrphanFolder(Folder var1, FolderId var2) {
         this.m_folder = var1;
         this.m_parentFolderId = var2;
      }

      public Folder getFolder() {
         return this.m_folder;
      }

      public FolderId getParentFolderId() {
         return this.m_parentFolderId;
      }
   }
}
