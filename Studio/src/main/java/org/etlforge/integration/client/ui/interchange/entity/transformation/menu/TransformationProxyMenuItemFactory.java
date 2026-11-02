package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import org.etlforge.application.ui.view.menu.ViewMenuItemBeanFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.TransformationActionDictionary;

public final class TransformationProxyMenuItemFactory extends ViewMenuItemBeanFactory {
   public TransformationProxyMenuItemFactory(String var1) {
      super("interchange", var1, TransformationActionDictionary.DICTIONARY);
   }
}
