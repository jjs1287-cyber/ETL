package org.etlforge.integration.server.implementation.webservice.activity.impl;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.activity.client.history.SourceHistoryEntry;

final class SourceHistoryResponseParser {
   private String nextSql;
   private List<SourceHistoryEntry> entries;

   public List<SourceHistoryEntry> parse(String[] var1) {
      this.entries = Lists.newArrayList();
      if (var1 != null) {
         for (String var5 : var1) {
            this.parseLine(var5);
         }
      }

      return this.entries;
   }

   private void parseLine(String var1) {
      SourceHistoryEntryLineParser var2 = new SourceHistoryEntryLineParser();
      SourceHistoryEntry var3 = var2.parse(var1);
      if (var3 != null) {
         this.entries.add(var3);
      } else if (var2.getNextSql() != null) {
         this.nextSql = var2.getNextSql();
      }
   }

   public String getNextSql() {
      return this.nextSql;
   }
}
