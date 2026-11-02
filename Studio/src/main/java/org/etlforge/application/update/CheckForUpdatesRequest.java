package org.etlforge.application.update;

public interface CheckForUpdatesRequest {
   void cancel();

   void waitUntilDone() throws InterruptedException;
}
