package org.etlforge.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum StringMatchers implements StringMatcher {
   EQUALS {
      @Override
      public boolean matches(String var1, String var2) {
         return var1.equals(var2);
      }
   },
   EQUALS_IGNORE_CASE {
      @Override
      public boolean matches(String var1, String var2) {
         return var1.equalsIgnoreCase(var2);
      }
   },
   CONTAINS {
      @Override
      public boolean matches(String var1, String var2) {
         return var2 != null && var1.length() > 0 && var2.indexOf(var1) >= 0;
      }
   },
   CONTAINS_IGNORE_CASE {
      @Override
      public boolean matches(String var1, String var2) {
         return var2 != null && CONTAINS.matches(var1.toLowerCase(), var2.toLowerCase());
      }
   },
   STARTS_WITH {
      @Override
      public boolean matches(String var1, String var2) {
         return var2 != null && var1.length() > 0 && var2.startsWith(var1);
      }
   },
   STARTS_WITH_IGNORE_CASE {
      @Override
      public boolean matches(String var1, String var2) {
         return var2 != null && STARTS_WITH.matches(var1.toLowerCase(), var2.toLowerCase());
      }
   },
   ENDS_WITH {
      @Override
      public boolean matches(String var1, String var2) {
         return var2 != null && var1.length() > 0 && var2.endsWith(var1);
      }
   },
   ENDS_WITH_IGNORE_CASE {
      @Override
      public boolean matches(String var1, String var2) {
         return var2 != null && ENDS_WITH.matches(var1.toLowerCase(), var2.toLowerCase());
      }
   },
   REGEX {
      @Override
      public boolean matches(String var1, String var2) {
         return var2 != null && Pattern.matches(var1, var2);
      }
   },
   REGEX_IGNORE_CASE {
      @Override
      public boolean matches(String var1, String var2) {
         Pattern var3 = Pattern.compile(var1, 2);
         Matcher var4 = var3.matcher(var2);
         return var4.matches();
      }
   };

   StringMatchers() {
   }
}
