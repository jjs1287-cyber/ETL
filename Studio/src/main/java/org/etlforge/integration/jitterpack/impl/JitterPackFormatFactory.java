package org.etlforge.integration.jitterpack.impl;

import org.etlforge.integration.client.info.Versions;
import org.etlforge.integration.client.project.jitterpack.JitterPackParameters;
import org.etlforge.util.version.UnsupportedVersionException;
import org.etlforge.util.version.Version;

final class JitterPackFormatFactory {
   private static final Version FIRST_VERSION_SUPPORTED = Versions.V_1_0_0;

   private JitterPackFormatFactory() {
   }

   static JitterPackFormat getFormat(Version var0, JitterPackParameters var1) throws UnsupportedVersionException {
      if (var0.isEarlierThan(FIRST_VERSION_SUPPORTED)) {
         throw new UnsupportedVersionException(var0, "The minimum version required is " + FIRST_VERSION_SUPPORTED.toString());
      } else if (var0.isEarlierThan(Versions.V_1_1_0)) {
         throw new UnsupportedOperationException("The version " + var0.getPatchLevelString() + " is no longer supported.");
      } else {
         return var0.isEarlierThan(Versions.V_1_2_0) ? new V110_JitterPackFormat(var1) : new V1200_JitterpackFormat(var1);
      }
   }
}
