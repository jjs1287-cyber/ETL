package org.etlforge.integration.data.entity.link;

import java.text.MessageFormat;
import org.etlforge.integration.data.IntegrationId;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class LinkUtils {
   public static void logMissingLink(IntegrationEntity var0, EntityType var1, IntegrationId var2) {
      logMissingLink(var0, var1.name(), var2);
   }

   public static void logMissingLink(IntegrationEntity var0, String var1, IntegrationId var2) {
      if (!var0.isDeleted()) {
         String var3 = getWarning(var0, var1, var2);
         System.err.println(var3);
      }
   }

   private static String getWarning(IntegrationEntity var0, String var1, IntegrationId var2) {
      Object[] var3 = new Object[]{var1, var2, var0.getEntityType(), var0.getName(), var0.getID()};
      return MessageFormat.format("Unknown {0} ID [{1}] used by the {2} \"{3}\" [id {4}].", var3);
   }

   private LinkUtils() {
   }
}
