package org.etlforge.integration.client.ui.core.window.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.application.ui.window.action.WindowActionDictionary;

public final class ClientWindowActionDictionary {
   private static final String STRINGS = "org.etlforge.integration.client.ui.core.window.action.resources.Strings";
   private static final String ICONS = "org.etlforge.integration.client.ui.core.window.action.resources.Icons";
   public static final ActionDictionary DICTIONARY = NewResourceBasedActionDictionary.newDictionary(
      "org.etlforge.integration.client.ui.core.window.action.resources.Strings",
      ApplicationResources.MNEMONIC_FACTORY,
      "org.etlforge.integration.client.ui.core.window.action.resources.Icons",
      WindowActionDictionary.DICTIONARY
   );

   private ClientWindowActionDictionary() {
   }
}
