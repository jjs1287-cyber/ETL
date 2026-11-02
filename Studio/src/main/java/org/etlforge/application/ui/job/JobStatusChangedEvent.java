package org.etlforge.application.ui.job;

import com.google.common.base.Preconditions;

public class JobStatusChangedEvent {
   private final UiJob job;
   private final JobStatus newStatus;

   public JobStatusChangedEvent(UiJob var1, JobStatus var2) {
      this.job = Preconditions.checkNotNull(var1, "job");
      this.newStatus = Preconditions.checkNotNull(var2, "newStatus");
   }

   public UiJob getJob() {
      return this.job;
   }

   public JobStatus getNewStatus() {
      return this.newStatus;
   }
}
