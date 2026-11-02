package org.etlforge.integration.salesforce.soap.ui;

import org.etlforge.integration.salesforce.soap.header.UserTerritoryDeleteHeader;
import org.etlforge.integration.salesforce.wsdl.SoapHeader;

public final class UserTerritoryDeleteHeaderUi extends StringValueHeaderUi {
   public UserTerritoryDeleteHeaderUi(UserTerritoryDeleteHeader var1) {
      super("Transfer to user ID", var1.getTransferToUserId());
   }

   @Override
   protected SoapHeader createHeader(String var1) {
      return new UserTerritoryDeleteHeader(var1);
   }
}
