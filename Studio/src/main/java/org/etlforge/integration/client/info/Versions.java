package org.etlforge.integration.client.info;

import org.etlforge.application.VersionFactory;
import org.etlforge.util.version.Version;

public final class Versions {
   public static final Version V_1_0_0 = VersionFactory.newVersion(1, 0, 0, 0);
   public static final Version V_1_1_0 = VersionFactory.newVersion(1, 1, 0, 0);
   public static final Version V_1_2_0 = VersionFactory.newVersion(1, 2, 0);

   private Versions() {
   }
}
