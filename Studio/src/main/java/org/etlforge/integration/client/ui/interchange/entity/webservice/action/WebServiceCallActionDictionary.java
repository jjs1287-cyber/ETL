package org.etlforge.integration.client.ui.interchange.entity.webservice.action;

import org.etlforge.application.ui.action.ActionDictionaries;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.ActionIcons;
import org.etlforge.application.ui.action.ActionStrings;

@ActionStrings("org.etlforge.integration.client.ui.interchange.entity.webservice.action.Strings")
@ActionIcons("org.etlforge.integration.client.ui.interchange.entity.webservice.action.Icons")
public final class WebServiceCallActionDictionary {
   public static final ActionDictionary DICTIONARY = ActionDictionaries.get(WebServiceCallActionDictionary.class, null);

   private WebServiceCallActionDictionary() {
   }
}
