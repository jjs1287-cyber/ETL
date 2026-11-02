package com.etlforge.integration.client.ui.sfdc.delete.page.v4;

import com.etlforge.integration.client.ui.sfdc.page.v4.AbstractSalesforceItemPageV4;
import com.etlforge.integration.data.entity.SalesforceDelete;
import org.etlforge.integration.salesforce.SfdcMethod;

public final class SalesforceDeletePageV4 extends AbstractSalesforceItemPageV4<SalesforceDelete> {
   public SalesforceDeletePageV4(SalesforceDelete var1) {
      super(var1, SfdcMethod.DELETE);
   }

   protected SalesforceDeletePanelV4 createDataPanel() {
      SalesforceDeletePanelV4 var1 = new SalesforceDeletePanelV4(this.getWindow(), this.getObject(), this.getLinks());
      this.doDefaultPanelConfiguration(var1);
      return var1;
   }
}
