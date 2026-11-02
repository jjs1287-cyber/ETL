package org.etlforge.application.update;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import org.etlforge.util.version.Version;

public final class Branch implements Comparable<Branch> {
   private final int major;
   private final int minor;

   public static Branch forVersion(Version var0) {
      return new Branch(var0.getMajorVersion(), var0.getMinorVersion());
   }

   static Branch parse(String var0) {
      String[] var1 = var0.split("\\.");
      int var2 = Integer.parseInt(var1[0]);
      int var3 = Integer.parseInt(var1[1]);
      return new Branch(var2, var3);
   }

   public Branch(int var1, int var2) {
      this.major = var1;
      this.minor = var2;
   }

   public int compareTo(Branch var1) {
      return ComparisonChain.start().compare(this.major, var1.major).compare(this.minor, var1.minor).result();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.major, this.minor);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Branch)) {
         return false;
      }

      Branch var2 = (Branch)var1;
      return this.major == var2.major && this.minor == var2.minor;
   }

   @Override
   public String toString() {
      return this.major + "." + this.minor;
   }
}
