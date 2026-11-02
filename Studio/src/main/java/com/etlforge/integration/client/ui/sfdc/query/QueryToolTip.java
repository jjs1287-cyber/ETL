package com.etlforge.integration.client.ui.sfdc.query;

import com.google.common.base.Strings;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;
import org.etlforge.util.string.StringLimiter;

public final class QueryToolTip extends IntegrationEntityToolTip {
   public QueryToolTip(SalesforceQuery var1) {
      super(var1);
      this.addQueryString(var1);
      this.addEntry("Include deleted rows:", var1.isQueryAll() ? "Yes" : "No");
      this.addTargetEntry(var1);
   }

   private void addQueryString(SalesforceQuery var1) {
      String var2 = var1.getQueryString();
      if (!Strings.isNullOrEmpty(var2)) {
         this.addEntry("Query:", StringLimiter.limit(var2, 100));
      }
   }

   private void addTargetEntry(SalesforceQuery var1) {
      WizardItemBrowser var2 = new WizardItemBrowser(var1);
      Target var3 = var2.getOperationTarget();
      if (var3 != null) {
         this.addEntry("Write to:", var3.getLocation().getDataLocationType());
      }
   }
}
