package com.etlforge.integration.client.ui.sfdc.bulkload.page;

import com.etlforge.integration.client.ui.sfdc.page.AbstractSalesforceItemPanel;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.ui.widget.label.KongaLabel;

final class ActionAndObjectInfo {
   private final KongaLabel actionField;
   private final KongaLabel externalIdField;

   public ActionAndObjectInfo(SalesforceBulkLoad var1) {
      BulkAction var2 = var1.getAction();
      this.actionField = new KongaLabel(var2.toString() + " " + var1.getObjectName());
      if (var2.requiresExternalId()) {
         this.externalIdField = new KongaLabel(var1.getExternalIdName());
      } else {
         this.externalIdField = null;
      }
   }

   public void layoutIn(AbstractSalesforceItemPanel.Layout var1) {
      var1.addRow("Action:", this.actionField, null);
      if (this.externalIdField != null) {
         var1.addRow("External ID:", this.externalIdField, null);
      }
   }
}
