package org.etlforge.integration.client.ui.core.window.toolbar;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.toolbar.AbstractToolBarExtension;
import org.etlforge.application.ui.window.toolbar.ToolBarEntry;
import org.etlforge.application.ui.window.toolbar.ToolBarExtensionPoint;
import org.etlforge.integration.client.ui.core.window.action.ClientWindowActionDictionary;

public class ActionsToolBarExtension extends AbstractToolBarExtension {
   private final ApplicationAction[] actions;

   public ActionsToolBarExtension(ApplicationAction... var1) {
      super("toolbar.Actions");
      this.actions = var1;
   }

   @Override
   protected List<ToolBarEntry> createExtension(ToolBarExtensionPoint var1) {
      if (var1 != ToolBarExtensionPoint.EXTENSION_POINT_2) {
         return null;
      }

      ArrayList var2 = Lists.newArrayList();

      for (ApplicationAction var6 : this.actions) {
         ToolBarEntry var7 = ToolBarEntry.of(var6, ClientWindowActionDictionary.DICTIONARY);
         var2.add(var7);
      }

      return var2;
   }
}
