package org.etlforge.integration.client.ui.admin.cluster;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.ui.admin.audit.AuditLogService;
import org.etlforge.integration.client.ui.admin.core.action.ShowConsoleAction;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.client.ui.admin.resources.AdminIcons;

public final class ShowClusterAction extends ShowConsoleAction {
 public ShowClusterAction(ApplicationWindow window,ServerConnectionService connection,CppWebServiceConfiguration configuration){
  super("admin.action.Cluster",window,()->new Page(new AuditLogService(configuration)));setConnectionService(connection);
 }
 private static final class Page extends AbstractConsolePage {
  private final ClusterPanel panel;
  Page(AuditLogService service){super("Admin.Console.Cluster","Multi-server Configuration");panel=new ClusterPanel(service);setIcon(AdminIcons.SYSTEM_CONSOLE_16);setMementoPath(new String[]{"Admin","Cluster"});useDefaultCategory();setMainPanel(panel);}
  @Override public void addToWindow(ApplicationWindow window){super.addToWindow(window);panel.disconnect();if(isConnected())panel.start();}
  @Override protected void serverConnectionChanged(ServerConnectionEvent event){panel.disconnect();if(event.isConnected())panel.start();}
 }
}
