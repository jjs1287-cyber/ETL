package org.etlforge.application.update;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;

public final class LatestVersions {
   public static final LatestVersions UNKNOWN = new LatestVersions(LatestVersion.UNKNOWN, ImmutableSet.of());
   private final LatestVersion recommended;
   private final ImmutableSet<LatestVersion> allOtherLaterVersions;

   public LatestVersions(LatestVersion var1, Collection<LatestVersion> var2) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkNotNull(var2);
      this.recommended = var1;
      HashSet var3 = Sets.newHashSet();
      var3.remove(var1);
      this.allOtherLaterVersions = ImmutableSet.copyOf(var3);
   }

   public LatestVersion getRecommended() {
      return this.recommended;
   }

   public ImmutableSet<LatestVersion> getAllOtherLaterVersions() {
      return this.allOtherLaterVersions;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.recommended, this.allOtherLaterVersions);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof LatestVersions)) {
         return false;
      }

      LatestVersions var2 = (LatestVersions)var1;
      return this.recommended.equals(var2.recommended) && this.allOtherLaterVersions.equals(var2.allOtherLaterVersions);
   }

   @Override
   public String toString() {
      return this == UNKNOWN
         ? "Unknown"
         : Objects.toStringHelper(this).add("recommended", this.recommended).add("allOthers", this.allOtherLaterVersions).toString();
   }
}
