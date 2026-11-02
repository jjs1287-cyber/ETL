package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.xml.rpc.ServiceException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.application.filestore.FileStoreFile;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.client.deploy.serverapi.DeployCallback;
import org.etlforge.integration.client.deploy.serverapi.DeployData;
import org.etlforge.integration.client.deploy.serverapi.EntityWarning;
import org.etlforge.integration.client.deploy.serverapi.JitterPackServerBackup;
import org.etlforge.integration.client.deploy.serverapi.UploadJitterPackCallback;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePluginStore;
import org.etlforge.integration.client.project.deploy.DeployLog;
import org.etlforge.integration.client.project.permission.GroupPermission;
import org.etlforge.integration.client.project.permission.ItemPermissions;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.project.permission.UserPermission;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.common.server.ClientServerCommunicationUtils;
import org.etlforge.integration.data.CallRestrictions;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.TransformationChainLink;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.httpendpoint.OperationRunMode;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperation;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperations;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.CallStructureSchema;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.WebServiceStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.data.structure.XsdBasedStructure;
import org.etlforge.integration.data.structure.database.DbFields;
import org.etlforge.integration.data.structure.database.DbTables;
import org.etlforge.integration.data.structure.database.DbTranFilters;
import org.etlforge.integration.data.structure.database.DbTranRelations;
import org.etlforge.integration.data.structure.database.SqlScript;
import org.etlforge.integration.data.structure.ldap.LdapSearchParameters;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.server.implementation.webservice.impl.common.folders.FolderItem;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ClientServerDeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.DeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ServerPropertyKey;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ServerPropertyKeyType;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CDeployResults;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CDeployWarning;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CFileCheckResult;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CFileDependencies;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CIntegrationEntity;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CJtrFile;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CJtrFiles;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CPermission;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CRequiredFile;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CRequiredFiles;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CUploadedJitterpak;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.CUserPermissions;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.DeployInterchangeData;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.DeployInterchangeDataServiceLocator;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeDataHolder;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeDataRow;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeDataTable;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.InterchangeIdHolder;
import org.etlforge.integration.server.implementation.webservice.interchange.deploy.client.PermissionsHolder;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.RoleId;
import org.etlforge.integration.user.UserId;
import org.etlforge.util.zip.ZipUtils;

public final class DeployInterchangeModelWsImplWrapper {
   private static final String SERVICE_NAME = "konga_deploy_interchange_data";
   private static final int schemaFileType = 0;
   private static final int dtdFileType = 1;
   private static final int wsdlFileType = 2;
   private static final String SIBLING_OPERATION_ID = "siblingoperation_id";
   private final WebServiceConfiguration wsConfiguration;
   private final ServerConnectionService connectionService;
   private Optional<Predicate<ServerInfo>> localHostDetector = Optional.absent();
   private ServerInfo serverInfo;
   private final Map<RoleId, List<CPermission>> m_userPermissions = Maps.newHashMap();
   private final List<InterchangeDataRow> m_updatedHttpEndpointOperations = new ArrayList<>();
   private final List<InterchangeDataRow> m_updatedDataElements = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedJmsLocationProperties = Lists.newArrayList();
   private final List<InterchangeDataRow> m_commonSourceOperations = Lists.newArrayList();
   private final List<InterchangeDataRow> m_operationPipelines = Lists.newArrayList();
   private final List<InterchangeDataRow> m_operationGraphLayout = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedCallRestrictions = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedDocumentDefinitions = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedDocumentFieldValidations = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedDbFields = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedDbTables = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedDbTranRelations = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedSqlScript = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedTranFilters = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedTranDef = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedTranChainLinks = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedLdapSearchParameters = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedLdapStructureEntry = Lists.newArrayList();
   private final List<CRequiredFile> m_requiredFiles = Lists.newArrayList();
   private final Map<String, List<String>> m_fileDependencies = Maps.newHashMap();
   private String[] m_clientColumnNamesFolders = null;
   private String[] m_serverColumnNamesFolders = null;
   private String[] m_serverColumnNamesExtendedFolders = null;
   private final List<InterchangeDataRow> m_updatedFolders = Lists.newArrayList();
   private final List<InterchangeDataRow> m_updatedFolderItems = Lists.newArrayList();
   private String[] m_clientColumnNames = null;
   private String[] m_serverColumnNames = null;
   private String[] m_serverColumnNamesExtended = null;
   private List<InterchangeDataRow> m_updatedRows = null;
   private List<String> m_deletedIds = null;
   private final List<InterchangeDataRow> m_extendedEntityProperties = Lists.newArrayList();
   private DeployData m_deployData;
   private PermissionsManager m_permissionsManager;
   private FileStore m_fileStore;
   private AppliedPipelinePluginStore m_pipelinePluginStore;
   private final ArrayList<EntityPipelinePlugin> m_entityPipelinePlugins = Lists.newArrayList();
   private final List<CJtrFile> m_transformationJtrs = Lists.newArrayList();

   public DeployInterchangeModelWsImplWrapper(WebServiceConfiguration var1, ServerConnectionService var2) {
      this.wsConfiguration = var1;
      this.connectionService = var2;
   }

   public void setLocalHostDetector(Optional<Predicate<ServerInfo>> var1) {
      this.localHostDetector = Preconditions.checkNotNull(var1);
   }

   private DeployInterchangeData getDeployer(ServerCallback var1) throws IntegrationServerException {
      try {
         DeployInterchangeDataServiceLocator var2 = new DeployInterchangeDataServiceLocator();
         StringBuilder var3 = new StringBuilder();
         if (!this.wsConfiguration.configureWebservice(var2, "konga_deploy_interchange_data", var3, var1)) {
            return null;
         }

         this.serverInfo = this.wsConfiguration.getServerInfo();
         var2.setkonga_deploy_interchange_dataEndpointAddress(var3.toString());
         return var2.getkonga_deploy_interchange_data();
      } catch (ServiceException var4) {
         throw new IntegrationServerException("Failed to call webservice: " + var4.getMessage(), var4);
      }
   }

   private void updateEntity(IntegrationEntity var1, DeployKeyMappings var2) {
      if (this.m_serverColumnNames == null) {
         this.m_serverColumnNamesExtended = null;
         this.m_clientColumnNames = var1.getPropertyKeys();
         ArrayList var3 = Lists.newArrayList();
         ArrayList var4 = Lists.newArrayList();

         for (String var8 : this.m_clientColumnNames) {
            ServerPropertyKey var9 = var2.get(var8);
            if (var9 == null) {
               reportMissingDeployPropertyKey(var8, this.getClass());
               return;
            }

            if (var9.getType() == ServerPropertyKeyType.STANDARD) {
               var3.add(var9.getServerKeyName());
            } else {
               var4.add(var9.getServerKeyName());
            }
         }

         this.m_serverColumnNames = (String[]) var3.toArray(new String[var3.size()]);
         if (var4.size() > 0) {
            this.m_serverColumnNamesExtended = (String[]) var4.toArray(new String[var4.size()]);
         }
      }

      updateEntityDetails(
         var1,
         var2,
         this.m_serverColumnNames,
         this.m_serverColumnNamesExtended,
         this.m_clientColumnNames,
         this.m_updatedRows,
         this.m_extendedEntityProperties,
         this.getClass()
      );
   }

