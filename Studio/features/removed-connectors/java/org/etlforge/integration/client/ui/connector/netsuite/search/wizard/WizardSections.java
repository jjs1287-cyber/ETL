package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import org.etlforge.lang.Arrays2;

final class WizardSections {
   public static final String BUILD_QUERY = "Build Query";
   public static final String SEARCH_PARA = "Search Type";
   public static final String START = "Endpoint";

   public static String[] all() {
      return Arrays2.of("Endpoint", "Object", "Search Type", "Build Query");
   }

   private WizardSections() {
   }
}
