package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import org.etlforge.application.ui.action.ActionDictionaries;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.ActionIcons;
import org.etlforge.application.ui.action.ActionStrings;

@ActionStrings("org.etlforge.integration.client.ui.interchange.entity.transformation.action.Strings")
@ActionIcons("org.etlforge.integration.client.ui.interchange.entity.transformation.action.Icons")
public final class TransformationActionDictionary {
   public static final ActionDictionary DICTIONARY = ActionDictionaries.get(TransformationActionDictionary.class);

   private TransformationActionDictionary() {
   }
}
