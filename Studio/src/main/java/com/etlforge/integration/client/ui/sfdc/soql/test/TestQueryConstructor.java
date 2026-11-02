package com.etlforge.integration.client.ui.sfdc.soql.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.etlforge.system.ArgCheck;

public final class TestQueryConstructor {
   private int limit = 10;

   public void setLimit(int var1) {
      this.limit = ArgCheck.checkPositive(var1);
   }

   public String getTestableQuery(String var1) {
      ArgCheck.checkNotBlank(var1);
      String var2 = var1.trim();
      ExistingLimit var3 = ExistingLimitFinder.extract(var2);
      if (var3 != null) {
         return var3.replaceIfNecessary(this.limit);
      }

      int var4 = this.lookForEndingOffset(var2);
      return var4 == -1 ? var2 + " LIMIT " + this.limit : var1.substring(0, var4) + "LIMIT " + this.limit + " " + var1.substring(var4);
   }

   private int lookForEndingOffset(String var1) {
      String var2 = ".*\\s+(offset\\s+-?\\d+)\\s*\\z";
      Pattern var3 = Pattern.compile(var2, 32);
      Matcher var4 = var3.matcher(var1.toLowerCase());
      return var4.matches() ? var4.start(1) : -1;
   }
}
