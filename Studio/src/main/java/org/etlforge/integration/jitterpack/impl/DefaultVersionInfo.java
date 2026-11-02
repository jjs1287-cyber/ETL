package org.etlforge.integration.jitterpack.impl;

import org.etlforge.collections.Pair;
import org.etlforge.integration.client.project.jitterpack.VersionInfo;
import org.etlforge.util.version.Version;

final class DefaultVersionInfo implements VersionInfo {
   private final Pair<Version, Version> data;

   public DefaultVersionInfo(Version var1, Version var2) {
      this.data = Pair.of(var1, var2);
   }

   @Override
   public Version createdIn() {
      return this.data.first;
   }

   @Override
   public Version minimumRequired() {
      return this.data.second;
   }

   @Override
   public String toString() {
      return "[Created: " + this.createdIn() + ", Minimum: " + this.minimumRequired() + "]";
   }
}
