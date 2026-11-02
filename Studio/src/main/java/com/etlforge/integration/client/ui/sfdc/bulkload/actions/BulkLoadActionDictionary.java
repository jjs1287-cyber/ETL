package com.etlforge.integration.client.ui.sfdc.bulkload.actions;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;

public final class BulkLoadActionDictionary {
   private static final String STRINGS = "com.etlforge.integration.client.ui.sfdc.bulkload.actions.Strings";
   private static final String ICONS = "com.etlforge.integration.client.ui.sfdc.bulkload.actions.Icons";
   public static final ActionDictionary DICTIONARY = NewResourceBasedActionDictionary.newDictionary(
      "com.etlforge.integration.client.ui.sfdc.bulkload.actions.Strings",
      ApplicationResources.MNEMONIC_FACTORY,
      "com.etlforge.integration.client.ui.sfdc.bulkload.actions.Icons",
      null
   );

   private BulkLoadActionDictionary() {
   }
}
