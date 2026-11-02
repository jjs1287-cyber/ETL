package org.etlforge.integration.jitterpack.impl;

import org.etlforge.integration.client.info.Versions;
import org.etlforge.integration.client.project.jitterpack.JitterPackParameters;
import org.etlforge.util.version.Version;

final class V1200_JitterpackFormat extends AbstractJitterpackFormat {
   public V1200_JitterpackFormat(JitterPackParameters var1) {
      super(var1);
   }

   @Override
   protected Version getRequiredVersion() {
      return Versions.V_1_2_0;
   }
}
