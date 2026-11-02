package org.etlforge.string;

import com.google.common.base.Predicate;
import java.io.Serializable;

public final class StringPredicates {
   public static final Predicate<String> contains(String var0) {
      return new StringPredicates.Contains(var0);
   }

   public static final Predicate<String> containsIgnoreCase(String var0) {
      return new StringPredicates.ContainsIgnoreCase(var0);
   }

   public static Predicate<String> startsWith(String var0) {
      return new StringPredicates.StartsWith(var0);
   }

   public static Predicate<String> endsWith(String var0) {
      return new StringPredicates.EndsWith(var0);
   }

   private StringPredicates() {
   }

   private static final class Contains implements Predicate<String>, Serializable {
      private final String searchString;

      public Contains(String var1) {
         this.searchString = var1;
      }

      public boolean apply(String var1) {
         return var1 != null && var1.contains(this.searchString);
      }

      @Override
      public String toString() {
         return "contains \"" + this.searchString + "\"";
      }
   }

   private static final class ContainsIgnoreCase implements Predicate<String>, Serializable {
      private final String searchString;

      public ContainsIgnoreCase(String var1) {
         this.searchString = var1.toLowerCase();
      }

      public boolean apply(String var1) {
         return var1 != null && var1.toLowerCase().contains(this.searchString);
      }

      @Override
      public String toString() {
         return "contains (ignoring case) \"" + this.searchString + "\"";
      }
   }

   private static final class EndsWith implements Predicate<String>, Serializable {
      private final String suffix;

      private EndsWith(String var1) {
         this.suffix = var1;
      }

      public boolean apply(String var1) {
         return var1 != null && var1.endsWith(this.suffix);
      }

      @Override
      public String toString() {
         return "ends-with \"" + this.suffix + "\"";
      }
   }

   private static final class StartsWith implements Predicate<String>, Serializable {
      private final String prefix;

      private StartsWith(String var1) {
         this.prefix = var1;
      }

      public boolean apply(String var1) {
         return var1 != null && var1.startsWith(this.prefix);
      }

      @Override
      public String toString() {
         return "starts-with \"" + this.prefix + "\"";
      }
   }
}
