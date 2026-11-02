package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

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

public class InterchangeIdHolder implements Serializable {
   private String[] callRestrictionsTab_ids;
   private String[] DBFieldsTab_ids;
   private String[] DBTablesTab_ids;
   private String[] document_DefinitionsTab_ids;
   private String[] documentsTab_ids;
   private String[] folderItemTab_ids;
   private String[] foldersTab_ids;
   private String[] groupsTab_ids;
   private String[] httpEndpointsTab_ids;
   private String[] interchangeProjectsTab;
   private String[] jmsMessagesTab_ids;
   private String[] messagesTab_ids;
   private String[] operationDiff_Details_ids;
   private String[] operationsTab_ids;
   private String[] salesforceConnectorsTab_ids;
   private String[] salesforceQueriesTab_ids;
   private String[] salesforceUpsertsTab_ids;
   private String[] salesforceCreatesTab_ids;
   private String[] salesforceUpdatesTab_ids;
   private String[] salesforceDeletesTab_ids;
   private String[] salesforceBulkLoadsTab_ids;
   private String[] sapEndpointsTab_ids;
   private String[] sapFunctionsTab_ids;
   private String[] MSCrmEndpointsTab_ids;
   private String[] MSCrmFunctionsTab_ids;
   private String[] MSAXEndpointsTab_ids;
   private String[] MSAXFunctionsTab_ids;
   private String[] MSGPEndpointsTab_ids;
   private String[] MSGPFunctionsTab_ids;
   private String[] ebsEndpointsTab_ids;
   private String[] ebsFunctionsTab_ids;
   private String[] autoDeskEndpointsTab_ids;
   private String[] autoDeskFunctionsTab_ids;
   private String[] netSuiteEndpointsTab_ids;
   private String[] netSuiteFunctionsTab_ids;
   private String[] schedulesTab_ids;
   private String[] scriptsTab_ids;
   private String[] sourcesTab_ids;
   private String[] sqlScriptTab_ids;
   private String[] targetsTab_ids;
   private String[] tran_MessagesTab_ids;
   private String[] tran_RelationsTab_ids;
   private String[] trans_FilterTab_ids;
   private String[] transformation_DefinitionsTab_ids;
   private String[] transformationChainLinksTab_ids;
   private String[] transformationsTab_New_ids;
   private String[] user_GroupTab_ids;
   private String[] usersTab_ids;
   private String[] webServicesTab_ids;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(InterchangeIdHolder.class, true);

   public InterchangeIdHolder() {
   }

   public InterchangeIdHolder(
      String[] var1,
      String[] var2,
      String[] var3,
      String[] var4,
      String[] var5,
      String[] var6,
      String[] var7,
      String[] var8,
      String[] var9,
      String[] var10,
      String[] var11,
      String[] var12,
      String[] var13,
      String[] var14,
      String[] var15,
      String[] var16,
      String[] var17,
      String[] var18,
      String[] var19,
      String[] var20,
      String[] var21,
      String[] var22,
      String[] var23,
      String[] var24,
      String[] var25,
      String[] var26,
      String[] var27,
      String[] var28,
      String[] var29,
      String[] var30,
      String[] var31,
      String[] var32,
      String[] var33,
      String[] var34,
      String[] var35,
      String[] var36,
      String[] var37,
      String[] var38,
      String[] var39,
      String[] var40,
      String[] var41,
      String[] var42,
      String[] var43,
      String[] var44,
      String[] var45,
      String[] var46,
      String[] var47,
      String[] var48,
      String[] var49
   ) {
      this.callRestrictionsTab_ids = var1;
      this.DBFieldsTab_ids = var2;
      this.DBTablesTab_ids = var3;
      this.document_DefinitionsTab_ids = var4;
      this.documentsTab_ids = var5;
      this.folderItemTab_ids = var6;
      this.foldersTab_ids = var7;
      this.groupsTab_ids = var8;
      this.httpEndpointsTab_ids = var9;
      this.interchangeProjectsTab = var10;
      this.jmsMessagesTab_ids = var11;
      this.messagesTab_ids = var12;
      this.operationDiff_Details_ids = var13;
      this.operationsTab_ids = var14;
      this.salesforceConnectorsTab_ids = var15;
      this.salesforceQueriesTab_ids = var16;
      this.salesforceUpsertsTab_ids = var17;
      this.salesforceCreatesTab_ids = var18;
      this.salesforceUpdatesTab_ids = var19;
      this.salesforceDeletesTab_ids = var20;
      this.salesforceBulkLoadsTab_ids = var21;
      this.sapEndpointsTab_ids = var22;
      this.sapFunctionsTab_ids = var23;
      this.MSCrmEndpointsTab_ids = var24;
      this.MSCrmFunctionsTab_ids = var25;
      this.MSAXEndpointsTab_ids = var26;
      this.MSAXFunctionsTab_ids = var27;
      this.MSGPEndpointsTab_ids = var28;
      this.MSGPFunctionsTab_ids = var29;
      this.ebsEndpointsTab_ids = var30;
      this.ebsFunctionsTab_ids = var31;
      this.autoDeskEndpointsTab_ids = var32;
      this.autoDeskFunctionsTab_ids = var33;
      this.netSuiteEndpointsTab_ids = var34;
      this.netSuiteFunctionsTab_ids = var35;
      this.schedulesTab_ids = var36;
      this.scriptsTab_ids = var37;
      this.sourcesTab_ids = var38;
      this.sqlScriptTab_ids = var39;
      this.targetsTab_ids = var40;
      this.tran_MessagesTab_ids = var41;
      this.tran_RelationsTab_ids = var42;
      this.trans_FilterTab_ids = var43;
      this.transformation_DefinitionsTab_ids = var44;
      this.transformationChainLinksTab_ids = var45;
      this.transformationsTab_New_ids = var46;
      this.user_GroupTab_ids = var47;
      this.usersTab_ids = var48;
      this.webServicesTab_ids = var49;
   }

   public String[] getCallRestrictionsTab_ids() {
      return this.callRestrictionsTab_ids;
   }

   public void setCallRestrictionsTab_ids(String[] var1) {
      this.callRestrictionsTab_ids = var1;
   }

   public String[] getDBFieldsTab_ids() {
      return this.DBFieldsTab_ids;
   }

