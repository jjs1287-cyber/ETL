package org.etlforge.integration.client.ui.core.main;

import org.etlforge.integration.client.ui.core.resources.CoreResources;

final class PackageResources {
   private PackageResources() {
   }

   static class AboutDialog {
      static final String VERSION = CoreResources.getString("AboutDialog.Version");
      static final String CLIENT_ID = CoreResources.getString("AboutDialog.ClientId");
      static final String BUILD_TIMESTAMP = CoreResources.getString("AboutDialog.BuildTimestamp");
      static final String ON_THE_WEB = CoreResources.getString("AboutDialog.OnTheWeb");
      static final String HOMEPAGE = CoreResources.getString("AboutDialog.Homepage");
      static final String FORUM = CoreResources.getString("AboutDialog.Forum");
      static final String SESSION_DETAILS = CoreResources.getString("AboutDialog.SessionDetails");
      static final String SESSION_DETAILS_TITLE = CoreResources.getString("AboutDialog.SessionDetails.Title");
   }

   static class ConnectionStatus {
      static final String NOT_CONNECTED = CoreResources.getString("ConnectionStatus.NotConnected");
      static final String NONE = CoreResources.getString("ConnectionStatus.None");
   }

   static class SessionDetails {
      static final String STARTUP = CoreResources.getString("SessionDetails.Startup");
      static final String APPDIR = CoreResources.getString("SessionDetails.AppDir");
      static final String PLATFORM = CoreResources.getString("SessionDetails.Platform");
      static final String SYSTEM = CoreResources.getString("SessionDetails.System");
   }
}
