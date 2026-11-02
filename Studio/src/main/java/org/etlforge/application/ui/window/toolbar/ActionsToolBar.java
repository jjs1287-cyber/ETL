package org.etlforge.application.ui.window.toolbar;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public final class ActionsToolBar extends ToolBar {
   public ActionsToolBar() {
      super("toolbar.Actions");
   }

   @Override
   protected List<ToolBarEntry> getEntries() {
      ArrayList var1 = Lists.newArrayList();
      this.addExtensionsToEntryList(var1, ToolBarExtensionPoint.EXTENSION_POINT_1);
      this.addExtensionsToEntryList(var1, ToolBarExtensionPoint.EXTENSION_POINT_2);
      return var1;
   }
}
