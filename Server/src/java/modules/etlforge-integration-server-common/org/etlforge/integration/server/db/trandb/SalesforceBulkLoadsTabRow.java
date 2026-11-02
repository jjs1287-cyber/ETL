package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.db.ColumnData;

public final class SalesforceBulkLoadsTabRow implements TranDbTableRow {
   private final ColumnData<Integer> salesforcebulkload_id = ColumnData.newIntColumn("salesforcebulkload_id");
   private final ColumnData<String> salesforcebulkload_name = ColumnData.newStringColumn("salesforcebulkload_name");
   private final ColumnData<Integer> operation_id = ColumnData.newIntColumn("operation_id");
   private final ColumnData<String> salesforce_object = ColumnData.newStringColumn("salesforce_object");
   private final ColumnData<String> action = ColumnData.newStringColumn("action");
   private final ColumnData<String> external_id = ColumnData.newStringColumn("external_id");
   private final ColumnData<String> source_header = ColumnData.newStringColumn("source_header");
   private final ColumnData<String> source_column_indices = ColumnData.newStringColumn("source_column_indices");
   private final ColumnData<String> target_fields = ColumnData.newStringColumn("target_fields");
   private final ColumnData<Integer> recs_per_batch = ColumnData.newIntColumn("recs_per_batch");
   private final ColumnData<Integer> chars_per_batch = ColumnData.newIntColumn("chars_per_batch");
   private final ColumnData<String> timezone = ColumnData.newStringColumn("timezone");
   private final ColumnData<String> date_fmt = ColumnData.newStringColumn("date_fmt");
   private final ColumnData<String> datetime_fmt = ColumnData.newStringColumn("datetime_fmt");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.salesforcebulkload_id.fetchValue(var1);
      this.salesforcebulkload_name.fetchValue(var1);
      this.operation_id.fetchValue(var1);
      this.salesforce_object.fetchValue(var1);
      this.action.fetchValue(var1);
      this.external_id.fetchValue(var1);
      this.source_header.fetchValue(var1);
      this.source_column_indices.fetchValue(var1);
      this.target_fields.fetchValue(var1);
      this.recs_per_batch.fetchValue(var1);
      this.chars_per_batch.fetchValue(var1);
      this.timezone.fetchValue(var1);
      this.date_fmt.fetchValue(var1);
      this.datetime_fmt.fetchValue(var1);
   }

   public Integer get_salesforcebulkload_id() {
      return this.salesforcebulkload_id.getValue();
   }

   public String get_salesforcebulkload_name() {
      return this.salesforcebulkload_name.getValue();
   }

   public Integer get_operation_id() {
      return this.operation_id.getValue();
   }

   public String get_salesforce_object() {
      return this.salesforce_object.getValue();
   }

   public String get_action() {
      return this.action.getValue();
   }

   public String get_external_id() {
      return this.external_id.getValue();
   }

   public String get_source_header() {
      return this.source_header.getValue();
   }

   public String get_source_column_indices() {
      return this.source_column_indices.getValue();
   }

   public String get_target_fields() {
      return this.target_fields.getValue();
   }

   public Integer get_recs_per_batch() {
      return this.recs_per_batch.getValue();
   }

   public Integer get_chars_per_batch() {
      return this.chars_per_batch.getValue();
   }

   public String get_timezone() {
      return this.timezone.getValue();
   }

   public String get_date_fmt() {
      return this.date_fmt.getValue();
   }

   public String get_datetime_fmt() {
      return this.datetime_fmt.getValue();
   }
}
