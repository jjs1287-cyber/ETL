package org.etlforge.integration.server.implementation.webservice.interchange.checkout.client;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class InterchangeDataHolder implements Serializable {
   private String[] warnings;
   private InterchangeDataTable callRestrictionsTab;
   private InterchangeDataTable DBFieldsTab;
   private InterchangeDataTable DBTablesTab;
   private InterchangeDataTable document_DefinitionsTab;
   private InterchangeDataTable documentsTab;
   private InterchangeDataTable folderItemTab;
   private InterchangeDataTable foldersTab;
   private InterchangeDataTable functionsTab;
   private InterchangeDataTable groupsTab;
   private InterchangeDataTable interchangeProjectsTab;
   private InterchangeDataTable messagesTab;
   private InterchangeDataTable operationDiff_Details;
   private InterchangeDataTable operationsTab;
   private InterchangeDataTable operationTransformationTab;
   private InterchangeDataTable salesforceConnectorsTab;
   private InterchangeDataTable salesforceQueriesTab;
   private InterchangeDataTable salesforceUpsertsTab;
   private InterchangeDataTable salesforceCreatesTab;
   private InterchangeDataTable salesforceUpdatesTab;
   private InterchangeDataTable salesforceDeletesTab;
   private InterchangeDataTable salesforceBulkLoadsTab;
   private InterchangeDataTable schedulesTab;
   private InterchangeDataTable sourcesTab;
   private InterchangeDataTable sqlScriptTab;
   private InterchangeDataTable targetsTab;
   private InterchangeDataTable tran_MessagesTab;
   private InterchangeDataTable tran_RelationsTab;
   private InterchangeDataTable trans_FilterTab;
   private InterchangeDataTable transformation_DefinitionsTab;
   private InterchangeDataTable transformationChainLinksTab;
   private InterchangeDataTable transformationsTab_New;
   private InterchangeDataTable user_GroupTab;
   private InterchangeDataTable usersTab;
   private InterchangeDataTable webServicesTab;
   private InterchangeDataTable webServiceFunctionTransformationView;
   private InterchangeDataTable wsdlTab;
   private PermissionsHolder permissions;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(InterchangeDataHolder.class, true);

   public InterchangeDataHolder() {
   }

   public InterchangeDataHolder(
      String[] var1,
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
      PermissionsHolder var38
   ) {
      this.warnings = var1;
      this.callRestrictionsTab = var2;
      this.DBFieldsTab = var3;
      this.DBTablesTab = var4;
      this.document_DefinitionsTab = var5;
      this.documentsTab = var6;
      this.folderItemTab = var7;
      this.foldersTab = var8;
      this.functionsTab = var9;
      this.groupsTab = var10;
      this.interchangeProjectsTab = var11;
      this.messagesTab = var12;
      this.operationDiff_Details = var13;
      this.operationsTab = var14;
      this.operationTransformationTab = var15;
      this.salesforceConnectorsTab = var16;
      this.salesforceQueriesTab = var17;
      this.salesforceUpsertsTab = var18;
      this.salesforceCreatesTab = var19;
      this.salesforceUpdatesTab = var20;
      this.salesforceDeletesTab = var21;
      this.salesforceBulkLoadsTab = var22;
      this.schedulesTab = var23;
      this.sourcesTab = var24;
      this.sqlScriptTab = var25;
      this.targetsTab = var26;
      this.tran_MessagesTab = var27;
      this.tran_RelationsTab = var28;
      this.trans_FilterTab = var29;
      this.transformation_DefinitionsTab = var30;
      this.transformationChainLinksTab = var31;
      this.transformationsTab_New = var32;
      this.user_GroupTab = var33;
      this.usersTab = var34;
      this.webServicesTab = var35;
      this.webServiceFunctionTransformationView = var36;
      this.wsdlTab = var37;
      this.permissions = var38;
   }

   public String[] getWarnings() {
      return this.warnings;
   }

   public void setWarnings(String[] var1) {
      this.warnings = var1;
   }

   public InterchangeDataTable getCallRestrictionsTab() {
      return this.callRestrictionsTab;
   }

   public void setCallRestrictionsTab(InterchangeDataTable var1) {
      this.callRestrictionsTab = var1;
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

   public InterchangeDataTable getFunctionsTab() {
      return this.functionsTab;
   }

   public void setFunctionsTab(InterchangeDataTable var1) {
      this.functionsTab = var1;
   }

   public InterchangeDataTable getGroupsTab() {
      return this.groupsTab;
   }

   public void setGroupsTab(InterchangeDataTable var1) {
      this.groupsTab = var1;
   }

   public InterchangeDataTable getInterchangeProjectsTab() {
      return this.interchangeProjectsTab;
   }

   public void setInterchangeProjectsTab(InterchangeDataTable var1) {
      this.interchangeProjectsTab = var1;
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

   public InterchangeDataTable getSchedulesTab() {
      return this.schedulesTab;
   }

   public void setSchedulesTab(InterchangeDataTable var1) {
      this.schedulesTab = var1;
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

   public InterchangeDataTable getTran_MessagesTab() {
      return this.tran_MessagesTab;
   }

   public void setTran_MessagesTab(InterchangeDataTable var1) {
      this.tran_MessagesTab = var1;
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

   public InterchangeDataTable getUser_GroupTab() {
      return this.user_GroupTab;
   }

   public void setUser_GroupTab(InterchangeDataTable var1) {
      this.user_GroupTab = var1;
   }

   public InterchangeDataTable getUsersTab() {
      return this.usersTab;
   }

   public void setUsersTab(InterchangeDataTable var1) {
      this.usersTab = var1;
   }

   public InterchangeDataTable getWebServicesTab() {
      return this.webServicesTab;
   }

   public void setWebServicesTab(InterchangeDataTable var1) {
      this.webServicesTab = var1;
   }

   public InterchangeDataTable getWebServiceFunctionTransformationView() {
      return this.webServiceFunctionTransformationView;
   }

   public void setWebServiceFunctionTransformationView(InterchangeDataTable var1) {
      this.webServiceFunctionTransformationView = var1;
   }

   public InterchangeDataTable getWsdlTab() {
      return this.wsdlTab;
   }

   public void setWsdlTab(InterchangeDataTable var1) {
      this.wsdlTab = var1;
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
      boolean var3 = (this.warnings == null && var2.getWarnings() == null || this.warnings != null && Arrays.equals(this.warnings, var2.getWarnings()))
         && (
            this.callRestrictionsTab == null && var2.getCallRestrictionsTab() == null
               || this.callRestrictionsTab != null && this.callRestrictionsTab.equals(var2.getCallRestrictionsTab())
         )
         && (this.DBFieldsTab == null && var2.getDBFieldsTab() == null || this.DBFieldsTab != null && this.DBFieldsTab.equals(var2.getDBFieldsTab()))
         && (this.DBTablesTab == null && var2.getDBTablesTab() == null || this.DBTablesTab != null && this.DBTablesTab.equals(var2.getDBTablesTab()))
         && (
            this.document_DefinitionsTab == null && var2.getDocument_DefinitionsTab() == null
               || this.document_DefinitionsTab != null && this.document_DefinitionsTab.equals(var2.getDocument_DefinitionsTab())
         )
         && (this.documentsTab == null && var2.getDocumentsTab() == null || this.documentsTab != null && this.documentsTab.equals(var2.getDocumentsTab()))
         && (this.folderItemTab == null && var2.getFolderItemTab() == null || this.folderItemTab != null && this.folderItemTab.equals(var2.getFolderItemTab()))
         && (this.foldersTab == null && var2.getFoldersTab() == null || this.foldersTab != null && this.foldersTab.equals(var2.getFoldersTab()))
         && (this.functionsTab == null && var2.getFunctionsTab() == null || this.functionsTab != null && this.functionsTab.equals(var2.getFunctionsTab()))
         && (this.groupsTab == null && var2.getGroupsTab() == null || this.groupsTab != null && this.groupsTab.equals(var2.getGroupsTab()))
         && (
            this.interchangeProjectsTab == null && var2.getInterchangeProjectsTab() == null
               || this.interchangeProjectsTab != null && this.interchangeProjectsTab.equals(var2.getInterchangeProjectsTab())
         )
         && (this.messagesTab == null && var2.getMessagesTab() == null || this.messagesTab != null && this.messagesTab.equals(var2.getMessagesTab()))
         && (
            this.operationDiff_Details == null && var2.getOperationDiff_Details() == null
               || this.operationDiff_Details != null && this.operationDiff_Details.equals(var2.getOperationDiff_Details())
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
         && (this.schedulesTab == null && var2.getSchedulesTab() == null || this.schedulesTab != null && this.schedulesTab.equals(var2.getSchedulesTab()))
         && (this.sourcesTab == null && var2.getSourcesTab() == null || this.sourcesTab != null && this.sourcesTab.equals(var2.getSourcesTab()))
         && (this.sqlScriptTab == null && var2.getSqlScriptTab() == null || this.sqlScriptTab != null && this.sqlScriptTab.equals(var2.getSqlScriptTab()))
         && (this.targetsTab == null && var2.getTargetsTab() == null || this.targetsTab != null && this.targetsTab.equals(var2.getTargetsTab()))
         && (
            this.tran_MessagesTab == null && var2.getTran_MessagesTab() == null
               || this.tran_MessagesTab != null && this.tran_MessagesTab.equals(var2.getTran_MessagesTab())
         )
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
         && (this.user_GroupTab == null && var2.getUser_GroupTab() == null || this.user_GroupTab != null && this.user_GroupTab.equals(var2.getUser_GroupTab()))
         && (this.usersTab == null && var2.getUsersTab() == null || this.usersTab != null && this.usersTab.equals(var2.getUsersTab()))
         && (
            this.webServicesTab == null && var2.getWebServicesTab() == null
               || this.webServicesTab != null && this.webServicesTab.equals(var2.getWebServicesTab())
         )
         && (
            this.webServiceFunctionTransformationView == null && var2.getWebServiceFunctionTransformationView() == null
               || this.webServiceFunctionTransformationView != null
                  && this.webServiceFunctionTransformationView.equals(var2.getWebServiceFunctionTransformationView())
         )
         && (this.wsdlTab == null && var2.getWsdlTab() == null || this.wsdlTab != null && this.wsdlTab.equals(var2.getWsdlTab()))
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
      if (this.getWarnings() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getWarnings()); var2++) {
            Object var3 = Array.get(this.getWarnings(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getCallRestrictionsTab() != null) {
         var1 += this.getCallRestrictionsTab().hashCode();
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

      if (this.getDocumentsTab() != null) {
         var1 += this.getDocumentsTab().hashCode();
      }

      if (this.getFolderItemTab() != null) {
         var1 += this.getFolderItemTab().hashCode();
      }

      if (this.getFoldersTab() != null) {
         var1 += this.getFoldersTab().hashCode();
      }

      if (this.getFunctionsTab() != null) {
         var1 += this.getFunctionsTab().hashCode();
      }

      if (this.getGroupsTab() != null) {
         var1 += this.getGroupsTab().hashCode();
      }

      if (this.getInterchangeProjectsTab() != null) {
         var1 += this.getInterchangeProjectsTab().hashCode();
      }

      if (this.getMessagesTab() != null) {
         var1 += this.getMessagesTab().hashCode();
      }

      if (this.getOperationDiff_Details() != null) {
         var1 += this.getOperationDiff_Details().hashCode();
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

      if (this.getSchedulesTab() != null) {
         var1 += this.getSchedulesTab().hashCode();
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

      if (this.getTran_MessagesTab() != null) {
         var1 += this.getTran_MessagesTab().hashCode();
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

      if (this.getUser_GroupTab() != null) {
         var1 += this.getUser_GroupTab().hashCode();
      }

      if (this.getUsersTab() != null) {
         var1 += this.getUsersTab().hashCode();
      }

      if (this.getWebServicesTab() != null) {
         var1 += this.getWebServicesTab().hashCode();
      }

      if (this.getWebServiceFunctionTransformationView() != null) {
         var1 += this.getWebServiceFunctionTransformationView().hashCode();
      }

      if (this.getWsdlTab() != null) {
         var1 += this.getWsdlTab().hashCode();
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataHolder")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("warnings");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Warnings"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("callRestrictionsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "CallRestrictionsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("DBFieldsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DBFieldsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("DBTablesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DBTablesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("document_DefinitionsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Document_DefinitionsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("documentsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "DocumentsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("folderItemTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "FolderItemTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("foldersTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "FoldersTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("functionsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "FunctionsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("groupsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "GroupsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("interchangeProjectsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeProjectsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("messagesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "MessagesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationDiff_Details");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "OperationDiff_Details")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "OperationsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationTransformationTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "OperationTransformationTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceConnectorsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceConnectorsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceQueriesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceQueriesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceUpsertsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceUpsertsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceCreatesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceCreatesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceUpdatesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceUpdatesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceDeletesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceDeletesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceBulkLoadsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SalesforceBulkLoadsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("schedulesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SchedulesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sourcesTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SourcesTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sqlScriptTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "SqlScriptTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targetsTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "TargetsTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("tran_MessagesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Tran_MessagesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("tran_RelationsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Tran_RelationsTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("trans_FilterTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Trans_FilterTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformation_DefinitionsTab");
      var0.setXmlName(
         new QName(
            "urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Transformation_DefinitionsTab"
         )
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationChainLinksTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "TransformationChainLinksTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationsTab_New");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "TransformationsTab_New")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("user_GroupTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "User_GroupTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("usersTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "UsersTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("webServicesTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "WebServicesTab")
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("webServiceFunctionTransformationView");
      var0.setXmlName(
         new QName(
            "urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData",
            "WebServiceFunctionTransformationView"
         )
      );
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("wsdlTab");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "WsdlTab"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "InterchangeDataTable")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("permissions");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "Permissions"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.checkout.CheckoutInterchangeData", "PermissionsHolder")
      );
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
