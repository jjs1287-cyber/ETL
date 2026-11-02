package com.etlforge.integration.server.engine.bulkload.db;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.salesforce.bulkload.backend.Batch;
import org.etlforge.integration.salesforce.bulkload.backend.BatchStatus;
import org.etlforge.integration.salesforce.bulkload.backend.BulkLoadInstanceId;
import org.etlforge.integration.salesforce.bulkload.backend.RecordCounts;
import org.etlforge.integration.salesforce.bulkload.backend.RecordOfSentBatch;
import org.etlforge.integration.salesforce.bulkload.backend.SectionOfFile;

public final class BulkLoadBatchTabQuery {
   private final Connection connection;
   private final BulkLoadInstanceId instanceId;

   public BulkLoadBatchTabQuery(Connection var1, BulkLoadInstanceId var2) {
      this.connection = Preconditions.checkNotNull(var1, "connection");
      this.instanceId = Preconditions.checkNotNull(var2, "instanceId");
   }

   public List<RecordOfSentBatch> listBatches() throws SQLException {
      Statement var1 = this.connection.createStatement();
      ResultSet var2 = null;

      try {
         var2 = var1.executeQuery(
            "SELECT * FROM bulkload_batch_tab WHERE operation_instance_id = " + this.instanceId.getOperationInstanceId() + " ORDER BY time_sent ASC"
         );
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

   private RecordOfSentBatch createRecord(ResultSet var1) throws SQLException {
      SectionOfFile var2 = new SectionOfFile(var1.getLong(6), var1.getLong(7));
      RecordCounts var3 = new RecordCounts(var1.getInt(8), var1.getInt(10), var1.getInt(11), var1.getInt(12));
      BatchStatus var4 = BatchStatus.ConvertToEnum(var1.getInt(13));
      Batch var5 = new Batch(var1.getString(5), var4, var2, var3);
      return new RecordOfSentBatch(this.instanceId, var5, var1.getTimestamp(9));
   }
}
