package org.etlforge.util.net;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Objects.ToStringHelper;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.EnumSet;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Pair;

public final class ProxySettings {
   public static final ProxySettings NONE = new ProxySettings("", Port.DEFAULT, "", "", "", ProxySettings.CertificatePolicy.STRICT);
   private final String host;
   private final Port port;
   private final String user;
   private final String password;
   private final String ntlmDomain;
   private final ProxySettings.CertificatePolicy certPolicy;
   private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
      @Override
      public X509Certificate[] getAcceptedIssuers() {
         return null;
      }

      @Override
      public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      }

      @Override
      public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      }
   }};

   public static ProxySettings of(String var0, String var1, String var2, String var3, ProxySettings.CertificatePolicy var4) {
      if (hasPortInfo(var0)) {
         int var5 = var0.indexOf(":");
         assert var5 != -1;
         String var6 = var0.substring(0, var5);
         String var7 = var5 == var0.length() - 1 ? null : var0.substring(var5 + 1);
         Port var8 = var7 != null ? Port.valueOf(var7) : Port.DEFAULT;
         return new ProxySettings(var6, var8, var1, var2, var3, var4);
      } else {
         return new ProxySettings(var0, Port.DEFAULT, var1, var2, var3, var4);
      }
   }

   public static Pair<String, String> toUserAndDomain(String var0) {
      Object var1 = null;
      String var2 = null;
      int var3 = var0.indexOf(47);
      if (var3 == -1) {
         var1 = var0;
      } else {
         var2 = var0.substring(0, var3);
         var1 = var0.substring(var3 + 1);
      }

      return Pair.of((String)var1, var2);
   }

   private static boolean hasPortInfo(String var0) {
      return var0 != null && var0.contains(":");
   }

   @Deprecated
   public ProxySettings(String var1, Port var2, String var3, String var4, String var5) {
      this(var1, var2, var3, var4, var5, ProxySettings.CertificatePolicy.STRICT);
   }

   public ProxySettings(String var1, Port var2, String var3, String var4, String var5, ProxySettings.CertificatePolicy var6) {
      this.host = Strings.nullToEmpty(var1);
      this.port = Preconditions.checkNotNull(var2);
      this.user = Strings.nullToEmpty(var3);
      this.password = Strings.nullToEmpty(var4);
      this.ntlmDomain = Strings.nullToEmpty(var5);
      this.certPolicy = Preconditions.checkNotNull(var6);
      if (!this.host.isEmpty() && this.certPolicy == ProxySettings.CertificatePolicy.TRUST_ALL) {
         try {
            synchronized (trustAllCerts) {
               SSLContext var8 = SSLContext.getInstance("SSL");
               var8.init(null, trustAllCerts, new SecureRandom());
               HttpsURLConnection.setDefaultSSLSocketFactory(var8.getSocketFactory());
            }
         } catch (GeneralSecurityException var11) {
            var11.printStackTrace();
         }
      }
   }

   public boolean isValid() {
      return StringUtils.isNotBlank(this.host) && !this.port.equals(Port.INVALID);
   }

   public boolean hasUsername() {
      return !this.user.isEmpty();
   }

   public String getHost() {
      return this.host;
   }

   public Port getPort() {
      return this.port;
   }

   public String getUser() {
      return this.user;
   }

   public String getDomainQualifiedUser() {
      return this.hasNtlmDomain() ? this.ntlmDomain + "/" + this.user : this.user;
   }

   public String getPassword() {
      return this.password;
   }

   public boolean hasNtlmDomain() {
      return !this.ntlmDomain.isEmpty();
   }

   public String getNtlmDomain() {
      return this.ntlmDomain;
   }

   public ProxySettings.CertificatePolicy getCertPolicy() {
      return this.certPolicy;
   }

   public Authenticator getAuthenticator() {
      Preconditions.checkState(this.hasUsername(), "These ProxySettings does not use authentication");
      return new ProxySettings.ProxyAuthenticator(this.user, this.password);
   }

   public void updateSystemProperties(EnumSet<ProxySettings.SystemProperties> var1) {
      Preconditions.checkArgument(!Preconditions.checkNotNull(var1, "mode").isEmpty(), "Must provide at least one mode");
      System.setProperty("proxySet", this.isValid() ? "true" : "false");

      for (ProxySettings.SystemProperties var3 : var1) {
         String var4 = var3.name().toLowerCase() + ".";
         setSystemProperty(var4 + "proxyHost", this.host);
         String var5 = this.port != Port.DEFAULT && this.port != Port.INVALID ? Integer.toString(this.port.getPort()) : "";
         setSystemProperty(var4 + "proxyPort", var5);
         setSystemProperty(var4 + "proxyUser", this.user);
         setSystemProperty(var4 + "proxyPassword", this.password);
         setSystemProperty(var4 + "auth.ntlm.domain", this.ntlmDomain);
      }
   }

   private static void setSystemProperty(String var0, String var1) {
      if (var1.isEmpty()) {
         System.clearProperty(var0);
      } else {
         System.setProperty(var0, var1);
      }
   }

   public ProxySettings withCertPolicy(ProxySettings.CertificatePolicy var1) {
      Preconditions.checkNotNull(var1);
      return var1 == this.certPolicy ? this : new ProxySettings(this.host, this.port, this.user, this.password, this.ntlmDomain, var1);
   }

   public boolean equivalentToNone() {
      return this.equalsIgnoringCertPolicy(NONE);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.host, this.port, this.user, this.password, this.ntlmDomain, this.certPolicy);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ProxySettings)) {
         return false;
      }

      ProxySettings var2 = (ProxySettings)var1;
      return this.equalsIgnoringCertPolicy(var2) && this.certPolicy == var2.certPolicy;
   }

   private boolean equalsIgnoringCertPolicy(ProxySettings var1) {
      return this.host.equals(var1.host)
         && this.port.equals(var1.port)
         && this.user.equals(var1.user)
         && this.password.equals(var1.password)
         && this.ntlmDomain.equals(var1.ntlmDomain);
   }

   @Override
   public String toString() {
      ToStringHelper var1 = Objects.toStringHelper(this);
      if (this != NONE) {
         String var2 = this.port == Port.INVALID ? "invalid" : (this.port == Port.DEFAULT ? "default" : "" + this.port.getPort());
         var1.add("host", this.host)
            .add("port", var2)
            .add("user", this.user)
            .add("password", "[not shown]")
            .add("ntlm", this.ntlmDomain)
            .add("certPolicy", this.certPolicy);
      }

      return var1.toString();
   }

   public enum CertificatePolicy {
      STRICT,
      TRUST_ALL;
   }

   private static class ProxyAuthenticator extends Authenticator {
      private final String userName;
      private final String password;

      public ProxyAuthenticator(String var1, String var2) {
         this.userName = var1;
         this.password = var2;
      }

      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
         return new PasswordAuthentication(this.userName, this.password.toCharArray());
      }
   }

   public enum SystemProperties {
      HTTP,
      HTTPS;
   }
}
