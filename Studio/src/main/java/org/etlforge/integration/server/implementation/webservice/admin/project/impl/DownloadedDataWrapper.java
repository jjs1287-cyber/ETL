package org.etlforge.integration.server.implementation.webservice.admin.project.impl;

import com.google.common.base.Objects;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.etlforge.integration.data.download.entity.EntityFactory;
import com.etlforge.integration.data.download.structure.DatabaseStructureFactory;
import com.etlforge.integration.data.download.structure.TextStructureFactory;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.entity.settings.FileResource;
import org.etlforge.integration.client.plugin.pipeline.AppliedPipelinePlugin;
import org.etlforge.integration.client.plugin.pipeline.MutableAppliedPipelinePluginStore;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.SystemObjects;
import org.etlforge.integration.client.project.permission.PermissionsManager;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.common.server.ClientServerCommunicationUtils;
import org.etlforge.integration.data.CallRestrictions;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.httpendpoint.OperationRunMode;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperation;
import org.etlforge.integration.data.entity.httpendpoint.TriggeredOperations;
import org.etlforge.integration.data.entity.id.HttpEndpointId;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.TextDocumentId;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.EntityActivity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.RemoveActivityVetoException;
import org.etlforge.integration.data.entity.operation.pipeline.ScriptActivity;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.data.structure.ldap.LdapSearchParameters;
import org.etlforge.integration.data.structure.ldap.LdapSearchScope;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.integration.server.implementation.webservice.WsKeyValue;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ClientServerDeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.DeployKeyMappings;
import org.etlforge.integration.server.implementation.webservice.impl.common.property_keys.ServerPropertyKey;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.DownloadDataHolder;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.DownloadedItemProperties;
import org.etlforge.integration.server.implementation.webservice.interchange.checkout.client.DownloadedItems;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.User;
import org.etlforge.integration.user.UserId;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.version.BuildVersion;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.zip.ZipUtils;

final class DownloadedDataWrapper {
   private final DownloadDataHolder rawData;
   private final Iterable<EntityType> supportedEntityTypes;
   private final EntityFactories entityFactories = new EntityFactories();
   private final Logger logger;
   private final DownloadedDataWrapper.PropertyMap guidPropertiesMap = new DownloadedDataWrapper.PropertyMap();
   private final Map<String, IntegrationEntity> idEntityMap = Maps.newHashMap();
   private Map<String, Collection<StringKeyValuePairs>> documentDefinitions = null;
   private Map<String, Collection<StringKeyValuePairs>> documentFieldValidations = null;
   private Map<String, Collection<StringKeyValuePairs>> dbFields = null;
   private Map<String, Collection<StringKeyValuePairs>> tranRelations = null;
   private Map<String, Collection<StringKeyValuePairs>> sqlScripts = null;
   private Map<String, Collection<StringKeyValuePairs>> tranFilters = null;
   private Map<String, StringKeyValuePairs> callRestrictions = null;
   private Map<String, StringKeyValuePairs> wsdlFiles = null;
   private Map<String, StringKeyValuePairs> operationGraphLayouts = null;
   private Map<String, Collection<StringKeyValuePairs>> dbTables = null;
   private Map<String, Collection<StringKeyValuePairs>> operationPipelines = null;
   private Map<String, Collection<StringKeyValuePairs>> siblingOperations = null;
   private Map<String, Collection<StringKeyValuePairs>> httpEndpointOperations = null;
   private Map<String, Collection<StringKeyValuePairs>> ldapSearchparameters;
   private Map<String, Collection<StringKeyValuePairs>> ldapStructureEntry = null;
   private Map<String, Function> nextFunctionCallMap;
   private Map<String, Function> previousFunctionCallMap;
   private Map<String, List<String>> mapLists = null;
   private Map<String, Folder> folders;
   private Map<DownloadedDataWrapper.EntityMapKey, Folder> parentFolders;
   private final SetMultimap<IntegrationEntityId, FileToDownload> filesToDownload = HashMultimap.create();
   private final Map<String, User> userCacheMap = Maps.newHashMap();
   private final Map<String, Group> groupCacheMap = Maps.newHashMap();
   private Map<String, Collection<StringKeyValuePairs>> pipelinePluginAssociations = null;
   private Map<String, String[]> jtrCache = null;

   private void addEntityToMap(IntegrationEntity var1) {
      this.idEntityMap.put(var1.getID().toString(), var1);
   }

   private Collection<Transformation> getTransformations() {
      ArrayList var1 = Lists.newArrayList();

      for (IntegrationEntity var3 : this.idEntityMap.values()) {
         if (var3.getEntityType() == EntityType.Transformation) {
            Transformation var4 = (Transformation)var3;
            if (!(var4 instanceof BuiltInIntegrationEntity)) {
               var1.add(var4);
            }
         }
      }

      return var1;
   }

   private Collection<Operation> getOperations() {
      ArrayList var1 = Lists.newArrayList();

      for (IntegrationEntity var3 : this.idEntityMap.values()) {
         if (var3.getEntityType() == EntityType.Operation) {
            Operation var4 = (Operation)var3;
            var1.add(var4);
         }
      }

      return var1;
   }

