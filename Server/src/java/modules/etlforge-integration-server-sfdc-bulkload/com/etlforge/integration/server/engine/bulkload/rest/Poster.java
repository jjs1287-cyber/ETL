package com.etlforge.integration.server.engine.bulkload.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public final class Poster {
   private final String url;
   private final String sessionId;

   public Poster(String var1, String var2) {
      this.url = var1;
      this.sessionId = var2;
   }

   public String post(String var1, HttpClient var2) throws Exception {
      return this.post(var1, "application/xml", var2);
   }

   public String post(String var1, String var2, HttpClient var3) throws Exception {
      HttpPost var4 = new HttpPost(this.url);
      var4.setHeader("X-SFDC-Session", this.sessionId);
      var4.setEntity(new StringEntity(var1, var2, "UTF-8"));
      ByteArrayResponseHandler var5 = new ByteArrayResponseHandler();
      byte[] var6 = var3.execute(var4, var5);
      if (var6 == null) {
         throw new Exception("No response");
      } else {
         return new String(var6, "UTF-8");
      }
   }
}
