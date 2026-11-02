package org.etlforge.integration.client.ui.admin.compare;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.admin.core.action.ShowConsoleAction;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
public final class ShowProjectComparisonAction extends ShowConsoleAction {
 public ShowProjectComparisonAction(ApplicationWindow window,ProjectManager manager){super("admin.action.ProjectComparison",window,()->new Page(manager));}
 private static final class Page extends AbstractConsolePage {
  Page(ProjectManager manager){super("Admin.Console.ProjectComparison","Project Differences");setIcon(org.etlforge.integration.client.ui.admin.resources.AdminIcons.PROJECT_CONSOLE_16);setMementoPath(new String[]{"Admin","ProjectComparison"});useDefaultCategory();setMainPanel(new ProjectComparisonPanel(manager));}
 }
}
