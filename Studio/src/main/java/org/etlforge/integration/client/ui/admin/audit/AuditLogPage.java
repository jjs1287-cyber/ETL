package org.etlforge.integration.client.ui.admin.audit;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.ui.admin.core.console.AbstractConsolePage;
import org.etlforge.integration.activity.ui.resources.ActivityIcons;

public final class AuditLogPage extends AbstractConsolePage {
    private final AuditLogPanel panel;

    public AuditLogPage(AuditLogPanel.Service service) {
        super("Admin.Console.AuditLog", "Audit Log");
        panel = new AuditLogPanel(service);
        setIcon(ActivityIcons.HISTORY_16);
        setMementoPath(new String[]{"Admin", "AuditLog"});
        useDefaultCategory();
        setMainPanel(panel);
    }

    @Override public void addToWindow(ApplicationWindow window) {
        super.addToWindow(window);
        updateConnection(isConnected());
    }

    @Override protected void serverConnectionChanged(ServerConnectionEvent event) {
        updateConnection(event.isConnected());
    }

    private void updateConnection(boolean connected) {
        panel.disconnect();
        if (connected) panel.start();
    }
}
