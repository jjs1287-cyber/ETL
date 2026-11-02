package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Preconditions;
import com.sforce.soap.partner.GetUserInfoResult;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.UpsertResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import java.util.List;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

final class WscPartnerConnectionWrapper implements SalesforceConnection {
   private final OrgIdentifier orgId;
   private PartnerConnection connection;
   private int sessionTimeoutInSeconds;
   private ConnectorConfig config;
   private String userId;

   public WscPartnerConnectionWrapper(OrgIdentifier var1, ConnectorConfig var2) {
      assert var2.isManualLogin() : "Connector.isManualLogin must be true.";
      this.orgId = var1;
      this.config = var2;
   }

   @Override
   public synchronized void open() throws SalesforceException {
      if (!this.isOpen()) {
         try {
            PartnerConnection var1 = new PartnerConnection(this.config);
            var1.setCallOptions("ETLForge/ETLForge/", null);
            GetUserInfoResult var2 = this.callLogin(var1);
            this.sessionTimeoutInSeconds = var2.getSessionSecondsValid();
            this.userId = var2.getUserId();
            this.connection = var1;
         } catch (ConnectionException var3) {
            throw ExceptionTranslator.translate(var3);
         }
      }
   }

   private GetUserInfoResult callLogin(PartnerConnection var1) throws ConnectionException, SalesforceException {
      if (this.orgId.getCredentials().isOAuth()) {
         org.etlforge.integration.salesforce.SalesforceOAuthClient.Token token =
            org.etlforge.integration.salesforce.SalesforceOAuthClient.login(this.orgId.getCredentials(), this.config);
         this.config.setServiceEndpoint(token.instanceUrl + "/services/Soap/u/" + this.orgId.getVersion().getId());
         this.config.setSessionId(token.accessToken);
         var1.setSessionHeader(token.accessToken);
         return var1.getUserInfo();
      }
      throw new SalesforceException("Salesforce requires OAuth 2.0 Client Credentials. Configure instance URL, client ID and client secret.");
   }

   @Override
   public synchronized boolean isOpen() {
      return this.connection != null;
   }

   @Override
   public OrgIdentifier getOrgIdentifier() {
      return this.orgId;
   }

   @Override
   public synchronized String getSessionId() {
      this.throwIfNotOpen();
      return this.config.getSessionId();
   }

   private void throwIfNotOpen() {
      Preconditions.checkState(this.isOpen(), "This connection is not open.");
   }

   @Override
   public String getUserId() {
      this.throwIfNotOpen();
      return this.userId;
   }

   @Override
   public synchronized int getSessionTimeoutInSeconds() {
      this.throwIfNotOpen();
      return this.sessionTimeoutInSeconds;
   }

   @Override
   public synchronized String getServiceEndpoint() {
      this.throwIfNotOpen();
      return this.config.getServiceEndpoint();
   }

   @Override
   public synchronized void close() {
      if (this.connection != null) {
         try {
            this.connection.logout();
         } catch (ConnectionException var5) {
            var5.printStackTrace();
         } finally {
            this.blankOutConnection();
         }
      }
   }

   private void blankOutConnection() {
      this.sessionTimeoutInSeconds = -1;
      this.userId = null;
      this.connection = null;
      
   }

   @Override
   public synchronized String toString() {
      return this.isOpen()
         ? String.format("User: %s, Session ID: **********, URL: %s", this.orgId.getUserName(), this.getServiceEndpoint())
         : "[connection closed]";
   }

   private SalesforceException handlePostLoginConnectionException(ConnectionException var1) {
      SalesforceException var2 = ExceptionTranslator.translate(var1);
      if (var2.isInvalidSessionId()) {
         this.blankOutConnection();
      }

      return var2;
   }

   @Override
   public synchronized QueryResult query(String var1) throws SalesforceException {
      this.open();

      try {
         WscPartnerConnectionQuery var2 = new WscPartnerConnectionQuery(this.connection);
         return var2.query(var1);
      } catch (ConnectionException var3) {
         throw this.handlePostLoginConnectionException(var3);
      }
   }

   @Override
   public synchronized QueryResult queryAll(String var1) throws SalesforceException {
      this.open();

      try {
         WscPartnerConnectionQuery var2 = new WscPartnerConnectionQuery(this.connection);
         return var2.queryAll(var1);
      } catch (ConnectionException var3) {
         throw this.handlePostLoginConnectionException(var3);
      }
   }

   public synchronized UpsertResult[] upsert(String var1, SObject... var2) throws SalesforceException {
      try {
         this.open();
         return this.connection.upsert(var1, var2);
      } catch (ConnectionException var4) {
         throw this.handlePostLoginConnectionException(var4);
      }
   }

   @Override
   public synchronized List<SObjectSchema> listAllObjects() throws SalesforceException {
      this.open();

      try {
         WscPartnerConnectionSchemaProvider var1 = new WscPartnerConnectionSchemaProvider(this.connection);
         return var1.listAllObjects();
      } catch (ConnectionException var2) {
         throw this.handlePostLoginConnectionException(var2);
      }
   }

   @Override
   public synchronized SObjectSchema describeObject(String var1) throws SalesforceException {
      this.open();

      try {
         WscPartnerConnectionSchemaProvider var2 = new WscPartnerConnectionSchemaProvider(this.connection);
         return var2.describeObject(var1);
      } catch (ConnectionException var3) {
         throw this.handlePostLoginConnectionException(var3);
      }
   }

   @Override
   public synchronized List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException {
      this.open();

      try {
         WscPartnerConnectionSchemaProvider var2 = new WscPartnerConnectionSchemaProvider(this.connection);
         return var2.describeObjects(var1);
      } catch (ConnectionException var3) {
         throw this.handlePostLoginConnectionException(var3);
      }
   }
}
