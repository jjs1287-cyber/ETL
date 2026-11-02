package org.etlforge.application.update;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeBasedTable;
import java.util.Collections;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;
import org.etlforge.util.version.Version;

final class BranchVersions {
   private final TreeBasedTable<String, Branch, BranchVersion> table = TreeBasedTable.create();

   public void add(BranchVersion var1) {
      String var2 = var1.getAppId();
      Branch var3 = var1.getBranch();
      BranchVersion var4 = this.table.get(var2, var3);
      if (var4 == null || !var4.getLatestVersion().isLaterThan(var1.getLatestVersion())) {
         this.table.put(var2, var3, var1);
      }
   }

   public SortedSet<LatestVersion> lookupLatestVersions(BranchId var1) {
      Branch var2 = Branch.forVersion(var1.currentVersion);
      SortedMap var3 = this.table.row(var1.appId);
      SortedMap var4 = var3.tailMap(var2);
      TreeSet var5 = Sets.newTreeSet(Collections.reverseOrder());

      for (Object __etlItem30 : var4.values()) {
         BranchVersion var7 = (BranchVersion) __etlItem30;
         var5.add(var7.getLatestVersion());
      }

      return var5;
   }

   @VisibleForTesting
   BranchVersion lookupBranch(String var1, Version var2) {
      Branch var3 = Branch.forVersion(var2);
      return this.table.get(var1, var3);
   }

   @Override
   public String toString() {
      return this.table.toString();
   }
}
