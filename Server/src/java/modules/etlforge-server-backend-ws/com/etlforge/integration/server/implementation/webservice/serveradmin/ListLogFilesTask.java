package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.server.log.LogDirectory;

abstract class ListLogFilesTask extends ListFilesTask {
   private final String startDate;
   private final String endDate;

   protected ListLogFilesTask(String var1, String var2, String var3, String var4, String var5) {
      super(var1, var2, var3);
      this.startDate = var4;
      this.endDate = var5;
   }

   @Override
   protected final File[] listFiles() throws Exception {
      LogDirectory var1 = this.getLogDirectory();
      return var1.listFilesFromModifiedDate(parseDateString(this.startDate), parseDateString(this.endDate));
   }

   protected abstract LogDirectory getLogDirectory() throws Exception;

   private static Date parseDateString(String var0) throws ParseException {
      return StringUtils.isNotEmpty(var0) ? Transformers.dateFormat.parse(var0) : null;
   }
}
