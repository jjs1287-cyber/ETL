package org.etlforge.integration.salesforce;

import java.util.List;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.io.Indentation;

final class PooledConnection implements SalesforceConnection {
   private final SalesforceConnection impl;
   private volatile boolean open;
   private SessionTimeoutWindow timeoutWindow = SessionTimeoutWindow.infinite();
   private boolean alive;
   private final Pool pool;
   private final Object poolLock;

   public PooledConnection(SalesforceConnection var1, Pool var2, Object var3) {
      this.impl = var1;
      this.pool = var2;
      this.poolLock = var3;
   }

   public SalesforceConnection getUnderlyingConnection() {
      return this.impl;
   }

   public synchronized boolean isExpired() {
      return this.timeoutWindow.isExpired();
   }

   public synchronized void terminate() {
      if (this.alive) {
         OrgIdentifier var1 = this.getOrgIdentifier();

         try {
            this.impl.close();
         } finally {
            this.open = false;
            this.timeoutWindow = SessionTimeoutWindow.exipred();
            this.alive = false;
            this.pool.remove(var1);
         }
      }
   }

   @Override
   public void open() throws SalesforceException {
      try {
         this.openImpl();
      } finally {
         if (!this.open) {
            this.removeFromPoolOnConnectionFailure();
         }
      }
   }

   private synchronized void openImpl() throws SalesforceException {
      if (!this.open) {
         if (!this.alive) {
            try {
               this.impl.open();
            } catch (SalesforceException var2) {
               this.timeoutWindow = SessionTimeoutWindow.exipred();
               throw var2;
            }

            short var1 = 300;
            this.timeoutWindow = new SessionTimeoutWindow(this.impl.getSessionTimeoutInSeconds(), var1);
            this.alive = true;
         }

         this.open = true;
      }
   }

   private void removeFromPoolOnConnectionFailure() {
      OrgIdentifier var1 = this.getOrgIdentifier();
      this.pool.removeIfSame(var1, this);
   }

   private void accessed() {
      this.timeoutWindow = this.timeoutWindow.sessionAccessed();
   }

   @Override
   public synchronized boolean isOpen() {
      return this.open && this.impl.isOpen();
   }

   @Override
   public synchronized void close() {
      if (this.open) {
         this.open = false;
         synchronized (this.poolLock) {
            if (this.isExpired()) {
               this.terminate();
            }
         }
      }
   }

   @Override
   public synchronized OrgIdentifier getOrgIdentifier() {
      return this.impl.getOrgIdentifier();
   }

   @Override
   public synchronized String getSessionId() {
      return this.impl.getSessionId();
   }

   @Override
   public synchronized String getUserId() {
      return this.impl.getUserId();
   }

   @Override
   public synchronized int getSessionTimeoutInSeconds() {
      return this.impl.getSessionTimeoutInSeconds();
   }

   @Override
   public synchronized String getServiceEndpoint() {
      return this.impl.getServiceEndpoint();
   }

   @Override
   public QueryResult query(String var1) throws SalesforceException {
      this.open();
      QueryResult var2 = this.impl.query(var1);
      this.accessed();
      return var2;
   }

   @Override
   public QueryResult queryAll(String var1) throws SalesforceException {
      this.open();
      QueryResult var2 = this.impl.queryAll(var1);
      this.accessed();
      return var2;
   }

   public synchronized void reportTo(StringBuilder var1, Indentation var2) {
      var2.writeTo(var1);
      var1.append("Org: ").append(this.getOrgIdentifier()).append(SystemUtils.LINE_SEPARATOR);
      var2.writeTo(var1);
      var1.append("Session ID: ").append(this.getSessionId()).append(SystemUtils.LINE_SEPARATOR);
      var2.writeTo(var1);
      var1.append("Endpoint: ").append(this.getServiceEndpoint()).append(SystemUtils.LINE_SEPARATOR);
      var2.writeTo(var1);
      var1.append("Timeout Window: ").append(this.timeoutWindow).append(SystemUtils.LINE_SEPARATOR);
   }

   @Override
   public List<SObjectSchema> listAllObjects() throws SalesforceException {
      this.open();
      List var1 = this.impl.listAllObjects();
      this.accessed();
      return var1;
   }

   @Override
   public SObjectSchema describeObject(String var1) throws SalesforceException {
      this.open();
      SObjectSchema var2 = this.impl.describeObject(var1);
      this.accessed();
      return var2;
   }

   @Override
   public List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException {
      this.open();
      List var2 = this.impl.describeObjects(var1);
      this.accessed();
      return var2;
   }

   @Override
   public String toString() {
      return "[Pooled Connection] " + this.impl.toString();
   }
}
