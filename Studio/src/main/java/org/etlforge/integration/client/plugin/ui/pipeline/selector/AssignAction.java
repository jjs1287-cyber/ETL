package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.plugin.ui.pipeline.PluginTableItem;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;

final class AssignAction extends AbstractSingleTypeSelectionAction<PluginTableItem> {
   private final PluginPositionSelectorController controller;

   public AssignAction(PluginPositionSelectorController var1) {
      super(PluginTableItem.class, PluginUiResources.getString("AppliedPluginsViewer.Assign.Name"), ApplicationResources.ICONS.getIcon("ArrowUp.16"));
      this.controller = var1;
      this.putValue("ShortDescription", PluginUiResources.getString("AppliedPluginsViewer.Assign.ToolTip"));
   }

   protected boolean isEnabledFor(PluginTableItem var1) {
      return !var1.isGlobal();
   }

   @Override
   protected void run(List<PluginTableItem> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (PluginTableItem var4 : var1) {
         var2.add(var4.getPipelinePosition());
      }

      this.controller.selectedPositions(var2);
   }
}
