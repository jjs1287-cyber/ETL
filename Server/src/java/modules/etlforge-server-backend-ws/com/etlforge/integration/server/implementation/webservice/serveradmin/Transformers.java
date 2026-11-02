package com.etlforge.integration.server.implementation.webservice.serveradmin;

import com.google.common.base.Function;
import java.io.File;
import java.util.Date;
import org.etlforge.integration.common.server.ServerDateFormat;
import org.etlforge.util.text.ThreadSafeDateFormat;

final class Transformers {
   static final ThreadSafeDateFormat dateFormat = new ServerDateFormat().getThreadSafeFormatter();
   public static final Function<File, WsServerFileLocator> TO_FILE_LOCATOR = new Function<File, WsServerFileLocator>() {
      public WsServerFileLocator apply(File var1) {
         String var2 = "";
         long var3 = var1.lastModified();
         if (var3 != 0L) {
            var2 = Transformers.dateFormat.format(new Date(var3));
         }

         return new WsServerFileLocator(var1.getAbsolutePath(), var2, var1.length());
      }
   };

   private Transformers() {
   }
}