   DownloadedDataWrapper(DownloadDataHolder var1, Iterable<EntityType> var2, Logger var3) {
      this.rawData = var1;
      this.supportedEntityTypes = var2;
      this.logger = var3;
      this.addEntity(this.rawData.getSources(), EntityType.Source);
      this.addEntity(this.rawData.getTargets(), EntityType.Target);
      this.addEntity(this.rawData.getSchedules(), EntityType.Schedule);
      this.addEntity(this.rawData.getEmailMessages(), EntityType.EmailMessage);
      this.addEntity(this.rawData.getScripts(), EntityType.Script);
      this.addEntity(this.rawData.getTextStructures(), EntityType.Document);
      this.addEntity(this.rawData.getWebServiceMethods(), EntityType.WebServiceCall);
      this.addEntity(this.rawData.getTransformations(), EntityType.Transformation);
      this.addEntity(this.rawData.getOperations(), EntityType.Operation);
      this.addEntity(this.rawData.getFolders(), EntityType.Folder);
      this.addEntity(this.rawData.getHttpEndpoints(), EntityType.HttpEndpoint);
      this.addEntity(this.rawData.getSalesforceConnectors(), EntityType.SalesforceConnector);
      this.addEntity(this.rawData.getSalesforceQueries(), EntityType.SalesforceQuery);
      this.addEntity(this.rawData.getSalesforceUpserts(), EntityType.SalesforceUpsert);
      this.addEntity(this.rawData.getSalesforceCreates(), EntityType.SalesforceCreate);
      this.addEntity(this.rawData.getSalesforceUpdates(), EntityType.SalesforceUpdate);
      this.addEntity(this.rawData.getSalesforceDeletes(), EntityType.SalesforceDelete);
      this.addEntity(this.rawData.getSalesforceBulkLoads(), EntityType.SalesforceBulkLoad);
      this.addEntity(this.rawData.getSapEndpoints(), EntityType.SapEndpoint);
      this.addEntity(this.rawData.getSapFunctions(), EntityType.SapFunction);
      this.addEntity(this.rawData.getMSAXEndpoints(), EntityType.MSAXEndpoint);
      this.addEntity(this.rawData.getMSAXFunctions(), EntityType.MSAXFunction);
      this.addEntity(this.rawData.getEbsEndpoints(), EntityType.EBSEndpoint);
      this.addEntity(this.rawData.getEbsFunctions(), EntityType.EBSFunction);
      this.addEntity(this.rawData.getMSCrmEndpoints(), EntityType.MSCrmEndpoint);
      String var4 = "mscrmoperation";
      this.addClientEntity(var4, this.rawData.getMSCrmFunctions(), EntityType.MSCrmCreate);
      this.addClientEntity(var4, this.rawData.getMSCrmFunctions(), EntityType.MSCrmDelete);
      this.addClientEntity(var4, this.rawData.getMSCrmFunctions(), EntityType.MSCrmUpdate);
      this.addClientEntity(var4, this.rawData.getMSCrmFunctions(), EntityType.MSCrmUpsert);
      this.addClientEntity(var4, this.rawData.getMSCrmFunctions(), EntityType.MSCrmQuery);
      this.addEntity(this.rawData.getMSGPEndpoints(), EntityType.MSGPEndpoint);
      var4 = "msgpoperation";
      this.addClientEntity(var4, this.rawData.getMSGPFunctions(), EntityType.MSGPCreate);
      this.addClientEntity(var4, this.rawData.getMSGPFunctions(), EntityType.MSGPDelete);
      this.addClientEntity(var4, this.rawData.getMSGPFunctions(), EntityType.MSGPUpdate);
      this.addEntity(this.rawData.getAutoDeskEndpoints(), EntityType.AutoDeskEndpoint);
      var4 = "autodeskoperation";
      this.addClientEntity(var4, this.rawData.getAutoDeskFunctions(), EntityType.AutoDeskGet);
      this.addClientEntity(var4, this.rawData.getAutoDeskFunctions(), EntityType.AutoDeskCreate);
      this.addClientEntity(var4, this.rawData.getAutoDeskFunctions(), EntityType.AutoDeskDelete);
      this.addClientEntity(var4, this.rawData.getAutoDeskFunctions(), EntityType.AutoDeskUpdate);
      this.addClientEntity(var4, this.rawData.getAutoDeskFunctions(), EntityType.AutoDeskUpsert);
      this.addEntity(this.rawData.getNetSuiteEndpoints(), EntityType.NetSuiteEndpoint);
      var4 = "netsuiteoperation";
      this.addClientEntity(var4, this.rawData.getNetSuiteFunctions(), EntityType.NetSuiteCreate);
      this.addClientEntity(var4, this.rawData.getNetSuiteFunctions(), EntityType.NetSuiteDelete);
      this.addClientEntity(var4, this.rawData.getNetSuiteFunctions(), EntityType.NetSuiteUpdate);
      this.addClientEntity(var4, this.rawData.getNetSuiteFunctions(), EntityType.NetSuiteUpsert);
      this.addClientEntity(var4, this.rawData.getNetSuiteFunctions(), EntityType.NetSuiteQuery);
      this.addFolders();
      this.addSystemEntities();
   }

   private void addSystemEntities() {
      for (BuiltInIntegrationEntity var4 : SystemObjects.getAll()) {
         this.addEntityToMap((IntegrationEntity)var4);
      }
   }

   IntegrationProject getIntegrationProject() {
      DownloadedItems var1 = this.rawData.getProjects();
      DownloadedItemProperties[] var2 = var1.getItems();
      if (var2.length != 1) {
         throw new IllegalStateException("More than one project has been downloaded! This is not supported in the current version!");
      }

      DownloadedItemProperties var3 = var2[0];
      StringKeyValuePairs var4 = this.getStringValuePairs(var3);
      String var5 = var4.getString("interchangeproject_name");
      String var6 = var4.getString("interchangeproject_id");
      String var7 = var4.getString("description");
      IntegrationProject var8 = IntegrationProject.createExistingProjectShell(var5, new IntegrationProjectId(var6), this.supportedEntityTypes);
      this.logger.fine(String.format("Created empty project shell: Name = %s, ID = %s", var5, var6));
      var8.setDescription(var7);
      this.addEntityToMap(var8);
      return var8;
   }

   Collection<IntegrationEntity> getIntegrationEntities(EntityType var1) throws Exception {
      this.logger.fine("Asked to recreate the downloaded " + var1.getPlural());
      ArrayList var2 = Lists.newArrayList();
      Collection var3 = this.guidPropertiesMap.get(var1);
      if (var3.isEmpty()) {
         return var2;
      }

      EntityFactory var4 = this.entityFactories.forType(var1);
      DownloadedDataWrapper.ConstructionCompleter var5 = new DownloadedDataWrapper.ConstructionCompleter();

      for (Object __etlItem265 : var3) {
         StringKeyValuePairs var7 = (StringKeyValuePairs) __etlItem265;
         IntegrationEntity var8 = var4.createItem(var7);
         this.logger.finer(String.format("Recreating the %s \"%s\" (ID = %s).", var8.getEntityType(), var8.getName(), var8.getID()));
         var5.complete(var8);
         var5.throwOnError();
         var2.add(var8);
         this.addEntityToMap(var8);
         this.logger.finer(String.format("The %s \"%s\" (ID = %s) was created successfully.", var8.getEntityType(), var8.getName(), var8.getID()));
      }

      return var2;
   }

   private Collection<StringKeyValuePairs> getDocumentDefinitions(String var1) {
      if (this.documentDefinitions == null) {
         this.documentDefinitions = Maps.newHashMap();
         this.fillDependentCollectionMap(this.documentDefinitions, this.rawData.getDocumentDefinitions());
      }

      return getNonNullCollection(this.documentDefinitions, var1);
   }

   private Collection<StringKeyValuePairs> getDocumentFieldValidations(String var1) {
      if (this.documentFieldValidations == null) {
         this.documentFieldValidations = Maps.newHashMap();
         this.fillDependentCollectionMap(this.documentFieldValidations, this.rawData.getDocumentFieldValidations());
      }

      return getNonNullCollection(this.documentFieldValidations, var1);
   }

   private Collection<StringKeyValuePairs> getDbFields(String var1, boolean var2) {
      if (this.dbFields == null) {
         this.dbFields = Maps.newHashMap();
         this.fillDependentCollectionMap(this.dbFields, this.rawData.getDbFields());
      }

      boolean var3 = !var2;
      Collection var4 = getNonNullCollection(this.dbFields, var1);
      ArrayList var5 = Lists.newArrayList();

      for (Object __etlItem306 : var4) {
         StringKeyValuePairs var7 = (StringKeyValuePairs) __etlItem306;
         int var8 = var7.getInt("keyfieldstype", -1);
         if (var8 == 2 && var2) {
            var5.add(var7);
         } else if (var8 == 3 && var3) {
            var5.add(var7);
         }
      }

      return var5;
   }

