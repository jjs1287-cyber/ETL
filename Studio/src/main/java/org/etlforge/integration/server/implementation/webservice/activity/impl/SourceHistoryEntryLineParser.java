package org.etlforge.integration.server.implementation.webservice.activity.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;
import org.etlforge.integration.data.entity.id.OperationId;

final class SourceHistoryEntryLineParser {
   private String nextSql;
   private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   public SourceHistoryEntryLineParser() {
      this.timestampFormat.setLenient(true);
   }

   public SourceHistoryEntry parse(String var1) {
      if (StringUtils.isEmpty(var1)) {
         return null;
      }

      if (var1.startsWith("WHERE")) {
         this.nextSql = var1;
         return null;
      }

      try {
         String[] var2 = var1.split("\\t");
         return this.buildEntryFromParts(var2);
      } catch (RuntimeException var3) {
         return null;
      }
   }

   private SourceHistoryEntry buildEntryFromParts(String[] var1) {
      SourceHistoryEntryBuilder var2 = new SourceHistoryEntryBuilder();
      var2.setOperationId(new OperationId(var1[1]));
      var2.setOperationName(var1[2]);
      var2.setEntryId(Integer.parseInt(var1[3]));
      var2.setSourceFiles(var1[4]);
      var2.setFileSizeInBytes(Integer.parseInt(var1[5]));

      try {
         var2.setTimeStamp(this.timestampFormat.parse(var1[6]));
      } catch (ParseException var4) {
         System.err.println("Unexpected format of file timestamp sent from server: " + var1[6]);
      }

      return var2.getEntry();
   }

   public String getNextSql() {
      return this.nextSql;
   }
}
