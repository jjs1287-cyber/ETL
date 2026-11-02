package org.etlforge.integration.client.ui.interchange.transformation.wizard;

public final class WizardSections {
   public static final String START = "Name";
   public static final String SOURCE = "Source";
   public static final String TARGET = "Target";

   public static String[] all() {
      return new String[]{"Name", "Source", "Target"};
   }

   private WizardSections() {
   }
}
