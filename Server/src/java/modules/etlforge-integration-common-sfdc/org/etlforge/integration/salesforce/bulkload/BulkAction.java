package org.etlforge.integration.salesforce.bulkload;

import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.SfdcMethod;

public enum BulkAction {
   UPSERT,
   CREATE,
   UPDATE,
   DELETE,
   HARD_DELETE,
   QUERY;

   public static BulkAction DEFAULT = UPSERT;

   public boolean isLoad() {
      return this != QUERY;
   }

   public boolean requiresExternalId() {
      return this == UPSERT;
   }

   public boolean requiresIdOnly() {
      return this == DELETE || this == HARD_DELETE;
   }

   public boolean supportsDateOptions() {
      return this != DELETE && this != HARD_DELETE && this != QUERY;
   }

   public boolean supportsForeignKeys() {
      return this.toSfdcMethod().supportsForeignKeys();
   }

   public String getSalesforceName() {
      switch (this) {
         case CREATE:
            return "insert";
         case HARD_DELETE:
            return "hardDelete";
         case QUERY:
            return "query";
         default:
            return this.name().toLowerCase();
      }
   }

   public SfdcMethod toSfdcMethod() {
      switch (this) {
         case CREATE:
            return SfdcMethod.CREATE;
         case HARD_DELETE:
            return SfdcMethod.HARD_DELETE;
         case QUERY:
            return SfdcMethod.QUERY;
         case UPSERT:
            return SfdcMethod.UPSERT;
         case UPDATE:
            return SfdcMethod.UPDATE;
         case DELETE:
            return SfdcMethod.DELETE;
         default:
            throw new RuntimeException("Unexpected action: " + this);
      }
   }

   @Override
   public String toString() {
      return this == HARD_DELETE ? "Hard Delete" : StringUtils.capitalize(this.getSalesforceName());
   }
}
