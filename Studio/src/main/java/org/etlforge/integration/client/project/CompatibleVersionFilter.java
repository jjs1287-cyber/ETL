package org.etlforge.integration.client.project;

import com.google.common.base.Predicate;
import org.etlforge.util.version.Version;

public final class CompatibleVersionFilter implements Predicate<ProjectLocation> {
   private final Version cutoff;

   public CompatibleVersionFilter(Version var1) {
      this.cutoff = var1;
   }

   public boolean apply(ProjectLocation var1) {
      Version var2 = var1.getVersionRequired();
      return this.isUnkownVersion(var2) || this.isCompatible(var2);
   }

   private boolean isUnkownVersion(Version var1) {
      return var1 == Version.UNKNOWN;
   }

   private boolean isCompatible(Version var1) {
      return var1.isSameAsOrEarlierThan(this.cutoff);
   }
}
