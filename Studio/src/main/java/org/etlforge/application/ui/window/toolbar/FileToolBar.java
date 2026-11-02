package org.etlforge.application.ui.window.toolbar;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.action.WindowActionDictionary;

public final class FileToolBar extends ToolBar {
   private final List<? extends ApplicationAction> actions;

   public FileToolBar(List<? extends ApplicationAction> var1) {
      super("toolbar.File");
      this.actions = var1;
   }

   @Override
   protected List<ToolBarEntry> getEntries() {
      ArrayList var1 = Lists.newArrayList();
                                                                               

      for (ApplicationAction var3 : this.actions) {
         var1.add(ToolBarEntry.of(var3, WindowActionDictionary.DICTIONARY));
      }

      this.addExtensionsToEntryList(var1, ToolBarExtensionPoint.EXTENSION_POINT_2);
      return var1;
   }
}
