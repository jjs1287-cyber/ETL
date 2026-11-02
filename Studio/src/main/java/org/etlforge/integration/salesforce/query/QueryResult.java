package org.etlforge.integration.salesforce.query;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import org.etlforge.util.csv.CsvFile;
import org.etlforge.util.csv.Record;

public final class QueryResult {
   private final ImmutableList<QueryRecord> records;
   private final boolean done;
   private final String queryLocator;

   public QueryResult(Iterable<QueryRecord> var1, boolean var2, String var3) {
      this.records = ImmutableList.copyOf(var1);
      this.queryLocator = var3;
      this.done = var2;
   }

   public QueryResult addMore(QueryResult var1) {
      ArrayList var2 = Lists.newArrayList(this.records);
      var2.addAll(var1.records);
      return new QueryResult(var2, var1.done, var1.queryLocator);
   }

   public ImmutableList<QueryRecord> getRecords() {
      return this.records;
   }

   public boolean isDone() {
      return this.done;
   }

   public String getQueryLocator() {
      return this.queryLocator;
   }

   public boolean isEmpty() {
      return this.records.isEmpty();
   }

   public int getNumberOfRecords() {
      return this.records.size();
   }

   public boolean isNested() {
      return this.records.isEmpty() ? false : this.records.get(0).isNested();
   }

   public CsvFile asCsvFile() {
      if (this.isEmpty()) {
         return CsvFile.empty();
      }

      if (this.isNested()) {
         throw new UnsupportedOperationException("A nested QueryResult does not have a CSV representation.");
      }

      Record var1 = new Record(this.records.get(0).getFieldNames());
      CsvFile var2 = new CsvFile(var1);

      for (QueryRecord var4 : this.records) {
         Record var5 = Record.of(var4.getFieldValues());
         var2.addRecord(var5);
      }

      return var2;
   }

   public String getStringRepresentation() {
      StringBuilder var1 = new StringBuilder("Number of records: ").append(this.records.size()).append("\n");
      var1.append("All records returned: ").append(this.done).append("\n");

      for (QueryRecord var3 : this.records) {
         try {
            var3.writeTo(var1);
         } catch (IOException var5) {
            throw new RuntimeException(var5);
         }
      }

      return var1.toString();
   }
}
