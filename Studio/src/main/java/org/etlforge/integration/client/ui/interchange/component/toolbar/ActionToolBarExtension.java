package org.etlforge.integration.client.ui.interchange.component.toolbar;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.toolbar.AbstractToolBarExtension;
import org.etlforge.application.ui.window.toolbar.ToolBarEntry;
import org.etlforge.application.ui.window.toolbar.ToolBarExtensionPoint;
import org.etlforge.integration.client.ui.interchange.core.action.InterchangeActionDictionary;

public final class ActionToolBarExtension extends AbstractToolBarExtension {
   private final List<? extends ApplicationAction> actions;

   public ActionToolBarExtension(List<? extends ApplicationAction> var1) {
      super("toolbar.Actions");
      this.actions = var1;
   }

   @Override
   protected List<ToolBarEntry> createExtension(ToolBarExtensionPoint var1) {
      if (var1 != ToolBarExtensionPoint.EXTENSION_POINT_1) {
         return null;
      }

      ArrayList var2 = Lists.newArrayList();

      for (ApplicationAction var4 : this.actions) {
         var2.add(ToolBarEntry.of(var4, InterchangeActionDictionary.DICTIONARY));
      }

      return var2;
   }
}
