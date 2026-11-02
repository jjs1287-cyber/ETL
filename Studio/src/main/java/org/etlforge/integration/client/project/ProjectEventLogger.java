package org.etlforge.integration.client.project;

public interface ProjectEventLogger {
   String SYSTEM_PROPERTY = "LogProjectEvents";

   void switchOn();

   void switchOff();
}
