package org.etlforge.integration.client.ui.interchange.location;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.EnumPreference;
import org.etlforge.integration.data.location.ftp.FtpSecurityOption;

public final class FtpSecurityOptionPreference {
   public static final EnumPreference<FtpSecurityOption> INSTANCE = create();

   private static EnumPreference<FtpSecurityOption> create() {
      String[] var0 = new String[]{"Sources & Targets", "FTP"};
      String var1 = "SecurityOption";
      FtpSecurityOption var2 = FtpSecurityOption.NONE;
      return ApplicationConfiguration.getPreferenceFactory().newEnumPreference(var0, var1, var2, FtpSecurityOption.class);
   }

   private FtpSecurityOptionPreference() {
   }
}
