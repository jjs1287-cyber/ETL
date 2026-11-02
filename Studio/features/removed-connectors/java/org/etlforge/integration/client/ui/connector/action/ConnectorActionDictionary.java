package org.etlforge.integration.client.ui.connector.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;

public final class ConnectorActionDictionary {
   private static final String STRINGS = "org.etlforge.integration.client.ui.connector.action.Strings";
   private static final String ICONS = "org.etlforge.integration.client.ui.connector.action.Icons";
   public static final ActionDictionary DICTIONARY = NewResourceBasedActionDictionary.newDictionary(
      "org.etlforge.integration.client.ui.connector.action.Strings",
      ApplicationResources.MNEMONIC_FACTORY,
      "org.etlforge.integration.client.ui.connector.action.Icons",
      null
   );

   private ConnectorActionDictionary() {
   }
}