   private Collection<StringKeyValuePairs> getTranRelations(String var1, boolean var2) {
      if (this.tranRelations == null) {
         this.tranRelations = Maps.newHashMap();
         this.fillDependentCollectionMap(this.tranRelations, this.rawData.getTransformationRelations());
      }

      Collection var3 = getNonNullCollection(this.tranRelations, var1);
      ArrayList var4 = Lists.newArrayList();
      boolean var5 = !var2;
      String var6 = String.valueOf('S');

      for (Object __etlItem329 : var3) {
         StringKeyValuePairs var8 = (StringKeyValuePairs) __etlItem329;
         String var9 = var8.getString("doc_type");
         if (var2 && var6.equalsIgnoreCase(var9)) {
            var4.add(var8);
         } else if (var5 && !var6.equalsIgnoreCase(var9)) {
            var4.add(var8);
         }
      }

      return var4;
   }

   private Collection<StringKeyValuePairs> getSqlScripts(String var1, boolean var2) {
      if (this.sqlScripts == null) {
         this.sqlScripts = Maps.newHashMap();
         this.fillDependentCollectionMap(this.sqlScripts, this.rawData.getSqlScripts());
      }

      Collection var3 = getNonNullCollection(this.sqlScripts, var1);
      if (var2) {
         return var3;
      }

      ArrayList var4 = Lists.newArrayList();

      for (Object __etlItem354 : var3) {
         StringKeyValuePairs var6 = (StringKeyValuePairs) __etlItem354;
         int var7 = var6.getInt("sqlscripttype");
         if (var7 != 3) {
            var4.add(var6);
         }
      }

      return var4;
   }

   private Collection<StringKeyValuePairs> getTranFilters(String var1) {
      if (this.tranFilters == null) {
         this.tranFilters = Maps.newHashMap();
         this.fillDependentCollectionMap(this.tranFilters, this.rawData.getTransformationFilters());
      }

      return getNonNullCollection(this.tranFilters, var1);
   }

   private static Collection<StringKeyValuePairs> getNonNullCollection(Map<String, Collection<StringKeyValuePairs>> var0, String var1) {
      Collection var2 = (Collection)var0.get(var1);
      return var2 == null ? Lists.newArrayList() : var2;
   }

   private void fillDependentCollectionMap(Map<String, Collection<StringKeyValuePairs>> var1, DownloadedItems var2) {
      if (var1 != null && var2 != null) {
         DownloadedItemProperties[] var3 = var2.getItems();

         for (DownloadedItemProperties var7 : var3) {
            String var8 = var7.getItemGUID();
            Collection var9 = (Collection)var1.get(var8);
            if (var9 == null) {
               var9 = Lists.newArrayList();
               var1.put(var8, var9);
            }

            var9.add(this.getStringValuePairs(var7));
         }
      }
   }

   private void fillMap(Map<String, StringKeyValuePairs> var1, DownloadedItems var2) {
      DownloadedItemProperties[] var3 = var2.getItems();

      for (DownloadedItemProperties var7 : var3) {
         var1.put(var7.getItemGUID(), this.getStringValuePairs(var7));
      }
   }

   private CallRestrictions getCallRestrictions(String var1) {
      if (this.callRestrictions == null) {
         this.callRestrictions = Maps.newHashMap();
         this.fillMap(this.callRestrictions, this.rawData.getCallRestrictions());
      }

      StringKeyValuePairs var2 = this.callRestrictions.get(var1);
      return var2 != null ? new CallRestrictions(var2) : new CallRestrictions();
   }

   private WsdlFile getWsdlFile(String var1) {
      if (this.wsdlFiles == null) {
         this.wsdlFiles = Maps.newHashMap();
         this.fillMap(this.wsdlFiles, this.rawData.getWsdls());
      }

      StringKeyValuePairs var2 = this.wsdlFiles.get(var1);
      return var2 != null ? new WsdlFile(var2) : new WsdlFile();
   }

   private String getOperationGraphLayout(String var1) {
      if (this.operationGraphLayouts == null) {
         this.operationGraphLayouts = Maps.newHashMap();
         this.fillMap(this.operationGraphLayouts, this.rawData.getOperationGraphLayout());
      }

      StringKeyValuePairs var2 = this.operationGraphLayouts.get(var1);
      if (var2 == null) {
         return null;
      }

      String var3 = var2.getString("layout");
      return var3 != null && !var3.isEmpty() ? var3 : null;
   }

   private StringKeyValuePairs getDbTables(String var1, boolean var2) {
      if (this.dbTables == null) {
         this.dbTables = Maps.newHashMap();
         this.fillDependentCollectionMap(this.dbTables, this.rawData.getDbTables());
      }

      boolean var3 = !var2;
      Collection var4 = this.dbTables.get(var1);
      if (var4 == null) {
         return new StringKeyValuePairs();
      }

      for (Object __etlItem450 : var4) {
         StringKeyValuePairs var6 = (StringKeyValuePairs) __etlItem450;
         int var7 = var6.getInt("tablestype", -1);
         if (var7 == 1 && var2) {
            return var6;
         }

         if (var7 == 2 && var3) {
            return var6;
         }
      }

      return new StringKeyValuePairs();
   }

   private OperationPipeline getOperationPipeline(Operation var1) throws Exception {
      if (this.operationPipelines == null) {
         this.operationPipelines = Maps.newHashMap();
         this.fillDependentCollectionMap(this.operationPipelines, this.rawData.getOperationPipelineActivities());
      }

      OperationType var2 = var1.getOperationType();
      OperationPipeline var3 = var2.createNewTemplate();
      Collection var4 = this.operationPipelines.get(var1.getID().toString());
      if (var4 == null) {
         throw new Exception(
            "Failed to download operation '"
               + var1.getName()
               + "'. The operation has no operation pipeline. This operation was probably deployed with a pre-2.0 version. Downloading such projects is currently not supported."
         );
      }

      DownloadedDataWrapper.DownloadPipelineActivityVisitor var5 = new DownloadedDataWrapper.DownloadPipelineActivityVisitor(var2, var3, var4);

      for (PipelineActivity var7 : var3.getActivities()) {
         var5.visit(var7);
      }

      var3 = var5.getOperationPipeline();
      var5.insertRemainingScripts(var3.getLastActivity());

      for (PipelineActivity var8 : var3.getActivities()) {
         if (var8.getContent() == null) {
            try {
               var3.removeActivity(var8);
            } catch (RemoveActivityVetoException var10) {
               this.logger.warning(var10.getMessage());
               var10.printStackTrace();
            }
         }
      }

      return var3;
   }

   private List<OperationId> getSiblingOperations(OperationId var1) {
      if (this.siblingOperations == null) {
         this.siblingOperations = Maps.newHashMap();
         this.fillDependentCollectionMap(this.siblingOperations, this.rawData.getSiblingOperations());
      }

      Collection var2 = this.siblingOperations.get(var1.toString());
      if (var2 != null && !var2.isEmpty()) {
         ArrayList var3 = Lists.newArrayList();

         for (Object __etlItem514 : var2) {
            StringKeyValuePairs var5 = (StringKeyValuePairs) __etlItem514;
            String var6 = var5.getString("siblingoperation_id");
            OperationId var7 = new OperationId(var6);
            if (!var6.isEmpty()) {
               var3.add(var7);
            }
         }

         return var3;
      } else {
         return Collections.emptyList();
      }
   }

