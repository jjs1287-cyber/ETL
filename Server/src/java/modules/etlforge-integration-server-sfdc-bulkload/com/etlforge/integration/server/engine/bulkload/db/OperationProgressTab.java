package com.etlforge.integration.server.engine.bulkload.db;

import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import com.etlforge.integration.server.engine.bulkload.PersistorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.progress.OperationProgressMessage;
import org.etlforge.system.Disposable;

public final class OperationProgressTab extends AbstractTable implements Disposable {
   private PreparedStatement updateProgressStmt;
   private boolean hasInsertedProgressMessage;

   public OperationProgressTab(Connection var1, BulkLoadParams var2) {
      super(var1, var2);
   }

   public void insertProgressMessage(OperationProgressMessage var1) throws PersistorException {
      try {
         if (!this.hasInsertedProgressMessage) {
            this.hasInsertedProgressMessage = this.doesOpInstanceRecordExist("operation_progress_tab");
            if (!this.hasInsertedProgressMessage) {
               this.prepareFirstProgressMessage(var1);
               this.hasInsertedProgressMessage = true;
            }
         }

         assert this.hasInsertedProgressMessage : "Failed to verify that there is an existing progress message in the DB";
         this.updateTimeEstimate(var1);
      } catch (SQLException var3) {
         throw new PersistorException(var3);
      }
   }

   private void prepareFirstProgressMessage(OperationProgressMessage var1) throws SQLException {
      PreparedStatement var2 = this.connection
         .prepareStatement(
            "INSERT INTO operation_progress_tab (operation_instance_id, section, message, percentage_done, source_file, source_file_index, number_of_source_files) VALUES (?, ?, ?, 0, ?, 1, 1)"
         );

      try {
         var2.setLong(1, this.params.getOperationInstance());
         var2.setString(2, var1.getSection());
         var2.setString(3, var1.getMessage());
         if (this.params.getCreateJobParams().isQuery()) {
            var2.setString(4, "");
         } else {
            var2.setString(4, this.params.getSourceFile().getAbsolutePath());
         }

         var2.execute();
      } catch (Exception var7) {
         var7.printStackTrace();
      } finally {
         KongaDbUtils.close(var2);
      }
   }

   private void updateTimeEstimate(OperationProgressMessage var1) throws SQLException {
      if (this.updateProgressStmt == null) {
         this.updateProgressStmt = this.connection
            .prepareStatement(
               "UPDATE operation_progress_tab SET section = ?, message = ?, percentage_done = ?, estimated_end_time = ? WHERE operation_instance_id = "
                  + this.params.getOperationInstance()
            );
      }

      this.updateProgressStmt.setString(1, var1.getSection());
      this.updateProgressStmt.setString(2, var1.getMessage());
      this.updateProgressStmt.setInt(3, var1.getPercentageDone());
      Date var2 = var1.getEstimatedEndTime();
      if (var2 != null) {
         this.updateProgressStmt.setTimestamp(4, getTimestamp(var2.getTime()));
      } else {
         this.updateProgressStmt.setNull(4, 93);
      }

      this.updateProgressStmt.execute();
   }

   @Override
   public void dispose() {
      if (closeStatement(this.updateProgressStmt)) {
         this.updateProgressStmt = null;
      }
   }
}