   private static void updateEntityDetails(
      IntegrationEntity var0,
      DeployKeyMappings var1,
      String[] var2,
      String[] var3,
      String[] var4,
      List<InterchangeDataRow> var5,
      List<InterchangeDataRow> var6,
      Class<?> var7
   ) {
      String[] var8 = var0.getPropertyKeys();
      String[] var9 = var0.getPropertyValuesForDeploy(var8);
      String[] var10 = new String[var2.length];
      StringKeyValuePairs var11 = new StringKeyValuePairs();
      int var12 = -1;
      int var13 = -1;

      for (int var14 = 0; var14 < var9.length; var14++) {
         String var15 = var8[var14];
         String var16 = var9[var14];
         ServerPropertyKey var17 = var1.get(var15);
         if (var17 == null) {
            reportMissingDeployPropertyKey(var15, var7);
            return;
         }

         if (var17.getType() == ServerPropertyKeyType.STANDARD) {
            var10[++var12] = var16;
         } else {
            var13++;
            boolean var18 = var14 < var4.length && var15.equals(var4[var14]);
            String var19 = var18 ? var3[var13] : var15;
            if (var16 != null) {
               var11.put(var19, var16);
            }
         }
      }

      var5.add(getInterchangeDataRow(var10));
      if (var11.size() > 0) {
         String var20 = Integer.toString(var0.getEntityType().getId());
         String var21 = var0.getID().toString();

         for (String var23 : var11.getKeys()) {
            String[] var24 = new String[]{var20, var21, var23, var11.get(var23)};
            var6.add(getInterchangeDataRow(var24));
         }
      }
   }

   private static void reportMissingDeployPropertyKey(String var0, Class<?> var1) {
      Exception var2 = new Exception("There is no server property key for client property " + var0);
      ErrorLog.attention(var1, "A client configuration error prevents deploy from continuing. This is a bug. Please report it to ETLForge Support.", var2);
   }

   private void updateEntity(IntegrationEntity var1) throws DeployException {
      if (var1 instanceof org.etlforge.integration.data.entity.ProjectVariable) {
         if (var1.isDeployDirty()) {
            org.etlforge.integration.data.entity.ProjectVariable variable = (org.etlforge.integration.data.entity.ProjectVariable)var1;
            for (String key : new String[]{"projectvariable_name", "default_value_utf8", "is_password", "description_utf8"}) {
               this.m_extendedEntityProperties.add(getInterchangeDataRow(new String[]{"35", variable.getID().toString(), key,
                  "default_value_utf8".equals(key) ? java.util.Base64.getEncoder().encodeToString(variable.getDefaultValue().getBytes(java.nio.charset.StandardCharsets.UTF_8)) : "is_password".equals(key) ? (variable.isPassword() ? "1" : "0") : "description_utf8".equals(key) ? java.util.Base64.getEncoder().encodeToString(variable.getDescription().getBytes(java.nio.charset.StandardCharsets.UTF_8)) : variable.getName()}));
            }
         }
         return;
      }
      if (var1 instanceof org.etlforge.integration.data.entity.XsltTransform) {
         if (var1.isDeployDirty()) {
            org.etlforge.integration.data.entity.XsltTransform xslt = (org.etlforge.integration.data.entity.XsltTransform)var1;
            for (String key : new String[]{"xslttransform_name", "xslt"}) {
               this.m_extendedEntityProperties.add(getInterchangeDataRow(new String[]{"34", xslt.getID().toString(), key,
                  "xslt".equals(key) ? xslt.getXslt() : xslt.getName()}));
            }
         }
         return;
      }
      if (var1.isDeployDirty()) {
         DeployKeyMappings var2 = ClientServerDeployKeyMappings.getClientServerKeyMappings(var1.getEntityType());
         if (var2 == null) {
            if (this.m_serverColumnNames == null) {
               this.m_serverColumnNames = var1.getPropertyKeys();
            }

            String[] var3 = var1.getPropertyValuesForDeploy();
            this.m_updatedRows.add(getInterchangeDataRow(var3));
         } else {
            this.updateEntity(var1, var2);
         }

         this.updateEntityDetails(var1);
         this.updatePipelinePlugins(var1);
      }
   }

   private void updatePermissions(IntegrationEntity var1) {
      this.updatePermissions(var1, true);
   }

   private void updatePermissions(IntegrationEntity var1, boolean var2) {
      if (!var2 || var1.isDeployDirty()) {
         ItemPermissions var3 = this.m_permissionsManager.getItemPermissions(var1);

         for (UserPermission var5 : var3.getUserPermissions()) {
            UserId var6 = var5.roleId;
            Permissions var7 = var5.permissions;
            List var8 = this.m_userPermissions.get(var6);
            if (var8 == null) {
               var8 = Lists.newArrayList();
               this.m_userPermissions.put(var6, var8);
            }

            var8.add(new CPermission(var1.getID().toString(), var1.getEntityType().getId(), var7.getAllowedBits(), var7.getDeniedBits()));
         }

         for (GroupPermission var10 : var3.getGroupPermissions()) {
            GroupId var11 = var10.roleId;
            Permissions var12 = var10.permissions;
            List var13 = this.m_userPermissions.get(var11);
            if (var13 == null) {
               var13 = Lists.newArrayList();
               this.m_userPermissions.put(var11, var13);
            }

            var13.add(new CPermission(var1.getID().toString(), var1.getEntityType().getId(), var12.getAllowedBits(), var12.getDeniedBits()));
         }
      }
   }

   private void updatePipelinePlugins(IntegrationEntity var1) {
      if (this.m_pipelinePluginStore != null) {
         if (var1.isDeployDirty()) {
            AppliedPipelinePlugin[] var2 = this.m_pipelinePluginStore.getAllAssociations(var1);
            if (var2.length == 0) {
               this.m_entityPipelinePlugins.add(EntityPipelinePlugin.getInstanceForDelete(var1));
            }

            for (AppliedPipelinePlugin var6 : var2) {
               if (!var6.getEntityId().equals(var1.getID())) {
                  throw new IllegalStateException("Internal Error: Applied pipeline plugin entity type is not equal to the entity type used to get it!");
               }

               EntityPipelinePlugin var7 = new EntityPipelinePlugin();
               var7.EntityId = var1.getID().toString();
               var7.EntityTypeId = var1.getEntityType().getServerId();
               PipelinePosition var8 = var6.getPipelinePosition();
               var7.PipelineEntityTypeId = var8.getEntityType().getId();
               var7.RelativePositionId = var8.getRelativePosition().toString();
               if (!var6.getPluginId().equals(var8.getPluginId())) {
                  throw new IllegalStateException("Internal Error: Applied pipeline and pipeline position have different Plugins associated with them.");
               }

               var7.PipelinePluginGuid = var6.getPluginId().getName();
               this.m_entityPipelinePlugins.add(var7);
            }
         }
      }
   }

   private void addFolderItem(Folder var1, IntegrationEntity var2) {
      if (var2.getEntityType() != EntityType.Project) {
         String[] var3 = FolderItem.getPropertyValues(var1, var2);
         if (var3[0] != null && !var3[0].isEmpty() && var3[0] != "0") {
            InterchangeDataRow var4 = getInterchangeDataRow(var3);
            this.m_updatedFolderItems.add(var4);
         }
      }
   }

   private void updateFolder(Folder var1) throws DeployException {
      if (var1 != null) {
         for (IntegrationEntity var3 : var1.getAllChildren()) {
            this.updatePermissions(var3);
            if (var3 instanceof Folder) {
               Folder var4 = (Folder)var3;
               if (var4.isDeployDirty() || var4.hasMoved()) {
                  this.addFolderProperties(var4, var1);
               }

               this.updatePermissions(var3);
               this.updateFolder(var4);
            } else {
               this.updateEntity(var3);
               this.addFolderItem(var1, var3);
            }
         }
      }
   }

