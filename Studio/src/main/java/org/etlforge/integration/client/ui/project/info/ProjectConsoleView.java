package org.etlforge.integration.client.ui.project.info;

import java.util.List;
import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.wait.WaitService;

public interface ProjectConsoleView extends Disposable, UiProvider {
   ApplicationWindow getAppWin();

   ProjectConsoleModel getModel();

   ServerProjectsViewer getProjectsViewer();

   void setDownloadAction(Action var1);

   void setConsoleActions(List<ProjectConsoleAction> var1);

   void setWaitService(WaitService var1);

   WaitService getWaitService();

   ProjectConsoleSubView getSubView(String var1);

   void showSubView(ProjectConsoleSubView var1);

   void closeSubView(String var1);
}
