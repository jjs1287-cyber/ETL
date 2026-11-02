package org.etlforge.integration.data.entity;

import com.google.common.base.Optional;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.CC;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.entity.id.SalesforceConnectorId;
import org.etlforge.integration.data.entity.sfdc.OrgConnectionStatus;
import org.etlforge.integration.data.location.LoginProtectedResource;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.integration.salesforce.ConnectionResult;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceVersion;
import org.etlforge.integration.salesforce.ServerHost;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.version.Version;

public final class SalesforceConnector extends IntegrationEntity implements LoginProtectedResource {
   private static final Version VERSION_INTRODUCED = VersionFactory.newVersion(5, 0, 0, 2);
   @IdProperty
   public static final String SALESFORCE_CONNECTOR_ID = "salesforceconnector_id";
   @NameProperty
   public static final String SALESFORCE_CONNECTOR_NAME = "salesforceconnector_name";
   public static final String SALESFORCE_API_VERSION = "api_version";
   public static final String SERVER_HOST = "server_host";
   public static final String LOGIN_CREDENTIALS = "login_credentials";
   public static final String USER_NAME = "user_name";
   public static final String PASSWORD = "password";
   public static final String SECURITY_TOKEN = "security_token"; public static final String AUTH_TYPE = "auth_type"; public static final String INSTANCE_URL = "instance_url"; public static final String CLIENT_ID = "client_id"; public static final String CLIENT_SECRET = "client_secret";
   public static final String CONNECTION_STATUS = "status";
   public static final String ORG_IDENTIFIER = "org_identifier";
   private static final String SANDBOX = "sandbox";
   private OrgConnectionStatus connectionStatus = OrgConnectionStatus.MISSING;

   public SalesforceConnector(String var1) {
      this(null, var1);
   }

   public SalesforceConnector(SalesforceConnectorId var1, String var2) {
      super(EntityType.SalesforceConnector, var1, var2);
      this.setSalesforceVersion(SalesforceVersion.V_61); this.setProperty(AUTH_TYPE, LoginCredentials.OAUTH);
      this.setServerHost(ServerHost.defaultHost());
   }

   public SalesforceConnectorId getID() {
      return (SalesforceConnectorId)super.getID();
   }

   public OrgIdentifier getOrgIdentifier() {
      synchronized (this.getDataLock()) {
         return new OrgIdentifier(this.getServerHost(), this.getSalesforceVersion(), this.getLoginCredentials());
      }
   }

   public void setOrgIdentifier(OrgIdentifier var1) {
      OrgIdentifier var2;
      synchronized (this.getDataLock()) {
         var2 = this.getOrgIdentifier();
         if (var1.equals(var2)) {
            return;
         }

         this.setServerHost(var1.getServer());
         this.setLoginCredentials(var1.getCredentials());
         this.setSalesforceVersion(var1.getVersion());
         this.setConnectionStatus(OrgConnectionStatus.UNVERIFIED);
      }

      this.firePropertyChange("org_identifier", var2, var1);
   }

   private void setServerHost(ServerHost var1) {
      this.setStringProperty("server_host", var1.getUrl());
   }

   private void setLoginCredentials(LoginCredentials var1) {
      if (var1 == null) {
         this.setProperty(AUTH_TYPE, LoginCredentials.OAUTH);
         this.setProperty(INSTANCE_URL, (String)null);
         this.setProperty(CLIENT_ID, (String)null);
         this.setProperty(CLIENT_SECRET, (String)null);
         this.setProperty("user_name", (String)null);
         this.setProperty("password", (String)null);
         this.setProperty("security_token", (String)null);
         this.getKeyValues().putBoolean("sandbox", false);
      } else {
         this.setProperty(AUTH_TYPE, var1.authType);
         this.setProperty(INSTANCE_URL, var1.instanceUrl);
         this.setProperty(CLIENT_ID, var1.clientId);
         this.setProperty(CLIENT_SECRET, CC.hide(var1.clientSecret));
         this.setProperty("user_name", var1.username);
         this.setProperty("password", CC.hide(var1.password));
         this.setProperty("security_token", CC.hide(var1.securityToken));
         this.getKeyValues().putBoolean("sandbox", var1.sandbox);
      }
   }

   public void setSalesforceVersion(SalesforceVersion var1) {
      synchronized (this.getDataLock()) {
         this.setProperty("api_version", var1.getId());
      }
   }