   public void setDBFieldsTab_ids(String[] var1) {
      this.DBFieldsTab_ids = var1;
   }

   public String[] getDBTablesTab_ids() {
      return this.DBTablesTab_ids;
   }

   public void setDBTablesTab_ids(String[] var1) {
      this.DBTablesTab_ids = var1;
   }

   public String[] getDocument_DefinitionsTab_ids() {
      return this.document_DefinitionsTab_ids;
   }

   public void setDocument_DefinitionsTab_ids(String[] var1) {
      this.document_DefinitionsTab_ids = var1;
   }

   public String[] getDocumentsTab_ids() {
      return this.documentsTab_ids;
   }

   public void setDocumentsTab_ids(String[] var1) {
      this.documentsTab_ids = var1;
   }

   public String[] getFolderItemTab_ids() {
      return this.folderItemTab_ids;
   }

   public void setFolderItemTab_ids(String[] var1) {
      this.folderItemTab_ids = var1;
   }

   public String[] getFoldersTab_ids() {
      return this.foldersTab_ids;
   }

   public void setFoldersTab_ids(String[] var1) {
      this.foldersTab_ids = var1;
   }

   public String[] getGroupsTab_ids() {
      return this.groupsTab_ids;
   }

   public void setGroupsTab_ids(String[] var1) {
      this.groupsTab_ids = var1;
   }

   public String[] getHttpEndpointsTab_ids() {
      return this.httpEndpointsTab_ids;
   }

   public void setHttpEndpointsTab_ids(String[] var1) {
      this.httpEndpointsTab_ids = var1;
   }

   public String[] getInterchangeProjectsTab() {
      return this.interchangeProjectsTab;
   }

   public void setInterchangeProjectsTab(String[] var1) {
      this.interchangeProjectsTab = var1;
   }

   public String[] getJmsMessagesTab_ids() {
      return this.jmsMessagesTab_ids;
   }

   public void setJmsMessagesTab_ids(String[] var1) {
      this.jmsMessagesTab_ids = var1;
   }

   public String[] getMessagesTab_ids() {
      return this.messagesTab_ids;
   }

   public void setMessagesTab_ids(String[] var1) {
      this.messagesTab_ids = var1;
   }

   public String[] getOperationDiff_Details_ids() {
      return this.operationDiff_Details_ids;
   }

   public void setOperationDiff_Details_ids(String[] var1) {
      this.operationDiff_Details_ids = var1;
   }

   public String[] getOperationsTab_ids() {
      return this.operationsTab_ids;
   }

   public void setOperationsTab_ids(String[] var1) {
      this.operationsTab_ids = var1;
   }

   public String[] getSalesforceConnectorsTab_ids() {
      return this.salesforceConnectorsTab_ids;
   }

   public void setSalesforceConnectorsTab_ids(String[] var1) {
      this.salesforceConnectorsTab_ids = var1;
   }

   public String[] getSalesforceQueriesTab_ids() {
      return this.salesforceQueriesTab_ids;
   }

   public void setSalesforceQueriesTab_ids(String[] var1) {
      this.salesforceQueriesTab_ids = var1;
   }

   public String[] getSalesforceUpsertsTab_ids() {
      return this.salesforceUpsertsTab_ids;
   }

   public void setSalesforceUpsertsTab_ids(String[] var1) {
      this.salesforceUpsertsTab_ids = var1;
   }

   public String[] getSalesforceCreatesTab_ids() {
      return this.salesforceCreatesTab_ids;
   }

   public void setSalesforceCreatesTab_ids(String[] var1) {
      this.salesforceCreatesTab_ids = var1;
   }

   public String[] getSalesforceUpdatesTab_ids() {
      return this.salesforceUpdatesTab_ids;
   }

   public void setSalesforceUpdatesTab_ids(String[] var1) {
      this.salesforceUpdatesTab_ids = var1;
   }

   public String[] getSalesforceDeletesTab_ids() {
      return this.salesforceDeletesTab_ids;
   }

   public void setSalesforceDeletesTab_ids(String[] var1) {
      this.salesforceDeletesTab_ids = var1;
   }

   public String[] getSalesforceBulkLoadsTab_ids() {
      return this.salesforceBulkLoadsTab_ids;
   }

   public void setSalesforceBulkLoadsTab_ids(String[] var1) {
      this.salesforceBulkLoadsTab_ids = var1;
   }

   public String[] getSapEndpointsTab_ids() {
      return this.sapEndpointsTab_ids;
   }

   public void setSapEndpointsTab_ids(String[] var1) {
      this.sapEndpointsTab_ids = var1;
   }

   public String[] getSapFunctionsTab_ids() {
      return this.sapFunctionsTab_ids;
   }

   public void setSapFunctionsTab_ids(String[] var1) {
      this.sapFunctionsTab_ids = var1;
   }

   public String[] getMSCrmEndpointsTab_ids() {
      return this.MSCrmEndpointsTab_ids;
   }

   public void setMSCrmEndpointsTab_ids(String[] var1) {
      this.MSCrmEndpointsTab_ids = var1;
   }

   public String[] getMSCrmFunctionsTab_ids() {
      return this.MSCrmFunctionsTab_ids;
   }

   public void setMSCrmFunctionsTab_ids(String[] var1) {
      this.MSCrmFunctionsTab_ids = var1;
   }

   public String[] getMSAXEndpointsTab_ids() {
      return this.MSAXEndpointsTab_ids;
   }

   public void setMSAXEndpointsTab_ids(String[] var1) {
      this.MSAXEndpointsTab_ids = var1;
   }

   public String[] getMSAXFunctionsTab_ids() {
      return this.MSAXFunctionsTab_ids;
   }

   public void setMSAXFunctionsTab_ids(String[] var1) {
      this.MSAXFunctionsTab_ids = var1;
   }

   public String[] getMSGPEndpointsTab_ids() {
      return this.MSGPEndpointsTab_ids;
   }

   public void setMSGPEndpointsTab_ids(String[] var1) {
      this.MSGPEndpointsTab_ids = var1;
   }

   public String[] getMSGPFunctionsTab_ids() {
      return this.MSGPFunctionsTab_ids;
   }

   public void setMSGPFunctionsTab_ids(String[] var1) {
      this.MSGPFunctionsTab_ids = var1;
   }

   public String[] getEbsEndpointsTab_ids() {
      return this.ebsEndpointsTab_ids;
   }

