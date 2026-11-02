package com.etlforge.integration.server.email;

import com.google.common.base.Strings;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import org.apache.commons.lang.StringUtils;

public class MailServerSettings {
   private final String host;
   private final String userName;
   private final String password;
   private final int port;
   private final boolean useSSL;
   private static final long CONNECTION_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(1L);
   private static final long SOCKET_TIMEOUT_MILLIS = TimeUnit.MINUTES.toMillis(1L);
   private static final int STARTTLS_PORT = 587;

   String getProtocol() {
      return this.useSSL() ? "smtps" : "smtp";
   }

   public MailServerSettings(String var1, String var2, String var3, int var4, boolean var5) {
      if (StringUtils.isEmpty(var1)) {
         throw new IllegalArgumentException("SMTP host must be non-empty.");
      }

      this.host = var1;
      this.userName = Strings.nullToEmpty(var2);
      this.password = Strings.nullToEmpty(var3);
      this.port = var4 > 0 ? var4 : -1;
      this.useSSL = var5;
   }

   private boolean useSSL() {
      return this.useSSL;
   }

   private boolean useSTARTTLS() {
      return this.port == 587;
   }

   Session createSession() {
      Properties var1 = new Properties();
      var1.setProperty(this.getFullPropertyName("host"), this.host);
      if (this.doAuthentication()) {
         var1.setProperty(this.getFullPropertyName("user"), this.userName);
      }

      var1.setProperty(this.getFullPropertyName("connectiontimeout"), String.valueOf(CONNECTION_TIMEOUT_MILLIS));
      var1.setProperty(this.getFullPropertyName("timeout"), String.valueOf(SOCKET_TIMEOUT_MILLIS));
      var1.setProperty(this.getFullPropertyName("auth"), this.doAuthentication() ? "true" : "false");
      if (this.useSSL()) {
         var1.setProperty("mail.transport.protocol", "smtps");
         var1.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         var1.setProperty(this.getFullPropertyName("ssl.enable"), "true");
      } else {
         var1.setProperty("mail.transport.protocol", "smtp");
         if (this.useSTARTTLS()) {
            var1.setProperty(this.getFullPropertyName("starttls.enable"), "true");
         }
      }

      if (this.port > 0) {
         var1.setProperty(this.getFullPropertyName("port"), Integer.toString(this.port));
      }

      return this.doAuthentication() ? Session.getInstance(var1, new Authenticator() {
         @Override
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(MailServerSettings.this.userName, MailServerSettings.this.password);
         }
      }) : Session.getInstance(var1);
   }

   private boolean doAuthentication() {
      return !this.userName.isEmpty();
   }

   private String getFullPropertyName(String var1) {
      return this.useSSL() ? "mail.smtps." + var1 : "mail.smtp." + var1;
   }
}
