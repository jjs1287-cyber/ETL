package com.etlforge.integration.server.engine.bulkload.db;

import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.engine.bulkload.PersistorException;
import com.etlforge.integration.server.engine.bulkload.batch.ScanningResult;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadJobInfo;

public final class BulkLoadInstanceTab extends AbstractTable {
   public BulkLoadInstanceTab(Connection var1, BulkLoadParams var2) {
      super(var1, var2);
   }

   public void insertNewBulkLoadInstance() throws PersistorException {
      this.startNewInstanceRecord();
      this.updateInstanceWithStartTimeAndEncoding();
   }

   private void startNewInstanceRecord() throws PersistorException {
      PreparedStatement var1 = null;

      try {
         if (!this.doesOpInstanceRecordExist("bulkload_instance_tab")) {
            var1 = this.prepareInsertNewInstanceStatement();
            var1.execute();
         }
      } catch (SQLException var6) {
         throw new PersistorException(var6);
      } finally {
         closeStatement(var1);
      }
   }

   private PreparedStatement prepareInsertNewInstanceStatement() throws SQLException {
      PreparedStatement var1 = this.connection
         .prepareStatement(
            "INSERT INTO bulkload_instance_tab (operation_instance_id, last_operation_instance_id, bulkload_id, filename, filecharencode) VALUES (?, ?, ?, ?,?)"
         );
      var1.setLong(1, this.params.getOperationInstance());
      if (this.params.getLastOperationInstance() >= 0L) {
         var1.setLong(2, this.params.getLastOperationInstance());
      } else {
         var1.setNull(2, -5);
      }

      var1.setLong(3, this.params.getBulkloadId());
      var1.setTimestamp(4, getTimestamp());
      if (this.params.getLastOperationInstance() < 0L) {
         var1.setString(4, this.params.getSourceFile().getAbsolutePath());
         var1.setString(5, this.params.getEncoding().name());
      } else {
         var1.setNull(4, 12);
         var1.setNull(5, 12);
      }

      return var1;
   }

   private void updateInstanceWithStartTimeAndEncoding() throws PersistorException {
      PreparedStatement var1 = null;

      try {
         var1 = this.connection.prepareStatement("UPDATE bulkload_instance_tab SET start_time = ?, filecharencode = ? WHERE operation_instance_id = ?");
         var1.setTimestamp(1, getTimestamp());
         var1.setString(2, this.params.getEncoding().name());
         var1.setLong(3, this.params.getOperationInstance());
         var1.execute();
      } catch (SQLException var6) {
         throw new PersistorException(var6);
      } finally {
         closeStatement(var1);
      }
   }

   public void insertJobId(String var1) throws PersistorException {
      PreparedStatement var2 = null;

      try {
         var2 = this.connection.prepareStatement("UPDATE bulkload_instance_tab SET sfdc_job_id = ? WHERE operation_instance_id = ?");
         var2.setString(1, var1);
         var2.setLong(2, this.params.getOperationInstance());
         var2.execute();
      } catch (SQLException var7) {
         throw new PersistorException(var7);
      } finally {
         closeStatement(var2);
      }
   }

   public void insertScanningResult(ScanningResult var1) throws PersistorException {
      long var2 = var1.getFileSize();
      long var4 = var1.getStartOfFirstRecord();
      PreparedStatement var6 = null;

      try {
         var6 = this.connection.prepareStatement("UPDATE bulkload_instance_tab SET file_size = ?, start_pos = ? WHERE operation_instance_id = ?");
         var6.setLong(1, var2);
         var6.setInt(2, (int)var4);
         var6.setLong(3, this.params.getOperationInstance());
         var6.execute();
      } catch (SQLException var11) {
         throw new PersistorException(var11);
      } finally {
         closeStatement(var6);
      }
   }

   public void insertEndTime() throws PersistorException {
      PreparedStatement var1 = null;

      try {
         int var2 = BulkLoadJobInfo.BulkLoadJobInfoEnum.ConvertToInt(BulkLoadJobInfo.BulkLoadJobInfoEnum.CLOSED);
         var1 = this.connection.prepareStatement("UPDATE bulkload_instance_tab SET end_time = ? , status = ?  WHERE operation_instance_id = ?");
         var1.setTimestamp(1, getTimestamp());
         var1.setInt(2, var2);
         var1.setLong(3, this.params.getOperationInstance());
         var1.execute();
      } catch (SQLException var6) {
         throw new PersistorException(var6);
      } finally {
         closeStatement(var1);
      }
   }
}
