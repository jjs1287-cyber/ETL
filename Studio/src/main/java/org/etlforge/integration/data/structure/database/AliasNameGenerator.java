package org.etlforge.integration.data.structure.database;

import com.google.common.base.Preconditions;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseObject;

public final class AliasNameGenerator {
   private static final String MARKER = "_";

   public static final String getAliasName(DatabaseObject var0, int var1, BeginEndQuote var2) {
      return getAliasName(var0.getName(), var1, var2);
   }

   public static final String getAliasName(String var0, int var1, BeginEndQuote var2) {
      Preconditions.checkArgument(var1 >= 2, "instance must be >= 2 (was " + var1 + ")");
      return var2.isEnclosingName(var0) ? var0.substring(0, var0.length() - var2.end.length()) + "_" + (var1 - 1) + var2.end : var0 + "_" + (var1 - 1);
   }

   public static final DatabaseObject createAlias(DatabaseObject var0, int var1, BeginEndQuote var2) {
      String var3 = getAliasName(var0, var1, var2);
      return var0.createCopy(var3);
   }

   private AliasNameGenerator() {
   }
}
