package com.etlforge.integration.client.ui.admin.scheduler;

import com.google.common.collect.ImmutableSet;
import com.etlforge.integration.client.admin.project.EnableOperationSchedulesCallback;
import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.util.Collection;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.data.entity.id.OperationId;

public final class EnableOperationSchedulesJob extends UiJob {
   private final ProjectAdministration serverApi;
   private final ImmutableSet<OperationId> operationIds;
   private final boolean enabled;
   private final EnableOperationSchedulesCallback internalCallback;
   private final EnableOperationSchedulesCallback externalCallback;

   public EnableOperationSchedulesJob(ProjectAdministration var1, Collection<OperationId> var2, boolean var3, EnableOperationSchedulesCallback var4) {
      super(EnterpriseAdminResources.getString("EnableOperationSchedulesJob.Name"));
      this.serverApi = var1;
      this.operationIds = ImmutableSet.copyOf(var2);
      this.enabled = var3;
      this.externalCallback = var4;
      this.internalCallback = new EnableOperationSchedulesJob.InternalCallback();
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.internalCallback);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(EnterpriseAdminResources.getString("EnableOperationSchedulesJob.Start"));
      this.serverApi.setOperationSchedulesEnabled(this.operationIds, this.enabled, this.internalCallback);
   }

   @Override
   protected void onJobCancelled() {
      this.internalCallback.cancelled();
   }

   private class InternalCallback implements EnableOperationSchedulesCallback {
      private InternalCallback() {
      }

      @Override
      public void done(Collection<OperationId> var1, boolean var2) {
         EnableOperationSchedulesJob.this.externalCallback.done(var1, var2);
      }

      @Override
      public void cancelled() {
         EnableOperationSchedulesJob.this.externalCallback.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(EnableSchedulesJob.class, EnterpriseAdminResources.getString("EnableOperationSchedulesJob.Error") + ": " + var1.getMessage(), var1);
         EnableOperationSchedulesJob.this.externalCallback.caught(var1);
      }
   }
}
