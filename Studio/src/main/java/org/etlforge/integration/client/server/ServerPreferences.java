package org.etlforge.integration.client.server;

import java.util.prefs.BackingStoreException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.util.preference.KongaPreferences;

public final class ServerPreferences {
   public static KongaPreferences forServer(ServerInfo var0) {
      return forServer(var0.getGuid());
   }

   public static KongaPreferences forServer(ServerGuid var0) {
      return KongaPreferences.getPreferences(ServerPreferences.class, var0.toString());
   }

   public static void delete(ServerGuid var0) {
      try {
         forServer(var0).removeNode();
      } catch (BackingStoreException var2) {
         ErrorLog.log(ServerPreferences.class, "Failed to remove the preferences for the server " + var0 + ".", var2);
      }
   }

   private ServerPreferences() {
   }
}
