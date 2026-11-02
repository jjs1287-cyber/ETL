package org.etlforge.integration.client.ui.admin.ssh;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.core.action.ShowConsoleAction;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.ui.admin.audit.AuditLogService;
public final class ShowSshKeysAction extends ShowConsoleAction {
 public ShowSshKeysAction(ApplicationWindow window,ServerConnectionService connection,CppWebServiceConfiguration config){super("admin.action.SshKeys",window,()->new SshKeysPage(new AuditLogService(config)));setConnectionService(connection);}
}
