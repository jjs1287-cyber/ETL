package org.etlforge.integration.salesforce;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import com.sforce.ws.ConnectorConfig;
public final class SalesforceOAuthSmoke {
   private static void require(boolean value) { if (!value) throw new AssertionError("OAuth parser assertion failed"); }
   private static SalesforceOAuthClient.Token parse(String json) throws Exception {
      return SalesforceOAuthClient.parse(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));
   }
   private static void reject(String json) throws Exception {
      try { parse(json); throw new AssertionError("Invalid response accepted"); }
      catch (SalesforceException expected) { require(!expected.getMessage().contains("private-token")); }
   }
   public static void main(String[] args) throws Exception {
      if (args.length == 0) {
         require(SalesforceOAuthClient.instanceUrl("https://tenant.develop.lightning.force.com/").toString().equals("https://tenant.develop.my.salesforce.com"));
         require(SalesforceOAuthClient.instanceUrl("https://tenant--test.sandbox.lightning.force.com").toString().equals("https://tenant--test.sandbox.my.salesforce.com"));
         for(String invalid : new String[]{"http://tenant.lightning.force.com", "https://tenant.lightning.force.com.evil.example", "https://user:secret@tenant.lightning.force.com", "https://tenant.lightning.force.com/path", "https://tenant.lightning.force.com?secret=value"}) {
            try { SalesforceOAuthClient.instanceUrl(invalid); throw new AssertionError("Invalid instance URL accepted"); }
            catch(SalesforceException expected) {}
         }
         SalesforceOAuthClient.Token token = parse("{\"access_token\":\"private-token\",\"instance_url\":\"https://example.my.salesforce.com\",\"token_type\":\"Bearer\",\"extra\":{\"ignored\":true}}");
         require(token.accessToken.equals("private-token") && !token.toString().contains("private-token"));
         reject("{\"access_token\":\"private-token\",\"instance_url\":\"http://example.my.salesforce.com\",\"token_type\":\"Bearer\"}");
         reject("{\"access_token\":\"private-token\",\"instance_url\":\"https://attacker.example\",\"token_type\":\"Bearer\"}");
         reject("{\"access_token\":\"private-token\",\"instance_url\":\"https://example.my.salesforce.com\"}");
         reject("{\"access_token\":"); reject("[]");
         reject("{\"access_token\":\"private-token\",\"instance_url\":\"https://example.my.salesforce.com\",\"token_type\":\"Bearer\"} {}");
         byte[] huge = new byte[65537];
         try { SalesforceOAuthClient.parse(new ByteArrayInputStream(huge)); throw new AssertionError("Size cap ignored"); }
         catch (SalesforceException expected) {}
         System.out.println("PASS: OAuth parser, HTTPS host validation, malformed response handling, size bound, secret redaction");
         return;
      }
      Map<String,String> fields = new HashMap<String,String>();
      for (String line : Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8)) {
         int colon = line.indexOf(':');
         if (colon > 0) fields.put(line.substring(0,colon).trim().toLowerCase(Locale.ROOT),line.substring(colon+1).trim());
      }
      LoginCredentials credentials = new LoginCredentials("", "", "", false, "oauth", fields.get("instance url"), fields.get("client id"), fields.get("client secret"));
      SalesforceOAuthClient.Token token = SalesforceOAuthClient.login(credentials, new ConnectorConfig());
      require(token.accessToken.length() > 0);
      if (args.length > 1 && "negative".equals(args[1])) {
         LoginCredentials wrong = new LoginCredentials("", "", "", false, "oauth", credentials.instanceUrl, credentials.clientId, "ETLForge-intentionally-invalid-test-secret");
         try { SalesforceOAuthClient.login(wrong, new ConnectorConfig()); throw new AssertionError("Invalid secret accepted"); }
         catch (SalesforceException expected) { require(!expected.getMessage().contains(credentials.clientSecret)); }
         java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(3);
         try {
            java.util.List<java.util.concurrent.Future<Boolean>> results = new java.util.ArrayList<java.util.concurrent.Future<Boolean>>();
            for (int i=0;i<3;i++) results.add(executor.submit(() -> SalesforceOAuthClient.login(credentials, new ConnectorConfig()).accessToken.length() > 0));
            for (java.util.concurrent.Future<Boolean> result : results) require(result.get(45,java.util.concurrent.TimeUnit.SECONDS));
         } finally { executor.shutdownNow(); }
         System.out.println("PASS: live invalid-secret rejection, redacted errors and three concurrent OAuth exchanges");
      }
      if (args.length > 1 && "soap".equals(args[1])) {
         OrgIdentifier org = new OrgIdentifier(ServerHost.defaultHost(), SalesforceVersion.V_61, credentials);
         SalesforceConnection connection = new org.etlforge.integration.salesforce.wsdl.partner.wsc.WscPartnerConnectionFactory().newConnection(org);
         try { connection.open(); require(connection.getUserId() != null); System.out.println("PASS: live OAuth SOAP getUserInfo on API 61.0");
            require(connection.query("SELECT Id FROM User LIMIT 1").getNumberOfRecords() == 1);
            ConnectorConfig schemaConfig = new ConnectorConfig(); schemaConfig.setManualLogin(true); schemaConfig.setSessionId(connection.getSessionId()); schemaConfig.setServiceEndpoint(connection.getServiceEndpoint()); schemaConfig.setConnectionTimeout(15000); schemaConfig.setReadTimeout(30000);
            com.sforce.soap.partner.PartnerConnection partner = new com.sforce.soap.partner.PartnerConnection(schemaConfig); partner.setSessionHeader(connection.getSessionId());
            require(partner.describeSObject("Account").getFields().length > 0);
            System.out.println("PASS: read-only User query and Account metadata describe; no field values logged");
            java.lang.reflect.Field field = connection.getClass().getDeclaredField("connection");field.setAccessible(true);
            ((com.sforce.soap.partner.PartnerConnection)field.get(connection)).setSessionHeader("invalid-session-for-etlforge-test");
            try { connection.query("SELECT Id FROM User LIMIT 1"); throw new AssertionError("Invalid session accepted"); }
            catch (SalesforceException expected) { require(expected.isInvalidSessionId()); }
            require(connection.query("SELECT Id FROM User LIMIT 1").getNumberOfRecords() == 1);
            System.out.println("PASS: invalid session discarded and OAuth session reacquired on subsequent read"); }
         finally { connection.close(); }
      }
      System.out.println("PASS: live Salesforce client credentials token exchange; token and account details withheld");
   }
}
