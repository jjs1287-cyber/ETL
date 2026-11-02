package com.etlforge.integration.client.ui.admin.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.ui.admin.core.action.AdminActionDictionary;

public final class EnterpriseAdminActionDictionary {
   private static final String STRINGS = "com.etlforge.integration.client.ui.admin.action.Strings";
   private static final String ICONS = "com.etlforge.integration.client.ui.admin.action.Icons";
   public static final ActionDictionary DICTIONARY = NewResourceBasedActionDictionary.newDictionary(
      "com.etlforge.integration.client.ui.admin.action.Strings",
      ApplicationResources.MNEMONIC_FACTORY,
      "com.etlforge.integration.client.ui.admin.action.Icons",
      AdminActionDictionary.DICTIONARY
   );

   private EnterpriseAdminActionDictionary() {
   }
}
