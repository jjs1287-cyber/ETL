package org.etlforge.integration.salesforce;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.salesforce.query.Field;
import org.etlforge.integration.salesforce.query.QueryRecord;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class FakeConnectionFactory implements SalesforceConnectionFactory {
   @Override
   public SalesforceConnection newConnection(OrgIdentifier var1) {
      return new FakeConnectionFactory.FakeConnection(var1);
   }

   private static class FakeConnection implements SalesforceConnection {
      private final OrgIdentifier orgId;
      private boolean open;

      public FakeConnection(OrgIdentifier var1) {
         this.orgId = var1;
      }

      @Override
      public synchronized void open() {
         this.open = true;
      }

      @Override
      public synchronized boolean isOpen() {
         return this.open;
      }

      @Override
      public synchronized void close() {
         this.open = false;
      }

      @Override
      public OrgIdentifier getOrgIdentifier() {
         return this.orgId;
      }

      @Override
      public String getSessionId() {
         return this.open ? "banana-breakfast-is-saved" : null;
      }

      @Override
      public String getUserId() {
         return this.open ? "danger-is-over" : null;
      }

      @Override
      public int getSessionTimeoutInSeconds() {
         Preconditions.checkState(this.open);
         return 3600;
      }

      @Override
      public String getServiceEndpoint() {
         return this.open ? "http://www.example.com/pretend-salesforce" : null;
      }

      @Override
      public QueryResult query(String var1) throws SalesforceException {
         return this.createFakeQueryResult();
      }

      private QueryResult createFakeQueryResult() {
         List var1 = Arrays.asList(new Field("Id", "12345"), new Field("Name", "Homer Simpson"));
         QueryRecord var2 = new QueryRecord("Account", var1);
         return new QueryResult(Collections.singletonList(var2), true, null);
      }

      @Override
      public QueryResult queryAll(String var1) throws SalesforceException {
         return this.createFakeQueryResult();
      }

      @Override
      public List<SObjectSchema> listAllObjects() throws SalesforceException {
         throw new NotImplementedYetException();
      }

      @Override
      public SObjectSchema describeObject(String var1) throws SalesforceException {
         throw new NotImplementedYetException();
      }

      @Override
      public List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException {
         throw new NotImplementedYetException();
      }
   }
}
