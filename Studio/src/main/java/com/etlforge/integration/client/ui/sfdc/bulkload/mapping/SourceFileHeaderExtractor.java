package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Callable;
import org.etlforge.collections.Collections2;

final class SourceFileHeaderExtractor implements Callable<String> {
   private final File file;

   public SourceFileHeaderExtractor(File var1) {
      this.file = var1;
   }

   public String call() throws Exception {
      Charset var1 = Charset.defaultCharset();
      SampleFile var2 = SampleFile.create(this.file, var1, 5);
      List var3 = var2.getHeader();
      return Collections2.join(var3, ",");
   }
}
