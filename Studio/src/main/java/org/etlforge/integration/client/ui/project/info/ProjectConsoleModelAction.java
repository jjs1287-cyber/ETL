package org.etlforge.integration.client.ui.project.info;

import javax.swing.Action;
import org.etlforge.integration.client.project.info.ProjectInfoCache;

public interface ProjectConsoleModelAction extends Action {
   void setModel(ProjectConsoleModel var1);

   void setProjectCache(ProjectInfoCache var1);
}
