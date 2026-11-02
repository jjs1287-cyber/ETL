package org.etlforge.integration.server.implementation.webservice.interchange.deploy.impl;

public final class TriggeredOperationsProperties {
   public static final String D_HTTPENDPOINT_ID = "httpendpoint_id";
   public static final String D_OPERATION_ID = "operation_id";
   public static final String D_SYNCHRONOUS = "synchronous";
   public static final String D_USE_REQUEST_AS_SOURCE = "use_request_as_source";
   public static final String D_FINAL_TARGET_AS_RESPONSE = "final_target_as_response";
   public static final String D_HTTP_VERB = "http_verb";
   public static final String[] DEPLOY_PROPERTIES = new String[]{"httpendpoint_id", "operation_id", "synchronous", "final_target_as_response", "http_verb"};

   public static final String[] getPropertyKeys() {
      return (String[])DEPLOY_PROPERTIES.clone();
   }

   private TriggeredOperationsProperties() {
   }
}
