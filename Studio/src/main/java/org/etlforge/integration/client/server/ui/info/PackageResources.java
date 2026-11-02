package org.etlforge.integration.client.server.ui.info;

import javax.swing.Icon;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.server.ui.resources.ServerUiResources;

final class PackageResources {
   private static String getString(String var0) {
      return ServerUiResources.getString(var0);
   }

   private PackageResources() {
   }

   static class Actions {
      static class Login {
         static final String TOOLTIP = PackageResources.getString("ServerInfo.Action.Login.ToolTip");
         static final String EXPLANATION = PackageResources.getString("ServerInfo.Action.Login.Explanation");
      }

      static class Refresh {
         static final String TOOLTIP = PackageResources.getString("ServerInfo.Action.Refresh.ToolTip");
      }

      static class Remove {
         static final String NAME = PackageResources.getString("ServerInfo.Action.Remove.Name");
         static final Icon ICON = CommonIcons.DELETE_16;
         static final String TOOLTIP = PackageResources.getString("ServerInfo.Action.Remove.ToolTip");
         static final String EXPLANATION = PackageResources.getString("ServerInfo.Action.Remove.Explanation");
      }

      static class ViewDetails {
         static final String TOOLTIP = PackageResources.getString("ServerInfo.Action.ViewDetails.ToolTip");
      }
   }

   static class View {
      static final String TITLE = PackageResources.getString("ServerInfo.View.Title");
   }

   static class Viewer {
      static final String TITLE = PackageResources.getString("ServerInfo.Viewer.Title");
      static final String NAME = PackageResources.getString("ServerInfo.Viewer.Name");
      static final String VERSION = PackageResources.getString("ServerInfo.Viewer.Version");
      static final String UNKNOWN_VERSION = PackageResources.getString("ServerInfo.Viewer.UnknownVersion");
      static final String OS = PackageResources.getString("ServerInfo.Viewer.OS");
      static final String ID = PackageResources.getString("ServerInfo.Viewer.ID");
      static final String FIRST_LOGIN = PackageResources.getString("ServerInfo.Viewer.FirstLogin");
      static final String LATEST_LOGIN = PackageResources.getString("ServerInfo.Viewer.LatestLogin");
      static final String LATEST_USER = PackageResources.getString("ServerInfo.Viewer.LatestUser");
   }
}
