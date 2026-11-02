package com.etlforge.integration.server.engine.bulkload.db;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstance;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceFilter;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceId;
import org.etlforge.integration.salesforce.bulkload.backend.FileStats;
import org.etlforge.integration.salesforce.bulkload.backend.SuccessAndFailureCounts;
import org.etlforge.integration.salesforce.bulkload.backend.TimeWindowFilter;
import org.etlforge.integration.server.exception.NotDeployedException;
import org.etlforge.integration.server.idmappings.GuidToIdMapper;
import org.etlforge.integration.server.idmappings.GuidToIdMappingException;

public final class BulkLoadInstanceTabQuery {
   private final List<BulkLoadInstanceTabQuery.Parameter> params = Lists.newArrayList();
   private final Connection connection;
   private final BulkLoadInstanceFilter filter;
   private final GuidToIdMapper idMapper;

   public BulkLoadInstanceTabQuery(Connection var1, BulkLoadInstanceFilter var2, GuidToIdMapper var3) {
      this.connection = var1;
      this.filter = var2;
      this.idMapper = var3;
   }

   public List<BulkLoadInstance> listInstances() throws SQLException, NotDeployedException, GuidToIdMappingException {
      long var1 = this.getBulkLoadId();
      ArrayList var3 = Lists.newArrayList();
      PreparedStatement var4 = this.prepareStatement(var1);
      ResultSet var5 = null;

      try {
         var5 = var4.executeQuery();

         while (var5.next()) {
            var3.add(this.toInstance(var5));
         }
      } finally {
         KongaDbUtils.close(var5);
         KongaDbUtils.close(var4);
      }

      return var3;
   }

   private long getBulkLoadId() throws NotDeployedException, GuidToIdMappingException {
      Long var1 = this.idMapper.getId(this.filter.getBulkloadGuid());
      if (var1 == null) {
         throw new NotDeployedException(this.filter.getBulkloadGuid());
      } else {
         return var1;
      }
   }

   private PreparedStatement prepareStatement(long var1) throws SQLException, GuidToIdMappingException {
      String var3 = this.buildSql(var1);
      PreparedStatement var4 = this.connection.prepareStatement(var3);

      for (int var5 = 0; var5 < this.params.size(); var5++) {
         this.params.get(var5).apply(var4, var5 + 1);
      }

      return var4;
   }

   private String buildSql(long var1) throws GuidToIdMappingException {
      String var3 = "i.operation_instance_id, i.last_operation_instance_id, i.bulkload_id, i.sfdc_job_id, i.start_time, i.end_time, i.filename, i.file_size, i.start_pos";
      StringBuilder var4 = new StringBuilder("SELECT ");
      var4.append(var3);
      var4.append(
         ", SUM(b.successrecords), SUM(b.failurerecords) FROM bulkload_instance_tab i LEFT JOIN bulkload_batch_tab b on i.operation_instance_id = b.operation_instance_id WHERE i.bulkload_id = ?"
      );
      this.params.add(new BulkLoadInstanceTabQuery.LongParameter(var1));
      TimeWindowFilter var5 = this.filter.getTimeWindowFilter();
      if (var5.getStartCutoff() != null) {
         var4.append(" AND i.start_time >= ?");
         this.params.add(new BulkLoadInstanceTabQuery.TimestampParameter(var5.getStartCutoff()));
      }

      if (var5.getEndCutoff() != null) {
         var4.append(" AND i.start_time <= ?");
         this.params.add(new BulkLoadInstanceTabQuery.TimestampParameter(var5.getEndCutoff()));
      }

      var4.append(" GROUP BY ").append(var3);
      var4.append(" ORDER BY i.start_time DESC");
      if (this.filter.getMaxRecordsToGet() != null) {
         var4.append(" LIMIT ?");
         this.params.add(new BulkLoadInstanceTabQuery.IntParameter(this.filter.getMaxRecordsToGet()));
      }

      return var4.toString();
   }

   private BulkLoadInstance toInstance(ResultSet var1) throws SQLException, GuidToIdMappingException {
      BulkLoadInstanceId var2 = new BulkLoadInstanceId(
         (SalesforceBulkLoadId)this.idMapper.getGuid(EntityType.SalesforceBulkLoad, var1.getLong(3)), var1.getLong(1)
      );
      BulkLoadInstance var3 = new BulkLoadInstance(var2, var1.getTimestamp(5));
      long var4 = var1.getLong(2);
      if (var1.wasNull()) {
         var3.setLastOperationInstanceId(null);
      } else {
         var3.setLastOperationInstanceId(var4);
      }

      var3.setJobId(var1.getString(4));
      var3.setEndTime(var1.getTimestamp(6));
      var3.setFilePath(var1.getString(7));
      long var6 = var1.getLong(8);
      if (!var1.wasNull()) {
         int var8 = var1.getInt(9);
         if (!var1.wasNull()) {
            var3.setFileStats(new FileStats(var6, var8));
         }
      }

      long var12 = Math.max(var1.getLong(10), 0L);
      long var10 = Math.max(var1.getLong(11), 0L);
      var3.setSuccessAndFailureCounts(new SuccessAndFailureCounts(var12, var10));
      return var3;
   }

   private static class IntParameter implements BulkLoadInstanceTabQuery.Parameter {
      private final int value;

      public IntParameter(int var1) {
         this.value = var1;
      }

      @Override
      public void apply(PreparedStatement var1, int var2) throws SQLException {
         var1.setInt(var2, this.value);
      }
   }

   private static class LongParameter implements BulkLoadInstanceTabQuery.Parameter {
      private final long value;

      public LongParameter(long var1) {
         this.value = var1;
      }

      @Override
      public void apply(PreparedStatement var1, int var2) throws SQLException {
         var1.setLong(var2, this.value);
      }
   }

   private interface Parameter {
      void apply(PreparedStatement var1, int var2) throws SQLException;
   }

   private static class TimestampParameter implements BulkLoadInstanceTabQuery.Parameter {
      private final Timestamp value;

      public TimestampParameter(Date var1) {
         this.value = new Timestamp(var1.getTime());
      }

      @Override
      public void apply(PreparedStatement var1, int var2) throws SQLException {
         var1.setTimestamp(var2, this.value);
      }
   }
}
