package org.etlforge.integration.jitterpack.impl;

import org.etlforge.integration.client.info.Versions;
import org.etlforge.integration.client.project.jitterpack.JitterPackParameters;
import org.etlforge.util.version.Version;

final class V110_JitterPackFormat extends AbstractJitterpackFormat {
   V110_JitterPackFormat(JitterPackParameters var1) {
      super(var1);
   }

   @Override
   protected Version getRequiredVersion() {
      return Versions.V_1_0_0;
   }
}
