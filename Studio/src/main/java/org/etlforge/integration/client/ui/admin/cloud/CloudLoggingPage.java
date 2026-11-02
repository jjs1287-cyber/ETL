package org.etlforge.integration.client.ui.admin.cloud;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.activity.ui.resources.ActivityIcons;
import org.etlforge.integration.client.ui.admin.audit.AuditLogPanel;
public final class CloudLoggingPage extends AbstractConsolePage {
    private final CloudLoggingPanel panel;
    public CloudLoggingPage(AuditLogPanel.Service service){super("Admin.Console.CloudLogging","Cloud logging");panel=new CloudLoggingPanel(service);setIcon(ActivityIcons.LOG_16);setMementoPath(new String[]{"Admin","CloudLogging"});useDefaultCategory();setMainPanel(panel);}
    @Override public void addToWindow(ApplicationWindow window){super.addToWindow(window);panel.disconnect();if(isConnected())panel.start();}
    @Override protected void serverConnectionChanged(ServerConnectionEvent event){panel.disconnect();if(event.isConnected())panel.start();}
}
