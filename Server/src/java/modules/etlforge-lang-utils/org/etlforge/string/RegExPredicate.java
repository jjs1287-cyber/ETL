package org.etlforge.string;

import com.google.common.base.Predicate;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegExPredicate implements Predicate<String>, Serializable {
   private final Pattern pattern;
   private final boolean exactMatch;

   public RegExPredicate(String var1) {
      this(var1, true, 0);
   }

   public RegExPredicate(String var1, boolean var2) {
      this(var1, var2, 0);
   }

   public RegExPredicate(String var1, int var2) {
      this(var1, true, var2);
   }

   public RegExPredicate(String var1, boolean var2, int var3) {
      this.pattern = Pattern.compile(var1, var3);
      this.exactMatch = var2;
   }

   public boolean apply(String var1) {
      Matcher var2 = this.pattern.matcher(var1);
      return this.exactMatch ? var2.matches() : var2.find();
   }
}
