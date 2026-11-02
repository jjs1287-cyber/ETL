package org.etlforge.integration.salesforce.wsdl;

import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.xml.ExpandedName;

public final class SalesforceWsdlConstants {
   public static final String SERVICE_NAME = "SforceService";
   public static final String SOAP_STYLE = "document";
   public static final String SOAP_BODY_USE = "literal";
   public static final String SOAP_ACTION = "";
   public static final String PORT_NAME = "Soap";
   public static final String BINDING_NAME = "SoapBinding";
   public static final String PARTNER_NAMESPACE = "urn:partner.soap.sforce.com";
   public static final String SOBJECT_PARTNER_NAMESPACE = "urn:sobject.partner.soap.sforce.com";
   public static final String SOBJECT_ENTERPRISE_NAMESPACE = "urn:sobject.enterprise.soap.sforce.com";
   public static final String REQUEST_HEADER_PATH = "root$transaction.header$";
   private static final String REQUEST_BODY_PATH = "root$transaction.body$";
   private static final String RESPONSE_BODY_PATH = "root$transaction.response$body$";
   public static final String QUERIED_OBJECT_PARENT_PATH = "root$transaction.response$body$queryResponse$result$records.";
   public static final String PATH_TO_REQUEST_ID_IN_DELETE_RESPONSE = "root$transaction.request$body$delete$ids.";
   public static final String PATH_TO_EXTERNAL_ID_NAME_IN_REQUEST = "root$transaction.body$upsert$externalIDFieldName$";
   public static final String AGGREGATE_RESULT = "AggregateResult";

   public static String getInputMessageName(SfdcMethod var0) {
      return var0.getMethodName() + "Request";
   }

   public static String getOutputMessageName(SfdcMethod var0) {
      return var0.getMethodName() + "Response";
   }

   public static String getPathToObjectNodeInUcuRequest(SfdcMethod var0, String var1) {
      return "root$transaction.body$" + var0.getMethodName() + "$" + var1 + ".";
   }

   public static final String getPathToSuccessElement(SfdcMethod var0) {
      return "root$transaction.response$body$" + var0.getResponseElementName() + "$result.success$";
   }

   public static final String getPathToErrorMessageElement(SfdcMethod var0) {
      return "root$transaction.response$body$" + var0.getResponseElementName() + "$result.errors#.message$";
   }

   public static final String getPathToGeneratorInUcuResponse(SfdcMethod var0) {
      return String.format("[root$transaction.response$body$" + var0.getResponseElementName() + "$result.]");
   }

   public static final String getPathToExternalIdFieldInResponse(String var0, String var1) {
      return "root$transaction.request$body$upsert$" + var0 + "#." + var1 + "$";
   }

   public static boolean isPartnerWsdlObject(ExpandedName var0) {
      return "urn:sobject.partner.soap.sforce.com".equals(var0.getNamespace());
   }

   private SalesforceWsdlConstants() {
   }
}
