package com.etlforge.integration.server.engine.bulkload.rest;

import java.util.logging.Logger;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

final class Utils {
   public static final String NAMESPACE = "http://www.force.com/2009/06/asyncapi/dataload";
   public static final String CONTENT_TYPE = "application/xml";
   public static final String ENCODING = "UTF-8";
   public static final String MIME_TYPE = "application/xml; charset=UTF-8";

   public static void logError(Exception var0, Logger var1, String var2, String var3, String var4) {
      if (var1 != null) {
         String var5 = String.format(
            "%s. Request:%s%s%sResponse:%s%s%sError:%s%s",
            var2,
            SystemUtils.LINE_SEPARATOR,
            var3,
            SystemUtils.LINE_SEPARATOR,
            SystemUtils.LINE_SEPARATOR,
            var4,
            SystemUtils.LINE_SEPARATOR,
            SystemUtils.LINE_SEPARATOR,
            ExceptionUtils.getFullStackTrace(var0)
         );
         var1.severe(var5);
      }
   }

   private Utils() {
   }
}
