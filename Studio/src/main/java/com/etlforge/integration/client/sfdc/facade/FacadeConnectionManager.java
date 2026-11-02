package com.etlforge.integration.client.sfdc.facade;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SalesforceException;

final class FacadeConnectionManager {
   private final SalesforceConnectionFactory connectionFactory;
   private SalesforceConnection connection;

   public FacadeConnectionManager(SalesforceConnectionFactory var1) {
      this.connectionFactory = var1;
   }

   public SalesforceConnectionFactory getConnectionFactory() {
      return this.connectionFactory;
   }

   public SalesforceConnection getConnection() {
      return this.connection;
   }

   public synchronized SalesforceConnection login(OrgIdentifier var1) throws SalesforceException {
      validateInput(var1);
      if (this.isNewConnectionNeeded(var1)) {
         this.closeCurrentConnection();
         this.connection = this.loginImpl(var1);
      }

      return this.connection;
   }

   private static void validateInput(OrgIdentifier var0) throws SalesforceException {
      boolean var1 = StringUtils.isBlank(var0.getUserName());
      boolean var2 = StringUtils.isBlank(var0.getPassword());
      if (var1 && var2) {
         throw new SalesforceException("Must provide a user name and password.");
      }

      if (var1) {
         throw new SalesforceException("Must provide a user name.");
      }

      if (var2) {
         throw new SalesforceException("Must provide a password.");
      }
   }

   private boolean isNewConnectionNeeded(OrgIdentifier var1) {
      if (this.connection != null && this.connection.isOpen()) {
         OrgIdentifier var2 = this.connection.getOrgIdentifier();
         return !var2.equals(var1);
      } else {
         return true;
      }
   }

   public void closeCurrentConnection() {
      if (this.connection != null) {
         this.connection.close();
         this.connection = null;
      }
   }

   private SalesforceConnection loginImpl(OrgIdentifier var1) throws SalesforceException {
      SalesforceConnection var2 = this.connectionFactory.newConnection(var1);
      var2.open();
      return var2;
   }
}
