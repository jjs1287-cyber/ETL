package org.etlforge.util.progress;

import java.util.EventListener;

public interface ProgressListener extends EventListener {
   void progressStarted(ProgressEvent var1);

   void progressEnded(ProgressEvent var1);

   void progressUpdated(ProgressEvent var1);
}
