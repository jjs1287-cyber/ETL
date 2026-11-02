package org.etlforge.integration.activity.ui.core.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;

public final class ActivityActionDictionary {
   private static final String STRINGS = "org.etlforge.integration.activity.ui.core.action.resources.Strings";
   private static final String ICONS = "org.etlforge.integration.activity.ui.core.action.resources.Icons";
   public static final ActionDictionary DICTIONARY = NewResourceBasedActionDictionary.newDictionary(
      "org.etlforge.integration.activity.ui.core.action.resources.Strings",
      ApplicationResources.MNEMONIC_FACTORY,
      "org.etlforge.integration.activity.ui.core.action.resources.Icons",
      null
   );

   private ActivityActionDictionary() {
   }
}
