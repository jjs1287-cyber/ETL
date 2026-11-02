package com.etlforge.integration.server.engine.bulkload.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.etlforge.integration.server.engine.bulkload.ETLForgeSystemProperty;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadBatchInfo;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadJobInfo;
import org.etlforge.system.Disposable;

public class BulkLoadJobInfoUpdate implements Disposable {
   private final Connection connection;
   private final long operationInstanceId;

   public BulkLoadJobInfoUpdate(Connection var1, long var2) {
      this.connection = Preconditions.checkNotNull(var1, "connection");
      this.operationInstanceId = var2;
   }

   public long getOperationInstanceId() {
      return this.operationInstanceId;
   }

   private String InstanceSQLWhereClause() {
      return " WHERE operation_instance_id = " + this.getOperationInstanceId();
   }

   public BulkLoadJobInfo getBulkLoadJobInfo() throws SQLException {
      BulkLoadJobInfo var1 = null;
      Statement var2 = this.connection.createStatement();
      ResultSet var3 = null;

      try {
         var3 = var2.executeQuery("SELECT * FROM bulkload_instance_view" + this.InstanceSQLWhereClause());
         if (var3.next()) {
            Charset var4 = ETLForgeSystemProperty.getCharset(var3.getString("filecharencode"));
            var1 = new BulkLoadJobInfo(var3.getString("sfdc_job_id"), var3.getInt("status"), var3.getString("filename"), var4);
            var1.setOperationid(var3.getInt("operation_id"));
            var1.setBulkloadid(var3.getInt("bulkload_id"));
            var1.setSalesforceAction(var3.getString("bulkloadaction"));
            var1.setHeader(var3.getString("source_header"));
         }

         return var1;
      } finally {
         KongaDbUtils.close(var3);
         KongaDbUtils.close(var2);
      }
   }

   public List<BulkLoadBatchInfo> getBulkLoadBatchInfoList() throws SQLException {
      Statement var1 = this.connection.createStatement();
      ResultSet var2 = null;

      try {
         var2 = var1.executeQuery("SELECT * FROM bulkload_batch_tab" + this.InstanceSQLWhereClause() + " order by start_pos");
         ArrayList var3 = Lists.newArrayList();

         while (var2.next()) {
            var3.add(this.createRecord(var2));
         }

         return var3;
      } finally {
         KongaDbUtils.close(var2);
         KongaDbUtils.close(var1);
      }
   }

   public boolean addOperationFileStatusTab(int var1, long var2, int var4, long var5, String var7, String var8, String var9) throws SQLException {
      String var10 = UUID.randomUUID().toString();
      PreparedStatement var11 = this.connection
         .prepareStatement(
            "INSERT INTO operationfilestatustab(operation_id, operation_instance_id,records,filecategory, filesize, filerelativepathname,filecharencode, fileguid, filestatusmessage) VALUES (?, ?, ?, ?, ?,?,?,?,?)"
         );

      try {
         var11.setInt(1, var1);
         var11.setLong(2, this.getOperationInstanceId());
         var11.setLong(3, var2);
         var11.setInt(4, var4);
         var11.setLong(5, var5);
         var11.setString(6, var7);
         var11.setString(7, var8);
         var11.setString(8, var10);
         var11.setString(9, var9);
         return var11.execute();
      } finally {
         KongaDbUtils.close(var11);
      }
   }

   private BulkLoadBatchInfo createRecord(ResultSet var1) throws SQLException {
      Long var2 = this.getOperationInstanceId();
      long var3 = var1.getLong("processedrecords");
      long var5 = var1.getLong("records");
      BulkLoadBatchInfo var7 = new BulkLoadBatchInfo(
         var2.toString(), var1.getString("batch_id"), var1.getInt("status"), var3, var1.getLong("start_pos"), var1.getLong("end_pos")
      );
      var7.setNumberOfRecordsSent(var5);
      var7.setSuccessRecords(var1.getLong("successrecords"));
      var7.setFailureRecords(var1.getLong("failurerecords"));
      return var7;
   }

   public void insertBulkQueryBatchInfo(BulkLoadBatchInfo var1, BulkLoadJobInfo var2, int var3) throws SQLException {
      int var4 = BatchStatus.ConvertToInt(var1.getStatusEnum());
      String var5 = "INSERT INTO bulkload_batch_tab(operation_instance_id,bulkload_id,batch_index,batch_id,records,processedrecords,successrecords,status,statusmessage,time_sent,last_operation_instance_id,start_pos, end_pos,failurerecords)";
      var5 = var5 + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, -1, -1, -1, 0)";
      PreparedStatement var6 = this.connection.prepareStatement(var5);

      try {
         int var7 = 1;
         var6.setLong(var7++, this.getOperationInstanceId());
         var6.setInt(var7++, var2.getBulkloadid());
         var6.setInt(var7++, var3);
         var6.setString(var7++, var1.getBatchId());
         var6.setLong(var7++, var1.getNumberRecordsProcessed());
         var6.setLong(var7++, var1.getNumberRecordsProcessed());
         var6.setLong(var7++, var1.getNumberRecordsProcessed());
         var6.setInt(var7++, var4);
         var6.setString(var7++, var1.getstatusMessage());
         var6.setTimestamp(var7++, AbstractTable.getTimestamp());
         var6.execute();
      } finally {
         KongaDbUtils.close(var6);
      }
   }

   public void updateBatchInfo(BulkLoadBatchInfo var1) throws SQLException {
      int var2 = BatchStatus.ConvertToInt(var1.getStatusEnum());
      boolean var3 = var1.getStatusEnum() == BatchStatus.NOTPROCESSED || var1.getStatusEnum() == BatchStatus.FAILED;
      String var4 = "UPDATE bulkload_batch_tab set status=?,statusmessage=?,processedrecords=?,successrecords=?,failurerecords=?";
      var4 = var4 + this.InstanceSQLWhereClause() + " and batch_id='" + var1.getBatchId() + "'";
      PreparedStatement var5 = this.connection.prepareStatement(var4);

      try {
         var5.setInt(1, var2);
         var5.setString(2, var1.getstatusMessage());
         if (var3) {
            long var6 = var1.getNumberOfRecordsSent();
            var5.setLong(3, var6);
            var5.setLong(4, 0L);
            var5.setLong(5, var6);
         } else {
            var5.setLong(3, var1.getNumberRecordsProcessed());
            var5.setLong(4, var1.getSuccessRecords());
            var5.setLong(5, var1.getFailureRecords());
         }

         var5.execute();
      } finally {
         KongaDbUtils.close(var5);
      }
   }

   public boolean updateJobInfo(BulkLoadJobInfo.BulkLoadJobInfoEnum var1) throws SQLException {
      int var2 = BulkLoadJobInfo.BulkLoadJobInfoEnum.ConvertToInt(var1);
      String var3 = "UPDATE bulkload_instance_tab set status=? " + this.InstanceSQLWhereClause();
      PreparedStatement var4 = this.connection.prepareStatement(var3);

      try {
         var4.setInt(1, var2);
         var4.execute();
         return true;
      } finally {
         KongaDbUtils.close(var4);
      }
   }

   @Override
   public void dispose() {
      KongaDbUtils.close(this.connection);
   }
}
