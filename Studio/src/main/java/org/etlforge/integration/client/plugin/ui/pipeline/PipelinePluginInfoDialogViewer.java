package org.etlforge.integration.client.plugin.ui.pipeline;

import java.awt.Dialog.ModalityType;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.util.UiUtils;

public final class PipelinePluginInfoDialogViewer {
   private PipelinePluginManifestCache manifestCache;

   public void view(PipelinePluginManifest var1) {
      PipelinePluginInfoViewer var2 = this.getViewer();
      var2.view(var1);
      KongaDialog var3 = createDialog(var2);
      var3.setVisible(true);
   }

   private static KongaDialog createDialog(UiProvider var0) {
      KongaDialog var1 = new KongaDialog(UiUtils.getActiveWindow(), Strings.get("PipelinePlugin.InfoDialog.Title"), ModalityType.APPLICATION_MODAL);
      var1.standardLayout(var0, KongaDialog.CLOSE_ONLY);
      var1.setResizable(false);
      var1.manageLocation(PipelinePluginInfoDialogViewer.class);
      var1.setCloseButtonAsDefault();
      return var1;
   }

   public void view(PluginIdentifier var1) {
      PipelinePluginInfoDialogViewer.ManifestLoader var2 = new PipelinePluginInfoDialogViewer.ManifestLoader(var1);
      var2.execute();
   }

   public void setManifestCache(PipelinePluginManifestCache var1) {
      this.manifestCache = var1;
   }

   private PipelinePluginInfoViewer getViewer() {
      PipelinePluginInfoViewer var1 = new PipelinePluginInfoViewer();
      PipelinePluginManifestViewer var2 = new PipelinePluginManifestViewer();
      var1.setPluginManifestViewer(var2);
      return var1;
   }

   private final class ManifestLoader extends KongaSwingWorker<PipelinePluginManifest, Void> {
      private final PluginIdentifier id;

      public ManifestLoader(PluginIdentifier var2) {
         this.id = var2;
         this.setErrorMessage(Strings.get("PipelinePlugin.CacheError"));
      }

      protected PipelinePluginManifest doInBackground() throws Exception {
         return PipelinePluginInfoDialogViewer.this.manifestCache.getCachedManifest(this.id.getName());
      }

      protected void onSuccess(PipelinePluginManifest var1) {
         if (var1 != null) {
            PipelinePluginInfoDialogViewer.this.view(var1);
         } else {
            Alert.error("Could not find information for the plugin " + this.id.getName(), "Manifest Not Found");
         }
      }
   }
}
