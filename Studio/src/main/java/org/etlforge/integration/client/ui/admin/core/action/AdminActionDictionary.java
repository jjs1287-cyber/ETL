package org.etlforge.integration.client.ui.admin.core.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;

public final class AdminActionDictionary {
   private static final String STRINGS = "org.etlforge.integration.client.ui.admin.core.action.resources.Strings";
   private static final String ICONS = "org.etlforge.integration.client.ui.admin.core.action.resources.Icons";
   public static final ActionDictionary DICTIONARY = NewResourceBasedActionDictionary.newDictionary(
      "org.etlforge.integration.client.ui.admin.core.action.resources.Strings",
      ApplicationResources.MNEMONIC_FACTORY,
      "org.etlforge.integration.client.ui.admin.core.action.resources.Icons",
      null
   );

   private AdminActionDictionary() {
   }
}
