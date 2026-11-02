package org.etlforge.application.update;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.version.Version;

public final class BranchId {
   public final String appId;
   public final Version currentVersion;

   public BranchId(String var1, Version var2) {
      this.appId = ArgCheck.checkNotBlank(var1).toLowerCase();
      this.currentVersion = Preconditions.checkNotNull(var2);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.appId, this.currentVersion);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof BranchId)) {
         return false;
      }

      BranchId var2 = (BranchId)var1;
      return this.appId.equals(var2.appId) && this.currentVersion.equals(var2.currentVersion);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("app-id", this.appId).add("current-version", this.currentVersion).toString();
   }
}
