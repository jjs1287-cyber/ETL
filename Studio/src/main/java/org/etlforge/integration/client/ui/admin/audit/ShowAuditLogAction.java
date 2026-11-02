package org.etlforge.integration.client.ui.admin.audit;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.client.ui.admin.core.action.ShowConsoleAction;
import org.etlforge.integration.client.server.webservice.CppWebServiceConfiguration;

public final class ShowAuditLogAction extends ShowConsoleAction {
    public ShowAuditLogAction(ApplicationWindow window, ServerConnectionService connection, CppWebServiceConfiguration configuration) {
        super("admin.action.AuditLog", window, () -> new AuditLogPage(new AuditLogService(configuration)));
        setConnectionService(connection);
    }
}
