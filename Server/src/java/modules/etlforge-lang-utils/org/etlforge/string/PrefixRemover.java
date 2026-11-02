package org.etlforge.string;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.apache.commons.lang.StringUtils;

public abstract class PrefixRemover implements Function<String, String>, Serializable {
   protected final String prefix;

   public static PrefixRemover withCase(String var0) {
      return new PrefixRemover.WithCase(var0);
   }

   public static PrefixRemover ignoringCase(String var0) {
      return new PrefixRemover.IgnoringCase(var0);
   }

   private PrefixRemover(String var1) {
      this.prefix = Preconditions.checkNotNull(var1);
   }

   private static class IgnoringCase extends PrefixRemover {
      public IgnoringCase(String var1) {
         super(var1);
      }

      public String apply(String var1) {
         return StringUtils.removeStartIgnoreCase(var1, this.prefix);
      }

      @Override
      public String toString() {
         return Objects.toStringHelper(PrefixRemover.class).add("prefix ignoring case", this.prefix).toString();
      }
   }

   private static class WithCase extends PrefixRemover {
      public WithCase(String var1) {
         super(var1);
      }

      public String apply(String var1) {
         return StringUtils.removeStart(var1, this.prefix);
      }

      @Override
      public String toString() {
         return Objects.toStringHelper(PrefixRemover.class).add("prefix with case", this.prefix).toString();
      }
   }
}
