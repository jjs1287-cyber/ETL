package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import org.etlforge.util.persist.Persistor;

public final class StringOptions {
   public static final StringOptions DEFAULT = new StringOptions(false, false);
   public final boolean ignoreCase;
   public final boolean trimToLength;

   public StringOptions(boolean var1, boolean var2) {
      this.ignoreCase = var1;
      this.trimToLength = var2;
   }

   public void writeTo(Persistor var1) {
      var1.putBoolean("ignoreCase", this.ignoreCase);
      var1.putBoolean("trimToLength", this.trimToLength);
   }

   public static Optional<StringOptions> readFrom(Persistor var0) {
      Boolean var1 = var0.getBoolean("ignoreCase");
      if (var1 != null) {
         Boolean var2 = var0.getBoolean("trimToLength");
         if (var2 != null) {
            return Optional.of(new StringOptions(var1, var2));
         }
      }

      return Optional.absent();
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.ignoreCase, this.trimToLength);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof StringOptions)) {
         return false;
      }

      StringOptions var2 = (StringOptions)var1;
      return this.ignoreCase == var2.ignoreCase && this.trimToLength == var2.trimToLength;
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).add("ignore-case", this.ignoreCase).add("trimToLength", this.trimToLength).toString();
   }
}
