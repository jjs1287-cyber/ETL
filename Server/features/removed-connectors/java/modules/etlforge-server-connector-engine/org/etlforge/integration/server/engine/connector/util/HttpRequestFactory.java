package org.etlforge.integration.server.engine.connector.util;

import com.google.common.base.Strings;
import java.io.File;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerNotSupportedRuntimeException;

public class HttpRequestFactory {
   public static HttpRequestBase newHttpRequestBase(String var0, String var1) {
      if (var0.equalsIgnoreCase("GET")) {
         return new HttpGet(var1);
      } else if (var0.equalsIgnoreCase("DELETE")) {
         return new HttpDelete(var1);
      } else if (var0.equalsIgnoreCase("PUT")) {
         return new HttpPut(var1);
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("The http command " + var0 + " is not Supported");
      }
   }

   public static HttpEntityEnclosingRequestBase newHttpEntityEnclosingRequestBase(String var0, String var1, String var2) {
      StringEntity var3 = null;
      if (!Strings.isNullOrEmpty(var2)) {
         String var4 = ConnectorExecutionSystem.ConnectorDefaultCharset_UTF8.name();
         var3 = new StringEntity(var2, var4);
      }

      return newHttpEntityEnclosingRequestBase(var0, var1, var3);
   }

   public static HttpEntityEnclosingRequestBase newHttpEntityEnclosingRequestBase(String var0, String var1, File var2) {
      FileEntity var3 = new FileEntity(var2, "application/xml;charset=utf-8");
      return newHttpEntityEnclosingRequestBase(var0, var1, var3);
   }

   private static HttpEntityEnclosingRequestBase newHttpEntityEnclosingRequestBase(String var0, String var1, HttpEntity var2) {
      HttpEntityEnclosingRequestBase var3 = newHttpEntityEnclosingRequestBase(var0, var1);
      if (var2 != null) {
         var3.setEntity(var2);
      }

      return var3;
   }

   private static HttpEntityEnclosingRequestBase newHttpEntityEnclosingRequestBase(String var0, String var1) {
      if (var0.equalsIgnoreCase("POST")) {
         return new HttpPost(var1);
      } else if (var0.equalsIgnoreCase("PUT")) {
         return new HttpPut(var1);
      } else {
         throw new ConnectorServerNotSupportedRuntimeException("The http command " + var0);
      }
   }
}