   public void setEbsEndpointsTab_ids(String[] var1) {
      this.ebsEndpointsTab_ids = var1;
   }

   public String[] getEbsFunctionsTab_ids() {
      return this.ebsFunctionsTab_ids;
   }

   public void setEbsFunctionsTab_ids(String[] var1) {
      this.ebsFunctionsTab_ids = var1;
   }

   public String[] getAutoDeskEndpointsTab_ids() {
      return this.autoDeskEndpointsTab_ids;
   }

   public void setAutoDeskEndpointsTab_ids(String[] var1) {
      this.autoDeskEndpointsTab_ids = var1;
   }

   public String[] getAutoDeskFunctionsTab_ids() {
      return this.autoDeskFunctionsTab_ids;
   }

   public void setAutoDeskFunctionsTab_ids(String[] var1) {
      this.autoDeskFunctionsTab_ids = var1;
   }

   public String[] getNetSuiteEndpointsTab_ids() {
      return this.netSuiteEndpointsTab_ids;
   }

   public void setNetSuiteEndpointsTab_ids(String[] var1) {
      this.netSuiteEndpointsTab_ids = var1;
   }

   public String[] getNetSuiteFunctionsTab_ids() {
      return this.netSuiteFunctionsTab_ids;
   }

   public void setNetSuiteFunctionsTab_ids(String[] var1) {
      this.netSuiteFunctionsTab_ids = var1;
   }

   public String[] getSchedulesTab_ids() {
      return this.schedulesTab_ids;
   }

   public void setSchedulesTab_ids(String[] var1) {
      this.schedulesTab_ids = var1;
   }

   public String[] getScriptsTab_ids() {
      return this.scriptsTab_ids;
   }

   public void setScriptsTab_ids(String[] var1) {
      this.scriptsTab_ids = var1;
   }

   public String[] getSourcesTab_ids() {
      return this.sourcesTab_ids;
   }

   public void setSourcesTab_ids(String[] var1) {
      this.sourcesTab_ids = var1;
   }

   public String[] getSqlScriptTab_ids() {
      return this.sqlScriptTab_ids;
   }

   public void setSqlScriptTab_ids(String[] var1) {
      this.sqlScriptTab_ids = var1;
   }

   public String[] getTargetsTab_ids() {
      return this.targetsTab_ids;
   }

   public void setTargetsTab_ids(String[] var1) {
      this.targetsTab_ids = var1;
   }

   public String[] getTran_MessagesTab_ids() {
      return this.tran_MessagesTab_ids;
   }

   public void setTran_MessagesTab_ids(String[] var1) {
      this.tran_MessagesTab_ids = var1;
   }

   public String[] getTran_RelationsTab_ids() {
      return this.tran_RelationsTab_ids;
   }

   public void setTran_RelationsTab_ids(String[] var1) {
      this.tran_RelationsTab_ids = var1;
   }

   public String[] getTrans_FilterTab_ids() {
      return this.trans_FilterTab_ids;
   }

   public void setTrans_FilterTab_ids(String[] var1) {
      this.trans_FilterTab_ids = var1;
   }

   public String[] getTransformation_DefinitionsTab_ids() {
      return this.transformation_DefinitionsTab_ids;
   }

   public void setTransformation_DefinitionsTab_ids(String[] var1) {
      this.transformation_DefinitionsTab_ids = var1;
   }

   public String[] getTransformationChainLinksTab_ids() {
      return this.transformationChainLinksTab_ids;
   }

   public void setTransformationChainLinksTab_ids(String[] var1) {
      this.transformationChainLinksTab_ids = var1;
   }

   public String[] getTransformationsTab_New_ids() {
      return this.transformationsTab_New_ids;
   }

   public void setTransformationsTab_New_ids(String[] var1) {
      this.transformationsTab_New_ids = var1;
   }

   public String[] getUser_GroupTab_ids() {
      return this.user_GroupTab_ids;
   }

   public void setUser_GroupTab_ids(String[] var1) {
      this.user_GroupTab_ids = var1;
   }

   public String[] getUsersTab_ids() {
      return this.usersTab_ids;
   }

   public void setUsersTab_ids(String[] var1) {
      this.usersTab_ids = var1;
   }

   public String[] getWebServicesTab_ids() {
      return this.webServicesTab_ids;
   }

