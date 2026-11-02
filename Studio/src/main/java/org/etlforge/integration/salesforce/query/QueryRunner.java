package org.etlforge.integration.salesforce.query;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.etlforge.integration.salesforce.ErrorCode;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SalesforceException;

public final class QueryRunner {
   private final Supplier<OrgIdentifier> orgIdSupplier;
   private SalesforceConnection connection;
   private final SalesforceConnectionFactory connectionFactory;
   private boolean includeDeletedRows;
   private boolean ownsConnection;

   public QueryRunner(SalesforceConnectionFactory var1, OrgIdentifier var2) {
      this(var1, Suppliers.ofInstance(var2));
   }

   public QueryRunner(SalesforceConnectionFactory var1, Supplier<OrgIdentifier> var2) {
      this.connectionFactory = Preconditions.checkNotNull(var1);
      this.orgIdSupplier = Preconditions.checkNotNull(var2);
   }

   public void useExistingConnection(SalesforceConnection var1) {
      this.connection = Preconditions.checkNotNull(var1);
   }

   public void setIncludeDeletedRows(boolean var1) {
      this.includeDeletedRows = var1;
   }

   public QueryResult query(String var1) throws SalesforceException {
      this.connectIfNecessary();

      try {
         return this.includeDeletedRows ? this.connection.queryAll(var1) : this.connection.query(var1);
      } catch (SalesforceException var7) {
         return this.handleException(var1, var7);
      } finally {
         if (this.ownsConnection) {
            this.connection.close();
            this.connection = null;
         }
      }
   }

   private void connectIfNecessary() throws SalesforceException {
      if (this.connection != null && this.connection.isOpen()) {
         this.ownsConnection = false;
      } else {
         this.connect();
      }
   }

   private void connect() throws SalesforceException {
      OrgIdentifier var1 = this.orgIdSupplier.get();
      if (var1 == null) {
         throw new SalesforceException("Must select a Salesforce Org.");
      }

      this.connection = this.connectionFactory.newConnection(var1);
      this.ownsConnection = true;
   }

   private QueryResult handleException(String var1, SalesforceException var2) throws SalesforceException {
      if (this.isSessionTimeOut(var2)) {
         return this.retryAfterSessionTimeOut(var1);
      } else if (this.isQueryTimeOut(var2)) {
         return this.connection.query(var1);
      } else {
         throw var2;
      }
   }

   private boolean isSessionTimeOut(SalesforceException var1) {
      return var1.isInvalidSessionId();
   }

   private QueryResult retryAfterSessionTimeOut(String var1) throws SalesforceException {
      this.connect();
      return this.includeDeletedRows ? this.connection.queryAll(var1) : this.connection.query(var1);
   }

   private boolean isQueryTimeOut(SalesforceException var1) {
      return var1.getErrorCode() == ErrorCode.QUERY_TIMEOUT;
   }
}
