package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class InterchangeDataHolder implements Serializable {
   private InterchangeDataTable callRestrictionsTab;
   private InterchangeDataTable dataElementsTab;
   private InterchangeDataTable DBFieldsTab;
   private InterchangeDataTable DBTablesTab;
   private InterchangeDataTable document_DefinitionsTab;
   private InterchangeDataTable documentFieldValidationTab;
   private InterchangeDataTable documentsTab;
   private InterchangeDataTable folderItemTab;
   private InterchangeDataTable foldersTab;
   private InterchangeDataTable httpEndpointsTab;
   private InterchangeDataTable httpEndpointOperationsTab;
   private InterchangeDataTable interchangeProjectsTab;
   private InterchangeDataTable jmsLocationPropertiesTab;
   private InterchangeDataTable jmsMessagesTab;
   private InterchangeDataTable messagesTab;
   private InterchangeDataTable operationDiff_Details;
   private InterchangeDataTable operationGraphLayoutTab;
   private InterchangeDataTable operationsTab;
   private InterchangeDataTable operationTransformationTab;
   private InterchangeDataTable salesforceConnectorsTab;
   private InterchangeDataTable salesforceQueriesTab;
   private InterchangeDataTable salesforceUpsertsTab;
   private InterchangeDataTable salesforceCreatesTab;
   private InterchangeDataTable salesforceUpdatesTab;
   private InterchangeDataTable salesforceDeletesTab;
   private InterchangeDataTable salesforceBulkLoadsTab;
   private InterchangeDataTable sapEndpointsTab;
   private InterchangeDataTable sapFunctionsTab;
   private InterchangeDataTable MSCrmEndpointsTab;
   private InterchangeDataTable MSCrmFunctionsTab;
   private InterchangeDataTable MSAXEndpointsTab;
   private InterchangeDataTable MSAXFunctionsTab;
   private InterchangeDataTable MSGPEndpointsTab;
   private InterchangeDataTable MSGPFunctionsTab;
   private InterchangeDataTable ebsEndpointsTab;
   private InterchangeDataTable ebsFunctionsTab;
   private InterchangeDataTable autoDeskEndpointsTab;
   private InterchangeDataTable autoDeskFunctionsTab;
   private InterchangeDataTable netSuiteEndpointsTab;
   private InterchangeDataTable netSuiteFunctionsTab;
   private InterchangeDataTable schedulesTab;
   private InterchangeDataTable scriptsTab;
   private InterchangeDataTable sourcesTab;
   private InterchangeDataTable sqlScriptTab;
   private InterchangeDataTable targetsTab;
   private InterchangeDataTable tran_RelationsTab;
   private InterchangeDataTable trans_FilterTab;
   private InterchangeDataTable transformation_DefinitionsTab;
   private InterchangeDataTable transformationChainLinksTab;
   private InterchangeDataTable transformationsTab_New;
   private InterchangeDataTable webServicesTab;
   private InterchangeDataTable commonSourceOperationsTab;
   private InterchangeDataTable ldapFiltersTab;
   private InterchangeDataTable ldapStructureEntriesTab;
   private InterchangeDataTable pipelinePluginEntityAssociations;
   private InterchangeDataTable entityProperties;
   private InterchangeDataTable operationPipelines;
   private PermissionsHolder permissions;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(InterchangeDataHolder.class, true);

   public InterchangeDataHolder() {
   }

   public InterchangeDataHolder(
      InterchangeDataTable var1,
      InterchangeDataTable var2,
      InterchangeDataTable var3,
      InterchangeDataTable var4,
      InterchangeDataTable var5,
      InterchangeDataTable var6,
      InterchangeDataTable var7,
      InterchangeDataTable var8,
      InterchangeDataTable var9,
      InterchangeDataTable var10,
      InterchangeDataTable var11,
      InterchangeDataTable var12,
      InterchangeDataTable var13,
      InterchangeDataTable var14,
      InterchangeDataTable var15,
      InterchangeDataTable var16,
      InterchangeDataTable var17,
      InterchangeDataTable var18,
      InterchangeDataTable var19,
      InterchangeDataTable var20,
      InterchangeDataTable var21,
      InterchangeDataTable var22,
      InterchangeDataTable var23,
      InterchangeDataTable var24,
      InterchangeDataTable var25,
      InterchangeDataTable var26,
      InterchangeDataTable var27,
      InterchangeDataTable var28,
      InterchangeDataTable var29,
      InterchangeDataTable var30,
      InterchangeDataTable var31,
      InterchangeDataTable var32,
      InterchangeDataTable var33,
      InterchangeDataTable var34,
      InterchangeDataTable var35,
      InterchangeDataTable var36,
      InterchangeDataTable var37,
      InterchangeDataTable var38,
      InterchangeDataTable var39,
      InterchangeDataTable var40,
      InterchangeDataTable var41,
      InterchangeDataTable var42,
      InterchangeDataTable var43,
      InterchangeDataTable var44,
      InterchangeDataTable var45,
      InterchangeDataTable var46,
      InterchangeDataTable var47,
      InterchangeDataTable var48,
      InterchangeDataTable var49,
      InterchangeDataTable var50,
      InterchangeDataTable var51,
      InterchangeDataTable var52,
      InterchangeDataTable var53,
      InterchangeDataTable var54,
      InterchangeDataTable var55,
      InterchangeDataTable var56,
      InterchangeDataTable var57,
      PermissionsHolder var58
   ) {
      this.callRestrictionsTab = var1;
      this.dataElementsTab = var2;
      this.DBFieldsTab = var3;
      this.DBTablesTab = var4;
      this.document_DefinitionsTab = var5;
      this.documentFieldValidationTab = var6;
      this.documentsTab = var7;
      this.folderItemTab = var8;
      this.foldersTab = var9;
      this.httpEndpointsTab = var10;
      this.httpEndpointOperationsTab = var11;
      this.interchangeProjectsTab = var12;
      this.jmsLocationPropertiesTab = var13;
      this.jmsMessagesTab = var14;
      this.messagesTab = var15;
      this.operationDiff_Details = var16;
      this.operationGraphLayoutTab = var17;
      this.operationsTab = var18;
      this.operationTransformationTab = var19;
      this.salesforceConnectorsTab = var20;
      this.salesforceQueriesTab = var21;
      this.salesforceUpsertsTab = var22;
      this.salesforceCreatesTab = var23;
      this.salesforceUpdatesTab = var24;
      this.salesforceDeletesTab = var25;
      this.salesforceBulkLoadsTab = var26;
      this.sapEndpointsTab = var27;
      this.sapFunctionsTab = var28;
      this.MSCrmEndpointsTab = var29;
      this.MSCrmFunctionsTab = var30;
      this.MSAXEndpointsTab = var31;
      this.MSAXFunctionsTab = var32;
      this.MSGPEndpointsTab = var33;
      this.MSGPFunctionsTab = var34;
      this.ebsEndpointsTab = var35;
      this.ebsFunctionsTab = var36;
      this.autoDeskEndpointsTab = var37;
      this.autoDeskFunctionsTab = var38;
      this.netSuiteEndpointsTab = var39;
      this.netSuiteFunctionsTab = var40;
      this.schedulesTab = var41;
      this.scriptsTab = var42;
      this.sourcesTab = var43;
      this.sqlScriptTab = var44;
      this.targetsTab = var45;
      this.tran_RelationsTab = var46;
      this.trans_FilterTab = var47;
      this.transformation_DefinitionsTab = var48;
      this.transformationChainLinksTab = var49;
      this.transformationsTab_New = var50;
      this.webServicesTab = var51;
      this.commonSourceOperationsTab = var52;
      this.ldapFiltersTab = var53;
      this.ldapStructureEntriesTab = var54;
      this.pipelinePluginEntityAssociations = var55;
      this.entityProperties = var56;
      this.operationPipelines = var57;
      this.permissions = var58;
   }

   public InterchangeDataTable getCallRestrictionsTab() {
      return this.callRestrictionsTab;
   }

   public void setCallRestrictionsTab(InterchangeDataTable var1) {
      this.callRestrictionsTab = var1;
   }

   public InterchangeDataTable getDataElementsTab() {
      return this.dataElementsTab;
   }

   public void setDataElementsTab(InterchangeDataTable var1) {
      this.dataElementsTab = var1;
   }

   public InterchangeDataTable getDBFieldsTab() {
      return this.DBFieldsTab;
   }

   public void setDBFieldsTab(InterchangeDataTable var1) {
      this.DBFieldsTab = var1;
   }

   public InterchangeDataTable getDBTablesTab() {
      return this.DBTablesTab;
   }

   public void setDBTablesTab(InterchangeDataTable var1) {
      this.DBTablesTab = var1;
   }

   public InterchangeDataTable getDocument_DefinitionsTab() {
      return this.document_DefinitionsTab;
   }

   public void setDocument_DefinitionsTab(InterchangeDataTable var1) {
      this.document_DefinitionsTab = var1;
   }

   public InterchangeDataTable getDocumentFieldValidationTab() {
      return this.documentFieldValidationTab;
   }

   public void setDocumentFieldValidationTab(InterchangeDataTable var1) {
      this.documentFieldValidationTab = var1;
   }

   public InterchangeDataTable getDocumentsTab() {
      return this.documentsTab;
   }

   public void setDocumentsTab(InterchangeDataTable var1) {
      this.documentsTab = var1;
   }

   public InterchangeDataTable getFolderItemTab() {
      return this.folderItemTab;
   }

   public void setFolderItemTab(InterchangeDataTable var1) {
      this.folderItemTab = var1;
   }

   public InterchangeDataTable getFoldersTab() {
      return this.foldersTab;
   }

   public void setFoldersTab(InterchangeDataTable var1) {
      this.foldersTab = var1;
   }

   public InterchangeDataTable getHttpEndpointsTab() {
      return this.httpEndpointsTab;
   }

   public void setHttpEndpointsTab(InterchangeDataTable var1) {
      this.httpEndpointsTab = var1;
   }

   public InterchangeDataTable getHttpEndpointOperationsTab() {
      return this.httpEndpointOperationsTab;
   }

   public void setHttpEndpointOperationsTab(InterchangeDataTable var1) {
      this.httpEndpointOperationsTab = var1;
   }

   public InterchangeDataTable getInterchangeProjectsTab() {
      return this.interchangeProjectsTab;
   }

   public void setInterchangeProjectsTab(InterchangeDataTable var1) {
      this.interchangeProjectsTab = var1;
   }

   public InterchangeDataTable getJmsLocationPropertiesTab() {
      return this.jmsLocationPropertiesTab;
   }

   public void setJmsLocationPropertiesTab(InterchangeDataTable var1) {
      this.jmsLocationPropertiesTab = var1;
   }

   public InterchangeDataTable getJmsMessagesTab() {
      return this.jmsMessagesTab;
   }

   public void setJmsMessagesTab(InterchangeDataTable var1) {
      this.jmsMessagesTab = var1;
   }

   public InterchangeDataTable getMessagesTab() {
      return this.messagesTab;
   }

   public void setMessagesTab(InterchangeDataTable var1) {
      this.messagesTab = var1;
   }

   public InterchangeDataTable getOperationDiff_Details() {
      return this.operationDiff_Details;
   }

   public void setOperationDiff_Details(InterchangeDataTable var1) {
      this.operationDiff_Details = var1;
   }

   public InterchangeDataTable getOperationGraphLayoutTab() {
      return this.operationGraphLayoutTab;
   }

   public void setOperationGraphLayoutTab(InterchangeDataTable var1) {
      this.operationGraphLayoutTab = var1;
   }

   public InterchangeDataTable getOperationsTab() {
      return this.operationsTab;
   }

   public void setOperationsTab(InterchangeDataTable var1) {
      this.operationsTab = var1;
   }

   public InterchangeDataTable getOperationTransformationTab() {
      return this.operationTransformationTab;
   }

   public void setOperationTransformationTab(InterchangeDataTable var1) {
      this.operationTransformationTab = var1;
   }

   public InterchangeDataTable getSalesforceConnectorsTab() {
      return this.salesforceConnectorsTab;
   }

   public void setSalesforceConnectorsTab(InterchangeDataTable var1) {
      this.salesforceConnectorsTab = var1;
   }

   public InterchangeDataTable getSalesforceQueriesTab() {
      return this.salesforceQueriesTab;
   }

   public void setSalesforceQueriesTab(InterchangeDataTable var1) {
      this.salesforceQueriesTab = var1;
   }

   public InterchangeDataTable getSalesforceUpsertsTab() {
      return this.salesforceUpsertsTab;
   }

   public void setSalesforceUpsertsTab(InterchangeDataTable var1) {
      this.salesforceUpsertsTab = var1;
   }

   public InterchangeDataTable getSalesforceCreatesTab() {
      return this.salesforceCreatesTab;
   }

   public void setSalesforceCreatesTab(InterchangeDataTable var1) {
      this.salesforceCreatesTab = var1;
   }

   public InterchangeDataTable getSalesforceUpdatesTab() {
      return this.salesforceUpdatesTab;
   }

   public void setSalesforceUpdatesTab(InterchangeDataTable var1) {
      this.salesforceUpdatesTab = var1;
   }

   public InterchangeDataTable getSalesforceDeletesTab() {
      return this.salesforceDeletesTab;
   }

   public void setSalesforceDeletesTab(InterchangeDataTable var1) {
      this.salesforceDeletesTab = var1;
   }

   public InterchangeDataTable getSalesforceBulkLoadsTab() {
      return this.salesforceBulkLoadsTab;
   }

   public void setSalesforceBulkLoadsTab(InterchangeDataTable var1) {
      this.salesforceBulkLoadsTab = var1;
   }

   public InterchangeDataTable getSapEndpointsTab() {
      return this.sapEndpointsTab;
   }

   public void setSapEndpointsTab(InterchangeDataTable var1) {
      this.sapEndpointsTab = var1;
   }

   public InterchangeDataTable getSapFunctionsTab() {
      return this.sapFunctionsTab;
   }

   public void setSapFunctionsTab(InterchangeDataTable var1) {
      this.sapFunctionsTab = var1;
   }

   public InterchangeDataTable getMSCrmEndpointsTab() {
      return this.MSCrmEndpointsTab;
   }

   public void setMSCrmEndpointsTab(InterchangeDataTable var1) {
      this.MSCrmEndpointsTab = var1;
   }

   public InterchangeDataTable getMSCrmFunctionsTab() {
      return this.MSCrmFunctionsTab;
   }

   public void setMSCrmFunctionsTab(InterchangeDataTable var1) {
      this.MSCrmFunctionsTab = var1;
   }

   public InterchangeDataTable getMSAXEndpointsTab() {
      return this.MSAXEndpointsTab;
   }

   public void setMSAXEndpointsTab(InterchangeDataTable var1) {
      this.MSAXEndpointsTab = var1;
   }

   public InterchangeDataTable getMSAXFunctionsTab() {
      return this.MSAXFunctionsTab;
   }

   public void setMSAXFunctionsTab(InterchangeDataTable var1) {
      this.MSAXFunctionsTab = var1;
   }

   public InterchangeDataTable getMSGPEndpointsTab() {
      return this.MSGPEndpointsTab;
   }

   public void setMSGPEndpointsTab(InterchangeDataTable var1) {
      this.MSGPEndpointsTab = var1;
   }

   public InterchangeDataTable getMSGPFunctionsTab() {
      return this.MSGPFunctionsTab;
   }

   public void setMSGPFunctionsTab(InterchangeDataTable var1) {
      this.MSGPFunctionsTab = var1;
   }

   public InterchangeDataTable getEbsEndpointsTab() {
      return this.ebsEndpointsTab;
   }

   public void setEbsEndpointsTab(InterchangeDataTable var1) {
      this.ebsEndpointsTab = var1;
   }

   public InterchangeDataTable getEbsFunctionsTab() {
      return this.ebsFunctionsTab;
   }

   public void setEbsFunctionsTab(InterchangeDataTable var1) {
      this.ebsFunctionsTab = var1;
   }

   public InterchangeDataTable getAutoDeskEndpointsTab() {
      return this.autoDeskEndpointsTab;
   }

   public void setAutoDeskEndpointsTab(InterchangeDataTable var1) {
      this.autoDeskEndpointsTab = var1;
   }

   public InterchangeDataTable getAutoDeskFunctionsTab() {
      return this.autoDeskFunctionsTab;
   }

   public void setAutoDeskFunctionsTab(InterchangeDataTable var1) {
      this.autoDeskFunctionsTab = var1;
   }

   public InterchangeDataTable getNetSuiteEndpointsTab() {
      return this.netSuiteEndpointsTab;
   }

   public void setNetSuiteEndpointsTab(InterchangeDataTable var1) {
      this.netSuiteEndpointsTab = var1;
   }

   public InterchangeDataTable getNetSuiteFunctionsTab() {
      return this.netSuiteFunctionsTab;
   }

   public void setNetSuiteFunctionsTab(InterchangeDataTable var1) {
      this.netSuiteFunctionsTab = var1;
   }

   public InterchangeDataTable getSchedulesTab() {
      return this.schedulesTab;
   }

   public void setSchedulesTab(InterchangeDataTable var1) {
      this.schedulesTab = var1;
   }

   public InterchangeDataTable getScriptsTab() {
      return this.scriptsTab;
   }

   public void setScriptsTab(InterchangeDataTable var1) {
      this.scriptsTab = var1;
   }

   public InterchangeDataTable getSourcesTab() {
      return this.sourcesTab;
   }

   public void setSourcesTab(InterchangeDataTable var1) {
      this.sourcesTab = var1;
   }

   public InterchangeDataTable getSqlScriptTab() {
      return this.sqlScriptTab;
   }

   public void setSqlScriptTab(InterchangeDataTable var1) {
      this.sqlScriptTab = var1;
   }

   public InterchangeDataTable getTargetsTab() {
      return this.targetsTab;
   }

   public void setTargetsTab(InterchangeDataTable var1) {
      this.targetsTab = var1;
   }

   public InterchangeDataTable getTran_RelationsTab() {
      return this.tran_RelationsTab;
   }

   public void setTran_RelationsTab(InterchangeDataTable var1) {
      this.tran_RelationsTab = var1;
   }

   public InterchangeDataTable getTrans_FilterTab() {
      return this.trans_FilterTab;
   }

   public void setTrans_FilterTab(InterchangeDataTable var1) {
      this.trans_FilterTab = var1;
   }

   public InterchangeDataTable getTransformation_DefinitionsTab() {
      return this.transformation_DefinitionsTab;
   }

   public void setTransformation_DefinitionsTab(InterchangeDataTable var1) {
      this.transformation_DefinitionsTab = var1;
   }

   public InterchangeDataTable getTransformationChainLinksTab() {
      return this.transformationChainLinksTab;
   }

   public void setTransformationChainLinksTab(InterchangeDataTable var1) {
      this.transformationChainLinksTab = var1;
   }

   public InterchangeDataTable getTransformationsTab_New() {
      return this.transformationsTab_New;
   }

   public void setTransformationsTab_New(InterchangeDataTable var1) {
      this.transformationsTab_New = var1;
   }

   public InterchangeDataTable getWebServicesTab() {
      return this.webServicesTab;
   }

   public void setWebServicesTab(InterchangeDataTable var1) {
      this.webServicesTab = var1;
   }

   public InterchangeDataTable getCommonSourceOperationsTab() {
      return this.commonSourceOperationsTab;
   }

   public void setCommonSourceOperationsTab(InterchangeDataTable var1) {
      this.commonSourceOperationsTab = var1;
   }

   public InterchangeDataTable getLdapFiltersTab() {
      return this.ldapFiltersTab;
   }

   public void setLdapFiltersTab(InterchangeDataTable var1) {
      this.ldapFiltersTab = var1;
   }

   public InterchangeDataTable getLdapStructureEntriesTab() {
      return this.ldapStructureEntriesTab;
   }

   public void setLdapStructureEntriesTab(InterchangeDataTable var1) {
      this.ldapStructureEntriesTab = var1;
   }

   public InterchangeDataTable getPipelinePluginEntityAssociations() {
      return this.pipelinePluginEntityAssociations;
   }

   public void setPipelinePluginEntityAssociations(InterchangeDataTable var1) {
      this.pipelinePluginEntityAssociations = var1;
   }

   public InterchangeDataTable getEntityProperties() {
      return this.entityProperties;
   }

   public void setEntityProperties(InterchangeDataTable var1) {
      this.entityProperties = var1;
   }

   public InterchangeDataTable getOperationPipelines() {
      return this.operationPipelines;
   }

   public void setOperationPipelines(InterchangeDataTable var1) {
      this.operationPipelines = var1;
   }

   public PermissionsHolder getPermissions() {
      return this.permissions;
   }

   public void setPermissions(PermissionsHolder var1) {
      this.permissions = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof InterchangeDataHolder)) {
         return false;
      }

      InterchangeDataHolder var2 = (InterchangeDataHolder)var1;
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
      boolean var3 = (
            this.callRestrictionsTab == null && var2.getCallRestrictionsTab() == null
               || this.callRestrictionsTab != null && this.callRestrictionsTab.equals(var2.getCallRestrictionsTab())
         )
         && (
            this.dataElementsTab == null && var2.getDataElementsTab() == null
               || this.dataElementsTab != null && this.dataElementsTab.equals(var2.getDataElementsTab())
         )
         && (this.DBFieldsTab == null && var2.getDBFieldsTab() == null || this.DBFieldsTab != null && this.DBFieldsTab.equals(var2.getDBFieldsTab()))
         && (this.DBTablesTab == null && var2.getDBTablesTab() == null || this.DBTablesTab != null && this.DBTablesTab.equals(var2.getDBTablesTab()))
         && (
            this.document_DefinitionsTab == null && var2.getDocument_DefinitionsTab() == null
               || this.document_DefinitionsTab != null && this.document_DefinitionsTab.equals(var2.getDocument_DefinitionsTab())
         )
         && (
            this.documentFieldValidationTab == null && var2.getDocumentFieldValidationTab() == null
               || this.documentFieldValidationTab != null && this.documentFieldValidationTab.equals(var2.getDocumentFieldValidationTab())
         )
         && (this.documentsTab == null && var2.getDocumentsTab() == null || this.documentsTab != null && this.documentsTab.equals(var2.getDocumentsTab()))
         && (this.folderItemTab == null && var2.getFolderItemTab() == null || this.folderItemTab != null && this.folderItemTab.equals(var2.getFolderItemTab()))
         && (this.foldersTab == null && var2.getFoldersTab() == null || this.foldersTab != null && this.foldersTab.equals(var2.getFoldersTab()))
         && (
            this.httpEndpointsTab == null && var2.getHttpEndpointsTab() == null
               || this.httpEndpointsTab != null && this.httpEndpointsTab.equals(var2.getHttpEndpointsTab())
         )
         && (
            this.httpEndpointOperationsTab == null && var2.getHttpEndpointOperationsTab() == null
               || this.httpEndpointOperationsTab != null && this.httpEndpointOperationsTab.equals(var2.getHttpEndpointOperationsTab())
         )
         && (
            this.interchangeProjectsTab == null && var2.getInterchangeProjectsTab() == null
               || this.interchangeProjectsTab != null && this.interchangeProjectsTab.equals(var2.getInterchangeProjectsTab())
         )
         && (
            this.jmsLocationPropertiesTab == null && var2.getJmsLocationPropertiesTab() == null
               || this.jmsLocationPropertiesTab != null && this.jmsLocationPropertiesTab.equals(var2.getJmsLocationPropertiesTab())
         )
         && (
            this.jmsMessagesTab == null && var2.getJmsMessagesTab() == null
               || this.jmsMessagesTab != null && this.jmsMessagesTab.equals(var2.getJmsMessagesTab())
         )
         && (this.messagesTab == null && var2.getMessagesTab() == null || this.messagesTab != null && this.messagesTab.equals(var2.getMessagesTab()))
         && (
            this.operationDiff_Details == null && var2.getOperationDiff_Details() == null
               || this.operationDiff_Details != null && this.operationDiff_Details.equals(var2.getOperationDiff_Details())
         )
         && (
            this.operationGraphLayoutTab == null && var2.getOperationGraphLayoutTab() == null
               || this.operationGraphLayoutTab != null && this.operationGraphLayoutTab.equals(var2.getOperationGraphLayoutTab())
         )
         && (this.operationsTab == null && var2.getOperationsTab() == null || this.operationsTab != null && this.operationsTab.equals(var2.getOperationsTab()))
         && (
            this.operationTransformationTab == null && var2.getOperationTransformationTab() == null
               || this.operationTransformationTab != null && this.operationTransformationTab.equals(var2.getOperationTransformationTab())
         )
         && (
            this.salesforceConnectorsTab == null && var2.getSalesforceConnectorsTab() == null
               || this.salesforceConnectorsTab != null && this.salesforceConnectorsTab.equals(var2.getSalesforceConnectorsTab())
         )
         && (
            this.salesforceQueriesTab == null && var2.getSalesforceQueriesTab() == null
               || this.salesforceQueriesTab != null && this.salesforceQueriesTab.equals(var2.getSalesforceQueriesTab())
         )
         && (
            this.salesforceUpsertsTab == null && var2.getSalesforceUpsertsTab() == null
               || this.salesforceUpsertsTab != null && this.salesforceUpsertsTab.equals(var2.getSalesforceUpsertsTab())
         )
         && (
            this.salesforceCreatesTab == null && var2.getSalesforceCreatesTab() == null
               || this.salesforceCreatesTab != null && this.salesforceCreatesTab.equals(var2.getSalesforceCreatesTab())
         )
         && (
            this.salesforceUpdatesTab == null && var2.getSalesforceUpdatesTab() == null
               || this.salesforceUpdatesTab != null && this.salesforceUpdatesTab.equals(var2.getSalesforceUpdatesTab())
         )
         && (
            this.salesforceDeletesTab == null && var2.getSalesforceDeletesTab() == null
               || this.salesforceDeletesTab != null && this.salesforceDeletesTab.equals(var2.getSalesforceDeletesTab())
         )
         && (
            this.salesforceBulkLoadsTab == null && var2.getSalesforceBulkLoadsTab() == null
               || this.salesforceBulkLoadsTab != null && this.salesforceBulkLoadsTab.equals(var2.getSalesforceBulkLoadsTab())
         )
         && (
            this.sapEndpointsTab == null && var2.getSapEndpointsTab() == null
               || this.sapEndpointsTab != null && this.sapEndpointsTab.equals(var2.getSapEndpointsTab())
         )
         && (
            this.sapFunctionsTab == null && var2.getSapFunctionsTab() == null
               || this.sapFunctionsTab != null && this.sapFunctionsTab.equals(var2.getSapFunctionsTab())
         )
         && (
            this.MSCrmEndpointsTab == null && var2.getMSCrmEndpointsTab() == null
               || this.MSCrmEndpointsTab != null && this.MSCrmEndpointsTab.equals(var2.getMSCrmEndpointsTab())
         )
         && (
            this.MSCrmFunctionsTab == null && var2.getMSCrmFunctionsTab() == null
               || this.MSCrmFunctionsTab != null && this.MSCrmFunctionsTab.equals(var2.getMSCrmFunctionsTab())
         )
         && (
            this.MSAXEndpointsTab == null && var2.getMSAXEndpointsTab() == null
               || this.MSAXEndpointsTab != null && this.MSAXEndpointsTab.equals(var2.getMSAXEndpointsTab())
         )
         && (
            this.MSAXFunctionsTab == null && var2.getMSAXFunctionsTab() == null
               || this.MSAXFunctionsTab != null && this.MSAXFunctionsTab.equals(var2.getMSAXFunctionsTab())
         )
         && (
            this.MSGPEndpointsTab == null && var2.getMSGPEndpointsTab() == null
               || this.MSGPEndpointsTab != null && this.MSGPEndpointsTab.equals(var2.getMSGPEndpointsTab())
         )
         && (
            this.MSGPFunctionsTab == null && var2.getMSGPFunctionsTab() == null
               || this.MSGPFunctionsTab != null && this.MSGPFunctionsTab.equals(var2.getMSGPFunctionsTab())
         )
         && (
            this.ebsEndpointsTab == null && var2.getEbsEndpointsTab() == null
               || this.ebsEndpointsTab != null && this.ebsEndpointsTab.equals(var2.getEbsEndpointsTab())
         )
         && (
            this.ebsFunctionsTab == null && var2.getEbsFunctionsTab() == null
               || this.ebsFunctionsTab != null && this.ebsFunctionsTab.equals(var2.getEbsFunctionsTab())
         )
         && (
            this.autoDeskEndpointsTab == null && var2.getAutoDeskEndpointsTab() == null
               || this.autoDeskEndpointsTab != null && this.autoDeskEndpointsTab.equals(var2.getAutoDeskEndpointsTab())
         )
         && (
            this.autoDeskFunctionsTab == null && var2.getAutoDeskFunctionsTab() == null
               || this.autoDeskFunctionsTab != null && this.autoDeskFunctionsTab.equals(var2.getAutoDeskFunctionsTab())
         )
         && (
            this.netSuiteEndpointsTab == null && var2.getNetSuiteEndpointsTab() == null
               || this.netSuiteEndpointsTab != null && this.netSuiteEndpointsTab.equals(var2.getNetSuiteEndpointsTab())
         )
         && (
            this.netSuiteFunctionsTab == null && var2.getNetSuiteFunctionsTab() == null
               || this.netSuiteFunctionsTab != null && this.netSuiteFunctionsTab.equals(var2.getNetSuiteFunctionsTab())
         )
         && (this.schedulesTab == null && var2.getSchedulesTab() == null || this.schedulesTab != null && this.schedulesTab.equals(var2.getSchedulesTab()))
         && (this.scriptsTab == null && var2.getScriptsTab() == null || this.scriptsTab != null && this.scriptsTab.equals(var2.getScriptsTab()))
         && (this.sourcesTab == null && var2.getSourcesTab() == null || this.sourcesTab != null && this.sourcesTab.equals(var2.getSourcesTab()))
         && (this.sqlScriptTab == null && var2.getSqlScriptTab() == null || this.sqlScriptTab != null && this.sqlScriptTab.equals(var2.getSqlScriptTab()))
         && (this.targetsTab == null && var2.getTargetsTab() == null || this.targetsTab != null && this.targetsTab.equals(var2.getTargetsTab()))
         && (
            this.tran_RelationsTab == null && var2.getTran_RelationsTab() == null
               || this.tran_RelationsTab != null && this.tran_RelationsTab.equals(var2.getTran_RelationsTab())
         )
         && (
            this.trans_FilterTab == null && var2.getTrans_FilterTab() == null
               || this.trans_FilterTab != null && this.trans_FilterTab.equals(var2.getTrans_FilterTab())
         )
         && (
            this.transformation_DefinitionsTab == null && var2.getTransformation_DefinitionsTab() == null
               || this.transformation_DefinitionsTab != null && this.transformation_DefinitionsTab.equals(var2.getTransformation_DefinitionsTab())
         )
         && (
            this.transformationChainLinksTab == null && var2.getTransformationChainLinksTab() == null
               || this.transformationChainLinksTab != null && this.transformationChainLinksTab.equals(var2.getTransformationChainLinksTab())
         )
         && (
            this.transformationsTab_New == null && var2.getTransformationsTab_New() == null
               || this.transformationsTab_New != null && this.transformationsTab_New.equals(var2.getTransformationsTab_New())
         )
         && (
            this.webServicesTab == null && var2.getWebServicesTab() == null
               || this.webServicesTab != null && this.webServicesTab.equals(var2.getWebServicesTab())
         )
         && (
            this.commonSourceOperationsTab == null && var2.getCommonSourceOperationsTab() == null
               || this.commonSourceOperationsTab != null && this.commonSourceOperationsTab.equals(var2.getCommonSourceOperationsTab())
         )
         && (
            this.ldapFiltersTab == null && var2.getLdapFiltersTab() == null
               || this.ldapFiltersTab != null && this.ldapFiltersTab.equals(var2.getLdapFiltersTab())
         )
         && (
            this.ldapStructureEntriesTab == null && var2.getLdapStructureEntriesTab() == null
               || this.ldapStructureEntriesTab != null && this.ldapStructureEntriesTab.equals(var2.getLdapStructureEntriesTab())
         )
         && (
            this.pipelinePluginEntityAssociations == null && var2.getPipelinePluginEntityAssociations() == null
               || this.pipelinePluginEntityAssociations != null && this.pipelinePluginEntityAssociations.equals(var2.getPipelinePluginEntityAssociations())
         )
         && (
            this.entityProperties == null && var2.getEntityProperties() == null
               || this.entityProperties != null && this.entityProperties.equals(var2.getEntityProperties())
         )
         && (
            this.operationPipelines == null && var2.getOperationPipelines() == null
               || this.operationPipelines != null && this.operationPipelines.equals(var2.getOperationPipelines())
         )
         && (this.permissions == null && var2.getPermissions() == null || this.permissions != null && this.permissions.equals(var2.getPermissions()));
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
      if (this.getCallRestrictionsTab() != null) {
         var1 += this.getCallRestrictionsTab().hashCode();
      }

      if (this.getDataElementsTab() != null) {
         var1 += this.getDataElementsTab().hashCode();
      }

      if (this.getDBFieldsTab() != null) {
         var1 += this.getDBFieldsTab().hashCode();
      }

      if (this.getDBTablesTab() != null) {
         var1 += this.getDBTablesTab().hashCode();
      }

      if (this.getDocument_DefinitionsTab() != null) {
         var1 += this.getDocument_DefinitionsTab().hashCode();
      }

      if (this.getDocumentFieldValidationTab() != null) {
         var1 += this.getDocumentFieldValidationTab().hashCode();
      }

      if (this.getDocumentsTab() != null) {
         var1 += this.getDocumentsTab().hashCode();
      }

      if (this.getFolderItemTab() != null) {
         var1 += this.getFolderItemTab().hashCode();
      }

      if (this.getFoldersTab() != null) {
         var1 += this.getFoldersTab().hashCode();
      }

      if (this.getHttpEndpointsTab() != null) {
         var1 += this.getHttpEndpointsTab().hashCode();
      }

      if (this.getHttpEndpointOperationsTab() != null) {
         var1 += this.getHttpEndpointOperationsTab().hashCode();
      }

      if (this.getInterchangeProjectsTab() != null) {
         var1 += this.getInterchangeProjectsTab().hashCode();
      }

      if (this.getJmsLocationPropertiesTab() != null) {
         var1 += this.getJmsLocationPropertiesTab().hashCode();
      }

      if (this.getJmsMessagesTab() != null) {
         var1 += this.getJmsMessagesTab().hashCode();
      }

      if (this.getMessagesTab() != null) {
         var1 += this.getMessagesTab().hashCode();
      }

      if (this.getOperationDiff_Details() != null) {
         var1 += this.getOperationDiff_Details().hashCode();
      }

      if (this.getOperationGraphLayoutTab() != null) {
         var1 += this.getOperationGraphLayoutTab().hashCode();
      }

      if (this.getOperationsTab() != null) {
         var1 += this.getOperationsTab().hashCode();
      }

      if (this.getOperationTransformationTab() != null) {
         var1 += this.getOperationTransformationTab().hashCode();
      }

      if (this.getSalesforceConnectorsTab() != null) {
         var1 += this.getSalesforceConnectorsTab().hashCode();
      }

      if (this.getSalesforceQueriesTab() != null) {
         var1 += this.getSalesforceQueriesTab().hashCode();
      }

      if (this.getSalesforceUpsertsTab() != null) {
         var1 += this.getSalesforceUpsertsTab().hashCode();
      }

      if (this.getSalesforceCreatesTab() != null) {
         var1 += this.getSalesforceCreatesTab().hashCode();
      }

      if (this.getSalesforceUpdatesTab() != null) {
         var1 += this.getSalesforceUpdatesTab().hashCode();
      }

      if (this.getSalesforceDeletesTab() != null) {
         var1 += this.getSalesforceDeletesTab().hashCode();
      }

      if (this.getSalesforceBulkLoadsTab() != null) {
         var1 += this.getSalesforceBulkLoadsTab().hashCode();
      }

      if (this.getSapEndpointsTab() != null) {
         var1 += this.getSapEndpointsTab().hashCode();
      }

      if (this.getSapFunctionsTab() != null) {
         var1 += this.getSapFunctionsTab().hashCode();
      }

      if (this.getMSCrmEndpointsTab() != null) {
         var1 += this.getMSCrmEndpointsTab().hashCode();
      }

      if (this.getMSCrmFunctionsTab() != null) {
         var1 += this.getMSCrmFunctionsTab().hashCode();
      }

      if (this.getMSAXEndpointsTab() != null) {
         var1 += this.getMSAXEndpointsTab().hashCode();
      }

      if (this.getMSAXFunctionsTab() != null) {
         var1 += this.getMSAXFunctionsTab().hashCode();
      }

      if (this.getMSGPEndpointsTab() != null) {
         var1 += this.getMSGPEndpointsTab().hashCode();
      }

      if (this.getMSGPFunctionsTab() != null) {
         var1 += this.getMSGPFunctionsTab().hashCode();
      }

      if (this.getEbsEndpointsTab() != null) {
         var1 += this.getEbsEndpointsTab().hashCode();
      }

      if (this.getEbsFunctionsTab() != null) {
         var1 += this.getEbsFunctionsTab().hashCode();
      }

      if (this.getAutoDeskEndpointsTab() != null) {
         var1 += this.getAutoDeskEndpointsTab().hashCode();
      }

      if (this.getAutoDeskFunctionsTab() != null) {
         var1 += this.getAutoDeskFunctionsTab().hashCode();
      }

      if (this.getNetSuiteEndpointsTab() != null) {
         var1 += this.getNetSuiteEndpointsTab().hashCode();
      }

      if (this.getNetSuiteFunctionsTab() != null) {
         var1 += this.getNetSuiteFunctionsTab().hashCode();
      }

      if (this.getSchedulesTab() != null) {
         var1 += this.getSchedulesTab().hashCode();
      }

      if (this.getScriptsTab() != null) {
         var1 += this.getScriptsTab().hashCode();
      }

      if (this.getSourcesTab() != null) {
         var1 += this.getSourcesTab().hashCode();
      }

      if (this.getSqlScriptTab() != null) {
         var1 += this.getSqlScriptTab().hashCode();
      }

      if (this.getTargetsTab() != null) {
         var1 += this.getTargetsTab().hashCode();
      }

      if (this.getTran_RelationsTab() != null) {
         var1 += this.getTran_RelationsTab().hashCode();
      }

      if (this.getTrans_FilterTab() != null) {
         var1 += this.getTrans_FilterTab().hashCode();
      }

      if (this.getTransformation_DefinitionsTab() != null) {
         var1 += this.getTransformation_DefinitionsTab().hashCode();
      }

      if (this.getTransformationChainLinksTab() != null) {
         var1 += this.getTransformationChainLinksTab().hashCode();
      }

      if (this.getTransformationsTab_New() != null) {
         var1 += this.getTransformationsTab_New().hashCode();
      }

      if (this.getWebServicesTab() != null) {
         var1 += this.getWebServicesTab().hashCode();
      }

      if (this.getCommonSourceOperationsTab() != null) {
         var1 += this.getCommonSourceOperationsTab().hashCode();
      }

      if (this.getLdapFiltersTab() != null) {
         var1 += this.getLdapFiltersTab().hashCode();
      }

      if (this.getLdapStructureEntriesTab() != null) {
         var1 += this.getLdapStructureEntriesTab().hashCode();
      }

      if (this.getPipelinePluginEntityAssociations() != null) {
         var1 += this.getPipelinePluginEntityAssociations().hashCode();
      }

      if (this.getEntityProperties() != null) {
         var1 += this.getEntityProperties().hashCode();
      }

      if (this.getOperationPipelines() != null) {
         var1 += this.getOperationPipelines().hashCode();
      }

      if (this.getPermissions() != null) {
         var1 += this.getPermissions().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataHolder")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("callRestrictionsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CallRestrictionsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("dataElementsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DataElementsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("DBFieldsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DBFieldsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("DBTablesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DBTablesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("document_DefinitionsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Document_DefinitionsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("documentFieldValidationTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DocumentFieldValidationTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("documentsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DocumentsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("folderItemTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "FolderItemTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("foldersTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "FoldersTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("httpEndpointsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "HttpEndpointsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("httpEndpointOperationsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "HttpEndpointOperationsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("interchangeProjectsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeProjectsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("jmsLocationPropertiesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "JmsLocationPropertiesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("jmsMessagesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "JmsMessagesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("messagesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MessagesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationDiff_Details");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OperationDiff_Details")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationGraphLayoutTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OperationGraphLayoutTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OperationsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationTransformationTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OperationTransformationTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceConnectorsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceConnectorsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceQueriesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceQueriesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceUpsertsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceUpsertsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceCreatesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceCreatesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceUpdatesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceUpdatesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceDeletesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceDeletesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceBulkLoadsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceBulkLoadsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sapEndpointsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SapEndpointsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sapFunctionsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SapFunctionsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSCrmEndpointsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSCrmEndpointsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSCrmFunctionsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSCrmFunctionsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSAXEndpointsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSAXEndpointsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSAXFunctionsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSAXFunctionsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSGPEndpointsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSGPEndpointsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSGPFunctionsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSGPFunctionsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ebsEndpointsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "EbsEndpointsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ebsFunctionsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "EbsFunctionsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("autoDeskEndpointsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "AutoDeskEndpointsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("autoDeskFunctionsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "AutoDeskFunctionsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("netSuiteEndpointsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "NetSuiteEndpointsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("netSuiteFunctionsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "NetSuiteFunctionsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("schedulesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SchedulesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("scriptsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ScriptsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sourcesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SourcesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sqlScriptTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SqlScriptTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targetsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "TargetsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("tran_RelationsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Tran_RelationsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("trans_FilterTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Trans_FilterTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformation_DefinitionsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Transformation_DefinitionsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationChainLinksTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "TransformationChainLinksTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationsTab_New");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "TransformationsTab_New")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("webServicesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "WebServicesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("commonSourceOperationsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CommonSourceOperationsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ldapFiltersTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "LdapFiltersTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ldapStructureEntriesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "LdapStructureEntriesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("pipelinePluginEntityAssociations");
      var0.setXmlName(
         new QName(
            "urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "PipelinePluginEntityAssociations"
         )
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("entityProperties");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "EntityProperties"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationPipelines");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OperationPipelines")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("permissions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Permissions"));
      var0.setXmlType(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "PermissionsHolder"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
