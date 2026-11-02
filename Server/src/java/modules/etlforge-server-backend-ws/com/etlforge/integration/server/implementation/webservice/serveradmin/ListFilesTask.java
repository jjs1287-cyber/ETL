package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.io.File;
import org.etlforge.lang.Arrays2;

abstract class ListFilesTask extends Task<WsServerFileLocators> {
   public ListFilesTask(String var1, String var2, String var3) {
      super(var1, var2, var3);
   }

   protected final WsServerFileLocators runTask() throws Exception {
      File[] var1 = this.listFiles();
      WsServerFileLocator[] var2 = convertToLocators(var1);
      return new WsServerFileLocators(var2);
   }

   protected abstract File[] listFiles() throws Exception;

   private static WsServerFileLocator[] convertToLocators(File[] var0) {
      return Arrays2.transform(var0, WsServerFileLocator.class, Transformers.TO_FILE_LOCATOR);
   }
}
