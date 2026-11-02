package com.etlforge.integration.client.sfdc.server;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.salesforce.ErrorCode;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.integration.salesforce.SalesforceException;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class RemoteConnectionFactory implements SalesforceConnectionFactory {
   private final SalesforceInfoProvider serverApi;

   public RemoteConnectionFactory(SalesforceInfoProvider var1) {
      this.serverApi = Preconditions.checkNotNull(var1, "serverApi");
   }

   @Override
   public SalesforceConnection newConnection(OrgIdentifier var1) {
      return new RemoteConnectionFactory.RemoteConnection(this.serverApi, Preconditions.checkNotNull(var1, "orgId"));
   }

   private static class CallbackBase<T> implements ServerCallback {
      private T result;
      private final CountDownLatch latch = new CountDownLatch(1);
      private SalesforceException error;

      private CallbackBase() {
      }

      public T waitForResult() throws SalesforceException {
         try {
            this.latch.await();
            if (this.error != null) {
               throw this.error;
            } else {
               return this.result;
            }
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            return null;
         }
      }

      protected final void set(T var1) {
         this.result = (T)var1;
         this.latch.countDown();
      }

      @Override
      public final void cancelled() {
         this.error = new SalesforceException("Canceled", ErrorCode.REQUEST_CANCELED);
         this.latch.countDown();
      }

      @Override
      public final void caught(Throwable var1) {
         this.error = launder(var1);
         this.latch.countDown();
      }

      private static SalesforceException launder(Throwable var0) {
         return var0 instanceof SalesforceException ? (SalesforceException)var0 : new SalesforceException(var0.getMessage(), var0);
      }
   }

   private static class DescribeObjectCallbackImpl extends RemoteConnectionFactory.CallbackBase<SObjectSchema> implements DescribeSObjectCallback {
      private DescribeObjectCallbackImpl() {
      }

      @Override
      public void gotDescription(SObjectSchema var1) {
         this.set(var1);
      }
   }

   private static class DescribeObjectsCallbackImpl
      extends RemoteConnectionFactory.CallbackBase<List<SObjectSchema>>
      implements DescribeMultipleSObjectsCallback {
      private DescribeObjectsCallbackImpl() {
      }

      @Override
      public void gotDescriptions(List<SObjectSchema> var1) {
         this.set(var1);
      }
   }

   private static class OpenConnectionCallbackImpl extends RemoteConnectionFactory.CallbackBase<SessionData> implements OpenConnectionCallback {
      private OpenConnectionCallbackImpl() {
      }

      @Override
      public void connectionWasOpened(SessionData var1) {
         this.set(var1);
      }
   }

   private static class QueryCallbackImpl extends RemoteConnectionFactory.CallbackBase<QueryResult> implements QueryCallback {
      private QueryCallbackImpl() {
      }

      @Override
      public void done(QueryResult var1) {
         this.set(var1);
      }
   }

   private static class RemoteConnection implements SalesforceConnection {
      private final SalesforceInfoProvider serverApi;
      private final OrgIdentifier orgId;
      private SessionData sessionData;

      public RemoteConnection(SalesforceInfoProvider var1, OrgIdentifier var2) {
         this.serverApi = var1;
         this.orgId = var2;
      }

      @Override
      public synchronized void open() throws SalesforceException {
         if (!this.isOpen()) {
            RemoteConnectionFactory.OpenConnectionCallbackImpl var1 = new RemoteConnectionFactory.OpenConnectionCallbackImpl();
            this.serverApi.openConnection(this.orgId, var1);
            this.sessionData = var1.waitForResult();
         }
      }

      @Override
      public synchronized boolean isOpen() {
         return this.sessionData != null;
      }

      @Override
      public synchronized void close() {
         this.sessionData = null;
      }

      @Override
      public OrgIdentifier getOrgIdentifier() {
         return this.orgId;
      }

      @Override
      public synchronized String getSessionId() {
         return this.isOpen() ? this.sessionData.getSessionId() : null;
      }

      @Override
      public String getUserId() {
         return this.isOpen() ? this.sessionData.getUserId() : null;
      }

      @Override
      public synchronized String getServiceEndpoint() {
         return this.isOpen() ? this.sessionData.getEndpoint() : null;
      }

      @Override
      public synchronized int getSessionTimeoutInSeconds() {
         return this.isOpen() ? this.sessionData.getSessionTimeoutInSeconds() : -1;
      }

      @Override
      public synchronized List<SObjectSchema> listAllObjects() throws SalesforceException {
         this.open();
         RemoteConnectionFactory.DescribeObjectsCallbackImpl var1 = new RemoteConnectionFactory.DescribeObjectsCallbackImpl();
         this.serverApi.listAllObjects(this.sessionData, var1);
         return var1.waitForResult();
      }

      @Override
      public synchronized SObjectSchema describeObject(String var1) throws SalesforceException {
         this.open();
         RemoteConnectionFactory.DescribeObjectCallbackImpl var2 = new RemoteConnectionFactory.DescribeObjectCallbackImpl();
         this.serverApi.describeObject(this.sessionData, var1, var2);
         return var2.waitForResult();
      }

      @Override
      public synchronized List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException {
         this.open();
         RemoteConnectionFactory.DescribeObjectsCallbackImpl var2 = new RemoteConnectionFactory.DescribeObjectsCallbackImpl();
         this.serverApi.describeObjects(this.sessionData, var1, var2);
         return var2.waitForResult();
      }

      @Override
      public synchronized QueryResult query(String var1) throws SalesforceException {
         this.open();
         RemoteConnectionFactory.QueryCallbackImpl var2 = new RemoteConnectionFactory.QueryCallbackImpl();
         this.serverApi.query(this.sessionData, var1, var2);
         return var2.waitForResult();
      }

      @Override
      public synchronized QueryResult queryAll(String var1) throws SalesforceException {
         this.open();
         RemoteConnectionFactory.QueryCallbackImpl var2 = new RemoteConnectionFactory.QueryCallbackImpl();
         this.serverApi.queryAll(this.sessionData, var1, var2);
         return var2.waitForResult();
      }

      @Override
      public synchronized String toString() {
         return this.isOpen()
            ? String.format("User: %s, Session ID: **********, URL: %s", this.orgId.getUserName(), this.getServiceEndpoint())
            : "[connection closed]";
      }
   }
}
