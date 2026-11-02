package org.etlforge.integration.salesforce.wsdl;

import com.google.common.base.Preconditions;
import java.util.List;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceException;

public final class DefaultSObjectSchemaProvider implements SObjectSchemaProvider {
   private final SalesforceConnection connection;

   public DefaultSObjectSchemaProvider(SalesforceConnection var1) {
      this.connection = Preconditions.checkNotNull(var1, "connection");
   }

   @Override
   public synchronized SObjectSchema describeObject(String var1) throws SalesforceException {
      return this.connection.describeObject(var1);
   }

   @Override
   public List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException {
      return this.connection.describeObjects(var1);
   }

   @Override
   public List<SObjectSchema> listAllObjects() throws SalesforceException {
      return this.connection.listAllObjects();
   }

   @Override
   public void close() {
      this.connection.close();
   }
}
