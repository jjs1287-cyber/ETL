package org.etlforge.integration.client.ui.interchange.location;

import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.FtpLocation;
import org.etlforge.integration.data.location.ftp.FtpSecurityOption;
import org.etlforge.integration.data.location.ftp.FtpTransferType;

public final class DataLocationFactory {
   private DataLocationFactory() {
   }

   public static DataLocation newLocation(DataLocationType var0) {
      DataLocation var1 = var0.createLocation();
      switch (var0) {
         case FTP:
            FtpLocation var2 = (FtpLocation)var1;
            FtpSecurityOption var3 = FtpSecurityOptionPreference.INSTANCE.get();
            var2.setSecurityOption(var3);
            FtpTransferType var4 = FtpTransferTypePreference.INSTANCE.get();
            var2.setTransferType(var4);
         default:
            return var1;
      }
   }
}
