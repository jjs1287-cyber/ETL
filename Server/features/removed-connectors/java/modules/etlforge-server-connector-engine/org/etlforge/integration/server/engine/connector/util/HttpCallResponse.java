package org.etlforge.integration.server.engine.connector.util;

import com.google.common.base.Strings;
import java.nio.charset.Charset;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorEndpointRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorWebServiceRuntimeException;

public class HttpCallResponse {
   boolean isReponseInFile;
   boolean hasNoResponseContent;
   String responseCharsetEncoding = "";
   Charset responseCharsetNonUtf8 = null;
   String response = "";
   long callingTimeMillis;
   private int statusCode;
   String statusLine;
   private String cookie;
   String redirectUrl;
   private static final int UnauthorizedStatusCode = 401;

   HttpCallResponse() {
      this.setStatusCode(-1);
      this.statusLine = "";
      this.hasNoResponseContent = true;
      this.setCookie(null);
      this.redirectUrl = null;
   }

   public boolean isRedirectUrl() {
      return this.getStatusCode() == 302;
   }

   public boolean isBadResponse() {
      return this.getStatusCode() >= 300;
   }

   public boolean isGoodResponse() {
      return !this.isBadResponse();
   }

   boolean isUnauthorized() {
      return 401 == this.getStatusCode();
   }

   void setStatusLine(int var1, String var2) {
      this.setStatusCode(var1);
      this.statusLine = var2;
   }

   void setResponseCharset(String var1) {
      this.responseCharsetNonUtf8 = null;
      this.responseCharsetEncoding = var1;
      if (!Strings.isNullOrEmpty(this.responseCharsetEncoding)) {
         this.responseCharsetNonUtf8 = Charset.forName(this.responseCharsetEncoding);
         if (ConnectorExecutionSystem.isUTF8(this.responseCharsetNonUtf8)) {
            this.responseCharsetNonUtf8 = null;
         }
      }
   }

   public boolean isUtf8() {
      return this.responseCharsetNonUtf8 == null;
   }

   public String getStatusLine() {
      return this.statusLine;
   }

   public String getResponse() {
      return this.response;
   }

   public void setHasNoResponseContent(boolean var1) {
      this.hasNoResponseContent = var1;
   }

   public boolean hasNoResponseContent() {
      return this.hasNoResponseContent;
   }

   public void setStatusLine(String var1) {
      this.statusLine = var1;
   }

   public void throwRuntimeExceptionOnBadResponse() {
      this.throwRuntimeExceptionOnBadResponse("");
   }

   public void throwRuntimeExceptionOnBadResponse(String var1) {
      if (this.isBadResponse()) {
         String var2 = Strings.isNullOrEmpty(var1) ? this.getStatusLine() : this.getStatusLine() + "\n" + var1;
         if (this.isUnauthorized()) {
            throw new ConnectorEndpointRuntimeException(var2);
         } else {
            throw new ConnectorWebServiceRuntimeException(var2);
         }
      }
   }

   public void throwRuntimeExceptionOnUnauthorized(String var1) {
      if (this.isBadResponse()) {
         String var2 = Strings.isNullOrEmpty(var1) ? this.getStatusLine() : this.getStatusLine() + "\n" + var1;
         if (this.isUnauthorized()) {
            throw new ConnectorEndpointRuntimeException(var2);
         }
      }
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public void setStatusCode(int var1) {
      this.statusCode = var1;
   }

   public String getCookie() {
      return this.cookie;
   }

   public void setCookie(String var1) {
      this.cookie = var1;
   }

   public String getRedirectUrl() {
      return this.redirectUrl;
   }
}
