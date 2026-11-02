package org.etlforge.integration.client.plugin.ui.pipeline.applied;

import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;

public class AppliedPipelinePluginsController {
   private final AppliedPipelinePluginsModel model;
   private final ProjectPluginInfo projectPlugins;

   public AppliedPipelinePluginsController(AppliedPipelinePluginsModel var1, ProjectPluginInfo var2) {
      this.model = var1;
      this.projectPlugins = var2;
   }

   public void applyCurrentSettings() {
      if (this.model.isDirty()) {
         this.model.setBusy(true);
         AppliedPipelinePluginsController.Applier var1 = new AppliedPipelinePluginsController.Applier();
         var1.execute();
      }
   }

   private class Applier extends KongaSwingWorker<Void, Void> {
      public Applier() {
         this.setErrorMessage("Failed to apply the plugin settings.");
      }

      protected Void doInBackground() throws Exception {
         AppliedPipelinePluginsController.this.projectPlugins
            .applyNewPluginSettings(
               AppliedPipelinePluginsController.this.model.getEntity(),
               AppliedPipelinePluginsController.this.model.getPlugins(),
               AppliedPipelinePluginsController.this.model.getCacheLookup()
            );
         return null;
      }

      @Override
      protected void finished() {
         AppliedPipelinePluginsController.this.model.setBusy(false);
      }

      protected void onSuccess(Void var1) {
         AppliedPipelinePluginsController.this.model.setDirty(false);
      }
   }
}
