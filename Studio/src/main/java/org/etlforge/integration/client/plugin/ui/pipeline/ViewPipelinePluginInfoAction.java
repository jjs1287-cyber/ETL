package org.etlforge.integration.client.plugin.ui.pipeline;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public abstract class ViewPipelinePluginInfoAction extends AbstractSingleObjectSelectionAction<PluginTableItem> {
   protected ViewPipelinePluginInfoAction() {
      super(PluginTableItem.class);
      ActionDecorators.VIEW_DETAILS.decorate(this);
      this.putValue("ShortDescription", Strings.get("PipelinePlugin.ViewDetailsAction.ToolTip"));
   }

   protected final void run(final PluginTableItem var1, ActionEvent var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewPipelinePluginInfoAction.this.displayDetails(var1);
         }
      });
   }

   protected abstract PipelinePluginManifestCache getManifestCache(PluginIdentifier var1);

   private void displayDetails(PluginTableItem var1) {
      PipelinePluginManifestCache var2 = this.getManifestCache(var1.getPluginId());
      if (var2 != null) {
         this.displayDetails(var1, var2);
      } else {
         Alert.error(Strings.get("PipelinePlugin.ViewDetailsAction.NoManifestFound"), Strings.get("Error"));
      }
   }

   private void displayDetails(PluginTableItem var1, PipelinePluginManifestCache var2) {
      PipelinePluginInfoDialogViewer var3 = new PipelinePluginInfoDialogViewer();
      var3.setManifestCache(var2);
      var3.view(var1.getPluginId());
   }
}