   public void setWebServicesTab_ids(String[] var1) {
      this.webServicesTab_ids = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof InterchangeIdHolder)) {
         return false;
      }

      InterchangeIdHolder var2 = (InterchangeIdHolder)var1;
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
            this.callRestrictionsTab_ids == null && var2.getCallRestrictionsTab_ids() == null
               || this.callRestrictionsTab_ids != null && Arrays.equals(this.callRestrictionsTab_ids, var2.getCallRestrictionsTab_ids())
         )
         && (
            this.DBFieldsTab_ids == null && var2.getDBFieldsTab_ids() == null
               || this.DBFieldsTab_ids != null && Arrays.equals(this.DBFieldsTab_ids, var2.getDBFieldsTab_ids())
         )
         && (
            this.DBTablesTab_ids == null && var2.getDBTablesTab_ids() == null
               || this.DBTablesTab_ids != null && Arrays.equals(this.DBTablesTab_ids, var2.getDBTablesTab_ids())
         )
         && (
            this.document_DefinitionsTab_ids == null && var2.getDocument_DefinitionsTab_ids() == null
               || this.document_DefinitionsTab_ids != null && Arrays.equals(this.document_DefinitionsTab_ids, var2.getDocument_DefinitionsTab_ids())
         )
         && (
            this.documentsTab_ids == null && var2.getDocumentsTab_ids() == null
               || this.documentsTab_ids != null && Arrays.equals(this.documentsTab_ids, var2.getDocumentsTab_ids())
         )
         && (
            this.folderItemTab_ids == null && var2.getFolderItemTab_ids() == null
               || this.folderItemTab_ids != null && Arrays.equals(this.folderItemTab_ids, var2.getFolderItemTab_ids())
         )
         && (
            this.foldersTab_ids == null && var2.getFoldersTab_ids() == null
               || this.foldersTab_ids != null && Arrays.equals(this.foldersTab_ids, var2.getFoldersTab_ids())
         )
         && (
            this.groupsTab_ids == null && var2.getGroupsTab_ids() == null
               || this.groupsTab_ids != null && Arrays.equals(this.groupsTab_ids, var2.getGroupsTab_ids())
         )
         && (
            this.httpEndpointsTab_ids == null && var2.getHttpEndpointsTab_ids() == null
               || this.httpEndpointsTab_ids != null && Arrays.equals(this.httpEndpointsTab_ids, var2.getHttpEndpointsTab_ids())
         )
         && (
            this.interchangeProjectsTab == null && var2.getInterchangeProjectsTab() == null
               || this.interchangeProjectsTab != null && Arrays.equals(this.interchangeProjectsTab, var2.getInterchangeProjectsTab())
         )
         && (
            this.jmsMessagesTab_ids == null && var2.getJmsMessagesTab_ids() == null
               || this.jmsMessagesTab_ids != null && Arrays.equals(this.jmsMessagesTab_ids, var2.getJmsMessagesTab_ids())
         )
         && (
            this.messagesTab_ids == null && var2.getMessagesTab_ids() == null
               || this.messagesTab_ids != null && Arrays.equals(this.messagesTab_ids, var2.getMessagesTab_ids())
         )
         && (
            this.operationDiff_Details_ids == null && var2.getOperationDiff_Details_ids() == null
               || this.operationDiff_Details_ids != null && Arrays.equals(this.operationDiff_Details_ids, var2.getOperationDiff_Details_ids())
         )
         && (
            this.operationsTab_ids == null && var2.getOperationsTab_ids() == null
               || this.operationsTab_ids != null && Arrays.equals(this.operationsTab_ids, var2.getOperationsTab_ids())
         )
         && (
            this.salesforceConnectorsTab_ids == null && var2.getSalesforceConnectorsTab_ids() == null
               || this.salesforceConnectorsTab_ids != null && Arrays.equals(this.salesforceConnectorsTab_ids, var2.getSalesforceConnectorsTab_ids())
         )
         && (
            this.salesforceQueriesTab_ids == null && var2.getSalesforceQueriesTab_ids() == null
               || this.salesforceQueriesTab_ids != null && Arrays.equals(this.salesforceQueriesTab_ids, var2.getSalesforceQueriesTab_ids())
         )
         && (
            this.salesforceUpsertsTab_ids == null && var2.getSalesforceUpsertsTab_ids() == null
               || this.salesforceUpsertsTab_ids != null && Arrays.equals(this.salesforceUpsertsTab_ids, var2.getSalesforceUpsertsTab_ids())
         )
         && (
            this.salesforceCreatesTab_ids == null && var2.getSalesforceCreatesTab_ids() == null
               || this.salesforceCreatesTab_ids != null && Arrays.equals(this.salesforceCreatesTab_ids, var2.getSalesforceCreatesTab_ids())
         )
         && (
            this.salesforceUpdatesTab_ids == null && var2.getSalesforceUpdatesTab_ids() == null
               || this.salesforceUpdatesTab_ids != null && Arrays.equals(this.salesforceUpdatesTab_ids, var2.getSalesforceUpdatesTab_ids())
         )
         && (
            this.salesforceDeletesTab_ids == null && var2.getSalesforceDeletesTab_ids() == null
               || this.salesforceDeletesTab_ids != null && Arrays.equals(this.salesforceDeletesTab_ids, var2.getSalesforceDeletesTab_ids())
         )
         && (
            this.salesforceBulkLoadsTab_ids == null && var2.getSalesforceBulkLoadsTab_ids() == null
               || this.salesforceBulkLoadsTab_ids != null && Arrays.equals(this.salesforceBulkLoadsTab_ids, var2.getSalesforceBulkLoadsTab_ids())
         )
         && (
            this.sapEndpointsTab_ids == null && var2.getSapEndpointsTab_ids() == null
               || this.sapEndpointsTab_ids != null && Arrays.equals(this.sapEndpointsTab_ids, var2.getSapEndpointsTab_ids())
         )
         && (
            this.sapFunctionsTab_ids == null && var2.getSapFunctionsTab_ids() == null
               || this.sapFunctionsTab_ids != null && Arrays.equals(this.sapFunctionsTab_ids, var2.getSapFunctionsTab_ids())
         )
         && (
            this.MSCrmEndpointsTab_ids == null && var2.getMSCrmEndpointsTab_ids() == null
               || this.MSCrmEndpointsTab_ids != null && Arrays.equals(this.MSCrmEndpointsTab_ids, var2.getMSCrmEndpointsTab_ids())
         )
         && (
            this.MSCrmFunctionsTab_ids == null && var2.getMSCrmFunctionsTab_ids() == null
               || this.MSCrmFunctionsTab_ids != null && Arrays.equals(this.MSCrmFunctionsTab_ids, var2.getMSCrmFunctionsTab_ids())
         )
         && (
            this.MSAXEndpointsTab_ids == null && var2.getMSAXEndpointsTab_ids() == null
               || this.MSAXEndpointsTab_ids != null && Arrays.equals(this.MSAXEndpointsTab_ids, var2.getMSAXEndpointsTab_ids())
         )
         && (
            this.MSAXFunctionsTab_ids == null && var2.getMSAXFunctionsTab_ids() == null
               || this.MSAXFunctionsTab_ids != null && Arrays.equals(this.MSAXFunctionsTab_ids, var2.getMSAXFunctionsTab_ids())
         )
         && (
            this.MSGPEndpointsTab_ids == null && var2.getMSGPEndpointsTab_ids() == null
               || this.MSGPEndpointsTab_ids != null && Arrays.equals(this.MSGPEndpointsTab_ids, var2.getMSGPEndpointsTab_ids())
         )
         && (
            this.MSGPFunctionsTab_ids == null && var2.getMSGPFunctionsTab_ids() == null
               || this.MSGPFunctionsTab_ids != null && Arrays.equals(this.MSGPFunctionsTab_ids, var2.getMSGPFunctionsTab_ids())
         )
         && (
            this.ebsEndpointsTab_ids == null && var2.getEbsEndpointsTab_ids() == null
               || this.ebsEndpointsTab_ids != null && Arrays.equals(this.ebsEndpointsTab_ids, var2.getEbsEndpointsTab_ids())
         )
         && (
            this.ebsFunctionsTab_ids == null && var2.getEbsFunctionsTab_ids() == null
               || this.ebsFunctionsTab_ids != null && Arrays.equals(this.ebsFunctionsTab_ids, var2.getEbsFunctionsTab_ids())
         )
         && (
            this.autoDeskEndpointsTab_ids == null && var2.getAutoDeskEndpointsTab_ids() == null
               || this.autoDeskEndpointsTab_ids != null && Arrays.equals(this.autoDeskEndpointsTab_ids, var2.getAutoDeskEndpointsTab_ids())
         )
         && (
            this.autoDeskFunctionsTab_ids == null && var2.getAutoDeskFunctionsTab_ids() == null
               || this.autoDeskFunctionsTab_ids != null && Arrays.equals(this.autoDeskFunctionsTab_ids, var2.getAutoDeskFunctionsTab_ids())
         )
         && (
            this.netSuiteEndpointsTab_ids == null && var2.getNetSuiteEndpointsTab_ids() == null
               || this.netSuiteEndpointsTab_ids != null && Arrays.equals(this.netSuiteEndpointsTab_ids, var2.getNetSuiteEndpointsTab_ids())
         )
         && (
            this.netSuiteFunctionsTab_ids == null && var2.getNetSuiteFunctionsTab_ids() == null
               || this.netSuiteFunctionsTab_ids != null && Arrays.equals(this.netSuiteFunctionsTab_ids, var2.getNetSuiteFunctionsTab_ids())
         )
         && (
            this.schedulesTab_ids == null && var2.getSchedulesTab_ids() == null
               || this.schedulesTab_ids != null && Arrays.equals(this.schedulesTab_ids, var2.getSchedulesTab_ids())
         )
         && (
            this.scriptsTab_ids == null && var2.getScriptsTab_ids() == null
               || this.scriptsTab_ids != null && Arrays.equals(this.scriptsTab_ids, var2.getScriptsTab_ids())
         )
         && (
            this.sourcesTab_ids == null && var2.getSourcesTab_ids() == null
               || this.sourcesTab_ids != null && Arrays.equals(this.sourcesTab_ids, var2.getSourcesTab_ids())
         )
         && (
            this.sqlScriptTab_ids == null && var2.getSqlScriptTab_ids() == null
               || this.sqlScriptTab_ids != null && Arrays.equals(this.sqlScriptTab_ids, var2.getSqlScriptTab_ids())
         )
         && (
            this.targetsTab_ids == null && var2.getTargetsTab_ids() == null
               || this.targetsTab_ids != null && Arrays.equals(this.targetsTab_ids, var2.getTargetsTab_ids())
         )
         && (
            this.tran_MessagesTab_ids == null && var2.getTran_MessagesTab_ids() == null
               || this.tran_MessagesTab_ids != null && Arrays.equals(this.tran_MessagesTab_ids, var2.getTran_MessagesTab_ids())
         )
         && (
            this.tran_RelationsTab_ids == null && var2.getTran_RelationsTab_ids() == null
               || this.tran_RelationsTab_ids != null && Arrays.equals(this.tran_RelationsTab_ids, var2.getTran_RelationsTab_ids())
         )
         && (
            this.trans_FilterTab_ids == null && var2.getTrans_FilterTab_ids() == null
               || this.trans_FilterTab_ids != null && Arrays.equals(this.trans_FilterTab_ids, var2.getTrans_FilterTab_ids())
         )
         && (
            this.transformation_DefinitionsTab_ids == null && var2.getTransformation_DefinitionsTab_ids() == null
               || this.transformation_DefinitionsTab_ids != null
                  && Arrays.equals(this.transformation_DefinitionsTab_ids, var2.getTransformation_DefinitionsTab_ids())
         )
         && (
            this.transformationChainLinksTab_ids == null && var2.getTransformationChainLinksTab_ids() == null
               || this.transformationChainLinksTab_ids != null
                  && Arrays.equals(this.transformationChainLinksTab_ids, var2.getTransformationChainLinksTab_ids())
         )
         && (
            this.transformationsTab_New_ids == null && var2.getTransformationsTab_New_ids() == null
               || this.transformationsTab_New_ids != null && Arrays.equals(this.transformationsTab_New_ids, var2.getTransformationsTab_New_ids())
         )
         && (
            this.user_GroupTab_ids == null && var2.getUser_GroupTab_ids() == null
               || this.user_GroupTab_ids != null && Arrays.equals(this.user_GroupTab_ids, var2.getUser_GroupTab_ids())
         )
         && (
            this.usersTab_ids == null && var2.getUsersTab_ids() == null
               || this.usersTab_ids != null && Arrays.equals(this.usersTab_ids, var2.getUsersTab_ids())
         )
         && (
            this.webServicesTab_ids == null && var2.getWebServicesTab_ids() == null
               || this.webServicesTab_ids != null && Arrays.equals(this.webServicesTab_ids, var2.getWebServicesTab_ids())
         );
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
      if (this.getCallRestrictionsTab_ids() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getCallRestrictionsTab_ids()); var2++) {
            Object var3 = Array.get(this.getCallRestrictionsTab_ids(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getDBFieldsTab_ids() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getDBFieldsTab_ids()); var4++) {
            Object var52 = Array.get(this.getDBFieldsTab_ids(), var4);
            if (var52 != null && !var52.getClass().isArray()) {
               var1 += var52.hashCode();
            }
         }
      }

      if (this.getDBTablesTab_ids() != null) {
         for (int var5 = 0; var5 < Array.getLength(this.getDBTablesTab_ids()); var5++) {
            Object var53 = Array.get(this.getDBTablesTab_ids(), var5);
            if (var53 != null && !var53.getClass().isArray()) {
               var1 += var53.hashCode();
            }
         }
      }

      if (this.getDocument_DefinitionsTab_ids() != null) {
         for (int var6 = 0; var6 < Array.getLength(this.getDocument_DefinitionsTab_ids()); var6++) {
            Object var54 = Array.get(this.getDocument_DefinitionsTab_ids(), var6);
            if (var54 != null && !var54.getClass().isArray()) {
               var1 += var54.hashCode();
            }
         }
      }

      if (this.getDocumentsTab_ids() != null) {
         for (int var7 = 0; var7 < Array.getLength(this.getDocumentsTab_ids()); var7++) {
            Object var55 = Array.get(this.getDocumentsTab_ids(), var7);
            if (var55 != null && !var55.getClass().isArray()) {
               var1 += var55.hashCode();
            }
         }
      }

      if (this.getFolderItemTab_ids() != null) {
         for (int var8 = 0; var8 < Array.getLength(this.getFolderItemTab_ids()); var8++) {
            Object var56 = Array.get(this.getFolderItemTab_ids(), var8);
            if (var56 != null && !var56.getClass().isArray()) {
               var1 += var56.hashCode();
            }
         }
      }

      if (this.getFoldersTab_ids() != null) {
         for (int var9 = 0; var9 < Array.getLength(this.getFoldersTab_ids()); var9++) {
            Object var57 = Array.get(this.getFoldersTab_ids(), var9);
            if (var57 != null && !var57.getClass().isArray()) {
               var1 += var57.hashCode();
            }
         }
      }

      if (this.getGroupsTab_ids() != null) {
         for (int var10 = 0; var10 < Array.getLength(this.getGroupsTab_ids()); var10++) {
            Object var58 = Array.get(this.getGroupsTab_ids(), var10);
            if (var58 != null && !var58.getClass().isArray()) {
               var1 += var58.hashCode();
            }
         }
      }

      if (this.getHttpEndpointsTab_ids() != null) {
         for (int var11 = 0; var11 < Array.getLength(this.getHttpEndpointsTab_ids()); var11++) {
            Object var59 = Array.get(this.getHttpEndpointsTab_ids(), var11);
            if (var59 != null && !var59.getClass().isArray()) {
               var1 += var59.hashCode();
            }
         }
      }

      if (this.getInterchangeProjectsTab() != null) {
         for (int var12 = 0; var12 < Array.getLength(this.getInterchangeProjectsTab()); var12++) {
            Object var60 = Array.get(this.getInterchangeProjectsTab(), var12);
            if (var60 != null && !var60.getClass().isArray()) {
               var1 += var60.hashCode();
            }
         }
      }

      if (this.getJmsMessagesTab_ids() != null) {
         for (int var13 = 0; var13 < Array.getLength(this.getJmsMessagesTab_ids()); var13++) {
            Object var61 = Array.get(this.getJmsMessagesTab_ids(), var13);
            if (var61 != null && !var61.getClass().isArray()) {
               var1 += var61.hashCode();
            }
         }
      }

      if (this.getMessagesTab_ids() != null) {
         for (int var14 = 0; var14 < Array.getLength(this.getMessagesTab_ids()); var14++) {
            Object var62 = Array.get(this.getMessagesTab_ids(), var14);
            if (var62 != null && !var62.getClass().isArray()) {
               var1 += var62.hashCode();
            }
         }
      }

      if (this.getOperationDiff_Details_ids() != null) {
         for (int var15 = 0; var15 < Array.getLength(this.getOperationDiff_Details_ids()); var15++) {
            Object var63 = Array.get(this.getOperationDiff_Details_ids(), var15);
            if (var63 != null && !var63.getClass().isArray()) {
               var1 += var63.hashCode();
            }
         }
      }

      if (this.getOperationsTab_ids() != null) {
         for (int var16 = 0; var16 < Array.getLength(this.getOperationsTab_ids()); var16++) {
            Object var64 = Array.get(this.getOperationsTab_ids(), var16);
            if (var64 != null && !var64.getClass().isArray()) {
               var1 += var64.hashCode();
            }
         }
      }

      if (this.getSalesforceConnectorsTab_ids() != null) {
         for (int var17 = 0; var17 < Array.getLength(this.getSalesforceConnectorsTab_ids()); var17++) {
            Object var65 = Array.get(this.getSalesforceConnectorsTab_ids(), var17);
            if (var65 != null && !var65.getClass().isArray()) {
               var1 += var65.hashCode();
            }
         }
      }

      if (this.getSalesforceQueriesTab_ids() != null) {
         for (int var18 = 0; var18 < Array.getLength(this.getSalesforceQueriesTab_ids()); var18++) {
            Object var66 = Array.get(this.getSalesforceQueriesTab_ids(), var18);
            if (var66 != null && !var66.getClass().isArray()) {
               var1 += var66.hashCode();
            }
         }
      }

      if (this.getSalesforceUpsertsTab_ids() != null) {
         for (int var19 = 0; var19 < Array.getLength(this.getSalesforceUpsertsTab_ids()); var19++) {
            Object var67 = Array.get(this.getSalesforceUpsertsTab_ids(), var19);
            if (var67 != null && !var67.getClass().isArray()) {
               var1 += var67.hashCode();
            }
         }
      }

      if (this.getSalesforceCreatesTab_ids() != null) {
         for (int var20 = 0; var20 < Array.getLength(this.getSalesforceCreatesTab_ids()); var20++) {
            Object var68 = Array.get(this.getSalesforceCreatesTab_ids(), var20);
            if (var68 != null && !var68.getClass().isArray()) {
               var1 += var68.hashCode();
            }
         }
      }

      if (this.getSalesforceUpdatesTab_ids() != null) {
         for (int var21 = 0; var21 < Array.getLength(this.getSalesforceUpdatesTab_ids()); var21++) {
            Object var69 = Array.get(this.getSalesforceUpdatesTab_ids(), var21);
            if (var69 != null && !var69.getClass().isArray()) {
               var1 += var69.hashCode();
            }
         }
      }

      if (this.getSalesforceDeletesTab_ids() != null) {
         for (int var22 = 0; var22 < Array.getLength(this.getSalesforceDeletesTab_ids()); var22++) {
            Object var70 = Array.get(this.getSalesforceDeletesTab_ids(), var22);
            if (var70 != null && !var70.getClass().isArray()) {
               var1 += var70.hashCode();
            }
         }
      }

      if (this.getSalesforceBulkLoadsTab_ids() != null) {
         for (int var23 = 0; var23 < Array.getLength(this.getSalesforceBulkLoadsTab_ids()); var23++) {
            Object var71 = Array.get(this.getSalesforceBulkLoadsTab_ids(), var23);
            if (var71 != null && !var71.getClass().isArray()) {
               var1 += var71.hashCode();
            }
         }
      }

      if (this.getSapEndpointsTab_ids() != null) {
         for (int var24 = 0; var24 < Array.getLength(this.getSapEndpointsTab_ids()); var24++) {
            Object var72 = Array.get(this.getSapEndpointsTab_ids(), var24);
            if (var72 != null && !var72.getClass().isArray()) {
               var1 += var72.hashCode();
            }
         }
      }

      if (this.getSapFunctionsTab_ids() != null) {
         for (int var25 = 0; var25 < Array.getLength(this.getSapFunctionsTab_ids()); var25++) {
            Object var73 = Array.get(this.getSapFunctionsTab_ids(), var25);
            if (var73 != null && !var73.getClass().isArray()) {
               var1 += var73.hashCode();
            }
         }
      }

      if (this.getMSCrmEndpointsTab_ids() != null) {
         for (int var26 = 0; var26 < Array.getLength(this.getMSCrmEndpointsTab_ids()); var26++) {
            Object var74 = Array.get(this.getMSCrmEndpointsTab_ids(), var26);
            if (var74 != null && !var74.getClass().isArray()) {
               var1 += var74.hashCode();
            }
         }
      }

      if (this.getMSCrmFunctionsTab_ids() != null) {
         for (int var27 = 0; var27 < Array.getLength(this.getMSCrmFunctionsTab_ids()); var27++) {
            Object var75 = Array.get(this.getMSCrmFunctionsTab_ids(), var27);
            if (var75 != null && !var75.getClass().isArray()) {
               var1 += var75.hashCode();
            }
         }
      }

      if (this.getMSAXEndpointsTab_ids() != null) {
         for (int var28 = 0; var28 < Array.getLength(this.getMSAXEndpointsTab_ids()); var28++) {
            Object var76 = Array.get(this.getMSAXEndpointsTab_ids(), var28);
            if (var76 != null && !var76.getClass().isArray()) {
               var1 += var76.hashCode();
            }
         }
      }

      if (this.getMSAXFunctionsTab_ids() != null) {
         for (int var29 = 0; var29 < Array.getLength(this.getMSAXFunctionsTab_ids()); var29++) {
            Object var77 = Array.get(this.getMSAXFunctionsTab_ids(), var29);
            if (var77 != null && !var77.getClass().isArray()) {
               var1 += var77.hashCode();
            }
         }
      }

      if (this.getMSGPEndpointsTab_ids() != null) {
         for (int var30 = 0; var30 < Array.getLength(this.getMSGPEndpointsTab_ids()); var30++) {
            Object var78 = Array.get(this.getMSGPEndpointsTab_ids(), var30);
            if (var78 != null && !var78.getClass().isArray()) {
               var1 += var78.hashCode();
            }
         }
      }

      if (this.getMSGPFunctionsTab_ids() != null) {
         for (int var31 = 0; var31 < Array.getLength(this.getMSGPFunctionsTab_ids()); var31++) {
            Object var79 = Array.get(this.getMSGPFunctionsTab_ids(), var31);
            if (var79 != null && !var79.getClass().isArray()) {
               var1 += var79.hashCode();
            }
         }
      }

      if (this.getEbsEndpointsTab_ids() != null) {
         for (int var32 = 0; var32 < Array.getLength(this.getEbsEndpointsTab_ids()); var32++) {
            Object var80 = Array.get(this.getEbsEndpointsTab_ids(), var32);
            if (var80 != null && !var80.getClass().isArray()) {
               var1 += var80.hashCode();
            }
         }
      }

      if (this.getEbsFunctionsTab_ids() != null) {
         for (int var33 = 0; var33 < Array.getLength(this.getEbsFunctionsTab_ids()); var33++) {
            Object var81 = Array.get(this.getEbsFunctionsTab_ids(), var33);
            if (var81 != null && !var81.getClass().isArray()) {
               var1 += var81.hashCode();
            }
         }
      }

      if (this.getAutoDeskEndpointsTab_ids() != null) {
         for (int var34 = 0; var34 < Array.getLength(this.getAutoDeskEndpointsTab_ids()); var34++) {
            Object var82 = Array.get(this.getAutoDeskEndpointsTab_ids(), var34);
            if (var82 != null && !var82.getClass().isArray()) {
               var1 += var82.hashCode();
            }
         }
      }

      if (this.getAutoDeskFunctionsTab_ids() != null) {
         for (int var35 = 0; var35 < Array.getLength(this.getAutoDeskFunctionsTab_ids()); var35++) {
            Object var83 = Array.get(this.getAutoDeskFunctionsTab_ids(), var35);
            if (var83 != null && !var83.getClass().isArray()) {
               var1 += var83.hashCode();
            }
         }
      }

      if (this.getNetSuiteEndpointsTab_ids() != null) {
         for (int var36 = 0; var36 < Array.getLength(this.getNetSuiteEndpointsTab_ids()); var36++) {
            Object var84 = Array.get(this.getNetSuiteEndpointsTab_ids(), var36);
            if (var84 != null && !var84.getClass().isArray()) {
               var1 += var84.hashCode();
            }
         }
      }

      if (this.getNetSuiteFunctionsTab_ids() != null) {
         for (int var37 = 0; var37 < Array.getLength(this.getNetSuiteFunctionsTab_ids()); var37++) {
            Object var85 = Array.get(this.getNetSuiteFunctionsTab_ids(), var37);
            if (var85 != null && !var85.getClass().isArray()) {
               var1 += var85.hashCode();
            }
         }
      }

      if (this.getSchedulesTab_ids() != null) {
         for (int var38 = 0; var38 < Array.getLength(this.getSchedulesTab_ids()); var38++) {
            Object var86 = Array.get(this.getSchedulesTab_ids(), var38);
            if (var86 != null && !var86.getClass().isArray()) {
               var1 += var86.hashCode();
            }
         }
      }

      if (this.getScriptsTab_ids() != null) {
         for (int var39 = 0; var39 < Array.getLength(this.getScriptsTab_ids()); var39++) {
            Object var87 = Array.get(this.getScriptsTab_ids(), var39);
            if (var87 != null && !var87.getClass().isArray()) {
               var1 += var87.hashCode();
            }
         }
      }

      if (this.getSourcesTab_ids() != null) {
         for (int var40 = 0; var40 < Array.getLength(this.getSourcesTab_ids()); var40++) {
            Object var88 = Array.get(this.getSourcesTab_ids(), var40);
            if (var88 != null && !var88.getClass().isArray()) {
               var1 += var88.hashCode();
            }
         }
      }

      if (this.getSqlScriptTab_ids() != null) {
         for (int var41 = 0; var41 < Array.getLength(this.getSqlScriptTab_ids()); var41++) {
            Object var89 = Array.get(this.getSqlScriptTab_ids(), var41);
            if (var89 != null && !var89.getClass().isArray()) {
               var1 += var89.hashCode();
            }
         }
      }

      if (this.getTargetsTab_ids() != null) {
         for (int var42 = 0; var42 < Array.getLength(this.getTargetsTab_ids()); var42++) {
            Object var90 = Array.get(this.getTargetsTab_ids(), var42);
            if (var90 != null && !var90.getClass().isArray()) {
               var1 += var90.hashCode();
            }
         }
      }

      if (this.getTran_MessagesTab_ids() != null) {
         for (int var43 = 0; var43 < Array.getLength(this.getTran_MessagesTab_ids()); var43++) {
            Object var91 = Array.get(this.getTran_MessagesTab_ids(), var43);
            if (var91 != null && !var91.getClass().isArray()) {
               var1 += var91.hashCode();
            }
         }
      }

      if (this.getTran_RelationsTab_ids() != null) {
         for (int var44 = 0; var44 < Array.getLength(this.getTran_RelationsTab_ids()); var44++) {
            Object var92 = Array.get(this.getTran_RelationsTab_ids(), var44);
            if (var92 != null && !var92.getClass().isArray()) {
               var1 += var92.hashCode();
            }
         }
      }

      if (this.getTrans_FilterTab_ids() != null) {
         for (int var45 = 0; var45 < Array.getLength(this.getTrans_FilterTab_ids()); var45++) {
            Object var93 = Array.get(this.getTrans_FilterTab_ids(), var45);
            if (var93 != null && !var93.getClass().isArray()) {
               var1 += var93.hashCode();
            }
         }
      }

      if (this.getTransformation_DefinitionsTab_ids() != null) {
         for (int var46 = 0; var46 < Array.getLength(this.getTransformation_DefinitionsTab_ids()); var46++) {
            Object var94 = Array.get(this.getTransformation_DefinitionsTab_ids(), var46);
            if (var94 != null && !var94.getClass().isArray()) {
               var1 += var94.hashCode();
            }
         }
      }

      if (this.getTransformationChainLinksTab_ids() != null) {
         for (int var47 = 0; var47 < Array.getLength(this.getTransformationChainLinksTab_ids()); var47++) {
            Object var95 = Array.get(this.getTransformationChainLinksTab_ids(), var47);
            if (var95 != null && !var95.getClass().isArray()) {
               var1 += var95.hashCode();
            }
         }
      }

      if (this.getTransformationsTab_New_ids() != null) {
         for (int var48 = 0; var48 < Array.getLength(this.getTransformationsTab_New_ids()); var48++) {
            Object var96 = Array.get(this.getTransformationsTab_New_ids(), var48);
            if (var96 != null && !var96.getClass().isArray()) {
               var1 += var96.hashCode();
            }
         }
      }

      if (this.getUser_GroupTab_ids() != null) {
         for (int var49 = 0; var49 < Array.getLength(this.getUser_GroupTab_ids()); var49++) {
            Object var97 = Array.get(this.getUser_GroupTab_ids(), var49);
            if (var97 != null && !var97.getClass().isArray()) {
               var1 += var97.hashCode();
            }
         }
      }

      if (this.getUsersTab_ids() != null) {
         for (int var50 = 0; var50 < Array.getLength(this.getUsersTab_ids()); var50++) {
            Object var98 = Array.get(this.getUsersTab_ids(), var50);
            if (var98 != null && !var98.getClass().isArray()) {
               var1 += var98.hashCode();
            }
         }
      }

      if (this.getWebServicesTab_ids() != null) {
         for (int var51 = 0; var51 < Array.getLength(this.getWebServicesTab_ids()); var51++) {
            Object var99 = Array.get(this.getWebServicesTab_ids(), var51);
            if (var99 != null && !var99.getClass().isArray()) {
               var1 += var99.hashCode();
            }
         }
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
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeIdHolder")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("callRestrictionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "CallRestrictionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("DBFieldsTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DBFieldsTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("DBTablesTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DBTablesTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("document_DefinitionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Document_DefinitionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("documentsTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "DocumentsTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("folderItemTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "FolderItemTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("foldersTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "FoldersTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("groupsTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "GroupsTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("httpEndpointsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "HttpEndpointsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("interchangeProjectsTab");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeProjectsTab")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("jmsMessagesTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "JmsMessagesTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("messagesTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MessagesTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationDiff_Details_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OperationDiff_Details_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationsTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "OperationsTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceConnectorsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceConnectorsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceQueriesTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceQueriesTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceUpsertsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceUpsertsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceCreatesTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceCreatesTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceUpdatesTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceUpdatesTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceDeletesTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceDeletesTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("salesforceBulkLoadsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SalesforceBulkLoadsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sapEndpointsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SapEndpointsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sapFunctionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SapFunctionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSCrmEndpointsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSCrmEndpointsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSCrmFunctionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSCrmFunctionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSAXEndpointsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSAXEndpointsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSAXFunctionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSAXFunctionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSGPEndpointsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSGPEndpointsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("MSGPFunctionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "MSGPFunctionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ebsEndpointsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "EbsEndpointsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("ebsFunctionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "EbsFunctionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("autoDeskEndpointsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "AutoDeskEndpointsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("autoDeskFunctionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "AutoDeskFunctionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("netSuiteEndpointsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "NetSuiteEndpointsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("netSuiteFunctionsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "NetSuiteFunctionsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("schedulesTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SchedulesTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("scriptsTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "ScriptsTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sourcesTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SourcesTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sqlScriptTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "SqlScriptTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targetsTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "TargetsTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("tran_MessagesTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Tran_MessagesTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("tran_RelationsTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Tran_RelationsTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("trans_FilterTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Trans_FilterTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformation_DefinitionsTab_ids");
      var0.setXmlName(
         new QName(
            "urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "Transformation_DefinitionsTab_ids"
         )
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationChainLinksTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "TransformationChainLinksTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("transformationsTab_New_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "TransformationsTab_New_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("user_GroupTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "User_GroupTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("usersTab_ids");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "UsersTab_ids"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("webServicesTab_ids");
      var0.setXmlName(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "WebServicesTab_ids")
      );
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
