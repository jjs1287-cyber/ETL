package org.etlforge.integration.client.ui.debug.action;

import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.action.NewResourceBasedActionDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.ResourceBundleIconProvider;
import org.etlforge.ui.ResourceBundleStringResources;

public final class DebugActionDictionary {
   private static final String BUNDLE = "org.etlforge.integration.client.ui.debug.action.resources.DebugActionResources";
   public static final ActionDictionary DICTIONARY = new NewResourceBasedActionDictionary(
      new ResourceBundleStringResources(
         "org.etlforge.integration.client.ui.debug.action.resources.DebugActionResources", ApplicationResources.MNEMONIC_FACTORY
      ),
      new ResourceBundleIconProvider("org.etlforge.integration.client.ui.debug.action.resources.DebugActionResources"),
      null
   );

   private DebugActionDictionary() {
   }
}
