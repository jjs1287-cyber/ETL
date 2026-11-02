package com.etlforge.integration.data.download.entity;

import com.etlforge.integration.data.download.InvalidDownloadedDataException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class DownloadedDataUtils {
   public static String getRequiredValue(StringKeyValuePairs var0, String var1) throws InvalidDownloadedDataException {
      String var2 = var0.get(var1, null);
      if (StringUtils.isEmpty(var2)) {
         throw new InvalidDownloadedDataException("Missing value for the key " + var1);
      } else {
         return var2;
      }
   }

   public static IntegrationEntityId getEntityId(StringKeyValuePairs var0, String var1, EntityType var2) throws InvalidDownloadedDataException {
      String var3 = getRequiredValue(var0, var1);

      try {
         return var2.getIntegrationId(var3);
      } catch (Exception var5) {
         throw new InvalidDownloadedDataException("Invalid value for the ID field: " + var3, var5);
      }
   }

   private DownloadedDataUtils() {
   }
}
