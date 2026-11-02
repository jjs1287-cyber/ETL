package org.etlforge.application.update;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.version.Version;

final class BranchVersion {
   private final String appId;
   private final Branch branch;
   private final LatestVersion latestVersion;

   public BranchVersion(String var1, Version var2, LatestVersion var3) {
      this(var1, Branch.forVersion(var2), var3);
   }

   public BranchVersion(String var1, Branch var2, LatestVersion var3) {
      this.appId = ArgCheck.checkNotBlank(var1);
      this.branch = Preconditions.checkNotNull(var2);
      this.latestVersion = Preconditions.checkNotNull(var3);
   }

   public String getAppId() {
      return this.appId;
   }

   public Branch getBranch() {
      return this.branch;
   }

   public LatestVersion getLatestVersion() {
      return this.latestVersion;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.appId, this.branch, this.latestVersion);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof BranchVersion)) {
         return false;
      }

      BranchVersion var2 = (BranchVersion)var1;
      return this.appId.equals(var2.appId) && this.branch.equals(var2.branch) && this.latestVersion.equals(var2.latestVersion);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("app-id", this.appId).add("branch", this.branch).add("latest-version", this.latestVersion).toString();
   }
}
