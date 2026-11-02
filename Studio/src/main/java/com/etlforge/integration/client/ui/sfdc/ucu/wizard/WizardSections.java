package com.etlforge.integration.client.ui.sfdc.ucu.wizard;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.wizard.CommonWizardSections;
import java.util.ArrayList;

public final class WizardSections implements CommonWizardSections {
   public static String[] all(boolean var0) {
      ArrayList var1 = Lists.newArrayList("Login", "Object", "Source", "Schedule & Options");
      if (var0) {
         var1.add("Summary");
      }

      return (String[]) var1.toArray(new String[var1.size()]);
   }

   private WizardSections() {
   }
}
