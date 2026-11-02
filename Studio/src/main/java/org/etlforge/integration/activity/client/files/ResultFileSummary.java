package org.etlforge.integration.activity.client.files;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.ArrayList;
import org.etlforge.util.csv.CsvFile;
import org.etlforge.util.csv.Record;

public final class ResultFileSummary {
   private final File localFile;
   private CsvFile csvFileRepresentation;
   private final boolean complete;

   public ResultFileSummary(File var1, CsvFile var2, boolean var3) {
      this.localFile = var1;
      this.csvFileRepresentation = Preconditions.checkNotNull(var2);
      this.complete = var3;
   }

   public File getLocalFile() {
      return this.localFile;
   }

   public CsvFile getCsvFileRepresentation() {
      return this.csvFileRepresentation;
   }

   public void appendEmptyColumn() {
      if (!this.isEmpty()) {
         Record var1 = this.csvFileRepresentation.getHeader().append(" ");
         ArrayList var2 = Lists.newArrayList();

         for (Record var4 : this.csvFileRepresentation.getRecords()) {
            var2.add(var4.append(" "));
         }

         this.csvFileRepresentation = new CsvFile(var1, var2);
      }
   }

   public boolean isEmpty() {
      return this.csvFileRepresentation.isEmpty();
   }

   public boolean isComplete() {
      return this.complete;
   }

   public int getNumberOfRows() {
      return this.csvFileRepresentation.getNumberOfRecords();
   }
}
