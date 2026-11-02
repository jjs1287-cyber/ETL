package org.etlforge.application.ui.job;

public interface UiJobProgressMonitor {
   void started(UiJob var1);

   void ended();

   void setMessage(String var1);

   void setCompletionLevel(int var1);

   void setCanBeCancelled(boolean var1);
}
