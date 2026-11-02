package org.etlforge.integration.client.ui.interchange.location;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.integration.data.location.ftp.FtpTransferType;

public final class FtpTransferTypePreference {
   public static final EnumPreference<FtpTransferType> INSTANCE = create();

   private static EnumPreference<FtpTransferType> create() {
      String[] var0 = new String[]{"Sources & Targets", "FTP"};
      String var1 = "TransferType";
      FtpTransferType var2 = FtpTransferType.Binary;
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, var2, FtpTransferType.class);
   }

   private FtpTransferTypePreference() {
   }
}
