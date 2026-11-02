package com.etlforge.integration.server.engine.bulkload.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public final class Getter {
   private final String url;
   private final String sessionId;

   public Getter(String var1, String var2) {
      this.url = var1;
      this.sessionId = var2;
   }

   public String get(HttpClient var1) throws Exception {
      HttpGet var2 = new HttpGet(this.url);
      var2.setHeader("X-SFDC-Session", this.sessionId);
      ByteArrayResponseHandler var3 = new ByteArrayResponseHandler();
      byte[] var4 = var1.execute(var2, var3);
      if (var4 == null) {
         throw new Exception("No response for call " + this.url);
      } else {
         return new String(var4, "UTF-8");
      }
   }
}
