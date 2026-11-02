package org.etlforge.integration.client.ui.interchange.locatable.menu;

import org.etlforge.application.ui.view.menu.ViewMenuItemBeanFactory;
import org.etlforge.integration.client.ui.interchange.locatable.actions.LocatableActionDictionary;

public final class LocatableMenuItemBeanFactory extends ViewMenuItemBeanFactory {
   public LocatableMenuItemBeanFactory(String var1) {
      super("interchange", var1, LocatableActionDictionary.DICTIONARY);
   }
}
