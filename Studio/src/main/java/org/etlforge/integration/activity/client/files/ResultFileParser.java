package org.etlforge.integration.activity.client.files;

import com.google.common.base.Preconditions;
import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import org.etlforge.integration.common.server.file.OperationFile;
import org.etlforge.util.csv.CSVParams;
import org.etlforge.util.csv.CsvFile;

public final class ResultFileParser implements Callable<ResultFileSummary> {
   private final File localFile;
   private final Charset encoding;
   private final CSVParams csvParams;

   public ResultFileParser(OperationFile var1, CSVParams var2) {
      this(var1.asLocalFile(), var1.getEncoding(), var2);
      Preconditions.checkArgument(var1.existsLocally(), "opFile must be a local file.");
   }

   public ResultFileParser(File var1, Charset var2, CSVParams var3) {
      this.localFile = var1;
      this.encoding = var2;
      this.csvParams = var3;
   }

   public ResultFileSummary call() throws Exception {
      CsvFile.Factory var1 = CsvFile.newFactory().withCsvParams(this.csvParams).withEncoding(this.encoding).limitRecordCountTo(100);
      CsvFile var2 = var1.build(this.localFile);
      boolean var3 = var1.isFileComplete();
      return new ResultFileSummary(this.localFile, var2, var3);
   }
}
