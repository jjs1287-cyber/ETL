package org.etlforge.integration.client.server.ui.info;

import org.etlforge.application.ui.widget.BasicTwoColumnLabelDisplayer;
import org.etlforge.integration.client.server.KnownServer;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;

public final class ServerDetailsViewer implements UiProvider {
   private final BasicTwoColumnLabelDisplayer viewer = new BasicTwoColumnLabelDisplayer(BasicTwoColumnLabelDisplayer.Mode.TEXTFIELD);

   public ServerDetailsViewer(KnownServer var1) {
      this.layoutViewer(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.viewer.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.viewer.requestFocus();
   }

   private void layoutViewer(KnownServer var1) {
      ServerInfo var2 = var1.getServerInfo();
      this.viewer.addRow(PackageResources.Viewer.NAME, var2.getName(), ClientIcons.SERVER_16);
      this.viewer.addRow(PackageResources.Viewer.VERSION, var2.getVersion());
      this.viewer.addRow(PackageResources.Viewer.ID, var2.getGuid());
      this.viewer.addRow(PackageResources.Viewer.OS, var2.getPlatform().getOS());
      this.viewer.addRow(PackageResources.Viewer.FIRST_LOGIN, var1.getDateOfFirstLogin());
      this.viewer.addRow(PackageResources.Viewer.LATEST_LOGIN, var1.getDateOfLatestLogin());
      this.viewer.addRow(PackageResources.Viewer.LATEST_USER, var1.getLatestUser(), ClientIcons.USER_16);
   }
}
