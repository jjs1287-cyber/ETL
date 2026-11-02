package com.etlforge.integration.server.engine.bulkload;

import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.batch.BatchProcessMessage;
import com.etlforge.integration.server.engine.bulkload.batch.BatchSentMessage;
import com.etlforge.integration.server.engine.bulkload.batch.MessagePersistor;
import com.etlforge.integration.server.engine.bulkload.batch.NewProgressMessage;
import com.etlforge.integration.server.engine.bulkload.batch.ScanningDoneMessage;
import com.etlforge.integration.server.engine.bulkload.batch.ScanningResult;
import com.etlforge.integration.server.engine.bulkload.db.BulkLoadBatchTab;
import com.etlforge.integration.server.engine.bulkload.db.BulkLoadInstanceTab;
import com.etlforge.integration.server.engine.bulkload.db.OperationProgressTab;
import java.sql.Connection;
import java.util.logging.Logger;
import org.etlforge.integration.server.progress.EnterNewSectionMessage;
import org.etlforge.integration.server.progress.OperationProgressMessage;
import org.etlforge.system.Disposable;

public final class BulkLoadDbPersistor implements BulkLoadMonitor, MessagePersistor, Disposable {
   private final BulkLoadInstanceTab instanceTab;
   private final BulkLoadBatchTab batchTab;
   private final OperationProgressTab progressTab;
   private long fileSize = -1L;
   private long firstRecordPos = -1L;
   private Logger logger;
   private final boolean isQuery;

   public BulkLoadDbPersistor(Connection var1, BulkLoadParams var2, boolean var3) {
      Preconditions.checkNotNull(var1, "connection");
      Preconditions.checkNotNull(var2, "params");
      this.instanceTab = new BulkLoadInstanceTab(var1, var2);
      this.batchTab = new BulkLoadBatchTab(var1, var2);
      this.progressTab = new OperationProgressTab(var1, var2);
      this.isQuery = var3;
   }

   public void setLogger(Logger var1) {
      this.logger = var1;
   }

   public void processStarted() throws PersistorException {
      this.instanceTab.insertNewBulkLoadInstance();
   }

   @Override
   public void persistMessage(BatchProcessMessage var1) throws PersistorException {
      this.log(var1);
      if (var1 instanceof ScanningDoneMessage) {
         this.persistScanningResult((ScanningDoneMessage)var1);
      } else if (var1 instanceof BatchSentMessage) {
         this.persistBatchSent((BatchSentMessage)var1);
      } else if (var1 instanceof NewProgressMessage) {
         this.persistProgressMessage(((NewProgressMessage)var1).getProgressMessage());
      } else if (var1 == BatchProcessMessage.LAST_BATCH_SENT) {
         this.batchTab.dispose();
      }
   }

   private void log(BatchProcessMessage var1) {
      if (this.logger != null) {
         this.logger.fine(var1.toString());
      }
   }

   private void persistScanningResult(ScanningDoneMessage var1) throws PersistorException {
      ScanningResult var2 = var1.getResult();
      this.fileSize = var2.getFileSize();
      this.firstRecordPos = var2.getStartOfFirstRecord();
      this.instanceTab.insertScanningResult(var2);
   }

   private void persistBatchSent(BatchSentMessage var1) throws PersistorException {
      this.batchTab.recordBatch(var1.getBatch());
   }

   public void persistProgressMessage(OperationProgressMessage var1) throws PersistorException {
      this.progressTab.insertProgressMessage(var1);
   }

   @Override
   public void preCreateJob() throws PersistorException {
      this.persistProgressMessage(new EnterNewSectionMessage("Creating the Salesforce job."));
   }

   @Override
   public void postJobCreated(String var1) throws PersistorException {
      this.instanceTab.insertJobId(var1);
   }

   @Override
   public void preCloseJob() throws PersistorException {
      this.persistProgressMessage(new EnterNewSectionMessage("Closing the Salesforce job."));
   }

   @Override
   public void postCloseJob() throws PersistorException {
      if (this.isQuery) {
         this.persistProgressMessage(new EnterNewSectionMessage("Waiting for Salesforce to execute query."));
      } else {
         this.persistProgressMessage(new EnterNewSectionMessage("Retrieving result files."));
      }
   }

   @Override
   public void done() throws PersistorException {
      this.instanceTab.insertEndTime();
   }

   BulkLoadBatchTab getBatchTab() {
      return this.batchTab;
   }

   public boolean isComplete() {
      return this.batchTab.isComplete(this.firstRecordPos, this.fileSize);
   }

   public long countNumberOfRecordsSent() {
      return this.batchTab.countNumberOfRecordsSent();
   }

   @Override
   public void dispose() {
      this.batchTab.dispose();
      this.progressTab.dispose();
   }
}
