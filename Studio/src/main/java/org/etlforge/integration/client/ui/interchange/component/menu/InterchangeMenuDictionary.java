package org.etlforge.integration.client.ui.interchange.component.menu;

import org.etlforge.application.ui.menu.MenuDictionary;
import org.etlforge.application.ui.menu.NewResourceBasedMenuDictionary;
import org.etlforge.application.ui.resources.ApplicationResources;

public final class InterchangeMenuDictionary {
   private static final String RESOURCES = "org.etlforge.integration.client.ui.interchange.component.menu.resources.MenuResources";
   public static final MenuDictionary DICTIONARY = NewResourceBasedMenuDictionary.newDictionary(
      "org.etlforge.integration.client.ui.interchange.component.menu.resources.MenuResources",
      ApplicationResources.MNEMONIC_FACTORY,
      "org.etlforge.integration.client.ui.interchange.component.menu.resources.MenuResources",
      null
   );

   static void setParent(MenuDictionary var0) {
      ((NewResourceBasedMenuDictionary)DICTIONARY).setParent(var0);
   }

   private InterchangeMenuDictionary() {
   }
}
