package com.etlforge.integration.client.ui.interchange.core.menu;

import com.etlforge.integration.client.ui.interchange.core.action.EnterpriseActionDictionary;
import org.etlforge.application.ui.view.menu.ViewMenuItemBeanFactory;

public final class InterchangeMenuItemBeanFactory extends ViewMenuItemBeanFactory {
   public InterchangeMenuItemBeanFactory(String var1) {
      super("interchange", var1, EnterpriseActionDictionary.DICTIONARY);
   }
}
