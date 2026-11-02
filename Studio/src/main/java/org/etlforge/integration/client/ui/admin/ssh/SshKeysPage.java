package org.etlforge.integration.client.ui.admin.ssh;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.activity.ui.resources.ActivityIcons;
import org.etlforge.integration.client.ui.admin.audit.AuditLogPanel;
public final class SshKeysPage extends AbstractConsolePage {
 private final SshKeysPanel panel;
 public SshKeysPage(AuditLogPanel.Service service){super("Admin.Console.SshKeys","SSH and PGP Keys");panel=new SshKeysPanel(service);setIcon(ActivityIcons.LOG_16);setMementoPath(new String[]{"Admin","SshKeys"});useDefaultCategory();setMainPanel(panel);}
 @Override public void addToWindow(ApplicationWindow window){super.addToWindow(window);panel.disconnect();if(isConnected())panel.start();}
 @Override protected void serverConnectionChanged(ServerConnectionEvent event){panel.disconnect();if(event.isConnected())panel.start();}
}
