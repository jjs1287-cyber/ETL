package org.etlforge.integration.client.ui.interchange.entity.schedule.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;

public final class ScheduleActionDictionary {
   private static final String STRINGS = "org.etlforge.integration.client.ui.interchange.entity.schedule.action.ScheduleActionStrings";
   private static final String ICONS = "org.etlforge.integration.client.ui.interchange.entity.schedule.action.ScheduleActionIcons";
   public static final ActionDictionary DICTIONARY = NewResourceBasedActionDictionary.newDictionary(
      "org.etlforge.integration.client.ui.interchange.entity.schedule.action.ScheduleActionStrings",
      ApplicationResources.MNEMONIC_FACTORY,
      "org.etlforge.integration.client.ui.interchange.entity.schedule.action.ScheduleActionIcons",
      null
   );

   private ScheduleActionDictionary() {
   }
}
