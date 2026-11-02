package org.etlforge.integration.client.project.jitterpack;

import org.etlforge.util.version.Version;

public interface VersionInfo {
   VersionInfo UNKNOWN = new VersionInfo() {
      @Override
      public Version createdIn() {
         return null;
      }

      @Override
      public Version minimumRequired() {
         return null;
      }
   };

   Version createdIn();

   Version minimumRequired();
}
