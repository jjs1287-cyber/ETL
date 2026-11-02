package org.etlforge.integration.client.ui.admin.cloud;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.core.action.ShowConsoleAction;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;
import org.etlforge.integration.client.ui.admin.audit.AuditLogService;
public final class ShowCloudLoggingAction extends ShowConsoleAction {
    public ShowCloudLoggingAction(ApplicationWindow window,ServerConnectionService connection,CppWebServiceConfiguration configuration){super("admin.action.CloudLogging",window,()->new CloudLoggingPage(new AuditLogService(configuration)));setConnectionService(connection);}
}
