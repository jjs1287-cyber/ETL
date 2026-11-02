package com.etlforge.integration.server.engine.bulkload.batch;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.apache.commons.io.IOUtils;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;

final class BatchResponseParser {
   public static String getId(HttpURLConnection var0) throws Exception {
      return getId(getResponse(var0));
   }

   public static String getResponse(HttpURLConnection var0) throws IOException {
      InputStream var1 = null;

      try {
         var1 = var0.getInputStream();
         return IOUtils.toString(var1);
      } finally {
         KongaIoUtils.close(var1);
      }
   }

   public static String getId(String var0) throws Exception {
      BatchResponseParser.IdHandler var1 = new BatchResponseParser.IdHandler();
      KongaSaxParser var2 = new KongaSaxParser(var1);
      var2.parseString(var0);
      return var1.id;
   }

   private BatchResponseParser() {
   }

   private static class IdHandler {
      public String id;

      private IdHandler() {
      }

      @EndElementHandler("id")
      public void handleEnd(String var1) {
         this.id = var1.trim();
      }
   }
}
