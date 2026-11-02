package org.etlforge.integration.salesforce;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.dataelement.DataElementResolver;
import org.etlforge.lang.Objects;

                                                                                  
public final class LoginCredentials {
   public static final String BASIC_AUTH = "basicAuth";
   public static final String OAUTH = "oauth";
   public final String username;
   public final String password;
   public final String securityToken;
   public final boolean sandbox;
   public final String authType;
   public final String instanceUrl;
   public final String clientId;
   public final String clientSecret;

   public LoginCredentials(String username, String password, String securityToken) {
      this(username, password, securityToken, false);
   }

   public LoginCredentials(String username, String password, String securityToken, boolean sandbox) {
      this(username, password, securityToken, sandbox, BASIC_AUTH, "", "", "");
   }

   public LoginCredentials(String username, String password, String securityToken, boolean sandbox,
         String authType, String instanceUrl, String clientId, String clientSecret) {
      this.authType = StringUtils.isBlank(authType) ? BASIC_AUTH : authType.trim();
      Preconditions.checkArgument(BASIC_AUTH.equals(this.authType) || OAUTH.equals(this.authType), "Unsupported Salesforce authentication type");
      if (!OAUTH.equals(this.authType)) {
         Preconditions.checkArgument(StringUtils.isNotBlank(username), "Must provide a username");
      } else {
         Preconditions.checkArgument(StringUtils.isNotBlank(instanceUrl), "Must provide an instance URL");
         Preconditions.checkArgument(StringUtils.isNotBlank(clientId), "Must provide a client ID");
         Preconditions.checkArgument(StringUtils.isNotBlank(clientSecret), "Must provide a client secret");
      }
      this.username = trim(username);
      this.password = trim(password);
      this.securityToken = trim(securityToken);
      this.sandbox = sandbox;
      this.instanceUrl = trim(instanceUrl);
      this.clientId = trim(clientId);
                                                              
      this.clientSecret = clientSecret == null ? "" : clientSecret;
   }

   public boolean isOAuth() { return OAUTH.equals(this.authType); }

   private static String trim(String value) { return value == null ? "" : value.trim(); }

   @Override
   public int hashCode() {
      return Objects.hashCode(username, password, securityToken, sandbox, authType, instanceUrl, clientId, clientSecret);
   }

   @Override
   public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof LoginCredentials)) return false;
      LoginCredentials credentials = (LoginCredentials)other;
      return equalsIgnoringSandbox(credentials) && sandbox == credentials.sandbox;
   }

   @Override
   public String toString() {
      return "LoginCredentials{authType=" + authType + ", credentials=[REDACTED], sandbox=" + sandbox + "}";
   }

   public boolean equalsIgnoringSandbox(LoginCredentials other) {
      return other != null && username.equals(other.username) && password.equals(other.password)
         && securityToken.equals(other.securityToken) && authType.equals(other.authType)
         && instanceUrl.equals(other.instanceUrl) && clientId.equals(other.clientId) && clientSecret.equals(other.clientSecret);
   }

   LoginCredentials resolveDataElementDefaultValues() {
      return new LoginCredentials(
         DataElementResolver.resolveDefaultValues(username),
         DataElementResolver.resolveDefaultValues(password),
         DataElementResolver.resolveDefaultValues(securityToken), sandbox, authType,
         DataElementResolver.resolveDefaultValues(instanceUrl),
         DataElementResolver.resolveDefaultValues(clientId),
         DataElementResolver.resolveDefaultValues(clientSecret));
   }
}