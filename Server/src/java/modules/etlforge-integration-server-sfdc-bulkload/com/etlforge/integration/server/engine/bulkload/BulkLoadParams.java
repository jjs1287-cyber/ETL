package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Preconditions;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.StartAtRowProperty;
import org.etlforge.integration.salesforce.bulkload.BatchFileParams;
import org.etlforge.integration.salesforce.bulkload.CreateJobParams;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.util.name.Nameables;
import org.etlforge.util.net.ProxySettings;

public final class BulkLoadParams {
   private CreateJobParams createJobParams;
   private BatchFileParams batchFileParams;
   private DateConversionParams dateConversionParams;
   private List<SObjectSchema.Field> targetFields;
   private boolean sourceFileHasHeader;
   private File sourceFile;
   private File targetFile;
   private String queryString;
   private BulkLoadEndpoint endpoint;
   private int operationId;
   private long operationInstance;
   private long lastOperationInstance = -1L;
   private long bulkloadId;
   private int[] sourceColumns = new int[0];
   private boolean compress;
   private Charset encoding = Charset.defaultCharset();
   private StartAtRowProperty startAtRow = StartAtRowProperty.DEFAULT;
   private ProxySettings proxySettings = ProxySettings.NONE;

   public boolean isQuery() {
      return StringUtils.isNotEmpty(this.queryString);
   }

   public CreateJobParams getCreateJobParams() {
      return this.createJobParams;
   }

   public BulkLoadParams setCreateJobParams(CreateJobParams var1) {
      this.createJobParams = var1;
      return this;
   }

   public BatchFileParams getBatchFileParams() {
      return this.batchFileParams;
   }

   public BulkLoadParams setBatchFileParams(BatchFileParams var1) {
      this.batchFileParams = var1;
      return this;
   }

   public DateConversionParams getDateConversionParams() {
      return this.dateConversionParams;
   }

   public BulkLoadParams setDateConversionParams(DateConversionParams var1) {
      this.dateConversionParams = var1;
      return this;
   }

   public List<SObjectSchema.Field> getTargetFields() {
      return this.targetFields;
   }

   public BulkLoadParams setTargetFields(List<SObjectSchema.Field> var1) {
      this.targetFields = var1;
      return this;
   }

   public String getTargetHeader() {
      return Collections2.join(this.targetFields, Nameables.toNameFunction(), ",");
   }

   public boolean getSourceFileHasHeader() {
      return this.sourceFileHasHeader;
   }

   public BulkLoadParams setSourceFileHasHeader(boolean var1) {
      this.sourceFileHasHeader = var1;
      return this;
   }

   public File getSourceFile() {
      return this.sourceFile;
   }

   public File getTargetFile() {
      return this.targetFile;
   }

   public File getParentFile() {
      if (this.sourceFile != null) {
         return this.sourceFile.getParentFile();
      } else {
         return this.targetFile != null ? this.targetFile.getParentFile() : null;
      }
   }

   public String getQueryString() {
      return this.queryString;
   }

   public BulkLoadParams setSourceFile(File var1) {
      this.sourceFile = var1;
      return this;
   }

   public BulkLoadParams setTargetFile(File var1) {
      this.targetFile = var1;
      return this;
   }

   public BulkLoadParams setQueryString(String var1) {
      this.queryString = var1;
      return this;
   }

   public BulkLoadEndpoint getEndpoint() {
      return this.endpoint;
   }

   public BulkLoadParams setEndpoint(BulkLoadEndpoint var1) {
      this.endpoint = var1;
      return this;
   }

   public long getOperationInstance() {
      return this.operationInstance;
   }

   public BulkLoadParams setOperationInstance(long var1) {
      this.operationInstance = var1;
      return this;
   }

   public long getLastOperationInstance() {
      return this.lastOperationInstance;
   }

   public BulkLoadParams setLastOperationInstance(long var1) {
      this.lastOperationInstance = var1;
      return this;
   }

   public int getOperationId() {
      return this.operationId;
   }

   public BulkLoadParams setOperationId(int var1) {
      this.operationId = var1;
      return this;
   }

   public long getBulkloadId() {
      return this.bulkloadId;
   }

   public BulkLoadParams setBulkloadId(long var1) {
      this.bulkloadId = var1;
      return this;
   }

   public int[] getSourceColumns() {
      return (int[])this.sourceColumns.clone();
   }

   public BulkLoadParams setSourceColumns(int[] var1) {
      this.sourceColumns = (int[])var1.clone();
      return this;
   }

   public BulkLoadParams setCompress(boolean var1) {
      this.compress = var1;
      return this;
   }

   public boolean getCompress() {
      return this.compress;
   }

   public BulkLoadParams setEncoding(Charset var1) {
      this.encoding = Preconditions.checkNotNull(var1);
      return this;
   }

   public Charset getEncoding() {
      return this.encoding;
   }

   public BulkLoadParams setStartAtRow(StartAtRowProperty var1) {
      this.startAtRow = Preconditions.checkNotNull(var1);
      return this;
   }

   public long getStartAtRow() {
      return this.startAtRow.getRow();
   }

   public BulkLoadParams setProxySettings(ProxySettings var1) {
      this.proxySettings = Preconditions.checkNotNull(var1);
      return this;
   }

   public ProxySettings getProxySettings() {
      return this.proxySettings;
   }
}
