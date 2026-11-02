package org.etlforge.integration.salesforce.config;

import org.etlforge.integration.salesforce.SfdcMethod;

public final class HardCodedSalesforceConfig extends SalesforceConfig {
   @Override
   public String getSoapAction() {
      return "";
   }

   @Override
   public String[] getFieldsInheritedFromSObject() {
      return new String[]{"fieldsToNull", "Id"};
   }

   @Override
   public String getBindingName() {
      return "SoapBinding";
   }

   @Override
   public SfdcMethodConfig getMethodConfig(SfdcMethod var1) {
      switch (var1) {
         case LOGIN:
            return new DefaultSfdcMethodConfig("login");
         case LOGOUT:
            return new DefaultSfdcMethodConfig("logout");
         case QUERY:
            return new QueryMethodConfig();
         case UPSERT:
            return new UpsertMethodConfig();
         case DELETE:
            return new DefaultSfdcMethodConfig("delete");
         default:
            throw new RuntimeException("Unhandled SFDC method: " + var1);
      }
   }

   @Override
   public String getPortName() {
      return "Soap";
   }

   @Override
   public String getSalesforceNamespace() {
      return "urn:enterprise.soap.sforce.com";
   }

   @Override
   public String getServiceName() {
      return "SforceService";
   }

   @Override
   public String getSoapBodyUse() {
      return "literal";
   }

   @Override
   public String getSoapStyle() {
      return "document";
   }

   @Override
   public String getSObjectLocalName() {
      return "sObject";
   }

   @Override
   public String getSObjectNamespace() {
      return "urn:sobject.enterprise.soap.sforce.com";
   }

   @Override
   public int getChunkSize() {
      return 200;
   }

   @Override
   public String getMappingTreePath(MappingTreePath var1) {
      switch (var1) {
         case REQUEST_HEADER:
            return "root$transaction.header$";
         case REQUEST_BODY:
            return "root$transaction.body$";
         case RESPONSE_BODY:
            return "root$transaction.response$body$";
         case REQUEST_HEADER_IN_RESPONSE:
            return "root$transaction.request$header$";
         case REQUEST_BODY_IN_RESPONSE:
            return "root$transaction.request$body$";
         default:
            throw new RuntimeException("Unexpected path: " + var1);
      }
   }
}
