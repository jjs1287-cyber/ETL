package org.etlforge.integration.client.ui.interchange.component.menu.proxy;

import org.etlforge.application.ui.view.menu.ViewMenuItemBeanFactory;
import org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary;

public final class InterchangeMenuItemBeanFactory extends ViewMenuItemBeanFactory {
   public InterchangeMenuItemBeanFactory(String var1) {
      super("interchange", var1, InterchangeActionDictionary.DICTIONARY);
   }
}