   private void addFolderProperties(Folder var1, Folder var2) {
      DeployKeyMappings var3 = ClientServerDeployKeyMappings.getClientServerKeyMappings(var1.getEntityType());
      if (this.m_serverColumnNamesFolders == null) {
         this.m_serverColumnNamesExtendedFolders = null;
         this.m_clientColumnNamesFolders = var1.getPropertyKeys();
         ArrayList var4 = Lists.newArrayList();
         ArrayList var5 = Lists.newArrayList();

         for (String var9 : this.m_clientColumnNamesFolders) {
            ServerPropertyKey var10 = var3.get(var9);
            if (var10 == null) {
               reportMissingDeployPropertyKey(var9, this.getClass());
               return;
            }

            if (var10.getType() == ServerPropertyKeyType.STANDARD) {
               var4.add(var10.getServerKeyName());
            } else {
               var5.add(var10.getServerKeyName());
            }
         }

         this.m_serverColumnNamesFolders = (String[]) var4.toArray(new String[var4.size()]);
         if (var5.size() > 0) {
            this.m_serverColumnNamesExtendedFolders = (String[]) var5.toArray(new String[var5.size()]);
         }
      }

      updateEntityDetails(
         var1,
         var3,
         this.m_serverColumnNamesFolders,
         this.m_serverColumnNamesExtendedFolders,
         this.m_clientColumnNamesFolders,
         this.m_updatedFolders,
         this.m_extendedEntityProperties,
         this.getClass()
      );
      this.addFolderItem(var2, var1);
   }

   private void addTriggeredOperations(String var1, HttpVerb var2, Collection<TriggeredOperation> var3) {
      if (var3 != null && !var3.isEmpty()) {
         for (TriggeredOperation var5 : var3) {
            String[] var6 = new String[]{
               var1,
               var5.getOperationId().toString(),
               var5.getRunMode() == OperationRunMode.SYNCHRONOUS ? "1" : "0",
               var5.isResponseGenerator() ? "1" : "0",
               var2 == null ? "" : var2.toString()
            };
            this.m_updatedHttpEndpointOperations.add(getInterchangeDataRow(var6));
         }
      }
   }

   private void updateHttpEndpoint(HttpEndpoint var1) {
      TriggeredOperations var2 = var1.getTriggeredOperations();
      if (var2 != null && !var2.isEmpty()) {
         String var3 = var1.getID().toString();
         LinkedHashSet var4 = var2.getOperationsForVerb(null);
         this.addTriggeredOperations(var3, null, var4);

         for (HttpVerb var8 : HttpVerb.values()) {
            var4 = var2.getOperationsForVerb(var8);
            this.addTriggeredOperations(var3, var8, var4);
         }
      }
   }

   private void updateEntityDetails(IntegrationEntity var1) throws DeployException {
      switch (var1.getEntityType()) {
         case Document:
            this.updateTextDocument((TextDocument)var1);
            break;
         case Transformation:
            this.updateTransformation((Transformation)var1);
            break;
         case WebServiceCall:
            this.updateWebServiceCall((WebServiceCall)var1);
            break;
         case Operation:
            this.updateOperation((Operation)var1);
            break;
         case HttpEndpoint:
            this.updateHttpEndpoint((HttpEndpoint)var1);
            break;
         default:
            if (var1 instanceof ConnectorCall) {
               this.updateConnectorCall((ConnectorCall)var1);
            }
      }
   }

   private String[] getDataElementsKeyNames() {
      return new String[]{"ownerentitytypeid", "ownerentityid", "dataelementname", "dataelementtype", "dataelementdefaultvalue"};
   }

   private String[] getOperationPipelineKeys() {
      return new String[]{"Operation_ID", "ActivityType_ID", "Activity_ID"};
   }

   private String[] getOperationGraphLayoutKeys() {
      return new String[]{"Operation_ID", "Layout"};
   }

   private void updateOperation(Operation var1) {
      OperationPipeline var2 = var1.getPipeline();
      if (var2 != null) {
         DeployPipelineActivityVisitor var3 = new DeployPipelineActivityVisitor();
         List var4 = var2.getActivities();
         ArrayList var5 = Lists.newArrayList();

         for (Object __etlItem514 : var4) {
            PipelineActivity var7 = (PipelineActivity) __etlItem514;
            var3.visit(var7);
            IntegrationId var8 = var3.getFunctionId();
            if (var8 != null) {
               var5.add(var8);
            }
         }

         if (var5.size() > 1) {
            IntegrationId var16 = (IntegrationId)var5.get(0);
            String[] var20 = null;
            int var25 = 1;

            for (int var9 = var5.size(); var25 < var9; var25++) {
               IntegrationId var10 = (IntegrationId)var5.get(var25);
               var20 = TransformationChainLink.getPropertyValues(var1, var16, var10);
               this.m_updatedTranChainLinks.add(getInterchangeDataRow(var20));
               var16 = var10;
            }
         }
      }

      String var12 = var1.getID().toString();

      for (OperationId var17 : var1.getSiblingOperations()) {
         String[] var22 = new String[]{var12, var17.toString()};
         this.m_commonSourceOperations.add(new InterchangeDataRow(var22));
      }

      DeployPipelineActivityVisitor var15 = new DeployPipelineActivityVisitor();

      for (PipelineActivity var23 : var1.getPipeline().getActivities()) {
         var15.visit(var23);
         String var26 = var15.getGuid();
         if (var26 != null) {
            int var28 = var23.getType().getActivityTypeId();
            String[] var29 = new String[]{var12, Integer.toString(var28), var26};
            this.m_operationPipelines.add(new InterchangeDataRow(var29));
         }
      }

      EntityResources var19 = this.m_deployData.getEntityResources();
      String var24 = var19.getFileResource(var1, "GraphLayout.xml").read();
      if (var24 != null) {
         try {
            String[] var27 = new String[]{var12, ZipUtils.deflateAndBase64EncodeString(var24)};
            this.m_operationGraphLayout.add(new InterchangeDataRow(var27));
         } catch (IOException var11) {
            var11.printStackTrace();
         }
      }
   }

   private static boolean isUrl(String var0) {
      return var0.indexOf("://") != -1;
   }

   private void updateWebServiceCall(WebServiceCall var1) throws DeployException {
      WsdlFile var2 = var1.getWsdlFile();
      CallRestrictions var3 = var1.getCallRestrictions();
      String[] var4 = var3.getPropertyValues(var1.getID().toString(), 1);
      this.m_updatedCallRestrictions.add(getInterchangeDataRow(var4));

      try {
         String var5 = var2.getLocator();
         if (isUrl(var5)) {
            this.addRequiredUrl(var5, 2);
         } else {
            this.addRequiredFile(var5, 2);
         }
      } catch (DeployException var7) {
         String var6 = "Failed to get the WSDL file needed by the web service call " + var1.getName() + " from the file store. " + var7.getMessage();
         throw new DeployException(var6, var7);
      }
   }

   private void updateTextDocument(TextDocument var1) {
      String var2 = var1.getID().toString();
      ArrayList var3 = Lists.newArrayList();

      for (Object __etlItem594 : var1.getDataStructure().getFlatDefinition(var3)) {
         String[] var6 = (String[]) __etlItem594;
         var6[0] = var2;
         this.m_updatedDocumentDefinitions.add(getInterchangeDataRow(var6));
      }

      for (Object __etlItem599 : var3) {
         String[] var8 = (String[]) __etlItem599;
         var8[0] = var2;
         this.m_updatedDocumentFieldValidations.add(getInterchangeDataRow(var8));
      }
   }

