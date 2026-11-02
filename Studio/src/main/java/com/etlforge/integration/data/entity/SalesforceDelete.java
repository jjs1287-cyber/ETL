package com.etlforge.integration.data.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IdProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.NameProperty;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceModifier;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.id.SalesforceDeleteId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.WebServiceOperationType;
import org.etlforge.integration.data.entity.sfdc.SalesforceWizardEntityUtils;
import org.etlforge.integration.data.entity.sfdc.SalesforceWizardReferencesReplacer;
import org.etlforge.integration.data.entity.sfdc.SoapHeaders;
import org.etlforge.integration.data.entity.sfdc.WizardEntityLinkCollector;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.StartAtRowProperty;
import org.etlforge.util.version.Version;
import org.etlforge.xml.ExpandedName;

public final class SalesforceDelete extends IntegrationEntity implements SalesforceModifier {
   private static final Version VERSION_INTRODUCED = VersionFactory.newVersion(4, 1, 0, 15);
   public static final String OPERATION_ID = "operation_id";
   @IdProperty
   public static final String SALESFORCE_DELETE_ID = "salesforcedelete_id";
   @NameProperty
   public static final String SALESFORCE_DELETE_NAME = "salesforcedelete_name";

   public SalesforceDelete(String var1) {
      this(null, var1);
   }

   public SalesforceDelete(SalesforceDeleteId var1, String var2) {
      super(EntityType.SalesforceDelete, var1, var2);
   }

   public SalesforceDeleteId getID() {
      return (SalesforceDeleteId)super.getID();
   }

   @Override
   public Version getRequiredVersion() {
      return VERSION_INTRODUCED;
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      return WizardEntityLinkCollector.collectLinks(this, var1);
   }

   @Override
   public OperationId getImplementingOperationId() {
      return this.getProjectItemId("operation_id", EntityType.Operation, OperationId.class);
   }

   private <T extends IntegrationEntityId> T getProjectItemId(String var1, EntityType var2, Class<T> var3) {
      synchronized (this.getDataLock()) {
         String var5 = this.getProperty(var1, null);
         return (T)(var5 == null ? null : var3.cast(var2.getIntegrationId(var5)));
      }
   }

   @Override
   public void setImplementingOperation(Operation var1) {
      Preconditions.checkArgument(var1.getOperationType() == WebServiceOperationType.TYPE, "op must implement a web service call");
      this.setStringProperty("operation_id", var1.getID().toString());
   }

   @Override
   public SalesforceConnectorId getConnectorId() {
      return SalesforceWizardEntityUtils.getConnectorId(this);
   }

   @Override
   public void setConnector(SalesforceConnector var1) {
      SalesforceWizardEntityUtils.setConnector(this, var1);
   }

   @Override
   public String[] getPropertyKeys() {
      ArrayList var1 = Lists.newArrayList(
         "salesforcedelete_id", "salesforcedelete_name", "operation_id", "connector_id", "source_file_has_header", "start_at_row", "source_encoding"
      );
      var1.addAll(SoapHeaders.getExistingKeys(this));
      return (String[]) var1.toArray(new String[var1.size()]);
   }

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      SalesforceWizardReferencesReplacer var2 = new SalesforceWizardReferencesReplacer(var1);
      var2.replaceReferences(this);
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
      return SfdcMethod.DELETE;
   }

   @Override
   public ExpandedName getObject() {
      return null;
   }

   public boolean isBasedOnPartnerWsdl() {
      return !this.getSoapHeaders().isEmpty();
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      if (this.isBasedOnPartnerWsdl()) {
         SalesforceWizardEntityUtils.validateWizardHasConnector(this, var1);
      }
   }

   @Override
   public final void setSourceFileHasHeader(boolean var1) {
      this.setBooleanProperty("source_file_has_header", var1);
   }

   @Override
   public final boolean getSourceFileHasHeader() {
      synchronized (this.getDataLock()) {
         return this.getBooleanProperty("source_file_has_header", false);
      }
   }

   @Override
   public final void setStartAtRow(StartAtRowProperty var1) {
      this.setLongProperty("start_at_row", var1.getRow());
   }

   @Override
   public final StartAtRowProperty getStartAtRow() {
      synchronized (this.getDataLock()) {
         long var2 = StartAtRowProperty.firstRow();
         long var4 = Math.max(var2, this.getLongProperty("start_at_row", var2));
         return new StartAtRowProperty(var4);
      }
   }

   @Override
   public final void setFileEncodingName(String var1) {
      this.setStringProperty("source_encoding", var1);
   }

   @Override
   public final String getFileEncodingName() {
      synchronized (this.getDataLock()) {
         return this.getProperty("source_encoding", "");
      }
   }
}
