package org.etlforge.application;

public interface ExitListener {
   boolean isExitAllowed();

   void performExitWork();
}