   private void setConnectionStatus(OrgConnectionStatus var1) {
      this.connectionStatus = var1;
      this.setProperty("status", var1.encode());
   }

   public ServerHost getServerHost() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("server_host", "");
         return ServerHost.fromUrl(var2);
      }
   }

   public LoginCredentials getLoginCredentials() {
      synchronized (this.getDataLock()) {
         if (LoginCredentials.OAUTH.equals(this.getProperty(AUTH_TYPE))) {
            String url = this.getProperty(INSTANCE_URL, "");
            String id = this.getProperty(CLIENT_ID, "");
            String secret = PasswordUtils.getClearTextPassword(PasswordUtils.getPassword(this, CLIENT_SECRET));
            if (StringUtils.isBlank(url) || StringUtils.isBlank(id) || StringUtils.isBlank(secret)) return null;
            return new LoginCredentials("", "", "", this.getBooleanProperty("sandbox", false), LoginCredentials.OAUTH, url, id, secret);
         }
         String var2 = this.getProperty("user_name");
         if (var2 != null) {
            Optional var3 = this.getPassword();
            Optional var4 = PasswordUtils.getPassword(this, "security_token");
            boolean var5 = this.getBooleanProperty("sandbox", false);
            return new LoginCredentials(var2, PasswordUtils.getClearTextPassword(var3), PasswordUtils.getClearTextPassword(var4), var5);
         } else {
            return null;
         }
      }
   }

   public SalesforceVersion getSalesforceVersion() {
      synchronized (this.getDataLock()) {
         String var2 = this.getProperty("api_version");
         return StringUtils.isEmpty(var2) ? SalesforceVersion.DEFAULT_VERSION : SalesforceVersion.fromId(var2);
      }
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      if (!LoginCredentials.OAUTH.equals(this.getProperty(AUTH_TYPE))) { var1.addMessage("Salesforce requires OAuth 2.0 Client Credentials. Configure instance URL, client ID and client secret.", ValidationStatus.INVALID); return; }
      if (LoginCredentials.OAUTH.equals(this.getProperty(AUTH_TYPE))) {
         if (this.getLoginCredentials() == null) var1.addMessage("Must provide an instance URL, client ID and client secret.", ValidationStatus.INVALID);
         else this.connectionStatus.reportTo(var1);
         return;
      }
      boolean var2 = StringUtils.isEmpty(this.getProperty("user_name"));
      Password var3 = this.getPassword().orNull();
      boolean var4 = var3 == null || var3.clearText().isEmpty();
      if (var2 && var4) {
         var1.addMessage("Must provide a user name and password.", ValidationStatus.INVALID);
      } else if (var2) {
         var1.addMessage("Must provide a user name.", ValidationStatus.INVALID);
      } else if (var4) {
         var1.addMessage("Must provide a password.", ValidationStatus.INVALID);
      } else {
         this.connectionStatus.reportTo(var1);
      }
   }

   @Override
   public Version getRequiredVersion() {
      return VERSION_INTRODUCED;
   }

   @Override
   public String[] getPropertyKeys() {
      return new String[]{
         "salesforceconnector_id", "salesforceconnector_name", "api_version", "server_host", "user_name", "password", "security_token", "sandbox", "status", AUTH_TYPE, INSTANCE_URL, CLIENT_ID, CLIENT_SECRET
      };
   }

   @Override
   public boolean isDeepCopyable() {
      return false;
   }

   @Override
   public boolean isRenameable() {
      return false;
   }

   @Override
   public String getLogin() {
      synchronized (this.getDataLock()) {
         return this.getProperty("user_name");
      }
   }

   @Override
   public Optional<Password> getPassword() {
      return PasswordUtils.getPassword(this, LoginCredentials.OAUTH.equals(this.getProperty(AUTH_TYPE)) ? CLIENT_SECRET : PASSWORD);
   }

   @Override
   public String getPasswordPropertyName() {
      throw new UnsupportedOperationException();
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      this.connectionStatus = OrgConnectionStatus.decode(this.getProperty("status"));
   }

   public boolean updateConnectionStatus(ConnectionResult var1) {
      synchronized (this.getDataLock()) {
         OrgConnectionStatus var3 = this.connectionStatus;
         OrgIdentifier var4 = this.getOrgIdentifier();
         OrgConnectionStatus var5 = OrgConnectionStatus.fromConnectionResult(var4, var1);
         if (var5.equals(var3)) {
            return false;
         }

         this.setConnectionStatus(var5);
         return true;
      }
   }
}
