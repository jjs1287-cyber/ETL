package org.etlforge.integration.salesforce;

import java.util.Arrays;
import java.util.List;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.integration.salesforce.wsdl.SObjectSchemaProvider;

final class FakeSObjectSchemaProvider implements SObjectSchemaProvider {
   @Override
   public List<SObjectSchema> listAllObjects() {
      return Arrays.asList(FakeSObjects.ACCOUNT, FakeSObjects.CONTACT);
   }

   @Override
   public List<SObjectSchema> describeObjects(List<String> var1) throws SalesforceException {
      throw new UnsupportedOperationException();
   }

   @Override
   public SObjectSchema describeObject(String var1) throws SalesforceException {
      if ("Account".equals(var1)) {
         return FakeSObjects.ACCOUNT;
      } else if ("Contact".equals(var1)) {
         return FakeSObjects.CONTACT;
      } else {
         throw new SalesforceException("Unknown object type: " + var1);
      }
   }

   @Override
   public void close() {
   }
}
