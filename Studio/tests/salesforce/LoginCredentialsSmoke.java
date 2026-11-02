package org.etlforge.integration.salesforce;
public final class LoginCredentialsSmoke {
   private static void require(boolean value, String message) {
      if (!value) throw new AssertionError(message);
   }
   public static void main(String[] args) {
      LoginCredentials basic = new LoginCredentials(" legacy-user ", " legacy-password ", " legacy-token ", true);
      require(basic.username.equals("legacy-user") && basic.password.equals("legacy-password"), "Legacy trimming changed");
      require(!basic.isOAuth() && basic.sandbox, "Legacy defaults changed");
      LoginCredentials oauth = new LoginCredentials(null, null, null, false, "oauth", "https://example.my.salesforce.com", "app-id", " secret-value ");
      require(oauth.isOAuth() && oauth.username.isEmpty(), "OAuth requires legacy username");
      require(oauth.clientSecret.equals(" secret-value "), "Opaque secret changed");
      LoginCredentials same = new LoginCredentials(null, null, null, false, "oauth", "https://example.my.salesforce.com", "app-id", " secret-value ");
      require(oauth.equals(same) && oauth.hashCode() == same.hashCode(), "Cache identity mismatch");
      LoginCredentials changed = new LoginCredentials(null, null, null, false, "oauth", "https://example.my.salesforce.com", "app-id", "different-secret");
      require(!oauth.equals(changed), "Secret rotation reuses cache identity");
      require(!oauth.equalsIgnoringSandbox(null), "Null equality");
      require(!basic.toString().contains("legacy-") && !oauth.toString().contains("secret-value") && !oauth.toString().contains("app-id"), "Diagnostic credential disclosure");
      require(oauth.resolveDataElementDefaultValues().equals(oauth), "Literal resolution changed");
      boolean rejected = false;
      try { new LoginCredentials(null, null, null, false, "unsupported", "", "", ""); }
      catch (IllegalArgumentException expected) { rejected = true; }
      require(rejected, "Unknown mode accepted");
      rejected = false;
      try { new LoginCredentials(null, null, null, false, "oauth", "https://example.my.salesforce.com", "app-id", ""); }
      catch (IllegalArgumentException expected) { rejected = true; }
      require(rejected, "Missing secret accepted");
      try {
         SalesforceConnection forbidden = new org.etlforge.integration.salesforce.wsdl.partner.wsc.WscPartnerConnectionFactory().newConnection(new OrgIdentifier(ServerHost.defaultHost(), SalesforceVersion.V_61, basic));
         forbidden.open(); throw new AssertionError("Legacy Salesforce authentication was accepted");
      } catch (SalesforceException expected) { require(expected.getMessage().contains("OAuth 2.0"), "Unclear legacy migration error"); }
      System.out.println("PASS: legacy compatibility, OAuth identity, secret rotation, redaction, resolution, validation");
   }
}