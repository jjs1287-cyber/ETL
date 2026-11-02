package org.etlforge.integration.client.connect.wizard;

public final class WizardSections {
   public static final String SOURCE = "Source";
   public static final String TARGET = "Target";
   public static final String SUMMARY = "Summary";

   public static final String[] all() {
      return new String[]{"Source", "Target", "Summary"};
   }

   private WizardSections() {
   }
}