   private boolean hasFileBeenAdded(String var1) {
      for (CRequiredFile var3 : this.m_requiredFiles) {
         if (var3.getPath().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   private void addRequiredUrl(String var1, int var2) {
      if (!this.hasFileBeenAdded(var1)) {
         CRequiredFile var3 = new CRequiredFile();
         var3.setPath(var1);
         var3.setMD5("");
         var3.setModifiedTime(0L);
         var3.setType(var2);
         var3.setZippedBase64Contents(null);
         this.m_requiredFiles.add(var3);
      }
   }

   private void addRequiredFile(String var1, int var2) throws DeployException {
      if (!this.hasFileBeenAdded(var1)) {
         try {
            FileStoreFile var3 = this.m_fileStore.getFile(var1);
            if (var3 == null) {
               throw new DeployException("Failed to get the file \"" + var1 + "\" from the file store. This is probably a bug.");
            }

            CRequiredFile var4 = new CRequiredFile();
            var4.setMD5(var3.getMD5());
            var4.setModifiedTime(var3.getPhysicalRepresentation().lastModified());
            var4.setPath(var1);
            var4.setType(var2);
            var4.setZippedBase64Contents(null);
            this.m_requiredFiles.add(var4);
            List var5 = this.m_fileStore.getRequiredFiles(var1);
            if (var5.size() > 0) {
               List var6 = this.m_fileDependencies.get(var1);
               if (var6 == null) {
                  var6 = Lists.newArrayList();
                  this.m_fileDependencies.put(var1, var6);
               }

               for (Object __etlItem650 : var5) {
                  FileStoreFile var8 = (FileStoreFile) __etlItem650;
                  String var9 = var8.getPath();
                  if (!var6.contains(var9)) {
                     var6.add(var9);
                     this.addRequiredFile(var9, var2);
                  }
               }
            }
         } catch (FileNotFoundException var10) {
            throw new DeployException(var10);
         } catch (FileStoreException var11) {
            throw new DeployException(var11);
         } catch (DeployException var12) {
            throw new DeployException(var12);
         } catch (IOException var13) {
            throw new DeployException(var13);
         }
      }
   }

   private void addPathForXmlStructure(XmlStructure var1) throws DeployException {
      String var2 = var1.getFilePath();
      int var3 = StringUtils.endsWithIgnoreCase(var2, ".dtd") ? 1 : 0;
      this.addRequiredFile(var2, var3);
   }

   private void addPathForJsonStructure(JsonStructure var1) throws DeployException {
      String var2 = var1.getFileName();
      this.addRequiredFile(var2, 0);
   }

   private void serverEncodeColumn(int var1) {
      if (this.m_updatedRows != null && !this.m_updatedRows.isEmpty()) {
         for (InterchangeDataRow var3 : this.m_updatedRows) {
            String[] var4 = var3.getFields();
            String var5 = var4[var1];
            if (var5 != null && !var5.isEmpty()) {
               var4[var1] = ClientServerCommunicationUtils.encodeString(var5);
               var3.setFields(var4);
            }
         }
      }
   }

   private void updateConnectorCall(ConnectorCall var1) throws DeployException {
      CallStructureSchema var2 = var1.getInputStructure().getSchema();
      CallStructureSchema var3 = var1.getOutputStructure().getSchema();
      if (var1.hasRequestTransformation()) {
         this.addRequiredFile(var2.getSchemaName(), 0);
      }

      this.addRequiredFile(var3.getSchemaName(), 0);
   }

   private void updateTransformation(Transformation var1) throws DeployException {
      DeployInterchangeModelWsImplWrapper.DeployDataStructureVisitor var2 = new DeployInterchangeModelWsImplWrapper.DeployDataStructureVisitor(var1, true);
      DataStructure var3 = var1.getInputStructure();
      if (var3 != null) {
         var2.handle(var3);
         var2.throwOnError();
      }

      DataStructure var4 = var1.getOutputStructure();
      if (var4 != null) {
         var2.setIsTarget();
         var2.handle(var4);
         var2.throwOnError();
      }

      for (String[] var7 : var1.getMapListValues()) {
         String var8 = var7[2];
         var7[2] = ClientServerCommunicationUtils.encodeString(var8);
         this.m_updatedTranDef.add(getInterchangeDataRow(var7));
      }

      this.addJtrFiles(var1, var3, var4);
   }

   private void updateLdapStructureEntries(Transformation var1, LdapStructure var2, boolean var3) {
      LdapStructureEntry[] var4 = var2.getClassEntries();
      if (var4 != null && var4.length > 0) {
         String var5 = var1.getID().toString();

         for (LdapStructureEntry var9 : var4) {
            if (var9 != null) {
               String[] var10 = new String[]{
                  var5, var3 ? "0" : "1", var9.getOid().toString(), var9.getAttributesAsString(), var9.getAuxiliaryClassesAsString()
               };
               this.m_updatedLdapStructureEntry.add(new InterchangeDataRow(var10));
            }
         }
      }
   }

   private void updateLdapSourceSearchParameters(Transformation var1, LdapStructure var2) {
      NumericOid[] var3 = var2.getStructuralObjectClassOids();
      if (var3 != null && var3.length != 0) {
         String var4 = var1.getID().toString();

         for (NumericOid var8 : var3) {
            LdapSearchParameters var9 = var2.getSearchParameters(var8);
            if (var9 != null) {
               String var10 = var9.getClassName();
               if (var10 != null && !var10.isEmpty()) {
                  String var11 = var9.getBaseDN();
                  if (var11 != null) {
                     String var12 = var9.getFilter();
                     if (var12 == null) {
                        var12 = "";
                     }

                     int var13 = var9.getScope().getApiCode();
                     String[] var14 = new String[]{var4, var10, var11, var12, Integer.toString(var13), var8.toString()};
                     this.m_updatedLdapSearchParameters.add(new InterchangeDataRow(var14));
                  }
               }
            }
         }
      }
   }

   private void addJtrFiles(Transformation var1, DataStructure var2, DataStructure var3) throws DeployException {
      try {
         boolean var4 = shouldJtrBeSentToServer(var2);
         boolean var5 = shouldJtrBeSentToServer(var3);
         if (var4 || var5) {
            CJtrFile var6 = new CJtrFile();
            var6.setTransformationGuid(var1.getID().toString());
            var6.setSourceJtrZippedBase64(null);
            var6.setTargetJtrZippedBase64(null);
            if (var4) {
               String var7 = this.m_deployData.getInputMappingStructure(var1.getID());
               var6.setSourceJtrZippedBase64(var7);
            }

            if (var5) {
               String var9 = this.m_deployData.getOutputMappingStructure(var1.getID());
               var6.setTargetJtrZippedBase64(var9);
            }

            this.m_transformationJtrs.add(var6);
         }
      } catch (DataCacheException var8) {
         throw new DeployException("Failed to get the jtr files for the \"" + var1.getName() + "\" transformation. Reason: " + var8.getMessage(), var8);
      }
   }

   private static boolean shouldJtrBeSentToServer(DataStructure var0) {
      return true;
   }

   private void addTranRelations(DatabaseStructure var1, String var2) {
      TableRelationshipDeployFieldBuilder var3 = new TableRelationshipDeployFieldBuilder();

      for (String[] var5 : var3.build(var1, var2)) {
         this.m_updatedDbTranRelations.add(getInterchangeDataRow(var5));
      }
   }

   private void addDbTables(DatabaseStructure var1, String var2, boolean var3) {
      DbTables var4 = var1.getDbTables();
      String[] var5 = var4.getColumnValues(var2, var3);
      this.m_updatedDbTables.add(getInterchangeDataRow(var5));
   }

   private void addDbFields(DatabaseStructure var1, String var2) {
      DbFields var3 = var1.getDbFields();
      DbFields.Entry[] var4 = var3.getAllEntries();

      for (DbFields.Entry var8 : var4) {
         String[] var9 = var8.getColumnValues(var2);
         this.m_updatedDbFields.add(getInterchangeDataRow(var9));
      }
   }

   private void addSqlScript(DatabaseStructure var1, String var2) {
      SqlScript var3 = var1.getSqlScript();
      SqlScript.Entry[] var4 = var3.getAllEntries();

      for (SqlScript.Entry var8 : var4) {
         String[] var9 = var8.getColumnValues(var2);
         this.m_updatedSqlScript.add(getInterchangeDataRow(var9));
      }
   }

   private void addTranFilters(DatabaseStructure var1, String var2) {
      DbTranFilters var3 = var1.getHierarchicalFilters();

      for (DbTranFilters.Entry var6 : var3.getAllEntries()) {
         String[] var7 = var6.getColumnValues(var2, var3);
         this.m_updatedTranFilters.add(getInterchangeDataRow(var7));
      }
   }

   private void deleteItems(Collection<IntegrationId> var1) {
      for (IntegrationId var3 : var1) {
         this.m_deletedIds.add(var3.toString());
      }
   }

   private void initNewEntityType() {
      this.m_serverColumnNames = null;
      this.m_updatedRows = Lists.newArrayList();
      this.m_deletedIds = Lists.newArrayList();
   }

   private static InterchangeDataRow getInterchangeDataRow(String[] var0) {
      InterchangeDataRow var1 = new InterchangeDataRow();
      var1.setFields(var0);
      return var1;
   }

   private InterchangeDataTable getInterchangeDataTable(List<InterchangeDataRow> var1) {
      if (this.m_serverColumnNames == null) {
         throw new IllegalStateException("The column names have not been set (m_serverColumnNames). This is a bug.");
      }

      InterchangeDataTable var2 = new InterchangeDataTable();
      var2.setColumnNames(this.m_serverColumnNames);
      var2.setRows(var1.toArray(new InterchangeDataRow[var1.size()]));
      return var2;
   }

   public void uploadJitterpakBackup(JitterPackServerBackup var1, UploadJitterPackCallback var2) {
      DeployInterchangeData var3 = null;

      try {
         var3 = this.getDeployer(var2);
         if (var3 == null) {
            return;
         }
      } catch (IntegrationServerException var8) {
         var2.caught(var8);
         return;
      }

      try {
         CUploadedJitterpak var4 = new CUploadedJitterpak();
         var4.setProjectGuid(var1.getProjectId().toString());
         String var5 = var1.getBase64EncodedData();
         var4.setBase64EncodedJitterpak(var5);
         var4.setTag(var1.getTag());
         var3.uploadJitterpak(this.wsConfiguration.getUserName(), this.wsConfiguration.getPasswordHash(), var4);
         var2.uploadComplete();
      } catch (RemoteException var6) {
         var2.caught(this.wsConfiguration.convertToIntegrationServerException(var6));
      } catch (IOException var7) {
         var2.caught(var7);
      }
   }

   public void deleteIntegrationProjects(List<IntegrationProjectId> var1, DeleteProjectsCallback var2) {
      if (var1.size() == 0) {
         throw new IllegalArgumentException("projectIds cannot be empty!");
      }

      DeployInterchangeData var3 = null;

      try {
         var3 = this.getDeployer(var2);
         if (var3 == null) {
            return;
         }
      } catch (IntegrationServerException var12) {
         var2.caught(var12);
         return;
      }

      String[] var4 = new String[var1.size()];

      for (int var5 = 0; var5 < var1.size(); var5++) {
         var4[var5] = ((IntegrationProjectId)var1.get(var5)).toString();
      }

      try {
         CDeployResults var14 = var3.deleteInterchangeProjects(this.wsConfiguration.getUserName(), this.wsConfiguration.getPasswordHash(), var4);
         CDeployWarning[] var6 = var14.getWarnings();
         ArrayList var7 = new ArrayList();
         DeleteIntegrationProjectResultFactory var8 = new DeleteIntegrationProjectResultFactory();

         for (IntegrationProjectId var10 : var1) {
            var7.add(var8.create(var10, var6));
         }

         var2.projectsDeleted(var7, this.serverInfo);
      } catch (RemoteException var11) {
         var2.caught(this.wsConfiguration.convertToIntegrationServerException(var11));
      }
   }

   public void deployInterchangeModel(DeployData var1, DeployCallback var2) {
      DeployInterchangeData var3 = null;

      try {
         var3 = this.getDeployer(var2);
         if (var3 == null) {
            return;
         }
      } catch (IntegrationServerException var7) {
         var2.caught(var7);
         return;
      }

      ServerInfo var4 = this.connectionService.getCurrentServerInfo();
      String var5 = this.wsConfiguration.getUserName();
      if (Thread.currentThread().isInterrupted()) {
         var2.cancelled();
      } else {
         this.m_deployData = var1;
         this.m_permissionsManager = var1.getPermissions();
         this.m_fileStore = var1.getFileStore();
         this.m_pipelinePluginStore = var1.getPluginFilters();
         DeployInterchangeModelWsImplWrapper.Executor var6 = new DeployInterchangeModelWsImplWrapper.Executor(var3, var1, var2, var4, var5);
         var6.run();
      }
   }

   private IntegrationEntityId getIntegrationEntityId(CIntegrationEntity var1) {
      return EntityType.fromId(var1.getEntityTypeId()).getIntegrationId(var1.getId());
   }

   private CPermission[] getPermissions(List<CPermission> var1) {
      int var2 = var1.size();
      CPermission[] var3 = new CPermission[var2];
      int var4 = 0;

      for (CPermission var6 : var1) {
         var3[var4] = var6;
         var4++;
      }

      return var3;
   }

   private final class DeployDataStructureVisitor {
      private boolean isSource;
      private final Transformation tran;
      private String error;

      private DeployDataStructureVisitor(Transformation var2, boolean var3) {
         this.isSource = var3;
         this.tran = var2;
      }

      private void setIsTarget() {
         this.isSource = false;
         this.error = null;
      }

      public void handle(DataStructure var1) {
         if (var1 instanceof DatabaseStructure) {
            this.visit((DatabaseStructure)var1);
         } else if (var1 instanceof LdapStructure) {
            this.visit((LdapStructure)var1);
         } else if (var1 instanceof WebServiceStructure) {
            this.visit((WebServiceStructure)var1);
         } else if (var1 instanceof XmlStructure) {
            this.visit((XmlStructure)var1);
         } else if (var1 instanceof JsonStructure) {
            this.visit((JsonStructure)var1);
         } else if (var1 instanceof XsdBasedStructure) {
            this.visit((XsdBasedStructure)var1);
         }
      }

      private void visit(DatabaseStructure var1) {
         String var2 = this.tran.getID().toString();
         DeployInterchangeModelWsImplWrapper.this.addDbFields(var1, var2);
         DeployInterchangeModelWsImplWrapper.this.addDbTables(var1, var2, this.isSource);
         DeployInterchangeModelWsImplWrapper.this.addTranRelations(var1, var2);
         if (this.isSource) {
            DeployInterchangeModelWsImplWrapper.this.addTranFilters(var1, var2);
            DeployInterchangeModelWsImplWrapper.this.addSqlScript(var1, var2);
         }
      }

      private void visit(LdapStructure var1) {
         if (this.isSource) {
            DeployInterchangeModelWsImplWrapper.this.updateLdapSourceSearchParameters(this.tran, var1);
         }

         DeployInterchangeModelWsImplWrapper.this.updateLdapStructureEntries(this.tran, var1, this.isSource);
      }

      private void visit(WebServiceStructure var1) {
         if (this.isSource) {
            String[] var2 = TransformationChainLink.getPropertyValues(null, var1.getFunctionCallId(), this.tran.getFunctionID());
            DeployInterchangeModelWsImplWrapper.this.m_updatedTranChainLinks.add(DeployInterchangeModelWsImplWrapper.getInterchangeDataRow(var2));
         } else {
            String[] var3 = TransformationChainLink.getPropertyValues(null, this.tran.getFunctionID(), var1.getFunctionCallId());
            DeployInterchangeModelWsImplWrapper.this.m_updatedTranChainLinks.add(DeployInterchangeModelWsImplWrapper.getInterchangeDataRow(var3));
         }
      }

      private void visit(XsdBasedStructure var1) {
         String[] var2 = this.isSource
            ? TransformationChainLink.getPropertyValues(null, var1.getFunctionCallId(), this.tran.getFunctionID())
            : TransformationChainLink.getPropertyValues(null, this.tran.getFunctionID(), var1.getFunctionCallId());
         DeployInterchangeModelWsImplWrapper.this.m_updatedTranChainLinks.add(DeployInterchangeModelWsImplWrapper.getInterchangeDataRow(var2));
      }

      private void visit(XmlStructure var1) {
         try {
            DeployInterchangeModelWsImplWrapper.this.addPathForXmlStructure(var1);
         } catch (DeployException var3) {
            this.error = "Failed to get the XML schema/dtd file for XML structure " + var1.getName() + " from the file store. " + var3.getMessage();
         }
      }

      private void visit(JsonStructure var1) {
         try {
            DeployInterchangeModelWsImplWrapper.this.addPathForJsonStructure(var1);
         } catch (DeployException var3) {
            this.error = "Failed to get the JSON structure file " + var1.getFileName() + " from the file store. " + var3.getMessage();
         }
      }

      private void throwOnError() throws DeployException {
         if (this.error != null && !this.error.isEmpty()) {
            throw new DeployException(this.error);
         }
      }
   }

   private final class Executor implements Runnable {
      private final DeployInterchangeData deployer;
      private final DeployData deployData;
      private final IntegrationProject project;
      private final DeployCallback callback;
      private final ServerInfo serverInfo;
      private final String user;

      public Executor(DeployInterchangeData var2, DeployData var3, DeployCallback var4, ServerInfo var5, String var6) {
         this.deployer = var2;
         this.deployData = var3;
         this.project = var3.getProject();
         this.callback = var4;
         this.serverInfo = var5;
         this.user = var6;
      }

      @Override
      public void run() {
         try {
            this.checkLocalFiles();
         } catch (DeployException var18) {
            this.callback.caught(var18);
            return;
         }

         InterchangeDataHolder var1 = new InterchangeDataHolder();
         InterchangeIdHolder var2 = new InterchangeIdHolder();

         try {
            this.populateMainEntities(var1, var2);
         } catch (DeployException var17) {
            this.callback.caught(var17);
            return;
         }

         if (Thread.currentThread().isInterrupted()) {
            this.callback.cancelled();
         } else {
            this.populateAuxiliaryProperties(var1, var2);
            if (Thread.currentThread().isInterrupted()) {
               this.callback.cancelled();
            } else {
               DeployResultFactory var3 = new DeployResultFactory(this.project, this.serverInfo, this.user);

               try {
                  String var4 = DeployInterchangeModelWsImplWrapper.this.wsConfiguration.getUserName();
                  String var5 = DeployInterchangeModelWsImplWrapper.this.wsConfiguration.getPasswordHash();
                  CRequiredFiles var6 = this.getRequiredFilesWsInput();
                  CFileDependencies var7 = this.getFileDependencies();
                  if (var6 != null) {
                     CFileCheckResult var8 = this.deployer.checkRequiredFiles(var4, var5, var7, var6);
                     if (Thread.currentThread().isInterrupted()) {
                        this.callback.cancelled();
                        return;
                     }

                     if (var8.getFileCheckStatus() != 0) {
                        this.AddRequiredFilesWithContent(var8.getAvailableRequiredFiles());
                        var8 = this.deployer.checkRequiredFiles(var4, var5, var7, var6);
                        if (Thread.currentThread().isInterrupted()) {
                           this.callback.cancelled();
                           return;
                        }

                        if (var8.getFileCheckStatus() != 0) {
                           this.callback
                              .caught(
                                 new DeployException(
                                    "Failed to upload all required files. The server says that some files are missing but we don't have them to send. This is most likely a bug, please report it"
                                 )
                              );
                           return;
                        }
                     }
                  }

                  var1.setPipelinePluginEntityAssociations(
                     EntityPipelinePlugin.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_entityPipelinePlugins)
                  );
                  DeployLog.LOG.info("Now sending the project data to the server");
                  CDeployResults var22 = this.deployer.deployInterchangeData(var4, var5, var2, var1);
                  DeployLog.LOG.info("The server has acknowledged that it receieved the data");
                  if (Thread.currentThread().isInterrupted()) {
                     this.callback.cancelled();
                     return;
                  }

                  CDeployResults var9 = null;
                  if (DeployInterchangeModelWsImplWrapper.this.m_transformationJtrs.size() > 0) {
                     CJtrFiles var10 = new CJtrFiles();
                     var10.setJtrFiles(
                        DeployInterchangeModelWsImplWrapper.this.m_transformationJtrs
                           .toArray(new CJtrFile[DeployInterchangeModelWsImplWrapper.this.m_transformationJtrs.size()])
                     );
                     DeployLog.LOG.info("Now sending JTR files to the server");
                     var9 = this.deployer.uploadJtrFiles(var4, var5, var10);
                     DeployLog.LOG.info("The server has acknowledged that it receieved the JTR files");
                     if (Thread.currentThread().isInterrupted()) {
                        this.callback.cancelled();
                        return;
                     }
                  }

                  CDeployWarning[] var23 = var22.getWarnings();
                  LinkedHashSet var11 = new LinkedHashSet();
                  ArrayList var12 = Lists.newArrayList();
                  if (var23 != null) {
                     for (CDeployWarning var16 : var23) {
                        this.fillDeployWarning(var16, var11, var12);
                     }
                  }

                  if (var9 != null) {
                     var23 = var9.getWarnings();
                     if (var23 != null) {
                        for (CDeployWarning var28 : var23) {
                           this.fillDeployWarning(var28, var11, var12);
                        }
                     }
                  }

                  this.callback.deployComplete(this.deployData, var3.success(var12, Lists.newArrayList(var11)));
               } catch (DeployException var19) {
                  this.callback.deployComplete(this.deployData, var3.failure(var19));
               } catch (RemoteException var20) {
                  this.callback.deployComplete(this.deployData, var3.failure(var20));
               }
            }
         }
      }

      private void checkLocalFiles() throws DeployException {
         if (DeployInterchangeModelWsImplWrapper.this.localHostDetector.isPresent()) {
            if (!DeployInterchangeModelWsImplWrapper.this.localHostDetector.get().apply(this.serverInfo)) {
               IntegrationProject var1 = this.deployData.getProject();
               if (this.anyLocalFile(var1.getAllEntitiesOfClass(Source.class)) || this.anyLocalFile(var1.getAllEntitiesOfClass(Target.class))) {
                  throw new DeployException("Local File Sources and Targets can only be deployed to a server running on the local machine.");
               }
            }
         }
      }

      private boolean anyLocalFile(Iterable<? extends Locatable> var1) {
         for (Locatable var3 : var1) {
            if (var3.getLocation().getDataLocationType() == DataLocationType.LocalFile) {
               return true;
            }
         }

         return false;
      }

      private CFileDependencies getFileDependencies() throws DeployException {
         if (DeployInterchangeModelWsImplWrapper.this.m_fileDependencies.size() < 1) {
            return null;
         }

         ArrayList var1 = Lists.newArrayList();
         ArrayList var2 = Lists.newArrayList();

         for (String var4 : DeployInterchangeModelWsImplWrapper.this.m_fileDependencies.keySet()) {
            for (String var7 : DeployInterchangeModelWsImplWrapper.this.m_fileDependencies.get(var4)) {
               if (var4.length() > 0 && var7.length() > 0) {
                  var1.add(var4);
                  var2.add(var7);
               }
            }
         }

         int var8 = var1.size();
         if (var8 != var2.size()) {
            throw new DeployException("Internal Client Error: nDeps != childPaths.size(). Please report this bug.");
         }

         CFileDependencies var9 = new CFileDependencies();
         var9.setParentFilePath((String[]) var1.toArray(new String[var8]));
         var9.setChildFilePath((String[]) var2.toArray(new String[var8]));
         return var9;
      }

      private CRequiredFile FindRequiredFile(String var1, CRequiredFile[] var2) {
         if (var2 == null) {
            return null;
         }

         for (CRequiredFile var6 : var2) {
            if (var6.getPath().equals(var1)) {
               return var6;
            }
         }

         return null;
      }

      private void AddRequiredFilesWithContent(CRequiredFile[] var1) throws DeployException {
         for (CRequiredFile var3 : DeployInterchangeModelWsImplWrapper.this.m_requiredFiles) {
            CRequiredFile var4 = this.FindRequiredFile(var3.getPath(), var1);
            if (var4 == null || !var4.getMD5().equalsIgnoreCase(var3.getMD5())) {
               String var5 = var3.getPath();
               if (!DeployInterchangeModelWsImplWrapper.isUrl(var5)) {
                  try {
                     FileStoreFile var6 = DeployInterchangeModelWsImplWrapper.this.m_fileStore.getFile(var5);
                     File var7 = var6.getPhysicalRepresentation();
                     String var8 = ZipUtils.deflateAndBase64EncodeFileContents(var7.getAbsolutePath());
                     var3.setZippedBase64Contents(var8);
                     var3.setModifiedTime(var7.lastModified());
                  } catch (IOException var9) {
                     throw new DeployException(var9);
                  }
               }
            }
         }
      }

      private CRequiredFiles getRequiredFilesWsInput() {
         if (DeployInterchangeModelWsImplWrapper.this.m_requiredFiles.size() < 1) {
            return null;
         }

         CRequiredFiles var1 = new CRequiredFiles();
         var1.setRequiredFiles(
            DeployInterchangeModelWsImplWrapper.this.m_requiredFiles
               .toArray(new CRequiredFile[DeployInterchangeModelWsImplWrapper.this.m_requiredFiles.size()])
         );
         return var1;
      }

      private void fillDeployWarning(CDeployWarning var1, Set<EntityWarning> var2, List<String> var3) {
         CIntegrationEntity var4 = var1.getEntity();
         if (this.isGlobalWarning(var4)) {
            var3.add(var1.getWarning());
         } else {
            var2.add(new EntityWarningImpl(DeployInterchangeModelWsImplWrapper.this.getIntegrationEntityId(var4), var1.getWarning()));
         }
      }

      private boolean isGlobalWarning(CIntegrationEntity var1) {
         return var1.getEntityTypeId() < 1;
      }

      private void populateMainEntities(InterchangeDataHolder var1, InterchangeIdHolder var2) throws DeployException {
         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.Document));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.Document));
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setDocumentsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setDocumentsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedDocumentDefinitions.isEmpty()) {
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = TextDocument.getDocumentDefinitionsColumnNames();
            var1.setDocument_DefinitionsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedDocumentDefinitions)
            );
            if (!DeployInterchangeModelWsImplWrapper.this.m_updatedDocumentFieldValidations.isEmpty()) {
               DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = TextDocument.getDocumentFieldValidationColumnNames();
               var1.setDocumentFieldValidationTab(
                  DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedDocumentFieldValidations)
               );
            }
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.Source));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.Source));
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setSourcesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setSourcesTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.Target));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.Target));
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setTargetsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setTargetsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedJmsLocationProperties.size() > 0) {
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.JmsMessage));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.JmsMessage));
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setJmsMessagesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setJmsMessagesTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedDataElements.size() > 0) {
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = DeployInterchangeModelWsImplWrapper.this.getDataElementsKeyNames();
            var1.setDataElementsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedDataElements)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.Schedule));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.Schedule));
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setSchedulesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setSchedulesTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.EmailMessage));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.EmailMessage));
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setMessagesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setMessagesTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.WebServiceCall));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.WebServiceCall));
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setWebServicesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setWebServicesTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = CallRestrictions.getPropertyKeys();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedCallRestrictions.size() > 0) {
            var1.setCallRestrictionsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedCallRestrictions)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.Script));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.Script));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.XsltTransform));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.ProjectVariable));
         for (IntegrationId deletedVariable : this.deployData.getDeletedItemsOfType(EntityType.ProjectVariable)) {
            DeployInterchangeModelWsImplWrapper.this.m_extendedEntityProperties.add(getInterchangeDataRow(new String[]{"35", deletedVariable.toString(), "__deleted", "1"}));
         }
         for (IntegrationId deletedXslt : this.deployData.getDeletedItemsOfType(EntityType.XsltTransform)) {
            DeployInterchangeModelWsImplWrapper.this.m_extendedEntityProperties.add(getInterchangeDataRow(new String[]{"34", deletedXslt.toString(), "__deleted", "1"}));
         }
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setScriptsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         DeployInterchangeModelWsImplWrapper.this.serverEncodeColumn(2);
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setScriptsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.Transformation));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.Transformation));
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setTransformationsTab_New_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedRows.size() > 0) {
            var1.setTransformationsTab_New(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = DbFields.Entry.getColumnNames();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedDbFields.size() > 0) {
            var1.setDBFieldsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedDbFields));
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = DbTables.getColumnNames();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedDbTables.size() > 0) {
            var1.setDBTablesTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedDbTables));
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = SqlScript.Entry.getColumnNames();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedSqlScript.size() > 0) {
            var1.setSqlScriptTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedSqlScript));
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = DbTranFilters.Entry.getColumnNames();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedTranFilters.size() > 0) {
            var1.setTrans_FilterTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedTranFilters)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = DbTranRelations.Relation.getColumnNames();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedDbTranRelations.size() > 0) {
            var1.setTran_RelationsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedDbTranRelations)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = Transformation.getMapListColumnNames();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedTranDef.size() > 0) {
            var1.setTransformation_DefinitionsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedTranDef)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = LdapSearchParameters.getLdapSearchParametersColumnNames();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedLdapSearchParameters.size() > 0) {
            var1.setLdapFiltersTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedLdapSearchParameters)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = LdapStructureEntry.getLdapStructureEntriesColumnNames();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedLdapStructureEntry.size() > 0) {
            var1.setLdapStructureEntriesTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedLdapStructureEntry)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.Operation));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.Operation));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setOperationsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setOperationsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = DeployInterchangeModelWsImplWrapper.this.getOperationPipelineKeys();
         if (!DeployInterchangeModelWsImplWrapper.this.m_operationPipelines.isEmpty()) {
            var1.setOperationPipelines(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_operationPipelines)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = DeployInterchangeModelWsImplWrapper.this.getOperationGraphLayoutKeys();
         if (!DeployInterchangeModelWsImplWrapper.this.m_operationGraphLayout.isEmpty()) {
            var1.setOperationGraphLayoutTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_operationGraphLayout)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = TransformationChainLink.getPropertyKeys();
         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedTranChainLinks.isEmpty()) {
            var1.setTransformationChainLinksTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedTranChainLinks)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.HttpEndpoint));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.HttpEndpoint));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setHttpEndpointsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setHttpEndpointsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = TriggeredOperationsProperties.getPropertyKeys();
         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedHttpEndpointOperations.isEmpty()) {
            var1.setHttpEndpointOperationsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedHttpEndpointOperations)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SalesforceConnector));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SalesforceConnector));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSalesforceConnectorsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSalesforceConnectorsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SalesforceQuery));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SalesforceQuery));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSalesforceQueriesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSalesforceQueriesTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SalesforceUpsert));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SalesforceUpsert));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSalesforceUpsertsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSalesforceUpsertsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SalesforceCreate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SalesforceCreate));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSalesforceCreatesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSalesforceCreatesTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SalesforceUpdate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SalesforceUpdate));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSalesforceUpdatesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSalesforceUpdatesTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SalesforceDelete));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SalesforceDelete));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSalesforceDeletesTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSalesforceDeletesTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SalesforceBulkLoad));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SalesforceBulkLoad));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSalesforceBulkLoadsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSalesforceBulkLoadsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SapEndpoint));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SapEndpoint));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSapEndpointsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSapEndpointsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.SapFunction));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.SapFunction));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setSapFunctionsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setSapFunctionsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSAXEndpoint));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSAXEndpoint));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setMSAXEndpointsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setMSAXEndpointsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSAXFunction));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSAXFunction));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setMSAXFunctionsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setMSAXFunctionsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.EBSEndpoint));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.EBSEndpoint));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setEbsEndpointsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setEbsEndpointsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.EBSFunction));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.EBSFunction));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setEbsFunctionsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setEbsFunctionsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSCrmEndpoint));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSCrmEndpoint));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setMSCrmEndpointsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setMSCrmEndpointsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSCrmDelete));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSCrmDelete));
         String[] var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSCrmCreate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSCrmCreate));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSCrmUpdate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSCrmUpdate));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSCrmUpsert));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSCrmUpsert));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSCrmQuery));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSCrmQuery));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames == null) {
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = var3;
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setMSCrmFunctionsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setMSCrmFunctionsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSGPEndpoint));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSGPEndpoint));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setMSGPEndpointsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setMSGPEndpointsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSGPDelete));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSGPDelete));
         var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSGPCreate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSGPCreate));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.MSGPUpdate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.MSGPUpdate));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames == null) {
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = var3;
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setMSGPFunctionsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setMSGPFunctionsTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows));
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.AutoDeskEndpoint));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.AutoDeskEndpoint));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setAutoDeskEndpointsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setAutoDeskEndpointsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.AutoDeskDelete));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.AutoDeskDelete));
         var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.AutoDeskCreate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.AutoDeskCreate));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.AutoDeskUpdate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.AutoDeskUpdate));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.AutoDeskUpsert));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.AutoDeskUpsert));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.AutoDeskGet));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.AutoDeskGet));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames == null) {
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = var3;
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setAutoDeskFunctionsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setAutoDeskFunctionsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.NetSuiteEndpoint));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.NetSuiteEndpoint));
         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setNetSuiteEndpointsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setNetSuiteEndpointsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.NetSuiteDelete));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.NetSuiteDelete));
         var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.NetSuiteCreate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.NetSuiteCreate));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.NetSuiteUpdate));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.NetSuiteUpdate));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.NetSuiteUpsert));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.NetSuiteUpsert));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = null;
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.NetSuiteQuery));
         DeployInterchangeModelWsImplWrapper.this.updateFolder(this.project.getRootFolder(EntityType.NetSuiteQuery));
         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames != null) {
            var3 = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames;
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames == null) {
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = var3;
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_deletedIds.isEmpty()) {
            var2.setNetSuiteFunctionsTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (!DeployInterchangeModelWsImplWrapper.this.m_updatedRows.isEmpty()) {
            var1.setNetSuiteFunctionsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
            );
         }
      }

      private String[] toArray(List<String> var1) {
         return var1.toArray(new String[var1.size()]);
      }

      private void populateAuxiliaryProperties(InterchangeDataHolder var1, InterchangeIdHolder var2) {
         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = this.project.getPropertyKeys();
         DeployInterchangeModelWsImplWrapper.this.m_updatedRows
            .add(DeployInterchangeModelWsImplWrapper.getInterchangeDataRow(this.project.getPropertyValuesForDeploy()));
         var1.setInterchangeProjectsTab(
            DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedRows)
         );
         if (DeployInterchangeModelWsImplWrapper.this.m_extendedEntityProperties.size() > 0) {
            DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = ServerPropertyKey.getPropertyKeys();
            var1.setEntityProperties(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_extendedEntityProperties)
            );
         }

         DeployInterchangeModelWsImplWrapper.this.updatePermissions(this.project, false);
         if (!DeployInterchangeModelWsImplWrapper.this.m_commonSourceOperations.isEmpty()) {
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = new String[2];
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames[0] = "operation_id";
            DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames[1] = "siblingoperation_id";
            var1.setCommonSourceOperationsTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_commonSourceOperations)
            );
            if (Thread.currentThread().isInterrupted()) {
               this.callback.cancelled();
               return;
            }
         }

         DeployInterchangeModelWsImplWrapper.this.initNewEntityType();
         DeployInterchangeModelWsImplWrapper.this.deleteItems(this.deployData.getDeletedItemsOfType(EntityType.Folder));
         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = DeployInterchangeModelWsImplWrapper.this.m_serverColumnNamesFolders;
         if (DeployInterchangeModelWsImplWrapper.this.m_deletedIds.size() > 0) {
            var2.setFoldersTab_ids(this.toArray(DeployInterchangeModelWsImplWrapper.this.m_deletedIds));
         }

         if (DeployInterchangeModelWsImplWrapper.this.m_updatedFolders.size() > 0) {
            var1.setFoldersTab(DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedFolders));
         }

         DeployInterchangeModelWsImplWrapper.this.m_serverColumnNames = FolderItem.getPropertyKeysStatic();
         if (DeployInterchangeModelWsImplWrapper.this.m_updatedFolderItems.size() > 0) {
            var1.setFolderItemTab(
               DeployInterchangeModelWsImplWrapper.this.getInterchangeDataTable(DeployInterchangeModelWsImplWrapper.this.m_updatedFolderItems)
            );
         }

         if (Thread.currentThread().isInterrupted()) {
            this.callback.cancelled();
         } else {
            int var3 = DeployInterchangeModelWsImplWrapper.this.m_userPermissions.size();
            CUserPermissions[] var4 = new CUserPermissions[var3];
            int var5 = 0;

            for (Entry var7 : DeployInterchangeModelWsImplWrapper.this.m_userPermissions.entrySet()) {
               RoleId var8 = (RoleId)var7.getKey();
               var4[var5++] = new CUserPermissions(var8.toString(), DeployInterchangeModelWsImplWrapper.this.getPermissions((List<CPermission>)var7.getValue()));
            }

            PermissionsHolder var9 = new PermissionsHolder();
            var9.setUserPermissions(var4);
            var1.setPermissions(var9);
         }
      }
   }
}
