package com.etlforge.integration.client.ui.sfdc.bulkload;

import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class SalesforceBulkLoadToolTip extends IntegrationEntityToolTip {
   public SalesforceBulkLoadToolTip(SalesforceBulkLoad var1) {
      super(var1);
      this.addInfo(var1);
   }

   private void addInfo(SalesforceBulkLoad var1) {
      this.addEntry("Action:", var1.getAction() + " " + var1.getObjectName());
   }
}
