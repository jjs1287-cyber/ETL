package org.etlforge.integration.client.ui.structure.editor.db;

import java.util.concurrent.CountDownLatch;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProvider;
import org.etlforge.integration.client.database.serverapi.DatabaseInfoProviderFactory;
import org.etlforge.integration.client.database.serverapi.WhereClauseValidationCallback;
import org.etlforge.integration.client.ui.structure.editor.AbstractWizardServerCallback;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.database.info.DatabaseObject;

final class SingleTableWhereClauseValidator extends UiJob {
   private final SingleTableWhereClausePage page;
   private final ConnectionParams connectionParams;
   private final DatabaseObject dbObject;
   private final String where;
   private final CountDownLatch latch;
   private final SingleTableWhereClauseValidator.WhereClauseCallback callback;

   public SingleTableWhereClauseValidator(SingleTableWhereClausePage var1, ConnectionParams var2, DatabaseObject var3, String var4) {
      super(PackageResources.WhereClauseValidator.NAME);
      this.page = var1;
      this.connectionParams = var2;
      this.dbObject = var3;
      this.where = var4;
      this.latch = new CountDownLatch(1);
      this.callback = new SingleTableWhereClauseValidator.WhereClauseCallback();
      this.setUncaughtExceptionHandler(this.callback);
      this.setBlockUntilDone(true);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(PackageResources.WhereClauseValidator.STARTED);
      DatabaseInfoProvider var1 = DatabaseInfoProviderFactory.createProvider(this.connectionParams);
      var1.validateWhereClause(this.dbObject, this.where, this.callback);
      this.latch.await();
   }

   @Override
   protected void onJobCancelled() {
      this.page.stopWait();
   }

   public void waitUntilDone() throws InterruptedException {
      this.latch.await();
   }

   public boolean isValid() {
      return this.callback.succeeded();
   }

   private final class WhereClauseCallback extends AbstractWizardServerCallback implements WhereClauseValidationCallback {
      public WhereClauseCallback() {
         super(SingleTableWhereClauseValidator.this.page, SingleTableWhereClauseValidator.this.latch);
         this.setErrorMessage(PackageResources.WhereClauseValidator.ERROR_MESSAGE);
      }

      @Override
      public void whereClauseIsValid() {
         if (!SingleTableWhereClauseValidator.this.isCancelled()) {
            SingleTableWhereClauseValidator.this.setCanBeCancelled(false);
            SingleTableWhereClauseValidator.this.setMayBeInterrupted(false);
            SingleTableWhereClauseValidator.this.page.stopWait();
            this.countDown();
         }
      }

      @Override
      public void caught(Throwable var1) {
         if (!SingleTableWhereClauseValidator.this.isCancelled()) {
            SingleTableWhereClauseValidator.this.setStatus(JobStatus.ERROR);
            SingleTableWhereClauseValidator.this.setMessage(UiJobMessages.ERROR);
            SingleTableWhereClauseValidator.this.page.stopWait();
            super.caught(var1);
         }
      }

      @Override
      public void cancelled() {
         super.cancelled();
         SingleTableWhereClauseValidator.this.page.stopWait();
      }
   }
}
