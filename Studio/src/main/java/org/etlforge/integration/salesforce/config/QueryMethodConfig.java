package org.etlforge.integration.salesforce.config;

public final class QueryMethodConfig extends DefaultSfdcMethodConfig {
   public QueryMethodConfig() {
      super("query");
      this.addProperty("QueryStringPath", "root$transaction.body$query$queryString$");
      this.addProperty("BatchSizePath", "root$transaction.header$QueryOptions$batchSize$");
   }

   @Override
   public String getSimpleRootPathInResponse() {
      return "root$transaction.response$body$queryResponse$result$records.";
   }

   @Override
   public boolean responseHasSimpleMappingTreeRepresentation() {
      return true;
   }
}
