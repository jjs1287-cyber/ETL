package org.etlforge.integration.database.info;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ManualSqlStatement extends DatabaseObject {
   public ManualSqlStatement(String var1) {
      super(var1, null);
   }

   @Override
   public DatabaseObjectType getType() {
      return DatabaseObjectType.MANUAL_SQL;
   }

   public String guessTableName() {
      String var1 = this.getName();
      Pattern var2 = Pattern.compile(".*?\\s+from\\s+([^\\s,]+)\\s*", 2);
      Matcher var3 = var2.matcher(var1);
      return var3.find() ? var3.group(1) : null;
   }
}
