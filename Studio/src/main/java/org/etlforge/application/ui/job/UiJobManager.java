package org.etlforge.application.ui.job;

import java.util.concurrent.Future;

public interface UiJobManager {
   Future<?> submitForSequentialProcessing(UiJob var1);

   Future<?> submitForParallellProcessing(UiJob var1);

   boolean cancelJob(UiJob var1);

   void addGlobalJobStatusListener(JobStatusListener var1);

   void removeGlobalJobStatusListener(JobStatusListener var1);
}