   private TriggeredOperations getTriggeredOperations(HttpEndpointId var1) {
      if (this.httpEndpointOperations == null) {
         this.httpEndpointOperations = Maps.newHashMap();
         this.fillDependentCollectionMap(this.httpEndpointOperations, this.rawData.getHttpEndpointOperations());
      }

      TriggeredOperations var2 = new TriggeredOperations();
      Collection var3 = this.httpEndpointOperations.get(var1.toString());
      if (var3 != null && !var3.isEmpty()) {
         for (Object __etlItem537 : var3) {
            StringKeyValuePairs var5 = (StringKeyValuePairs) __etlItem537;
            String var6 = var5.getString("operation_id");
            OperationId var7 = new OperationId(var6);
            HttpVerb var8 = null;
            String var9 = var5.getString("http_verb");
            if (var9 != null && !var9.isEmpty()) {
               var8 = HttpVerb.valueOf(var9);
            }

            int var10 = var5.getInt("synchronous", 1);
            OperationRunMode var11 = EnumWithIdLookup.lookup(OperationRunMode.class, var10);
            boolean var12 = var5.getBoolean("final_target_as_response", false);
            boolean var13 = var5.getBoolean("use_request_as_source", true);
            TriggeredOperation var14 = new TriggeredOperation(var7, var8, var11, var12, var13);
            var2.addOperation(var14);
         }

         return var2;
      } else {
         return var2;
      }
   }

   private Collection<DownloadedDataWrapper.LdapSearchParametersWithOid> getLdapSearchParameters(Transformation var1) {
      if (this.ldapSearchparameters == null) {
         this.ldapSearchparameters = Maps.newHashMap();
         this.fillDependentCollectionMap(this.ldapSearchparameters, this.rawData.getLdapSearchParameters());
      }

      Collection var2 = this.ldapSearchparameters.get(var1.getID().toString());
      ArrayList var3 = Lists.newArrayList();
      if (var2 != null && !var2.isEmpty()) {
         for (Object __etlItem569 : var2) {
            StringKeyValuePairs var5 = (StringKeyValuePairs) __etlItem569;
            String var6 = var5.getString("classname");
            String var7 = var5.getString("path");
            String var8 = var5.getString("filter");
            String var9 = var5.getString("oid");
            LdapSearchScope var10 = LdapSearchScope.fromApiCode(var5.getInt("scope"));
            LdapSearchParameters var11 = new LdapSearchParameters(var6, var7, var8, var10);
            var3.add(new DownloadedDataWrapper.LdapSearchParametersWithOid(var11, var9));
         }
      }

      return var3;
   }

   private Collection<LdapStructureEntry> getLdapStructureEntries(Transformation var1, boolean var2) {
      if (this.ldapStructureEntry == null) {
         this.ldapStructureEntry = Maps.newHashMap();
         this.fillDependentCollectionMap(this.ldapStructureEntry, this.rawData.getLdapStructureEntries());
      }

      Collection var3 = this.ldapStructureEntry.get(var1.getID().toString());
      ArrayList var4 = Lists.newArrayList();
      if (var3 != null) {
         for (Object __etlItem592 : var3) {
            StringKeyValuePairs var6 = (StringKeyValuePairs) __etlItem592;
            int var7 = var6.getInt("type");
            if (var2 && var7 == 0 || !var2 && var7 == 1) {
               String var8 = var6.getString("oid");
               String var9 = var6.getString("attributes");
               String var10 = var6.getString("auxiliary_classes");
               LdapStructureEntry var11 = new LdapStructureEntry(
                  new NumericOid(var8), LdapStructureEntry.attributesFromString(var9), LdapStructureEntry.auxiliaryClassesFromString(var10)
               );
               var4.add(var11);
            }
         }
      }

      return var4;
   }

   private void fillTranLinkInfo() {
      this.nextFunctionCallMap = Maps.newHashMap();
      this.previousFunctionCallMap = Maps.newHashMap();
      DownloadedItems var1 = this.rawData.getTransformationChainLinks();
      if (var1 != null) {
         DownloadedItemProperties[] var2 = var1.getItems();
         if (var2 != null) {
            for (DownloadedItemProperties var6 : var2) {
               String var7 = var6.getItemGUID();
               StringKeyValuePairs var8 = this.getStringValuePairs(var6);
               String var9 = var8.get("transformation_id");
               String var10 = var8.get("nexttransformation_id");
               IntegrationEntity var11;
               Map var12;
               if (var9.equals(var7)) {
                  var11 = this.idEntityMap.get(var10);
                  var12 = this.nextFunctionCallMap;
               } else {
                  var11 = this.idEntityMap.get(var9);
                  var12 = this.previousFunctionCallMap;
               }

               if (var11 == null) {
                  String var13 = var9.equals(var7) ? var10 : var9;
                  String var14;
                  if (var13 != null && !var13.isEmpty()) {
                     var14 = "Function service guid " + var13 + " related to tranformation " + var7;
                  } else {
                     var14 = "No function service guid related to tranformation " + var7;
                  }

                  throw new RuntimeException(
                     var14 + "\n a possible reason is that the database may be corrupt! you can delete the project from the server and deploy it again"
                  );
               }

               if (var11 instanceof Function) {
                  var12.put(var7, (Function)var11);
               }
            }
         }
      }
   }

   private Function getFunctionCall(TransformationId var1, boolean var2) {
      if (this.nextFunctionCallMap == null || this.previousFunctionCallMap == null) {
         this.fillTranLinkInfo();
      }

      return var2 ? this.previousFunctionCallMap.get(var1.toString()) : this.nextFunctionCallMap.get(var1.toString());
   }

   private StringKeyValuePairs getStringValuePairs(DownloadedItemProperties var1) {
      return this.getStringValuePairs(var1, null);
   }

   private StringKeyValuePairs getStringValuePairs(DownloadedItemProperties var1, DeployKeyMappings var2) {
      WsKeyValuePairs var3 = var1.getProperties();
      WsKeyValue[] var4 = var3.getKeyValues();
      StringKeyValuePairs var5 = new StringKeyValuePairs();

      for (WsKeyValue var9 : var4) {
         String var10 = var9.getKey();
         String var11 = var9.getValue();
         if (var2 != null) {
            ServerPropertyKey var12 = var2.get(var10);
            if (var12 != null) {
               String var13 = var12.getClientKeyName();
               var5.putString(var13, var11);
            } else {
               this.logger.warning("The server key '" + var10 + "' does not have a corresponding client key.");
            }
         } else {
            var5.putString(var10, var11);
         }
      }

      return var5;
   }

   private void addEntity(DownloadedItems var1, EntityType var2) {
      if (var1 != null) {
         this.addEntity(var1.getItems(), var2);
      }
   }

   private void addEntity(DownloadedItemProperties[] var1, EntityType var2) {
      if (var1 != null && var1.length != 0) {
         this.logger.fine(String.format("Number of downloaded %s: %d", var2.getPlural(), var1.length));
         DeployKeyMappings var3 = ClientServerDeployKeyMappings.getServerClientKeyMappings(var2);

         for (DownloadedItemProperties var7 : var1) {
            StringKeyValuePairs var8 = this.getStringValuePairs(var7, var3);
            this.guidPropertiesMap.put(var2, var7.getItemGUID(), var8);
         }
      }
   }

   private void addClientEntity(String var1, DownloadedItems var2, EntityType var3) {
      if (var2 != null) {
         this.addClientEntity(var1, var2.getItems(), var3);
      }
   }

