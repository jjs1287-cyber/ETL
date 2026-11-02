package org.etlforge.integration.client.project.event;

import java.util.EventListener;

public interface ProjectManagerListener extends EventListener {
   void projectOpened(ProjectManagerEvent var1);

   void projectClosed(ProjectManagerEvent var1);
}
