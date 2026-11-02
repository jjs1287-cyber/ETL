package org.etlforge.integration.salesforce.wsdl.partner.crom;

final class UserTerritoryDeleteCromHeader extends CromHeaderElement {
   public UserTerritoryDeleteCromHeader() {
      super("UserTerritoryDeleteHeader", CromHeaderValue.salesforceId("transferToUserId").optional());
   }
}
