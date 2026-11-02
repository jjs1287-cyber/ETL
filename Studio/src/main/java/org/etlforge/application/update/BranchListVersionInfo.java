package org.etlforge.application.update;

import java.util.SortedSet;

final class BranchListVersionInfo implements VersionInfo {
   private final BranchVersions branchVersions = new BranchVersions();

   public void addBranch(BranchVersion var1) {
      this.branchVersions.add(var1);
   }

   @Override
   public LatestVersions getLatestVersions(BranchId var1) {
      SortedSet var2 = this.branchVersions.lookupLatestVersions(var1);
      if (var2.isEmpty()) {
         return LatestVersions.UNKNOWN;
      }

      LatestVersion var3 = this.getRecommendedVersion(var2, var1);
      return new LatestVersions(var3, var2);
   }

   private LatestVersion getRecommendedVersion(SortedSet<LatestVersion> var1, BranchId var2) {
      for (LatestVersion var4 : var1) {
         if (var4.isRecommendedFor(var2.currentVersion)) {
            return var4;
         }
      }

      return new LatestVersion(var2.currentVersion, null);
   }

   @Override
   public String toString() {
      return this.branchVersions.toString();
   }
}
