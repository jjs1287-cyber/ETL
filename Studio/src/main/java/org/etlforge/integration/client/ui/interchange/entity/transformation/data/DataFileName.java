package org.etlforge.integration.client.ui.interchange.entity.transformation.data;

import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.id.TransformationId;
import org.etlforge.integration.debug.client.file.DataFileIdentifier;
import org.etlforge.integration.debug.client.file.DataFileType;

class DataFileName {
   private static final String PREFIX = "DataFile";
   private static final String EXTENSION = "dat";

   public static String fromIdentifier(DataFileIdentifier var0) {
      return String.format("%s.%s.%d.%d.%s", "DataFile", var0.type.name().toLowerCase(), var0.instanceNumber, var0.chunkNumber, "dat");
   }

   public static DataFileIdentifier toIdentifier(String var0, TransformationId var1) {
      String[] var2 = var0.split("\\.");
      if (var2.length == 5 && var2[0].equals("DataFile") && var2[4].equals("dat")) {
         try {
            String var3 = var2[1].toUpperCase();
            DataFileType var4 = DataFileType.valueOf(var3);
            int var5 = Integer.parseInt(var2[2]);
            int var6 = Integer.parseInt(var2[3]);
            return new DataFileIdentifier(null, var1, var4, var5, var6);
         } catch (RuntimeException var7) {
            ErrorLog.internal(DataFileName.class, "Transformation data file name with unexpected name: " + var0, var7);
         }
      }

      return null;
   }

   private DataFileName() {
   }
}
