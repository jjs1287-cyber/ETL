package com.etlforge.integration.server.engine.bulkload.db;

import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.engine.bulkload.PersistorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.etlforge.integration.salesforce.bulkload.backend.Batch;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.system.Disposable;

public final class BulkLoadBatchTab extends AbstractTable implements Disposable {
   private PreparedStatement insertBatchStmt;
   private int batchIndex;
   int totalBatches;
   int failedBatches;
   StringBuilder failedBatchesMessage;

   public BulkLoadBatchTab(Connection var1, BulkLoadParams var2) {
      super(var1, var2);
   }

   public void recordBatch(Batch var1) throws PersistorException {
      try {
         if (this.insertBatchStmt == null) {
            this.insertBatchStmt = this.connection.prepareStatement("INSERT INTO bulkload_batch_tab VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
         }

         this.insertBatchStmt.setLong(1, this.params.getOperationInstance());
         this.insertBatchStmt.setLong(2, this.params.getLastOperationInstance());
         this.insertBatchStmt.setLong(3, this.params.getBulkloadId());
         this.insertBatchStmt.setInt(4, ++this.batchIndex);
         this.insertBatchStmt.setString(5, var1.salesforceId);
         this.insertBatchStmt.setLong(6, var1.getStartPosition());
         this.insertBatchStmt.setLong(7, var1.getEndPosition());
         this.insertBatchStmt.setLong(8, var1.getNumberOfRecordsSent());
         this.insertBatchStmt.setTimestamp(9, getTimestamp());
         this.insertBatchStmt.setLong(10, 0L);
         this.insertBatchStmt.setLong(11, 0L);
         this.insertBatchStmt.setLong(12, 0L);
         this.insertBatchStmt.setInt(13, 0);
         this.insertBatchStmt.setString(14, "");
         this.insertBatchStmt.execute();
      } catch (SQLException var3) {
         throw new PersistorException(var3);
      }
   }

   public boolean allBatchesFailed() {
      return this.totalBatches > 0 && this.totalBatches == this.failedBatches;
   }

   public String getFailedBatchesMessage() {
      return this.failedBatches <= 0
         ? null
         : String.format(
            "\n%d out of %d %s failed \n%s", this.failedBatches, this.totalBatches, this.totalBatches == 1 ? "batch" : "batches", this.failedBatchesMessage
         );
   }

   public boolean isComplete(long var1, long var3) {
      Statement var5 = null;
      ResultSet var6 = null;
      this.totalBatches = this.failedBatches = 0;
      this.failedBatchesMessage = new StringBuilder();

      try {
         var5 = this.connection.createStatement();
         var6 = var5.executeQuery(
            "SELECT start_pos, end_pos, status, statusmessage, batch_id FROM bulkload_batch_tab WHERE operation_instance_id = "
               + this.params.getOperationInstance()
               + " ORDER BY start_pos"
         );
         long var7 = -1L;
         long var9 = -1L;

         while (var6.next()) {
            long var11 = var6.getLong(1);
            long var13 = var6.getLong(2);
            if (var7 == -1L) {
               if (var11 != var1) {
                  return false;
               }
            } else if (var11 != var9) {
               return false;
            }

            var7 = var11;
            var9 = var13;
            this.totalBatches++;
            int var15 = var6.getInt(3);
            BatchStatus var16 = BatchStatus.ConvertToEnum(var15);
            if (var16 == BatchStatus.FAILED || var16 == BatchStatus.NOTPROCESSED) {
               this.failedBatches++;
               String var17 = var6.getString(5);
               String var18 = var6.getString(4);
               this.failedBatchesMessage.append(" ").append(var16.name().toLowerCase()).append(" batch:").append(var17);
               this.failedBatchesMessage.append(" reason: ").append(var18).append("\n");
            }
         }

         if (var9 == -1L) {
            return true;
         }

         long var24 = var9 - var3;
         return var24 == 0L || var24 == 1L;
      } catch (SQLException var22) {
      } finally {
         closeResultSet(var6);
         closeStatement(var5);
      }

      return false;
   }

   public long countNumberOfRecordsSent() {
      Statement var1 = null;
      ResultSet var2 = null;

      try {
         var1 = this.connection.createStatement();
         var2 = var1.executeQuery("SELECT SUM(records) FROM bulkload_batch_tab WHERE instance_id = " + this.params.getOperationInstance());
         if (var2.next()) {
            return var2.getLong(1);
         }
      } catch (SQLException var8) {
      } finally {
         closeResultSet(var2);
         closeStatement(var1);
      }

      return -1L;
   }

   @Override
   public void dispose() {
      if (closeStatement(this.insertBatchStmt)) {
         this.insertBatchStmt = null;
      }
   }
}
