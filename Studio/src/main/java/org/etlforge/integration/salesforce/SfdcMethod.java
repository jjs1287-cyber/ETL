package org.etlforge.integration.salesforce;

import org.apache.commons.lang.StringUtils;

public enum SfdcMethod {
   LOGIN,
   LOGOUT,
   QUERY,
   QUERY_ALL,
   UPSERT,
   CREATE,
   UPDATE,
   DELETE,
   HARD_DELETE,
   BULK_LOAD;

   public String getMethodName() {
      return this == QUERY_ALL ? "queryAll" : this.name().toLowerCase();
   }

   public String getDisplayName() {
      switch (this) {
         case QUERY_ALL:
            return "Query All";
         case CREATE:
            return "Insert";
         case HARD_DELETE:
            return "Hard Delete";
         case BULK_LOAD:
            return "Bulk Load";
         default:
            return StringUtils.capitalize(this.name().toLowerCase());
      }
   }

   public String getResponseElementName() {
      return this.getMethodName() + "Response";
   }

   public static SfdcMethod fromMethodName(String var0) {
      for (SfdcMethod var4 : values()) {
         if (var4.getMethodName().equals(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Unknown method: " + var0);
   }

   public boolean usesSoap() {
      return this != BULK_LOAD;
   }

   public boolean supportsForeignKeys() {
      return this == UPSERT || this == UPDATE || this == CREATE;
   }
}
