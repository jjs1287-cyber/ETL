package org.etlforge.integration.structure.mapping;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import java.util.StringTokenizer;

public final class PathSeparator {
   public static final String SINGLE = "$";
   public static final char SINGLE_CHAR = '$';
   public static final String LOOP = ".";
   public static final char LOOP_CHAR = '.';

   public static boolean starts(String var0) {
      return var0.startsWith("$") || var0.startsWith(".");
   }

   public static boolean terminates(String var0) {
      return var0.endsWith("$") || var0.endsWith(".");
   }

   public static boolean containsLoop(String var0) {
      return var0.indexOf(46) >= 0;
   }

   public static boolean startsWithLoopSeparator(String var0) {
      return !var0.isEmpty() && var0.charAt(0) == '.';
   }

   public static boolean endsWithLoopSeparator(String var0) {
      return !var0.isEmpty() && var0.charAt(var0.length() - 1) == '.';
   }

   public static boolean isSeparator(char var0) {
      return var0 == '$' || var0 == '.';
   }

   public static String removeLeading(String var0) {
      return !var0.startsWith("$") && !var0.startsWith(".") ? var0 : var0.substring(1);
   }

   public static String removeTrailing(String var0) {
      return !var0.endsWith("$") && !var0.endsWith(".") ? var0 : var0.substring(0, var0.length() - 1);
   }

   public static String[] allSeparators() {
      return new String[]{"$", "."};
   }

   public static String allSeparatorsAsString() {
      return "$.";
   }

   public static StringTokenizer tokenize(String var0, boolean var1) {
      return new StringTokenizer(var0, allSeparatorsAsString(), var1);
   }

   public static Iterable<String> split(String var0) {
      return Splitter.on(CharMatcher.anyOf("$.")).omitEmptyStrings().split(var0);
   }

   private PathSeparator() {
   }
}
