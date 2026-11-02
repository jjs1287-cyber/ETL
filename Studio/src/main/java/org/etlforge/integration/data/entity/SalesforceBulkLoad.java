package org.etlforge.integration.data.entity;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.axis.utils.StringUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.sfdc.DateConversionProperties;
import org.etlforge.integration.data.entity.sfdc.SalesforceWizardEntityUtils;
import org.etlforge.integration.data.entity.sfdc.SoapHeaders;
import org.etlforge.integration.data.entity.sfdc.WizardEntityLinkCollector;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.SoapHeaderUtils;
import org.etlforge.integration.salesforce.StartAtRowProperty;
import org.etlforge.integration.salesforce.bulkload.BatchFileParams;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.bulkload.FieldListEncoder;
import org.etlforge.integration.salesforce.bulkload.HeaderMappings;
import org.etlforge.integration.salesforce.bulkload.TargetFields;
import org.etlforge.integration.salesforce.wsdl.RelatedObject;
import org.etlforge.integration.salesforce.wsdl.RelatedObjectsListEncoder;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.lang.Objects;
import org.etlforge.util.version.Version;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public final class SalesforceBulkLoad extends IntegrationEntity implements SalesforceModifier {
   public static final String OBJECT_NAME = "salesforce_object";
   public static final String ACTION = "action";
   public static final String EXTERNAL_ID = "external_id";
   private static final String MAPPED_TARGET_FIELDS = "target_fields";
   private static final String RELATED_OBJECTS = "related_objects";
   private static final String ALL_TARGET_FIELDS = "all_target_fields";
   private static final String SOURCE_HEADER = "source_header";
   private static final String SOURCE_COLUMN_INDICES = "source_column_indices";
   public static final String BATCH_PARAMS = "batch_params";
   private static final String RECS_PER_BATCH = "recs_per_batch";
   private static final String CHARS_PER_BATCH = "chars_per_batch";
   private static final String OPERATION_ID = "operation_id";
   @IdProperty
   public static final String SALESFORCE_BULK_LOAD_ID = "salesforcebulkload_id";
   @NameProperty
   public static final String SALESFORCE_BULK_LOAD_NAME = "salesforcebulkload_name";
   public static final String INSERT_NULLS = "insert_null_values";
   public static final String SERIAL_MODE = "serial_mode";
   public static final String COMPRESS = "compress";
   public static final String FILE_ENCODING = "file_encoding";
   public static final String ASSIGNMENT_RULE_ID = "assignment_rule";
   public static final String QUERY_STRING = "query_string";
   public static final String HEADER_MAPPINGS = "header_mappings";
   private static final Version VERSION_INTRODUCED = VersionFactory.newVersion(5, 0, 0, 20);

   public SalesforceBulkLoad(String var1) {
      this(null, var1);
   }

   public SalesforceBulkLoad(SalesforceBulkLoadId var1, String var2) {
      super(EntityType.SalesforceBulkLoad, var1, var2);
   }

   public SalesforceBulkLoadId getID() {
      return (SalesforceBulkLoadId)super.getID();
   }

   @Override
   public OperationId getImplementingOperationId() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("operation_id", null);
         return var2 == null ? null : new OperationId(var2);
      }
   }

   @Override
   public void setImplementingOperation(Operation var1) {
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
         "salesforcebulkload_id",
         "salesforcebulkload_name",
         "operation_id",
         "salesforce_object",
         "action",
         "external_id",
         "target_fields",
         "all_target_fields",
         "source_header",
         "source_column_indices",
         "related_objects",
         "recs_per_batch",
         "chars_per_batch",
         "timezone",
         "date_fmt",
         "datetime_fmt",
         "insert_null_values",
         "serial_mode",
         "compress",
         "file_encoding",
         "start_at_row",
         "assignment_rule",
         "query_string",
         "connector_id"
      );
      var1.addAll(SoapHeaders.getExistingKeys(this));
      return (String[]) var1.toArray(new String[var1.size()]);
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      return WizardEntityLinkCollector.collectLinks(this, var1);
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
   public void replaceReferences(ReferenceReplacementService var1) {
      OperationId var2 = this.getImplementingOperationId();
      if (var2 != null) {
         Operation var3 = var1.getReplacement(var2, Operation.class);
         if (var3 != null) {
            this.setImplementingOperation(var3);
         }
      }
   }

   @Override
   public ExpandedName getObject() {
      XmlNameFactory var1 = XmlNameFactory.conforming();
      synchronized (this.getDataLock()) {
         return var1.newExpandedName("urn:partner.soap.sforce.com", this.getProperty("salesforce_object"));
      }
   }

   public String getObjectName() {
      synchronized (this.getDataLock()) {
         return this.getProperty("salesforce_object");
      }
   }

   public void setObjectName(String var1) {
      this.setStringProperty("salesforce_object", var1);
   }

   public BulkAction getAction() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("action");
         return var2 == null ? null : BulkAction.valueOf(var2);
      }
   }

   public void setAction(BulkAction var1) {
      this.setStringProperty("action", var1.name());
   }

   public String getExternalIdName() {
      synchronized (this.getDataLock()) {
         return this.getProperty("external_id");
      }
   }

   public void setExternalIdName(String var1) {
      this.setStringProperty("external_id", var1);
   }

   public Optional<DateConversionParams> getDateConversionParams() {
      return DateConversionProperties.restore(this);
   }

   public void setDateConversionParams(Optional<DateConversionParams> var1) {
      Optional var2 = DateConversionProperties.store(this, var1);
      if (!Objects.equal(var2, var1)) {
         this.firePropertyChange("date_conversion_params", var2, var1);
      }
   }

   public BatchFileParams getBatchParams() {
      synchronized (this.getDataLock()) {
         int var2 = this.getIntProperty("recs_per_batch", 10000);
         int var3 = this.getIntProperty("chars_per_batch", 10000000);
         return new BatchFileParams(var2, var3);
      }
   }

   public void setBatchParams(BatchFileParams var1) {
      BatchFileParams var2;
      synchronized (this.getDataLock()) {
         var2 = this.getBatchParams();
         this.setProperty("recs_per_batch", var1.recordsPerFile);
         this.setProperty("chars_per_batch", var1.charactersPerFile);
      }

      if (!Objects.equal(var2, var1)) {
         this.firePropertyChange("batch_params", var2, var1);
      }
   }

   public HeaderMappings getHeaderMappings() {
      synchronized (this.getDataLock()) {
         List var2 = this.getSourceFileHeader();
         if (var2 != null) {
            List var3 = this.getTargetFields("all_target_fields");
            List var4 = this.getRelatedObjects();
            TargetFields var5 = new TargetFields(var3, var4);
            List var6 = this.getSourceColumnIndeces();
            List var7 = this.getTargetFields("target_fields");
            return new HeaderMappings(var2, var5, var6, var7);
         } else {
            return null;
         }
      }
   }

   private List<String> getSourceFileHeader() {
      String var1 = this.getProperty("source_header");
      return var1 == null ? null : Arrays.asList(var1.split(","));
   }

   private List<SObjectSchema.Field> getTargetFields(String var1) {
      String var2 = this.getProperty(var1);
      return FieldListEncoder.decode(var2);
   }

   private List<RelatedObject> getRelatedObjects() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("related_objects");
         return RelatedObjectsListEncoder.decode(var2);
      }
   }

   private List<Integer> getSourceColumnIndeces() {
      String var1 = this.getProperty("source_column_indices");
      if (StringUtils.isEmpty(var1)) {
         return Collections.emptyList();
      }

      String[] var2 = var1.split(",");
      ArrayList var3 = Lists.newArrayList();

      for (String var7 : var2) {
         var3.add(Integer.valueOf(var7));
      }

      return var3;
   }

   public void setHeaderMappings(HeaderMappings var1) {
      HeaderMappings var2;
      synchronized (this.getDataLock()) {
         var2 = this.getHeaderMappings();
         this.setSourceFileHeader(var1.getSourceHeader());
         this.setTargetFields(var1.getObjectFields(), "all_target_fields");
         this.setTargetFields(var1.getMappedTargetFields(), "target_fields");
         this.setRelatedObjects(var1.getRelatedObjects());
         this.setSourceColumnIndices(var1.getSourceColumnIndices());
      }

      if (!var1.equals(var2)) {
         this.firePropertyChange("header_mappings", var2, var1);
      }
   }

   private void setSourceFileHeader(List<String> var1) {
      String var2 = var1 == null ? null : Collections2.join(var1, ",");
      this.setProperty("source_header", var2);
   }

   private void setTargetFields(Collection<SObjectSchema.Field> var1, String var2) {
      String var3 = FieldListEncoder.encode(var1);
      this.setProperty(var2, var3);
   }

   private void setRelatedObjects(Collection<RelatedObject> var1) {
      String var2 = RelatedObjectsListEncoder.encode(var1);
      this.setStringProperty("related_objects", var2);
   }

   private void setSourceColumnIndices(List<Integer> var1) {
      String var2 = Collections2.join(var1, ",");
      this.setProperty("source_column_indices", var2);
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
      return SfdcMethod.BULK_LOAD;
   }

   public boolean getInsertNullValues() {
      return this.getBooleanProperty("insert_null_values", false);
   }

   public void setInsertNullValues(boolean var1) {
      this.setBooleanProperty("insert_null_values", var1);
   }

   public boolean isSerialMode() {
      return this.getBooleanProperty("serial_mode", false);
   }

   public void setSerialMode(boolean var1) {
      this.setBooleanProperty("serial_mode", var1);
   }

   public boolean isCompressionEnabled() {
      return this.getBooleanProperty("compress", true);
   }

   public void setCompressionEnabled(boolean var1) {
      this.setBooleanProperty("compress", var1, true);
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      SalesforceWizardEntityUtils.validateWizardHasConnector(this, var1);
   }

   @Override
   public Version getRequiredVersion() {
      return VERSION_INTRODUCED;
   }

   @Override
   public final void setFileEncodingName(String var1) {
      this.setStringProperty("file_encoding", var1);
   }

   @Override
   public final String getFileEncodingName() {
      synchronized (this.getDataLock()) {
         return this.getProperty("file_encoding", "");
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
   public void setSourceFileHasHeader(boolean var1) {
      throw new UnsupportedOperationException();
   }

   @Override
   public boolean getSourceFileHasHeader() {
      return true;
   }

   public String getAssignmentRuleId() {
      synchronized (this.getDataLock()) {
         return this.getProperty("assignment_rule", "");
      }
   }

   public void setAssignmentRuleId(String var1) {
      this.setStringProperty("assignment_rule", var1);
   }

   public boolean isAssignmentRuleSupported() {
      synchronized (this.getDataLock()) {
         SfdcMethod var2 = this.getAction().toSfdcMethod();
         return SoapHeaderUtils.isAssignmentRuleApplicable(var2, this.getObject());
      }
   }

   public String getQueryString() {
      synchronized (this.getDataLock()) {
         return this.getProperty("query_string", null);
      }
   }

   public void setQueryString(String var1) {
      this.setStringProperty("query_string", var1);
   }

   public boolean isQuery() {
      return this.getAction() == BulkAction.QUERY;
   }

   public boolean isLoad() {
      return !this.isQuery();
   }
}
