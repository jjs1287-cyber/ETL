package org.etlforge.integration.client.ui.interchange.entity;

import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.DataLocationType;

public final class LocatablePageHelpIdProvider {
   public static String getDefaultHelpId(SourceTarget var0) {
      return "integration." + var0.name().toLowerCase() + ".main";
   }

   public static String getHelpId(SourceTarget var0, DataLocationType var1) {
      switch (var1) {
         case FTP:
         case FileShare:
         case TemporaryStorage:
         case Database:
         case HTTP:
         case LDAP:
            return "integration." + var0.name().toLowerCase() + "." + var1.name().toLowerCase();
         default:
            return getDefaultHelpId(var0);
      }
   }

   private LocatablePageHelpIdProvider() {
   }
}
