package com.etlforge.integration.client.ui.script.fcn.db.sql;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.database.ui.sqlbuilder.NameGenerator;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;

final class QuoteEscapingNameGenerator implements NameGenerator {
   private final NameGenerator impl;

   public QuoteEscapingNameGenerator(NameGenerator var1) {
      this.impl = var1;
   }

   @Override
   public String getTableName(DatabaseObject var1) {
      String var2 = this.impl.getTableName(var1);
      return escapeQuotes(var2);
   }

   private static String escapeQuotes(String var0) {
      return var0.startsWith("\"") && var0.endsWith("\"") ? "\\" + StringUtils.removeEnd(var0, "\"") + "\\\"" : var0;
   }

   @Override
   public String getColumnName(DatabaseColumn var1) {
      String var2 = this.impl.getColumnName(var1);
      return escapeQuotes(var2);
   }
}
