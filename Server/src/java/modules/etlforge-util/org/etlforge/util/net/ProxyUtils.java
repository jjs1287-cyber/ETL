package org.etlforge.util.net;

import java.security.cert.X509Certificate;
import javax.annotation.ParametersAreNonnullByDefault;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;

@ParametersAreNonnullByDefault
public final class ProxyUtils {
   private static final String HTTP_AUTH_NTLM_DOMAIN = "http.auth.ntlm.domain";

   public static void configureApacheHttpClient(DefaultHttpClient var0, ProxySettings var1) {
      if (var1.equivalentToNone()) {
         var0.getParams().setParameter("http.route.default-proxy", ConnRouteParams.NO_HOST);
         installCertificatePolicy(var0, ProxySettings.CertificatePolicy.STRICT);
      } else if (var1.isValid()) {
         if (!"appledonut".equals(var1.getHost())) {
            HttpHost var2 = createProxyHost(var1);
            var0.getParams().setParameter("http.route.default-proxy", var2);
            if (var1.hasUsername()) {
               Credentials var3 = getHttpCredentials(var1);
               AuthScope var4 = getScope(var1);
               var0.getCredentialsProvider().setCredentials(var4, var3);
            }
         }

         installCertificatePolicy(var0, var1.getCertPolicy());
      }
   }

   private static void installCertificatePolicy(DefaultHttpClient var0, ProxySettings.CertificatePolicy var1) {
      SSLSocketFactory var2 = createSslSocketFactory(var1);
      SchemeRegistry var3 = var0.getConnectionManager().getSchemeRegistry();
      Scheme var4 = createHttpsScheme(var3, var2);
      var3.register(var4);
   }

   private static SSLSocketFactory createSslSocketFactory(ProxySettings.CertificatePolicy var0) {
      switch (var0) {
         case STRICT:
            return SSLSocketFactory.getSocketFactory();
         case TRUST_ALL:
            try {
               TrustStrategy var1 = new TrustStrategy() {
                  @Override
                  public boolean isTrusted(X509Certificate[] var1, String var2) {
                     return true;
                  }
               };
               return new SSLSocketFactory(var1, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            } catch (Exception var2) {
               throw new RuntimeException(var2);
            }
         default:
            throw new AssertionError(var0.name());
      }
   }

   private static Scheme createHttpsScheme(SchemeRegistry var0, SSLSocketFactory var1) {
      Scheme var2 = var0.getScheme("https");
      return var2 == null ? new Scheme("https", 443, var1) : new Scheme(var2.getName(), var2.getDefaultPort(), var1);
   }

   private static HttpHost createProxyHost(ProxySettings var0) {
      int var1 = var0.getPort().getPort();
      return var1 > 0 ? new HttpHost(var0.getHost(), var1) : new HttpHost(var0.getHost());
   }

   private static AuthScope getScope(ProxySettings var0) {
      int var1 = var0.getPort().getPort();
      return new AuthScope(var0.getHost(), var1 > 0 ? var1 : -1);
   }

   private static Credentials getHttpCredentials(ProxySettings var0) {
      if (var0.hasNtlmDomain()) {
         String var1 = NetworkUtils.getComputerName().or("");
         return new NTCredentials(var0.getUser(), var0.getPassword(), var1, var0.getNtlmDomain());
      } else {
         return new UsernamePasswordCredentials(var0.getUser(), var0.getPassword());
      }
   }

   public static void updateNtlmDomainSystemProperty(ProxySettings var0) {
      if (var0.hasNtlmDomain()) {
         String var1 = var0.getNtlmDomain();
         System.setProperty("http.auth.ntlm.domain", var1);
      } else {
         System.clearProperty("http.auth.ntlm.domain");
      }
   }

   private ProxyUtils() {
   }
}
