package org.etlforge.integration.client.ui.interchange.locatable.actions;

import org.etlforge.application.ui.action.ActionDictionaries;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.ActionIcons;
import org.etlforge.application.ui.action.ActionStrings;

@ActionStrings("org.etlforge.integration.client.ui.interchange.locatable.actions.Strings")
@ActionIcons("org.etlforge.integration.client.ui.interchange.locatable.actions.Icons")
public final class LocatableActionDictionary {
   public static final ActionDictionary DICTIONARY = ActionDictionaries.get(LocatableActionDictionary.class);

   private LocatableActionDictionary() {
   }
}
