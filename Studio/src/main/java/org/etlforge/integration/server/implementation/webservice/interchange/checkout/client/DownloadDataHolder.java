package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class DownloadDataHolder implements Serializable {
   private DownloadedItems projects;
   private DownloadedItems sources;
   private DownloadedItems targets;
   private DownloadedItems schedules;
   private DownloadedItems emailMessages;
   private DownloadedItems scripts;
   private DownloadedItems textStructures;
   private DownloadedItems documentDefinitions;
   private DownloadedItems documentFieldValidations;
   private DownloadedItems transformations;
   private DownloadedItems transformationDefinitions;
   private DownloadedItems transformationRelations;
   private DownloadedItems transformationFilters;
   private DownloadedItems transformationChainLinks;
   private DownloadedItems dbFields;
   private DownloadedItems dbTables;
   private DownloadedItems sqlScripts;
   private DownloadedItems ldapSearchParameters;
   private DownloadedItems ldapStructureEntries;
   private DownloadedItems webServiceMethods;
   private DownloadedItems wsdls;
   private DownloadedItems callRestrictions;
   private DownloadedItems folders;
   private DownloadedItems folderItems;
   private DownloadedItems operations;
   private DownloadedItems operationPipelineActivities;
   private DownloadedItems operationTransformationTab;
   private DownloadedItems siblingOperations;
   private DownloadedItems operationGraphLayout;
   private DownloadedItems httpEndpoints;
   private DownloadedItems httpEndpointOperations;
   private DownloadedItems salesforceConnectors;
   private DownloadedItems salesforceQueries;
   private DownloadedItems salesforceUpserts;
   private DownloadedItems salesforceCreates;
   private DownloadedItems salesforceUpdates;
   private DownloadedItems salesforceDeletes;
   private DownloadedItems salesforceBulkLoads;
   private DownloadedItems sapEndpoints;
   private DownloadedItems sapFunctions;
   private DownloadedItems MSCrmEndpoints;
   private DownloadedItems MSCrmFunctions;
   private DownloadedItems MSAXEndpoints;
   private DownloadedItems MSAXFunctions;
   private DownloadedItems MSGPEndpoints;
   private DownloadedItems MSGPFunctions;
   private DownloadedItems ebsEndpoints;
   private DownloadedItems ebsFunctions;
   private DownloadedItems autoDeskEndpoints;
   private DownloadedItems autoDeskFunctions;
   private DownloadedItems netSuiteEndpoints;
   private DownloadedItems netSuiteFunctions;
   private DownloadedItems userPermissions;
   private DownloadedItems groupPermissions;
   private DownloadedItems appliedPlugins;
   private DownloadedItems jtrFiles;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(DownloadDataHolder.class, true);

   public DownloadDataHolder() {
   }

   public DownloadDataHolder(
      DownloadedItems var1,
      DownloadedItems var2,
      DownloadedItems var3,
      DownloadedItems var4,
      DownloadedItems var5,
      DownloadedItems var6,
      DownloadedItems var7,
      DownloadedItems var8,
      DownloadedItems var9,
      DownloadedItems var10,
      DownloadedItems var11,
      DownloadedItems var12,
      DownloadedItems var13,
      DownloadedItems var14,
      DownloadedItems var15,
      DownloadedItems var16,
      DownloadedItems var17,
      DownloadedItems var18,
      DownloadedItems var19,
      DownloadedItems var20,
      DownloadedItems var21,
      DownloadedItems var22,
      DownloadedItems var23,
      DownloadedItems var24,
      DownloadedItems var25,
      DownloadedItems var26,
      DownloadedItems var27,
      DownloadedItems var28,
      DownloadedItems var29,
      DownloadedItems var30,
      DownloadedItems var31,
      DownloadedItems var32,
      DownloadedItems var33,
      DownloadedItems var34,
      DownloadedItems var35,
      DownloadedItems var36,
      DownloadedItems var37,
      DownloadedItems var38,
      DownloadedItems var39,
      DownloadedItems var40,
      DownloadedItems var41,
      DownloadedItems var42,
      DownloadedItems var43,
      DownloadedItems var44,
      DownloadedItems var45,
      DownloadedItems var46,
      DownloadedItems var47,
      DownloadedItems var48,
      DownloadedItems var49,
      DownloadedItems var50,
      DownloadedItems var51,
      DownloadedItems var52,
      DownloadedItems var53,
      DownloadedItems var54,
      DownloadedItems var55,
      DownloadedItems var56
   ) {
      this.projects = var1;
      this.sources = var2;
      this.targets = var3;
      this.schedules = var4;
      this.emailMessages = var5;
      this.scripts = var6;
      this.textStructures = var7;
      this.documentDefinitions = var8;
      this.documentFieldValidations = var9;
      this.transformations = var10;
      this.transformationDefinitions = var11;
      this.transformationRelations = var12;
      this.transformationFilters = var13;
      this.transformationChainLinks = var14;
      this.dbFields = var15;
      this.dbTables = var16;
      this.sqlScripts = var17;
      this.ldapSearchParameters = var18;
      this.ldapStructureEntries = var19;
      this.webServiceMethods = var20;
      this.wsdls = var21;
      this.callRestrictions = var22;
      this.folders = var23;
      this.folderItems = var24;
      this.operations = var25;
      this.operationPipelineActivities = var26;
      this.operationTransformationTab = var27;
      this.siblingOperations = var28;
      this.operationGraphLayout = var29;
      this.httpEndpoints = var30;
      this.httpEndpointOperations = var31;
      this.salesforceConnectors = var32;
      this.salesforceQueries = var33;
      this.salesforceUpserts = var34;
      this.salesforceCreates = var35;
      this.salesforceUpdates = var36;
      this.salesforceDeletes = var37;
      this.salesforceBulkLoads = var38;
      this.sapEndpoints = var39;
      this.sapFunctions = var40;
      this.MSCrmEndpoints = var41;
      this.MSCrmFunctions = var42;
      this.MSAXEndpoints = var43;
      this.MSAXFunctions = var44;
      this.MSGPEndpoints = var45;
      this.MSGPFunctions = var46;
      this.ebsEndpoints = var47;
      this.ebsFunctions = var48;
      this.autoDeskEndpoints = var49;
      this.autoDeskFunctions = var50;
      this.netSuiteEndpoints = var51;
      this.netSuiteFunctions = var52;
      this.userPermissions = var53;
      this.groupPermissions = var54;
      this.appliedPlugins = var55;
      this.jtrFiles = var56;
   }

   public DownloadedItems getProjects() {
      return this.projects;
   }

   public void setProjects(DownloadedItems var1) {
      this.projects = var1;
   }

   public DownloadedItems getSources() {
      return this.sources;
   }

   public void setSources(DownloadedItems var1) {
      this.sources = var1;
   }

   public DownloadedItems getTargets() {
      return this.targets;
   }

   public void setTargets(DownloadedItems var1) {
      this.targets = var1;
   }

   public DownloadedItems getSchedules() {
      return this.schedules;
   }

   public void setSchedules(DownloadedItems var1) {
      this.schedules = var1;
   }

   public DownloadedItems getEmailMessages() {
      return this.emailMessages;
   }

   public void setEmailMessages(DownloadedItems var1) {
      this.emailMessages = var1;
   }

   public DownloadedItems getScripts() {
      return this.scripts;
   }

   public void setScripts(DownloadedItems var1) {
      this.scripts = var1;
   }

   public DownloadedItems getTextStructures() {
      return this.textStructures;
   }

   public void setTextStructures(DownloadedItems var1) {
      this.textStructures = var1;
   }

   public DownloadedItems getDocumentDefinitions() {
      return this.documentDefinitions;
   }

   public void setDocumentDefinitions(DownloadedItems var1) {
      this.documentDefinitions = var1;
   }

   public DownloadedItems getDocumentFieldValidations() {
      return this.documentFieldValidations;
   }

   public void setDocumentFieldValidations(DownloadedItems var1) {
      this.documentFieldValidations = var1;
   }

   public DownloadedItems getTransformations() {
      return this.transformations;
   }

   public void setTransformations(DownloadedItems var1) {
      this.transformations = var1;
   }

   public DownloadedItems getTransformationDefinitions() {
      return this.transformationDefinitions;
   }

   public void setTransformationDefinitions(DownloadedItems var1) {
      this.transformationDefinitions = var1;
   }

   public DownloadedItems getTransformationRelations() {
      return this.transformationRelations;
   }

   public void setTransformationRelations(DownloadedItems var1) {
      this.transformationRelations = var1;
   }

   public DownloadedItems getTransformationFilters() {
      return this.transformationFilters;
   }

   public void setTransformationFilters(DownloadedItems var1) {
      this.transformationFilters = var1;
   }

   public DownloadedItems getTransformationChainLinks() {
      return this.transformationChainLinks;
   }

   public void setTransformationChainLinks(DownloadedItems var1) {
      this.transformationChainLinks = var1;
   }

   public DownloadedItems getDbFields() {
      return this.dbFields;
   }

   public void setDbFields(DownloadedItems var1) {
      this.dbFields = var1;
   }

   public DownloadedItems getDbTables() {
      return this.dbTables;
   }

   public void setDbTables(DownloadedItems var1) {
      this.dbTables = var1;
   }

   public DownloadedItems getSqlScripts() {
      return this.sqlScripts;
   }

   public void setSqlScripts(DownloadedItems var1) {
      this.sqlScripts = var1;
   }

   public DownloadedItems getLdapSearchParameters() {
      return this.ldapSearchParameters;
   }

   public void setLdapSearchParameters(DownloadedItems var1) {
      this.ldapSearchParameters = var1;
   }

   public DownloadedItems getLdapStructureEntries() {
      return this.ldapStructureEntries;
   }

   public void setLdapStructureEntries(DownloadedItems var1) {
      this.ldapStructureEntries = var1;
   }

   public DownloadedItems getWebServiceMethods() {
      return this.webServiceMethods;
   }

   public void setWebServiceMethods(DownloadedItems var1) {
      this.webServiceMethods = var1;
   }

   public DownloadedItems getWsdls() {
      return this.wsdls;
   }

   public void setWsdls(DownloadedItems var1) {
      this.wsdls = var1;
   }

   public DownloadedItems getCallRestrictions() {
      return this.callRestrictions;
   }

   public void setCallRestrictions(DownloadedItems var1) {
      this.callRestrictions = var1;
   }

   public DownloadedItems getFolders() {
      return this.folders;
   }

   public void setFolders(DownloadedItems var1) {
      this.folders = var1;
   }

   public DownloadedItems getFolderItems() {
      return this.folderItems;
   }

   public void setFolderItems(DownloadedItems var1) {
      this.folderItems = var1;
   }

   public DownloadedItems getOperations() {
      return this.operations;
   }

   public void setOperations(DownloadedItems var1) {
      this.operations = var1;
   }

   public DownloadedItems getOperationPipelineActivities() {
      return this.operationPipelineActivities;
   }

   public void setOperationPipelineActivities(DownloadedItems var1) {
      this.operationPipelineActivities = var1;
   }

   public DownloadedItems getOperationTransformationTab() {
      return this.operationTransformationTab;
   }

   public void setOperationTransformationTab(DownloadedItems var1) {
      this.operationTransformationTab = var1;
   }

   public DownloadedItems getSiblingOperations() {
      return this.siblingOperations;
   }

   public void setSiblingOperations(DownloadedItems var1) {
      this.siblingOperations = var1;
   }

   public DownloadedItems getOperationGraphLayout() {
      return this.operationGraphLayout;
   }

   public void setOperationGraphLayout(DownloadedItems var1) {
      this.operationGraphLayout = var1;
   }

   public DownloadedItems getHttpEndpoints() {
      return this.httpEndpoints;
   }

   public void setHttpEndpoints(DownloadedItems var1) {
      this.httpEndpoints = var1;
   }

   public DownloadedItems getHttpEndpointOperations() {
      return this.httpEndpointOperations;
   }

   public void setHttpEndpointOperations(DownloadedItems var1) {
      this.httpEndpointOperations = var1;
   }

   public DownloadedItems getSalesforceConnectors() {
      return this.salesforceConnectors;
   }

   public void setSalesforceConnectors(DownloadedItems var1) {
      this.salesforceConnectors = var1;
   }

   public DownloadedItems getSalesforceQueries() {
      return this.salesforceQueries;
   }

   public void setSalesforceQueries(DownloadedItems var1) {
      this.salesforceQueries = var1;
   }

   public DownloadedItems getSalesforceUpserts() {
      return this.salesforceUpserts;
   }

   public void setSalesforceUpserts(DownloadedItems var1) {
      this.salesforceUpserts = var1;
   }

   public DownloadedItems getSalesforceCreates() {
      return this.salesforceCreates;
   }

   public void setSalesforceCreates(DownloadedItems var1) {
      this.salesforceCreates = var1;
   }

   public DownloadedItems getSalesforceUpdates() {
      return this.salesforceUpdates;
   }

   public void setSalesforceUpdates(DownloadedItems var1) {
      this.salesforceUpdates = var1;
   }

   public DownloadedItems getSalesforceDeletes() {
      return this.salesforceDeletes;
   }

   public void setSalesforceDeletes(DownloadedItems var1) {
      this.salesforceDeletes = var1;
   }

   public DownloadedItems getSalesforceBulkLoads() {
      return this.salesforceBulkLoads;
   }

   public void setSalesforceBulkLoads(DownloadedItems var1) {
      this.salesforceBulkLoads = var1;
   }

   public DownloadedItems getSapEndpoints() {
      return this.sapEndpoints;
   }

   public void setSapEndpoints(DownloadedItems var1) {
      this.sapEndpoints = var1;
   }

   public DownloadedItems getSapFunctions() {
      return this.sapFunctions;
   }

   public void setSapFunctions(DownloadedItems var1) {
      this.sapFunctions = var1;
   }

   public DownloadedItems getMSCrmEndpoints() {
      return this.MSCrmEndpoints;
   }

   public void setMSCrmEndpoints(DownloadedItems var1) {
      this.MSCrmEndpoints = var1;
   }

   public DownloadedItems getMSCrmFunctions() {
      return this.MSCrmFunctions;
   }

   public void setMSCrmFunctions(DownloadedItems var1) {
      this.MSCrmFunctions = var1;
   }

   public DownloadedItems getMSAXEndpoints() {
      return this.MSAXEndpoints;
   }

   public void setMSAXEndpoints(DownloadedItems var1) {
      this.MSAXEndpoints = var1;
   }

   public DownloadedItems getMSAXFunctions() {
      return this.MSAXFunctions;
   }

   public void setMSAXFunctions(DownloadedItems var1) {
      this.MSAXFunctions = var1;
   }

   public DownloadedItems getMSGPEndpoints() {
      return this.MSGPEndpoints;
   }

   public void setMSGPEndpoints(DownloadedItems var1) {
      this.MSGPEndpoints = var1;
   }

   public DownloadedItems getMSGPFunctions() {
      return this.MSGPFunctions;
   }

   public void setMSGPFunctions(DownloadedItems var1) {
      this.MSGPFunctions = var1;
   }

   public DownloadedItems getEbsEndpoints() {
      return this.ebsEndpoints;
   }

   public void setEbsEndpoints(DownloadedItems var1) {
      this.ebsEndpoints = var1;
   }

   public DownloadedItems getEbsFunctions() {
      return this.ebsFunctions;
   }

   public void setEbsFunctions(DownloadedItems var1) {
      this.ebsFunctions = var1;
   }

   public DownloadedItems getAutoDeskEndpoints() {
      return this.autoDeskEndpoints;
   }

   public void setAutoDeskEndpoints(DownloadedItems var1) {
      this.autoDeskEndpoints = var1;
   }

   public DownloadedItems getAutoDeskFunctions() {
      return this.autoDeskFunctions;
   }

   public void setAutoDeskFunctions(DownloadedItems var1) {
      this.autoDeskFunctions = var1;
   }

   public DownloadedItems getNetSuiteEndpoints() {
      return this.netSuiteEndpoints;
   }

   public void setNetSuiteEndpoints(DownloadedItems var1) {
      this.netSuiteEndpoints = var1;
   }

   public DownloadedItems getNetSuiteFunctions() {
      return this.netSuiteFunctions;
   }

   public void setNetSuiteFunctions(DownloadedItems var1) {
      this.netSuiteFunctions = var1;
   }

   public DownloadedItems getUserPermissions() {
      return this.userPermissions;
   }

   public void setUserPermissions(DownloadedItems var1) {
      this.userPermissions = var1;
   }

   public DownloadedItems getGroupPermissions() {
      return this.groupPermissions;
   }

   public void setGroupPermissions(DownloadedItems var1) {
      this.groupPermissions = var1;
   }

   public DownloadedItems getAppliedPlugins() {
      return this.appliedPlugins;
   }

   public void setAppliedPlugins(DownloadedItems var1) {
      this.appliedPlugins = var1;
   }

   public DownloadedItems getJtrFiles() {
      return this.jtrFiles;
   }

   public void setJtrFiles(DownloadedItems var1) {
      this.jtrFiles = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof DownloadDataHolder)) {
         return false;
      }

      DownloadDataHolder var2 = (DownloadDataHolder)var1;
      if (var1 == null) {
         return false;
      }

      if (this == var1) {
         return true;
      }

      if (this.__equalsCalc != null) {
         return this.__equalsCalc == var1;
      }

      this.__equalsCalc = var1;
      boolean var3 = (this.projects == null && var2.getProjects() == null || this.projects != null && this.projects.equals(var2.getProjects()))
         && (this.sources == null && var2.getSources() == null || this.sources != null && this.sources.equals(var2.getSources()))
         && (this.targets == null && var2.getTargets() == null || this.targets != null && this.targets.equals(var2.getTargets()))
         && (this.schedules == null && var2.getSchedules() == null || this.schedules != null && this.schedules.equals(var2.getSchedules()))
         && (this.emailMessages == null && var2.getEmailMessages() == null || this.emailMessages != null && this.emailMessages.equals(var2.getEmailMessages()))
         && (this.scripts == null && var2.getScripts() == null || this.scripts != null && this.scripts.equals(var2.getScripts()))
         && (
            this.textStructures == null && var2.getTextStructures() == null
               || this.textStructures != null && this.textStructures.equals(var2.getTextStructures())
         )
         && (
            this.documentDefinitions == null && var2.getDocumentDefinitions() == null
               || this.documentDefinitions != null && this.documentDefinitions.equals(var2.getDocumentDefinitions())
         )
         && (
            this.documentFieldValidations == null && var2.getDocumentFieldValidations() == null
               || this.documentFieldValidations != null && this.documentFieldValidations.equals(var2.getDocumentFieldValidations())
         )
         && (
            this.transformations == null && var2.getTransformations() == null
               || this.transformations != null && this.transformations.equals(var2.getTransformations())
         )
         && (
            this.transformationDefinitions == null && var2.getTransformationDefinitions() == null
               || this.transformationDefinitions != null && this.transformationDefinitions.equals(var2.getTransformationDefinitions())
         )
         && (
            this.transformationRelations == null && var2.getTransformationRelations() == null
               || this.transformationRelations != null && this.transformationRelations.equals(var2.getTransformationRelations())
         )
         && (
            this.transformationFilters == null && var2.getTransformationFilters() == null
               || this.transformationFilters != null && this.transformationFilters.equals(var2.getTransformationFilters())
         )
         && (
            this.transformationChainLinks == null && var2.getTransformationChainLinks() == null
               || this.transformationChainLinks != null && this.transformationChainLinks.equals(var2.getTransformationChainLinks())
         )
         && (this.dbFields == null && var2.getDbFields() == null || this.dbFields != null && this.dbFields.equals(var2.getDbFields()))
         && (this.dbTables == null && var2.getDbTables() == null || this.dbTables != null && this.dbTables.equals(var2.getDbTables()))
         && (this.sqlScripts == null && var2.getSqlScripts() == null || this.sqlScripts != null && this.sqlScripts.equals(var2.getSqlScripts()))
         && (
            this.ldapSearchParameters == null && var2.getLdapSearchParameters() == null
               || this.ldapSearchParameters != null && this.ldapSearchParameters.equals(var2.getLdapSearchParameters())
         )
         && (
            this.ldapStructureEntries == null && var2.getLdapStructureEntries() == null
               || this.ldapStructureEntries != null && this.ldapStructureEntries.equals(var2.getLdapStructureEntries())
         )
         && (
            this.webServiceMethods == null && var2.getWebServiceMethods() == null
               || this.webServiceMethods != null && this.webServiceMethods.equals(var2.getWebServiceMethods())
         )
         && (this.wsdls == null && var2.getWsdls() == null || this.wsdls != null && this.wsdls.equals(var2.getWsdls()))
         && (
            this.callRestrictions == null && var2.getCallRestrictions() == null
               || this.callRestrictions != null && this.callRestrictions.equals(var2.getCallRestrictions())
         )
         && (this.folders == null && var2.getFolders() == null || this.folders != null && this.folders.equals(var2.getFolders()))
         && (this.folderItems == null && var2.getFolderItems() == null || this.folderItems != null && this.folderItems.equals(var2.getFolderItems()))
         && (this.operations == null && var2.getOperations() == null || this.operations != null && this.operations.equals(var2.getOperations()))
         && (
            this.operationPipelineActivities == null && var2.getOperationPipelineActivities() == null
               || this.operationPipelineActivities != null && this.operationPipelineActivities.equals(var2.getOperationPipelineActivities())
         )
         && (
            this.operationTransformationTab == null && var2.getOperationTransformationTab() == null
               || this.operationTransformationTab != null && this.operationTransformationTab.equals(var2.getOperationTransformationTab())
         )
         && (
            this.siblingOperations == null && var2.getSiblingOperations() == null
               || this.siblingOperations != null && this.siblingOperations.equals(var2.getSiblingOperations())
         )
         && (
            this.operationGraphLayout == null && var2.getOperationGraphLayout() == null
               || this.operationGraphLayout != null && this.operationGraphLayout.equals(var2.getOperationGraphLayout())
         )
         && (this.httpEndpoints == null && var2.getHttpEndpoints() == null || this.httpEndpoints != null && this.httpEndpoints.equals(var2.getHttpEndpoints()))
         && (
            this.httpEndpointOperations == null && var2.getHttpEndpointOperations() == null
               || this.httpEndpointOperations != null && this.httpEndpointOperations.equals(var2.getHttpEndpointOperations())
         )
         && (
            this.salesforceConnectors == null && var2.getSalesforceConnectors() == null
               || this.salesforceConnectors != null && this.salesforceConnectors.equals(var2.getSalesforceConnectors())
         )
         && (
            this.salesforceQueries == null && var2.getSalesforceQueries() == null
               || this.salesforceQueries != null && this.salesforceQueries.equals(var2.getSalesforceQueries())
         )
         && (
            this.salesforceUpserts == null && var2.getSalesforceUpserts() == null
               || this.salesforceUpserts != null && this.salesforceUpserts.equals(var2.getSalesforceUpserts())
         )
         && (
            this.salesforceCreates == null && var2.getSalesforceCreates() == null
               || this.salesforceCreates != null && this.salesforceCreates.equals(var2.getSalesforceCreates())
         )
         && (
            this.salesforceUpdates == null && var2.getSalesforceUpdates() == null
               || this.salesforceUpdates != null && this.salesforceUpdates.equals(var2.getSalesforceUpdates())
         )
         && (
            this.salesforceDeletes == null && var2.getSalesforceDeletes() == null
               || this.salesforceDeletes != null && this.salesforceDeletes.equals(var2.getSalesforceDeletes())
         )
         && (
            this.salesforceBulkLoads == null && var2.getSalesforceBulkLoads() == null
               || this.salesforceBulkLoads != null && this.salesforceBulkLoads.equals(var2.getSalesforceBulkLoads())
         )
         && (this.sapEndpoints == null && var2.getSapEndpoints() == null || this.sapEndpoints != null && this.sapEndpoints.equals(var2.getSapEndpoints()))
         && (this.sapFunctions == null && var2.getSapFunctions() == null || this.sapFunctions != null && this.sapFunctions.equals(var2.getSapFunctions()))
         && (
            this.MSCrmEndpoints == null && var2.getMSCrmEndpoints() == null
               || this.MSCrmEndpoints != null && this.MSCrmEndpoints.equals(var2.getMSCrmEndpoints())
         )
         && (
            this.MSCrmFunctions == null && var2.getMSCrmFunctions() == null
               || this.MSCrmFunctions != null && this.MSCrmFunctions.equals(var2.getMSCrmFunctions())
         )
         && (this.MSAXEndpoints == null && var2.getMSAXEndpoints() == null || this.MSAXEndpoints != null && this.MSAXEndpoints.equals(var2.getMSAXEndpoints()))
         && (this.MSAXFunctions == null && var2.getMSAXFunctions() == null || this.MSAXFunctions != null && this.MSAXFunctions.equals(var2.getMSAXFunctions()))
         && (this.MSGPEndpoints == null && var2.getMSGPEndpoints() == null || this.MSGPEndpoints != null && this.MSGPEndpoints.equals(var2.getMSGPEndpoints()))
         && (this.MSGPFunctions == null && var2.getMSGPFunctions() == null || this.MSGPFunctions != null && this.MSGPFunctions.equals(var2.getMSGPFunctions()))
         && (this.ebsEndpoints == null && var2.getEbsEndpoints() == null || this.ebsEndpoints != null && this.ebsEndpoints.equals(var2.getEbsEndpoints()))
         && (this.ebsFunctions == null && var2.getEbsFunctions() == null || this.ebsFunctions != null && this.ebsFunctions.equals(var2.getEbsFunctions()))
         && (
            this.autoDeskEndpoints == null && var2.getAutoDeskEndpoints() == null
               || this.autoDeskEndpoints != null && this.autoDeskEndpoints.equals(var2.getAutoDeskEndpoints())
         )
         && (
            this.autoDeskFunctions == null && var2.getAutoDeskFunctions() == null
               || this.autoDeskFunctions != null && this.autoDeskFunctions.equals(var2.getAutoDeskFunctions())
         )
         && (
            this.netSuiteEndpoints == null && var2.getNetSuiteEndpoints() == null
               || this.netSuiteEndpoints != null && this.netSuiteEndpoints.equals(var2.getNetSuiteEndpoints())
         )
         && (
            this.netSuiteFunctions == null && var2.getNetSuiteFunctions() == null
               || this.netSuiteFunctions != null && this.netSuiteFunctions.equals(var2.getNetSuiteFunctions())
         )
         && (
            this.userPermissions == null && var2.getUserPermissions() == null
               || this.userPermissions != null && this.userPermissions.equals(var2.getUserPermissions())
         )
         && (
            this.groupPermissions == null && var2.getGroupPermissions() == null
               || this.groupPermissions != null && this.groupPermissions.equals(var2.getGroupPermissions())
         )
         && (
            this.appliedPlugins == null && var2.getAppliedPlugins() == null
               || this.appliedPlugins != null && this.appliedPlugins.equals(var2.getAppliedPlugins())
         )
         && (this.jtrFiles == null && var2.getJtrFiles() == null || this.jtrFiles != null && this.jtrFiles.equals(var2.getJtrFiles()));
      this.__equalsCalc = null;
      return var3;
   }

   @Override
   public synchronized int hashCode() {
      if (this.__hashCodeCalc) {
         return 0;
      }

      this.__hashCodeCalc = true;
      int var1 = 1;
      if (this.getProjects() != null) {
         var1 += this.getProjects().hashCode();
      }

      if (this.getSources() != null) {
         var1 += this.getSources().hashCode();
      }

      if (this.getTargets() != null) {
         var1 += this.getTargets().hashCode();
      }

      if (this.getSchedules() != null) {
         var1 += this.getSchedules().hashCode();
      }

      if (this.getEmailMessages() != null) {
         var1 += this.getEmailMessages().hashCode();
      }

      if (this.getScripts() != null) {
         var1 += this.getScripts().hashCode();
      }

      if (this.getTextStructures() != null) {
         var1 += this.getTextStructures().hashCode();
      }

      if (this.getDocumentDefinitions() != null) {
         var1 += this.getDocumentDefinitions().hashCode();
      }

      if (this.getDocumentFieldValidations() != null) {
         var1 += this.getDocumentFieldValidations().hashCode();
      }

      if (this.getTransformations() != null) {
         var1 += this.getTransformations().hashCode();
      }

      if (this.getTransformationDefinitions() != null) {
         var1 += this.getTransformationDefinitions().hashCode();
      }

      if (this.getTransformationRelations() != null) {
         var1 += this.getTransformationRelations().hashCode();
      }

      if (this.getTransformationFilters() != null) {
         var1 += this.getTransformationFilters().hashCode();
      }

      if (this.getTransformationChainLinks() != null) {
         var1 += this.getTransformationChainLinks().hashCode();
      }

      if (this.getDbFields() != null) {
         var1 += this.getDbFields().hashCode();
      }

      if (this.getDbTables() != null) {
         var1 += this.getDbTables().hashCode();
      }

      if (this.getSqlScripts() != null) {
         var1 += this.getSqlScripts().hashCode();
      }

      if (this.getLdapSearchParameters() != null) {
         var1 += this.getLdapSearchParameters().hashCode();
      }

      if (this.getLdapStructureEntries() != null) {
         var1 += this.getLdapStructureEntries().hashCode();
      }

      if (this.getWebServiceMethods() != null) {
         var1 += this.getWebServiceMethods().hashCode();
      }

      if (this.getWsdls() != null) {
         var1 += this.getWsdls().hashCode();
      }

      if (this.getCallRestrictions() != null) {
         var1 += this.getCallRestrictions().hashCode();
      }

      if (this.getFolders() != null) {
         var1 += this.getFolders().hashCode();
      }

      if (this.getFolderItems() != null) {
         var1 += this.getFolderItems().hashCode();
      }

      if (this.getOperations() != null) {
         var1 += this.getOperations().hashCode();
      }

      if (this.getOperationPipelineActivities() != null) {
         var1 += this.getOperationPipelineActivities().hashCode();
      }

      if (this.getOperationTransformationTab() != null) {
         var1 += this.getOperationTransformationTab().hashCode();
      }

      if (this.getSiblingOperations() != null) {
         var1 += this.getSiblingOperations().hashCode();
      }

      if (this.getOperationGraphLayout() != null) {
         var1 += this.getOperationGraphLayout().hashCode();
      }

      if (this.getHttpEndpoints() != null) {
         var1 += this.getHttpEndpoints().hashCode();
      }

      if (this.getHttpEndpointOperations() != null) {
         var1 += this.getHttpEndpointOperations().hashCode();
      }

      if (this.getSalesforceConnectors() != null) {
         var1 += this.getSalesforceConnectors().hashCode();
      }

      if (this.getSalesforceQueries() != null) {
         var1 += this.getSalesforceQueries().hashCode();
      }

      if (this.getSalesforceUpserts() != null) {
         var1 += this.getSalesforceUpserts().hashCode();
      }

      if (this.getSalesforceCreates() != null) {
         var1 += this.getSalesforceCreates().hashCode();
      }

      if (this.getSalesforceUpdates() != null) {
         var1 += this.getSalesforceUpdates().hashCode();
      }

      if (this.getSalesforceDeletes() != null) {
         var1 += this.getSalesforceDeletes().hashCode();
      }

      if (this.getSalesforceBulkLoads() != null) {
         var1 += this.getSalesforceBulkLoads().hashCode();
      }

      if (this.getSapEndpoints() != null) {
         var1 += this.getSapEndpoints().hashCode();
      }

      if (this.getSapFunctions() != null) {
         var1 += this.getSapFunctions().hashCode();
      }

      if (this.getMSCrmEndpoints() != null) {
         var1 += this.getMSCrmEndpoints().hashCode();
      }

      if (this.getMSCrmFunctions() != null) {
         var1 += this.getMSCrmFunctions().hashCode();
      }

      if (this.getMSAXEndpoints() != null) {
         var1 += this.getMSAXEndpoints().hashCode();
      }

      if (this.getMSAXFunctions() != null) {
         var1 += this.getMSAXFunctions().hashCode();
      }

      if (this.getMSGPEndpoints() != null) {
         var1 += this.getMSGPEndpoints().hashCode();
      }

      if (this.getMSGPFunctions() != null) {
         var1 += this.getMSGPFunctions().hashCode();
      }

      if (this.getEbsEndpoints() != null) {
         var1 += this.getEbsEndpoints().hashCode();
      }

      if (this.getEbsFunctions() != null) {
         var1 += this.getEbsFunctions().hashCode();
      }

      if (this.getAutoDeskEndpoints() != null) {
         var1 += this.getAutoDeskEndpoints().hashCode();
      }

      if (this.getAutoDeskFunctions() != null) {
         var1 += this.getAutoDeskFunctions().hashCode();
      }

      if (this.getNetSuiteEndpoints() != null) {
         var1 += this.getNetSuiteEndpoints().hashCode();
      }

      if (this.getNetSuiteFunctions() != null) {
         var1 += this.getNetSuiteFunctions().hashCode();
      }

      if (this.getUserPermissions() != null) {
         var1 += this.getUserPermissions().hashCode();
      }

      if (this.getGroupPermissions() != null) {
         var1 += this.getGroupPermissions().hashCode();
      }

      if (this.getAppliedPlugins() != null) {
         var1 += this.getAppliedPlugins().hashCode();
      }

      if (this.getJtrFiles() != null) {
         var1 += this.getJtrFiles().hashCode();
      }

      this.__hashCodeCalc = false;
      return var1;
   }

   public static TypeDesc getTypeDesc() {
      return typeDesc;
   }

   public static Serializer getSerializer(String var0, Class var1, QName var2) {
      return new BeanSerializer(var1, var2, typeDesc);
   }

   public static Deserializer getDeserializer(String var0, Class var1, QName var2) {
      return new BeanDeserializer(var1, var2, typeDesc);
   }

   static {
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadDataHolder")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("projects");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Projects"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sources");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Sources"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targets");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Targets"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("schedules");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Schedules"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("emailMessages");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "EmailMessages"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("scripts");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Scripts"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("textStructures");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "TextStructures")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("documentDefinitions");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DocumentDefinitions")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("documentFieldValidations");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DocumentFieldValidations")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformations");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Transformations")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationDefinitions");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "TransformationDefinitions")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationRelations");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "TransformationRelations")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationFilters");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "TransformationFilters")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationChainLinks");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "TransformationChainLinks")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dbFields");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DbFields"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dbTables");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DbTables"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sqlScripts");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SqlScripts"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ldapSearchParameters");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "LdapSearchParameters")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ldapStructureEntries");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "LdapStructureEntries")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("webServiceMethods");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "WebServiceMethods")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("wsdls");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Wsdls"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("callRestrictions");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "CallRestrictions")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("folders");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Folders"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("folderItems");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "FolderItems"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operations");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Operations"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationPipelineActivities");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "OperationPipelineActivities")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationTransformationTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "OperationTransformationTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("siblingOperations");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SiblingOperations")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationGraphLayout");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "OperationGraphLayout")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("httpEndpoints");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "HttpEndpoints"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("httpEndpointOperations");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "HttpEndpointOperations")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceConnectors");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceConnectors")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceQueries");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceQueries")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceUpserts");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceUpserts")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceCreates");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceCreates")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceUpdates");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceUpdates")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceDeletes");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceDeletes")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceBulkLoads");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceBulkLoads")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sapEndpoints");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SapEndpoints"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sapFunctions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SapFunctions"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSCrmEndpoints");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "MSCrmEndpoints")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSCrmFunctions");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "MSCrmFunctions")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSAXEndpoints");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "MSAXEndpoints"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSAXFunctions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "MSAXFunctions"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSGPEndpoints");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "MSGPEndpoints"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSGPFunctions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "MSGPFunctions"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ebsEndpoints");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "EbsEndpoints"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ebsFunctions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "EbsFunctions"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("autoDeskEndpoints");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "AutoDeskEndpoints")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("autoDeskFunctions");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "AutoDeskFunctions")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("netSuiteEndpoints");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "NetSuiteEndpoints")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("netSuiteFunctions");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "NetSuiteFunctions")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("userPermissions");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "UserPermissions")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("groupPermissions");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "GroupPermissions")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("appliedPlugins");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "AppliedPlugins")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("jtrFiles");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "JtrFiles"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DownloadedItems")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
