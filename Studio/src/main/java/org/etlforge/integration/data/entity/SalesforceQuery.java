package org.etlforge.integration.data.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.sfdc.data.SoqlObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.id.SalesforceQueryId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.sfdc.SalesforceWizardEntityUtils;
import org.etlforge.integration.data.entity.sfdc.SalesforceWizardReferencesReplacer;
import org.etlforge.integration.data.entity.sfdc.SoapHeaders;
import org.etlforge.integration.data.entity.sfdc.WizardEntityLinkCollector;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.data.entity.sfdc.WizardItemInfo;
import org.etlforge.integration.data.entity.sfquery.ResponseFilterCoordinator;
import org.etlforge.integration.data.entity.sfquery.TargetHeaderMode;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.wsdl.SalesforceWsdlConstants;
import org.etlforge.lang.Objects;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public final class SalesforceQuery extends IntegrationEntity implements SalesforceWizardEntity {
   private static final Version VERSION_INTRODUCED = VersionFactory.newVersion(3, 6, 0, 1);
   public static final String BATCH_SIZE = "batch_size";
   public static final String QUERY = "query";
   public static final String QUERY_STRING = "query_string";
   public static final String OBJECT = "salesforce_object";
   public static final String OPERATION_ID = "operation_id";
   public static final String QUERY_ALL = "query_all";
   @IdProperty
   public static final String SALESFORCE_QUERY_ID = "salesforcequery_id";
   @NameProperty
   public static final String SALESFORCE_QUERY_NAME = "salesforcequery_name";
   public static final String AUTO_RESPONSE = "auto_response";
   public static final String TARGET_ENCODING = "target_encoding";
   public static final String TARGET_HEADER_MODE = "target_header_mode";

   public SalesforceQuery(String var1) {
      this(null, var1);
   }

   public SalesforceQuery(SalesforceQueryId var1, String var2) {
      super(EntityType.SalesforceQuery, var1, var2);
   }

   @Override
   public String[] getPropertyKeys() {
      ArrayList var1 = Lists.newArrayList(
         "salesforcequery_id",
         "salesforcequery_name",
         "query_string",
         "salesforce_object",
         "operation_id",
         "query_all",
         "target_encoding",
         "target_header_mode",
         "connector_id"
      );
      var1.addAll(SoapHeaders.getExistingKeys(this));
      return (String[]) var1.toArray(new String[var1.size()]);
   }

   @Override
   public Version getRequiredVersion() {
      return VERSION_INTRODUCED;
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      return WizardEntityLinkCollector.collectLinks(this, var1);
   }

   public SoqlObject getQueryObject() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("query_string");
         if (var2 != null) {
            String var3 = this.getProperty("salesforce_object");
            if (var3 != null) {
               return new SoqlObject(var2, XmlNameFactory.conforming().newExpandedName(var3), this.isQueryAll());
            }
         }

         return null;
      }
   }

   public void setQueryObject(SoqlObject var1) {
      SoqlObject var2;
      synchronized (this.getDataLock()) {
         var2 = this.getQueryObject();
         this.setProperty("query_string", var1.getQueryString());
         this.setProperty("salesforce_object", var1.getObjectName().toString());
         this.setProperty("query_all", var1.isQueryAll() ? "1" : "0");
      }

      if (!Objects.equal(var2, var1)) {
         this.firePropertyChange("query", var2, var1);
      }
   }

   public String getQueryString() {
      synchronized (this.getDataLock()) {
         SoqlObject var2 = this.getQueryObject();
         return var2 != null ? var2.getQueryString() : null;
      }
   }

   @Override
   public ExpandedName getObject() {
      synchronized (this.getDataLock()) {
         SoqlObject var2 = this.getQueryObject();
         return var2 != null ? var2.getObjectName() : null;
      }
   }

   @Deprecated
   public int getBatchSize() {
      return this.getIntProperty("batch_size", 2000);
   }

   @Deprecated
   public void setBatchSize(int var1) {
      ArgCheck.checkInRange(var1, 200, 2000, "size");
      this.setIntProperty("batch_size", var1);
   }

   @Override
   public OperationId getImplementingOperationId() {
      return this.getProjectItemId("operation_id", EntityType.Operation, OperationId.class);
   }

   @Override
   public void setImplementingOperation(Operation var1) {
      Preconditions.checkArgument(var1.getOperationType() == WebServiceOperationType.TYPE, "op must implement a web service call");
      this.setStringProperty("operation_id", var1.getID().toString());
   }

   private <T extends IntegrationEntityId> T getProjectItemId(String var1, EntityType var2, Class<T> var3) {
      synchronized (this.getDataLock()) {
         String var5 = this.getProperty(var1, null);
         return (T)(var5 == null ? null : var3.cast(var2.getIntegrationId(var5)));
      }
   }

   @Override
   public SalesforceConnectorId getConnectorId() {
      return SalesforceWizardEntityUtils.getConnectorId(this);
   }

   @Override
   public void setConnector(SalesforceConnector var1) {
      SalesforceWizardEntityUtils.setConnector(this, var1);
   }

   public static boolean isValidBatchSize(int var0) {
      return var0 >= 200 && var0 <= 2000;
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      var1.putText("query_string", this.getQueryString());
   }

   @Override
   protected Set<String> getKeysToSkipWhenPersisting() {
      return Collections.singleton("query_string");
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      String var2 = var1.getText("query_string");
      this.setProperty("query_string", var2 != null ? var2 : "");
   }

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      SalesforceWizardReferencesReplacer var2 = new SalesforceWizardReferencesReplacer(var1);
      var2.replaceReferences(this);
   }

   @Override
   public void restoreDependentData(Persistor var1, IntegrationEntityLookup var2) {
      ResponseFilterCoordinator var3 = new ResponseFilterCoordinator(var2);
      var3.updateResponseFilter(this);
   }

   @Override
   public boolean isCopyable() {
      return false;
   }

   @Override
   public boolean isDeepCopyable() {
      return true;
   }

   @Override
   public Set<SalesforceWizardEntity.Header> getSoapHeaders() {
      return SoapHeaders.read(this);
   }

   @Override
   public void setSoapHeaders(Set<SalesforceWizardEntity.Header> var1) {
      SoapHeaders.store(this, var1);
   }

   @Override
   public SfdcMethod getMethod() {
      return SfdcMethod.QUERY;
   }

   public final boolean isBasedOnPartnerWsdl() {
      ExpandedName var1 = this.getObject();
      return var1 != null && SalesforceWsdlConstants.isPartnerWsdlObject(var1);
   }

   public boolean isQueryAll() {
      return this.getBooleanProperty("query_all", false);
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      if (this.isBasedOnPartnerWsdl()) {
         SalesforceWizardEntityUtils.validateWizardHasConnector(this, var1);
      }
   }

   public boolean isAutoResponse() {
      return this.getBooleanProperty("auto_response", false);
   }

   public void setAutoResponse(boolean var1) {
      this.setBooleanProperty("auto_response", var1);
   }

   @Override
   public void setFileEncodingName(String var1) {
      this.setStringProperty("target_encoding", var1);
   }

   @Override
   public String getFileEncodingName() {
      synchronized (this.getDataLock()) {
         return this.getProperty("target_encoding", "");
      }
   }

   public boolean isTargetHeaderSupported() {
      WizardItemInfo var1 = new WizardItemInfo(this);
      return var1.writesToFlatTextFile();
   }

   public TargetHeaderMode getTargetHeaderMode() {
      synchronized (this.getDataLock()) {
         int var2 = this.getIntProperty("target_header_mode", -1);
         return EnumWithIdLookup.lookupOr(var2, TargetHeaderMode.NONE);
      }
   }

   public void setTargetHeaderMode(TargetHeaderMode var1) {
      this.setEnumWithIntIdProperty("target_header_mode", var1, TargetHeaderMode.NONE);
   }

   public boolean isUsingBuiltInRequest() {
      WizardItemBrowser var1 = new WizardItemBrowser(this);
      return var1.getRequestTransformation() == SystemTransformation.SALESFORCE_QUERY_REQUEST;
   }
}
