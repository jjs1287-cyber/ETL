package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.wizard.CommonWizardSections;
import java.util.ArrayList;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.salesforce.bulkload.BulkAction;

final class WizardSections implements CommonWizardSections {
   public static final String ACTION = "Action";
   public static final String MAPPINGS = "Mappings";
   private final boolean includeSummary;

   public WizardSections(boolean var1) {
      this.includeSummary = var1;
   }

   public String[] forAction(BulkAction var1) {
      ArrayList var2 = Lists.newArrayList("Action", "Login", "Object");
      if (var1 == BulkAction.QUERY) {
         KongaListUtils.add(var2, "Query", "Target");
      } else {
         KongaListUtils.add(var2, "Source", "Mappings");
      }

      var2.add("Schedule");
      if (this.includeSummary) {
         var2.add("Summary");
      }

      return (String[]) var2.toArray(new String[var2.size()]);
   }
}