   private void addClientEntity(String var1, DownloadedItemProperties[] var2, EntityType var3) {
      if (var2 != null) {
         String var4 = String.format("%d", var3.getId());
         DownloadedItemProperties[] var5 = this.getSubProperties(var1, var4, var2);
         this.addEntity(var5, var3);
      }
   }

   private DownloadedItemProperties[] getSubProperties(String var1, String var2, DownloadedItemProperties[] var3) {
      ArrayList var4 = Lists.newArrayList();

      for (DownloadedItemProperties var8 : var3) {
         for (WsKeyValue var12 : var8.getProperties().getKeyValues()) {
            if (var12.getKey().equals(var1) && var12.getValue().equals(var2)) {
               var4.add(var8);
               break;
            }
         }
      }

      DownloadedItemProperties[] var13 = new DownloadedItemProperties[var4.size()];
      return (DownloadedItemProperties[])var4.toArray(var13);
   }

   private List<String> getMapList(TransformationId var1) {
      if (this.mapLists == null) {
         this.mapLists = Maps.newHashMap();
         DownloadedItems var2 = this.rawData.getTransformationDefinitions();
         DownloadedItemProperties[] var3 = var2.getItems();

         for (DownloadedItemProperties var7 : var3) {
            String var8 = var7.getItemGUID();
            List var9 = this.mapLists.get(var8);
            if (var9 == null) {
               var9 = Lists.newArrayList();
               this.mapLists.put(var8, var9);
            }

            StringKeyValuePairs var10 = this.getStringValuePairs(var7);
            String var11 = var10.getString("target_field_id");
            String var12 = var10.getString("transformation");
            String var13 = ClientServerCommunicationUtils.decodeString(var12);
            var9.add(var11 + "\t" + var13);
         }
      }

      return this.mapLists.get(var1.toString());
   }

