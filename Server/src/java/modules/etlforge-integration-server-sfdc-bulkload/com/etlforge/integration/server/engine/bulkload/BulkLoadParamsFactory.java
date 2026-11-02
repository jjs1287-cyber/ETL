package com.etlforge.integration.server.engine.bulkload;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.StartAtRowProperty;
import org.etlforge.integration.salesforce.bulkload.BatchFileParams;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.integration.salesforce.bulkload.CreateJobParams;
import org.etlforge.integration.salesforce.bulkload.FieldListEncoder;
import org.etlforge.integration.salesforce.bulkload.TimeZoneId;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.trandb.ProxySettingsSupplier;
import org.etlforge.integration.server.db.trandb.SalesforceBulkLoadsTab;
import org.etlforge.integration.server.db.trandb.SalesforceBulkLoadsTabRow;
import org.etlforge.util.net.ProxySettings;

public final class BulkLoadParamsFactory {
   private final Connection connection;
   private BulkLoadParams params;

   public BulkLoadParamsFactory(Connection var1) {
      this.connection = var1;
   }

   public BulkLoadParamsFactory(Connection var1, BulkLoadParams var2) {
      this.connection = var1;
      this.params = var2;
   }

   public BulkLoadParams fromId(long var1) throws ServerDbException {
      try {
         SalesforceBulkLoadsTab var3 = new SalesforceBulkLoadsTab(this.connection);
         SalesforceBulkLoadsTabRow var4 = var3.query(var1);
         if (var4 == null) {
            throw new IllegalArgumentException("Unknown Salesforce Bulk Load ID: " + var1);
         } else {
            return this.fromRow(var1, var4).fillInOtherProperties(var1);
         }
      } catch (SQLException var5) {
         throw new ServerDbException(var5);
      }
   }

   private BulkLoadParamsFactory fromRow(long var1, SalesforceBulkLoadsTabRow var3) throws SQLException {
      if (this.params == null) {
         this.params = new BulkLoadParams();
      }

      this.params.setCreateJobParams(this.getCreateJobParams(var1, var3)).setOperationId(var3.get_operation_id());
      if (!this.params.getCreateJobParams().isQuery()) {
         this.params
            .setBatchFileParams(this.getBatchFileParams(var3))
            .setDateConversionParams(this.getDateConversionParams(var3))
            .setSourceFileHasHeader(this.getSourceHasHeader(var3))
            .setSourceColumns(this.getSourceColumns(var3))
            .setTargetFields(this.getTargetFields(var3))
            .setProxySettings(this.getProxySettings());
      }

      return this;
   }

   private CreateJobParams getCreateJobParams(long var1, SalesforceBulkLoadsTabRow var3) throws SQLException {
      String var4 = var3.get_action();
      String var5 = var3.get_salesforce_object();
      String var6 = var3.get_external_id();
      SalesforceBulkLoadPropertiesTab var7 = new SalesforceBulkLoadPropertiesTab(this.connection);
      boolean var8 = var7.getBoolean(var1, "serial_mode", false);
      String var9 = var7.query(var1, "assignment_rule");
      return new CreateJobParams(var5, BulkAction.valueOf(var4), var6, var8, var9);
   }

   private BatchFileParams getBatchFileParams(SalesforceBulkLoadsTabRow var1) {
      return new BatchFileParams(var1.get_recs_per_batch(), var1.get_chars_per_batch());
   }

   private DateConversionParams getDateConversionParams(SalesforceBulkLoadsTabRow var1) {
      String var2 = var1.get_date_fmt();
      String var3 = var1.get_date_fmt();
      if (StringUtils.isNotEmpty(var2) && StringUtils.isNotEmpty(var3)) {
         String var4 = var1.get_timezone();
         return new DateConversionParams(TimeZoneId.valueOf(var4), var2, var3);
      } else {
         return null;
      }
   }

   private boolean getSourceHasHeader(SalesforceBulkLoadsTabRow var1) {
      return !var1.get_source_header().isEmpty();
   }

   private int[] getSourceColumns(SalesforceBulkLoadsTabRow var1) {
      String var2 = var1.get_source_column_indices();
      String[] var3 = var2.split(",");
      int[] var4 = new int[var3.length];

      for (int var5 = 0; var5 < var3.length; var5++) {
         var4[var5] = Integer.parseInt(var3[var5]);
      }

      return var4;
   }

   private List<SObjectSchema.Field> getTargetFields(SalesforceBulkLoadsTabRow var1) {
      String var2 = var1.get_target_fields();
      return FieldListEncoder.decode(var2);
   }

   private ProxySettings getProxySettings() {
      ProxySettingsSupplier var1 = new ProxySettingsSupplier(this.connection);
      return var1.get();
   }

   private BulkLoadParams fillInOtherProperties(long var1) throws SQLException {
      SalesforceBulkLoadPropertiesTab var3 = new SalesforceBulkLoadPropertiesTab(this.connection);
      boolean var4 = var3.getBoolean(var1, "compress", true);
      this.params.setCompress(var4);
      Charset var5 = var3.getFileEncoding(var1);
      this.params.setEncoding(var5);
      StartAtRowProperty var6 = var3.getStartAtRow(var1);
      this.params.setStartAtRow(var6);
      ProxySettings var7 = this.getProxySettings();
      this.params.setProxySettings(var7);
      return this.params;
   }
}
