package org.etlforge.application.ui.job;

public interface JobStatusListener {
   void jobStarted(JobStatusChangedEvent var1);

   void jobEnded(JobStatusChangedEvent var1);

   void statusChanged(JobStatusChangedEvent var1);
}