   private void addFolders() {
      Collection var1 = this.guidPropertiesMap.get(EntityType.Folder);
      this.folders = Maps.newHashMap();
      this.parentFolders = Maps.newHashMap();
      if (!var1.isEmpty()) {
         for (Object __etlItem767 : var1) {
            StringKeyValuePairs var3 = (StringKeyValuePairs) __etlItem767;
            int var4 = var3.getInt("contentsentitytype_id", -1);
            if (var4 != -1) {
               String var5 = var3.getString("folder_id");
               EntityType var6 = EntityType.fromId(var4);
               Folder var7 = Folder.createFolder(var3);
               this.addEntityToMap(var7);
               this.folders.put(var5, var7);
               this.logger.fine(String.format("Reconstructed empty %s folder \"%s\" [ID = %s]", var6.name(), var7.getName(), var7.getID().toString()));
            }
         }

         DownloadedItems var14 = this.rawData.getFolderItems();
         if (var14 != null) {
            DownloadedItemProperties[] var15 = var14.getItems();
            if (var15 != null) {
               for (DownloadedItemProperties var19 : var15) {
                  StringKeyValuePairs var8 = this.getStringValuePairs(var19);
                  String var9 = var8.get("parentfolder_id");
                  if (var9 != "0") {
                     String var10 = var8.getString("item_id");
                     int var11 = var8.getInt("entitytype_id", -1);
                     if (var11 != -1) {
                        EntityType var12 = EntityType.fromId(var11);
                        Folder var13 = this.folders.get(var9);
                        if (var13 == null) {
                           this.logger.warning(String.format("Could not find the parent of %s folder %s", var12, var10));
                        } else {
                           this.logger.finer(String.format("Linking parent %s folder %s to child %s", var12, var10, var13.getID().toString()));
                           this.parentFolders.put(new DownloadedDataWrapper.EntityMapKey(var12, var10), var13);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   Collection<Folder> getFolders() {
      return this.folders.values();
   }

   Folder getParentFolder(IntegrationEntity var1) {
      EntityType var2 = var1.getEntityType();
      String var3 = var1.getID().toString();
      return this.parentFolders.get(new DownloadedDataWrapper.EntityMapKey(var2, var3));
   }

   private void addFileToDownload(IntegrationEntity var1, String var2) {
      IntegrationEntityId var3 = var1.getID();
      FileToDownload var4 = new FileToDownload(var3, var2);
      this.filesToDownload.put(var3, var4);
   }

   Collection<FileToDownload> getFilesToDownload() {
      return this.filesToDownload.values();
   }

   private User getUser(String var1) {
      User var2 = this.userCacheMap.get(var1);
      if (var2 == null) {
         var2 = new User(new UserId(var1), var1);
         this.userCacheMap.put(var1, var2);
      }

      return var2;
   }

   private Group getGroup(String var1) {
      Group var2 = this.groupCacheMap.get(var1);
      if (var2 == null) {
         var2 = new Group(new GroupId(var1), var1);
         this.groupCacheMap.put(var1, var2);
      }

      return var2;
   }

   private void setRolePermissions(boolean var1, PermissionsManager var2, DownloadedItems var3) {
      if (var3 != null) {
         DownloadedItemProperties[] var4 = var3.getItems();
         if (var4 != null && var4.length != 0) {
            for (DownloadedItemProperties var8 : var4) {
               StringKeyValuePairs var9 = this.getStringValuePairs(var8);
               String var10 = var9.getString("item_id");
               IntegrationEntity var11 = this.idEntityMap.get(var10);
               if (var11 == null) {
                  this.logger.warning("Cannot set permissions for item " + var10 + " since it was not downloaded.");
               } else {
                  String var12 = var9.getString("role_id");
                  int var13 = var9.getInt("allow_bits");
                  int var14 = var9.getInt("deny_bits");
                  Permissions var15 = new Permissions(var13, var14);
                  if (var1) {
                     var2.setPermissions(this.getUser(var12), var11, var15, false);
                  } else {
                     var2.setPermissions(this.getGroup(var12), var11, var15, false);
                  }
               }
            }
         }
      }
   }

   void setPermissions(PermissionsManager var1) {
      this.setRolePermissions(true, var1, this.rawData.getUserPermissions());
      this.setRolePermissions(false, var1, this.rawData.getGroupPermissions());
   }

   void setPluginAssociations(MutableAppliedPipelinePluginStore var1) {
      if (this.pipelinePluginAssociations == null) {
         this.pipelinePluginAssociations = Maps.newHashMap();
         this.fillDependentCollectionMap(this.pipelinePluginAssociations, this.rawData.getAppliedPlugins());
      }

      for (String var3 : this.pipelinePluginAssociations.keySet()) {
         Collection var4 = this.pipelinePluginAssociations.get(var3);
         if (var4 != null && !var4.isEmpty()) {
            IntegrationEntity var5 = this.idEntityMap.get(var3);
            if (var5 != null) {
               ArrayList var6 = Lists.newArrayList();

               for (Object __etlItem890 : var4) {
                  StringKeyValuePairs var8 = (StringKeyValuePairs) __etlItem890;
                  String var9 = var8.getString("PipelinePluginGuid");
                  if (var9 != null && !var9.isEmpty()) {
                     String var10 = var8.getString("Version");

                     BuildVersion var11;
                     try {
                        var11 = BuildVersion.fromString(var10);
                     } catch (IllegalVersionException var19) {
                        var19.printStackTrace();
                        continue;
                     }

                     PluginIdentifier var12 = new PluginIdentifier(var9, var11);
                     String var13 = var8.getString("RelativePositionId");
                     RelativePosition var14 = RelativePosition.fromString(var13);
                     int var15 = var8.getInt("ExecutionOrder", 0);
                     boolean var16 = var8.getBoolean("Optional", true);
                     PipelinePosition var17 = new PipelinePosition(var12, var5.getEntityType(), var14, var15, var16);
                     AppliedPipelinePlugin var18 = new AppliedPipelinePlugin(var5.getID(), var9, var11, var17);
                     var6.add(var18);
                  }
               }

               if (!var6.isEmpty()) {
                  var1.setAssociations(var5, var6);
               }
            }
         }
      }
   }

   private void fillJtrCache() {
      if (this.jtrCache == null) {
         this.jtrCache = Maps.newHashMap();
         DownloadedItems var1 = this.rawData.getJtrFiles();
         DownloadedItemProperties[] var2 = var1.getItems();

         for (int var3 = 0; var3 < var2.length; var3++) {
            DownloadedItemProperties var4 = var2[var3];
            StringKeyValuePairs var5 = this.getStringValuePairs(var4);
            String var6 = var5.getString("guid");
            if (var6 != null) {
               String[] var7 = this.jtrCache.get(var6);
               if (var7 == null) {
                  var7 = new String[]{null, null};
                  this.jtrCache.put(var6, var7);
               }

               String var8 = var5.getString("tag");
               String var9 = var5.getString("jtr");
               if (var8 != null && var9 != null) {
                  Object var10 = null;

                  try {
                     var10 = ZipUtils.base64DecodeAndInflateString(var9);
                  } catch (DataFormatException var12) {
                     this.logger.warning("Failed to decode JTR sent from the server: " + var12.getMessage());
                     var12.printStackTrace();
                     continue;
                  }

                  if (var8.equalsIgnoreCase("s")) {
                     var7[0] = (String)var10;
                  } else if (var8.equalsIgnoreCase("t")) {
                     var7[1] = (String)var10;
                  }

                  var2[var3] = null;
               }
            }
         }

         this.rawData.setJtrFiles(null);
      }
   }

   private String[] getJtrFilesForTransformation(Transformation var1) {
      if (this.jtrCache == null) {
         this.fillJtrCache();
      }

      return this.jtrCache.get(var1.getID().toString());
   }

   private void writeJtrToCache(Writer var1, String var2) throws IOException {
      if (var1 != null && var2 != null) {
         try {
            var1.write(var2);
         } finally {
            var1.close();
         }
      }
   }

   void addJtrFiles(ManagedProject var1) throws DataCacheException, IOException {
      InputAndOutputMappingTreeStructureCache var2 = var1.getTransformationMappingStructureCache();

      for (Transformation var5 : this.getTransformations()) {
         String[] var6 = this.getJtrFilesForTransformation(var5);
         if (var6 != null) {
            if (var6[0] != null) {
               this.writeJtrToCache(var2.getInputStructureWriter(var5.getID()), var6[0]);
            }

            if (var6[1] != null) {
               this.writeJtrToCache(var2.getOutputStructureWriter(var5.getID()), var6[1]);
            }
         }
      }
   }

   void addEntityResources(EntityResources var1) {
      Collection var2 = this.getOperations();
      Iterator var3 = var2.iterator();

      while (true) {
         Operation var4;
         String var6;
         while (true) {
            if (!var3.hasNext()) {
               return;
            }

            var4 = (Operation)var3.next();
            String var5 = this.getOperationGraphLayout(var4.getID().toString());
            if (var5 != null) {
               try {
                  var6 = ZipUtils.base64DecodeAndInflateString(var5);
                  if (var6 == null) {
                     continue;
                  }
                  break;
               } catch (DataFormatException var8) {
                  var8.printStackTrace();
               }
            }
         }

         FileResource var7 = var1.getFileResource(var4, "GraphLayout.xml");
         if (var7 != null) {
            var7.write(var6);
         }
      }
   }

   private class ConstructionCompleter {
      private String error = null;

      private ConstructionCompleter() {
      }

      public void complete(IntegrationEntity var1) {
         switch (var1.getEntityType()) {
            case Document:
               this.visit((TextDocument)var1);
               break;
            case WebServiceCall:
               this.visit((WebServiceCall)var1);
               break;
            case Transformation:
               this.visit((Transformation)var1);
               break;
            case Operation:
               this.visit((Operation)var1);
               break;
            case HttpEndpoint:
               this.visit((HttpEndpoint)var1);
               break;
            default:
               if (var1 instanceof ConnectorCall) {
                  this.visit((ConnectorCall)var1);
               }
         }
      }

      private void visit(TextDocument var1) {
         String var2 = var1.getID().toString();
         Collection var3 = DownloadedDataWrapper.this.getDocumentDefinitions(var2);
         if (var3 != null) {
            Collection var4 = DownloadedDataWrapper.this.getDocumentFieldValidations(var2);
            TextStructureFactory var5 = new TextStructureFactory(var1);
            TextStructure var6 = var5.createTextStructure(var3, var4);
            var1.setStructure(var6);
         }
      }

      private void visit(WebServiceCall var1) {
         String var2 = var1.getID().toString();
         CallRestrictions var3 = DownloadedDataWrapper.this.getCallRestrictions(var2);
         var1.setCallRestrictions(var3);
         WsdlFile var4 = DownloadedDataWrapper.this.getWsdlFile(var2);
         var1.setWsdlFile(var4);
         String var5 = var4.getLocator();
         DownloadedDataWrapper.this.addFileToDownload(var1, var5);
      }

      private DatabaseStructure buildDatabaseStructure(String var1, DatabaseLocation var2, boolean var3) {
         if (var2 != null) {
            DatabaseStructureFactory var4 = new DatabaseStructureFactory(var2);
            var4.buildDbFields(DownloadedDataWrapper.this.getDbFields(var1, var3));
            var4.buildDbTables(DownloadedDataWrapper.this.getDbTables(var1, var3));
            var4.buildDbTranRelations(DownloadedDataWrapper.this.getTranRelations(var1, var3));
            var4.buildSqlScript(DownloadedDataWrapper.this.getSqlScripts(var1, var3));
            var4.buildTreeFilters(DownloadedDataWrapper.this.getTranFilters(var1));
            return var4.buildStructure();
         } else {
            return null;
         }
      }

      private DataStructure getInputOutputStructure(Transformation var1, boolean var2) throws Exception {
         DataStructureType var3 = var2 ? var1.getSourceType() : var1.getTargetType();
         switch (var3) {
            case ComplexText:
            case Text:
               return this.reconstructTextStructure(var1, var2);
            case Database:
               return this.reconstructDatabaseStructure(var1, var2);
            case None:
               return new NoStructure();
            case WebServiceRequest:
            case WebServiceResponse:
               return this.reconstructWebServiceStructure(var1, var2, var3);
            case XML:
               return this.reconstructXmlStructure(var1, var2);
            case JSON:
               return this.reconstructJsonStructure(var1, var2);
            case LDAP:
               return this.reconstructLdapStructure(var1, var2);
            case ConnectorCallRequest:
            case ConnectorCallResponse:
               return this.reconstructConnectorCallStructure(var1, var2, var3);
            default:
               throw new NotImplementedYetException("Downloading transformations using structure type '" + var3.toString() + "' is not implemented yet.");
         }
      }

      private DataStructure reconstructTextStructure(Transformation var1, boolean var2) throws Exception {
         TextDocumentId var3 = var2 ? var1.getSourceTextDocumentId() : var1.getTargetTextDocumentId();
         TextDocument var4 = (TextDocument)DownloadedDataWrapper.this.idEntityMap.get(var3.toString());
         if (var4 != null) {
            return var4.getDataStructure();
         }

         String var5 = this.getIncompleteTransformationErrorMessage(
            "The ETLForge server did not send all required data for the transformation \"{0}\" [GUID = {1}]: The transformation''s "
               + (var2 ? "input" : "output")
               + " structure is a Text Document, but the Text Document definition could not be found in the downloaded data. The GUID of the missing Text Document is "
               + var3
               + ".\n\nThis is a bug. Please report it to ETLForge Support.",
            var1
         );
         throw new Exception(var5);
      }

      private DataStructure reconstructDatabaseStructure(Transformation var1, boolean var2) throws Exception {
         String var3 = var1.getID().toString();
         IntegrationEntityId var4 = var2 ? var1.getSourceId() : var1.getTargetId();
         Locatable var5 = (Locatable)DownloadedDataWrapper.this.idEntityMap.get(var4.toString());
         if (var5 == null) {
            String var7 = this.getIncompleteTransformationErrorMessage(
               "The ETLForge server did not send all required data for the transformation \"{0}\" [GUID = {1}]: The transformation''s "
                  + (var2 ? "input" : "output")
                  + " structure is a database, but the database definition could not be found in the downloaded data. The GUID of the missing database "
                  + (var2 ? "source" : "target")
                  + " is "
                  + var4
                  + ".\n\nThis is a bug. Please report it to ETLForge Support.",
               var1
            );
            throw new Exception(var7);
         } else {
            DatabaseLocation var6 = (DatabaseLocation)var5.getLocation();
            return this.buildDatabaseStructure(var3, var6, var2);
         }
      }

      private DataStructure reconstructWebServiceStructure(Transformation var1, boolean var2, DataStructureType var3) throws Exception {
         WebServiceCall var4 = (WebServiceCall)DownloadedDataWrapper.this.getFunctionCall(var1.getID(), var2);
         if (var4 == null) {
            throw new Exception("Missing Function definition referred to by the Transformation with GUID " + var1.getID());
         } else {
            return var3 == DataStructureType.WebServiceRequest ? var4.getInputStructure() : var4.getOutputStructure();
         }
      }

      private DataStructure reconstructXmlStructure(Transformation var1, boolean var2) {
         XmlStructure var3 = (XmlStructure)(var2 ? var1.getInputStructure() : var1.getOutputStructure());
         if (var3 != null) {
            DownloadedDataWrapper.this.addFileToDownload(var1, var3.getFilePath());
         }

         return null;
      }

      private DataStructure reconstructJsonStructure(Transformation var1, boolean var2) {
         JsonStructure var3 = (JsonStructure)(var2 ? var1.getInputStructure() : var1.getOutputStructure());
         if (var3 != null) {
            DownloadedDataWrapper.this.addFileToDownload(var1, var3.getFileName());
         }

         return null;
      }

      private DataStructure reconstructLdapStructure(Transformation var1, boolean var2) throws Exception {
         LdapStructure var3 = new LdapStructure();
         if (var2) {
            for (DownloadedDataWrapper.LdapSearchParametersWithOid var6 : DownloadedDataWrapper.this.getLdapSearchParameters(var1)) {
               var3.addSearchParameters(new NumericOid(var6.getOid()), var6.getLdapSearchParameters());
            }
         }

         Collection var7 = DownloadedDataWrapper.this.getLdapStructureEntries(var1, var2);
         if (var7.isEmpty()) {
            throw new Exception(
               "A project with LDAP transformations can not be downloaded until it has been deployed in the current version first. Please load the original project and deploy it to the server and then deploy it again."
            );
         }

         var3.setClassEntries((LdapStructureEntry[]) (LdapStructureEntry[]) var7.toArray(new LdapStructureEntry[var7.size()]));
         return var3;
      }

      private DataStructure reconstructConnectorCallStructure(Transformation var1, boolean var2, DataStructureType var3) throws Exception {
         ConnectorCall var4 = (ConnectorCall)DownloadedDataWrapper.this.getFunctionCall(var1.getID(), var2);
         if (var4 == null) {
            throw new Exception("Missing Function definition referred to by the Transformation with GUID " + var1.getID());
         } else {
            return var3 == DataStructureType.ConnectorCallRequest ? var4.getInputStructure() : var4.getOutputStructure();
         }
      }

      private String getIncompleteTransformationErrorMessage(String var1, Transformation var2) {
         return MessageFormat.format(var1, var2.getName(), var2.getID());
      }

      private void visit(Transformation var1) {
         try {
            DataStructure var2 = this.getInputOutputStructure(var1, true);
            if (var2 != null) {
               var1.setInputStructure(var2);
            }

            var2 = this.getInputOutputStructure(var1, false);
            if (var2 != null) {
               var1.setOutputStructure(var2);
            }
         } catch (Exception var3) {
            this.error = var3.getMessage();
         }

         List var5 = DownloadedDataWrapper.this.getMapList(var1.getID());
         var1.setMapList(var5);
      }

      private void visit(Operation var1) {
         OperationPipeline var2 = null;

         try {
            var2 = DownloadedDataWrapper.this.getOperationPipeline(var1);
         } catch (Exception var4) {
            var4.printStackTrace();
            this.error = var4.getMessage();
            return;
         }

         var1.setPipeline(var2);
         var1.setSiblingOperations(DownloadedDataWrapper.this.getSiblingOperations(var1.getID()));
      }

      private void visit(HttpEndpoint var1) {
         TriggeredOperations var2 = DownloadedDataWrapper.this.getTriggeredOperations(var1.getID());
         var1.setTriggeredOperations(var2);
      }

      private void visit(ConnectorCall var1) {
         if (var1.hasRequestTransformation()) {
            String var2 = var1.getInputStructure().getXsdPath();
            DownloadedDataWrapper.this.addFileToDownload((IntegrationEntity)var1, var2);
         }

         String var3 = var1.getOutputStructure().getXsdPath();
         DownloadedDataWrapper.this.addFileToDownload((IntegrationEntity)var1, var3);
      }

      public void throwOnError() throws Exception {
         if (this.error != null && !this.error.isEmpty()) {
            Exception var1 = new Exception(this.error);
            DownloadedDataWrapper.this.logger.throwing(this.getClass().getName(), "throwOnError", var1);
            throw var1;
         }
      }
   }

   private final class DownloadPipelineActivityVisitor {
      private final OperationPipeline pipelineTemplate;
      private final Iterator<StringKeyValuePairs> itemIterator;
      private DownloadedDataWrapper.DownloadedPipelineActivity nextItem;
      private boolean allowsScriptsBefore;
      private boolean inNoScriptAllowedRegionOfWebServiceCall;

      private DownloadPipelineActivityVisitor(OperationType var2, OperationPipeline var3, Collection<StringKeyValuePairs> var4) {
         this.pipelineTemplate = new OperationPipeline(var2, var3.getActivities());
         this.itemIterator = var4.iterator();
         this.allowsScriptsBefore = var3.getType() != HostedWebServiceOperationType.TYPE;
      }

      public OperationPipeline getOperationPipeline() {
         return this.pipelineTemplate;
      }

      private DownloadedDataWrapper.DownloadedPipelineActivity getNext() {
         if (this.nextItem != null) {
            return this.nextItem;
         } else if (this.itemIterator.hasNext()) {
            this.nextItem = new DownloadedDataWrapper.DownloadedPipelineActivity(this.itemIterator.next());
            return this.nextItem;
         } else {
            return null;
         }
      }

      public void visit(PipelineActivity var1) {
         PipelineActivityType var2 = var1.getType();
         if (var2 == PipelineActivityType.TRANSFORMATION) {
            this.visit((TransformationActivity)var1);
         } else if (var2 == PipelineActivityType.SCRIPT) {
            this.visit((ScriptActivity)var1);
         } else {
            if (!(var1 instanceof EntityActivity)) {
               throw new UnsupportedOperationException("I don't know how to handle activities of type " + var2);
            }

            this.visitEntityActivity((EntityActivity)var1);
         }
      }

      private void visit(ScriptActivity var1) {
         for (DownloadedDataWrapper.DownloadedPipelineActivity var2 = this.getNext(); var2 != null; var2 = this.getNext()) {
            if (var2.activityType != PipelineActivityType.SCRIPT) {
               return;
            }

            Script var3 = (Script)DownloadedDataWrapper.this.idEntityMap.get(var2.activityGuid);
            if (var1.getContent() == null) {
               var1.setContent(var3);
            } else {
               ScriptActivity var4 = ScriptActivity.newInstance(var3, EntityType.Script.getDisplayName());
               this.pipelineTemplate.insertAfter(var4, var1);
               var1 = var4;
            }

            this.nextItem = null;
         }
      }

      private DownloadedDataWrapper.DownloadedPipelineActivity insertScriptsBefore(PipelineActivity var1) {
         DownloadedDataWrapper.DownloadedPipelineActivity var2 = this.getNext();
         if (var2 == null) {
            return null;
         }

         while (var2.activityType == PipelineActivityType.SCRIPT) {
            if (!this.allowsScriptsBefore) {
               return null;
            }

            Script var3 = (Script)DownloadedDataWrapper.this.idEntityMap.get(var2.activityGuid);
            ScriptActivity var4 = ScriptActivity.newInstance(var3, EntityType.Script.getDisplayName());
            this.nextItem = null;
            this.pipelineTemplate.insertBefore(var4, var1);
            var2 = this.getNext();
            if (var2 == null) {
               return null;
            }
         }

         return var2;
      }

      private void insertRemainingScripts(PipelineActivity var1) {
         DownloadedDataWrapper.DownloadedPipelineActivity var2 = this.getNext();
         if (var2 != null) {
            while (var2.activityType == PipelineActivityType.SCRIPT) {
               Script var3 = (Script)DownloadedDataWrapper.this.idEntityMap.get(var2.activityGuid);
               ScriptActivity var4 = ScriptActivity.newInstance(var3, EntityType.Script.getDisplayName());
               this.nextItem = null;
               this.pipelineTemplate.insertAfter(var4, var1);
               var1 = var4;
               var2 = this.getNext();
               if (var2 == null) {
                  return;
               }
            }
         }
      }

      private void visit(TransformationActivity var1) {
         DownloadedDataWrapper.DownloadedPipelineActivity var2 = this.insertScriptsBefore(var1);
         if (this.isWebServiceOperation()) {
            if (this.inNoScriptAllowedRegionOfWebServiceCall) {
               this.inNoScriptAllowedRegionOfWebServiceCall = false;
               this.allowsScriptsBefore = true;
            } else {
               this.inNoScriptAllowedRegionOfWebServiceCall = true;
               this.allowsScriptsBefore = false;
            }
         }

         if (var2 != null) {
            if (var2.activityType == PipelineActivityType.TRANSFORMATION) {
               var1.setContent((Transformation)DownloadedDataWrapper.this.idEntityMap.get(var2.activityGuid));
               this.nextItem = null;
            }
         }
      }

      private boolean isWebServiceOperation() {
         return this.pipelineTemplate.getType() == WebServiceOperationType.TYPE;
      }

      private void visitEntityActivity(EntityActivity var1) {
         DownloadedDataWrapper.DownloadedPipelineActivity var2 = this.insertScriptsBefore(var1);
         if (var2 != null) {
            if (var2.activityType == var1.getType()) {
               var1.setContent(DownloadedDataWrapper.this.idEntityMap.get(var2.activityGuid));
               this.nextItem = null;
            }
         }
      }
   }

   private static final class DownloadedPipelineActivity {
      public final PipelineActivityType activityType;
      public final String activityGuid;

      private DownloadedPipelineActivity(StringKeyValuePairs var1) {
         int var2 = var1.getInt("activitytype_id");
         this.activityType = PipelineActivityType.fromId(var2);
         this.activityGuid = var1.getString("activity_id");
      }
   }

   private static class EntityMapKey {
      final String guid;
      final EntityType entityType;

      EntityMapKey(EntityType var1, String var2) {
         this.guid = var2;
         this.entityType = var1;
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.guid, this.entityType);
      }

      @Override
      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         }

         if (!(var1 instanceof DownloadedDataWrapper.EntityMapKey)) {
            return false;
         }

         DownloadedDataWrapper.EntityMapKey var2 = (DownloadedDataWrapper.EntityMapKey)var1;
         return this.guid.equals(var2.guid) && this.entityType == var2.entityType;
      }
   }

   private static final class LdapSearchParametersWithOid {
      private final LdapSearchParameters ldapSearchParameters;
      private final String oid;

      private LdapSearchParametersWithOid(LdapSearchParameters var1, String var2) {
         this.ldapSearchParameters = var1;
         this.oid = var2;
      }

      private LdapSearchParameters getLdapSearchParameters() {
         return this.ldapSearchParameters;
      }

      private String getOid() {
         return this.oid;
      }
   }

   private static class PropertyMap {
      private final Map<DownloadedDataWrapper.EntityMapKey, StringKeyValuePairs> propertyMap = Maps.newHashMap();

      private PropertyMap() {
      }

      Collection<StringKeyValuePairs> get(EntityType var1) {
         ArrayList var2 = Lists.newArrayList();
         Set var3 = this.propertyMap.entrySet();
         if (var3 == null) {
            return var2;
         }

         for (Object __etlItem1494 : var3) {
            Entry var5 = (Entry) __etlItem1494;
            if (((DownloadedDataWrapper.EntityMapKey)var5.getKey()).entityType == var1) {
               var2.add(var5.getValue());
            }
         }

         return var2;
      }

      void put(EntityType var1, String var2, StringKeyValuePairs var3) {
         this.propertyMap.put(new DownloadedDataWrapper.EntityMapKey(var1, var2), var3);
      }
   }
}
