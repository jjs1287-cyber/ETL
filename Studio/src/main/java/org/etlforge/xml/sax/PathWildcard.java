package org.etlforge.xml.sax;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import org.apache.commons.lang.StringUtils;

final class PathWildcard {
   public static final Ordering<PathWildcard> ORDER_OF_SPECIFICITY = createOrderOfSpecificity();
   private final String pattern;
   private final PathWildcard.Level level;
   private final int depth;

   private static Ordering<PathWildcard> createOrderOfSpecificity() {
      Ordering var0 = Ordering.from(new Comparator<PathWildcard>() {
         public int compare(PathWildcard var1, PathWildcard var2) {
            return Ints.compare(var2.depth, var1.depth);
         }
      });
      return var0.compound(new Comparator<PathWildcard>() {
         public int compare(PathWildcard var1, PathWildcard var2) {
            return var1.level.compareTo(var2.level);
         }
      });
   }

   public PathWildcard(String var1) {
      this.pattern = StringUtils.substringBefore(var1, "*");
      Preconditions.checkArgument(this.pattern.isEmpty() || this.pattern.endsWith("/"), "Invalid pattern: must end with /* or /** (was: " + var1 + ")");
      String var2 = var1.substring(this.pattern.length());
      this.level = this.getLevel(var2, var1);
      if (this.pattern.isEmpty()) {
         this.depth = 0;
      } else {
         this.depth = Lists.newArrayList(Splitter.on('/').omitEmptyStrings().split(this.pattern)).size();
      }
   }

   private PathWildcard.Level getLevel(String var1, String var2) {
      if (var1.equals("*")) {
         return PathWildcard.Level.SINGLE;
      } else if (var1.equals("**")) {
         return PathWildcard.Level.ANY;
      } else {
         throw new IllegalArgumentException("Invalid pattern: Must end with * or ** (was: " + var2 + ")");
      }
   }

   public boolean matches(String var1) {
      if (Strings.commonPrefix(var1, this.pattern).equals(this.pattern)) {
         String var2 = var1.substring(this.pattern.length());
         if (!var2.isEmpty()) {
            switch (this.level) {
               case ANY:
                  return true;
               case SINGLE:
                  int var3 = var2.indexOf(47);
                  return var3 == -1 || var3 == var2.length() - 1;
            }
         }
      }

      return false;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.level, this.pattern);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof PathWildcard)) {
         return false;
      }

      PathWildcard var2 = (PathWildcard)var1;
      return this.level == var2.level && this.pattern.equals(var2.pattern);
   }

   @Override
   public String toString() {
      return this.pattern + (this.level == PathWildcard.Level.SINGLE ? "*" : "**");
   }

   private enum Level {
      SINGLE,
      ANY;
   }
}
