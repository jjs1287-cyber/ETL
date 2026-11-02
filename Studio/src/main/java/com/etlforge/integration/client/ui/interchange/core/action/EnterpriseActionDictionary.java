package com.etlforge.integration.client.ui.interchange.core.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary;

public final class EnterpriseActionDictionary {
   private static final String STRINGS = "com.etlforge.integration.client.ui.interchange.core.action.resources.Strings";
   private static final String ICONS = "com.etlforge.integration.client.ui.interchange.core.action.resources.Icons";
   public static final ActionDictionary DICTIONARY = NewResourceBasedActionDictionary.newDictionary(
      "com.etlforge.integration.client.ui.interchange.core.action.resources.Strings",
      ApplicationResources.MNEMONIC_FACTORY,
      "com.etlforge.integration.client.ui.interchange.core.action.resources.Icons",
      InterchangeActionDictionary.DICTIONARY
   );

   private EnterpriseActionDictionary() {
   }
}
