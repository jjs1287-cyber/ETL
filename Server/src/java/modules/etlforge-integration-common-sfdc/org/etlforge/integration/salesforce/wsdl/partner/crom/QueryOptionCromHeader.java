package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class QueryOptionCromHeader extends CromHeaderElement {
   public QueryOptionCromHeader() {
      super("QueryOptions", CromHeaderValue.integer("batchSize").optional());
   }
}
