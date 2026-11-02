package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;

final class RefreshAction extends AbstractAction {
   private final PluginPositionSelectorController controller;

   public RefreshAction(PluginPositionSelectorController var1) {
      ActionDecorators.REFRESH.decorate(this);
      this.controller = var1;
      this.putValue("ShortDescription", PluginUiResources.getString("PipelinePluginCacheRefreshAction.ToolTip"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RefreshAction.this.refresh();
         }
      });
   }

   private void refresh() {
      this.controller.refreshListOfPlugins();
   }
}
