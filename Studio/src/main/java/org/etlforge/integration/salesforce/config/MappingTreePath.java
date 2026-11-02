package org.etlforge.integration.salesforce.config;

public enum MappingTreePath {
   REQUEST_HEADER("RequestHeader"),
   REQUEST_BODY("RequestBody"),
   RESPONSE_BODY("ResponseBody"),
   REQUEST_HEADER_IN_RESPONSE("RequestHeaderInResponse"),
   REQUEST_BODY_IN_RESPONSE("RequestBodyInResponse");

   private final String key;

   MappingTreePath(String var3) {
      this.key = var3;
   }

   String getKey() {
      return this.key;
   }
}
