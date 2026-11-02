package org.etlforge.integration.salesforce;

import com.sforce.ws.ConnectorConfig;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import org.codehaus.jackson.*;

                                                                                                  
public final class SalesforceOAuthClient {
   private SalesforceOAuthClient() {}
   public static final class Token {
      public final String accessToken;
      public final String instanceUrl;
      private Token(String token, String instance) { accessToken = token; instanceUrl = instance; }
      @Override public String toString() { return "Salesforce OAuth token [REDACTED]"; }
   }
   static URL instanceUrl(String value) throws SalesforceException {
      try {
         URI uri = new URI(value);
         String host = uri.getHost();
                                                                                  
                                                                
         if (host != null) {
            host = host.toLowerCase(java.util.Locale.ROOT);
            String lightningSuffix = ".lightning.force.com";
            if (host.endsWith(lightningSuffix))
               host = host.substring(0, host.length() - lightningSuffix.length()) + ".my.salesforce.com";
         }
         if (!"https".equalsIgnoreCase(uri.getScheme()) || host == null
               || !host.toLowerCase(java.util.Locale.ROOT).endsWith(".salesforce.com")
               || uri.getUserInfo() != null || uri.getQuery() != null || uri.getFragment() != null
               || (uri.getPort() != -1 && uri.getPort() != 443)
               || !(uri.getPath().isEmpty() || "/".equals(uri.getPath()))) {
            throw new SalesforceException("Use the HTTPS Salesforce My Domain instance URL without a path, query or credentials.");
         }
         return new URL("https", host, "");
      } catch (URISyntaxException | MalformedURLException ex) {
         throw new SalesforceException("Invalid Salesforce instance URL.");
      }
   }
   public static Token login(LoginCredentials credentials, ConnectorConfig config) throws SalesforceException {
      URL base = instanceUrl(credentials.instanceUrl);
      HttpURLConnection connection = null;
      try {
         URL endpoint = new URL(base, "/services/oauth2/token");
         connection = (HttpURLConnection)(config.getProxy() == null ? endpoint.openConnection() : endpoint.openConnection(config.getProxy()));
         connection.setInstanceFollowRedirects(false);
         connection.setConnectTimeout(15000);
         connection.setReadTimeout(30000);
         connection.setRequestMethod("POST");
         connection.setDoOutput(true);
         connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
         connection.setRequestProperty("Accept", "application/json");
         if (config.getProxyUsername() != null && !config.getProxyUsername().isEmpty()) {
            String pair = config.getProxyUsername() + ":" + (config.getProxyPassword() == null ? "" : config.getProxyPassword());
            connection.setRequestProperty("Proxy-Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString(pair.getBytes(StandardCharsets.UTF_8)));
         }
         byte[] body = ("grant_type=client_credentials&client_id=" + URLEncoder.encode(credentials.clientId, "UTF-8")
               + "&client_secret=" + URLEncoder.encode(credentials.clientSecret, "UTF-8")).getBytes(StandardCharsets.UTF_8);
         connection.setFixedLengthStreamingMode(body.length);
         try (OutputStream stream = connection.getOutputStream()) { stream.write(body); }
         int status = connection.getResponseCode();
         if (status != 200) throw new SalesforceException("Salesforce OAuth authentication failed (HTTP " + status + "). Check the app credentials, Client Credentials flow and Run As user.");
         try (InputStream stream = connection.getInputStream()) { return parse(stream); }
      } catch (IOException ex) {
         throw new SalesforceException("Salesforce OAuth request failed. Check the instance URL, network, proxy and TLS configuration.");
      } finally { if (connection != null) connection.disconnect(); }
   }
   static Token parse(InputStream stream) throws SalesforceException {
      try {
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         byte[] buffer = new byte[2048]; int count;
         while ((count = stream.read(buffer)) != -1) {
            if (bytes.size() + count > 65536) throw new SalesforceException("Salesforce OAuth response exceeds the allowed size.");
            bytes.write(buffer, 0, count);
         }
         String accessToken = null, instance = null, tokenType = null;
         try (JsonParser parser = new JsonFactory().createJsonParser(bytes.toByteArray())) {
            if (parser.nextToken() != JsonToken.START_OBJECT) throw new IOException();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
               if (parser.getCurrentToken() != JsonToken.FIELD_NAME) throw new IOException();
               String name = parser.getCurrentName();
               JsonToken type = parser.nextToken();
               if ("access_token".equals(name) || "instance_url".equals(name) || "token_type".equals(name)) {
                  if (type != JsonToken.VALUE_STRING) throw new IOException();
                  if ("access_token".equals(name)) accessToken = parser.getText();
                  if ("instance_url".equals(name)) instance = parser.getText();
                  if ("token_type".equals(name)) tokenType = parser.getText();
               } else parser.skipChildren();
            }
            if (parser.nextToken() != null) throw new IOException();
         }
         if (accessToken == null || accessToken.isEmpty() || !"Bearer".equalsIgnoreCase(tokenType)
               || accessToken.indexOf('\r') >= 0 || accessToken.indexOf('\n') >= 0 || instance == null) throw new IOException();
         return new Token(accessToken, instanceUrl(instance).toString());
      } catch (IOException ex) { throw new SalesforceException("Salesforce returned an invalid OAuth token response."); }
   }
}
