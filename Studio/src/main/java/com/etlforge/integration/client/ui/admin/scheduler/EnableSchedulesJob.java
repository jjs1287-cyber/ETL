package com.etlforge.integration.client.ui.admin.scheduler;

import com.google.common.collect.ImmutableSet;
import com.etlforge.integration.client.admin.project.EnableSchedulesCallback;
import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.util.Collection;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.integration.data.entity.id.ScheduleId;

public final class EnableSchedulesJob extends UiJob {
   private final ProjectAdministration serverApi;
   private final ImmutableSet<ScheduleId> scheduleIds;
   private final boolean enabled;
   private final EnableSchedulesCallback internalCallback;
   private final EnableSchedulesCallback externalCallback;

   public EnableSchedulesJob(ProjectAdministration var1, Collection<ScheduleId> var2, boolean var3, EnableSchedulesCallback var4) {
      super(EnterpriseAdminResources.getString("EnableSchedulesJob.Name"));
      this.serverApi = var1;
      this.scheduleIds = ImmutableSet.copyOf(var2);
      this.enabled = var3;
      this.externalCallback = var4;
      this.internalCallback = new EnableSchedulesJob.InternalCallback();
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.internalCallback);
   }

   @Override
   protected void runImpl() throws InterruptedException {
      EnterpriseAdminResources.getString("EnableSchedulesJob.Start");
      this.serverApi.setSchedulesEnabled(this.scheduleIds, this.enabled, this.internalCallback);
   }

   @Override
   protected void onJobCancelled() {
      this.internalCallback.cancelled();
   }

   private class InternalCallback implements EnableSchedulesCallback {
      private InternalCallback() {
      }

      @Override
      public void done(Collection<ScheduleId> var1, boolean var2) {
         EnableSchedulesJob.this.externalCallback.done(var1, var2);
      }

      @Override
      public void cancelled() {
         EnableSchedulesJob.this.externalCallback.cancelled();
      }

      @Override
      public void caught(Throwable var1) {
         ErrorLog.attention(EnableSchedulesJob.class, EnterpriseAdminResources.getString("EnableSchedulesJob.Error") + ": " + var1.getMessage(), var1);
         EnableSchedulesJob.this.externalCallback.caught(var1);
      }
   }
}
