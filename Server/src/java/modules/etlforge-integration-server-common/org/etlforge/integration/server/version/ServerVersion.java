package org.etlforge.integration.server.version;

import org.etlforge.util.version.BuildVersion;
import org.etlforge.util.version.Version;

public final class ServerVersion {
   private static final int MAJOR = 42;
   private static final int MINOR = 0;
   private static final int PATCH = 0;
   private static final int BUILD = 0;

   public static Version getServerVersion() {
      return new BuildVersion(MAJOR, MINOR, PATCH, BUILD);
   }

   private ServerVersion() {
   }
}
