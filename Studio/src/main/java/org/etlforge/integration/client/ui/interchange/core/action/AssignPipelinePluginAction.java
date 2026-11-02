package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import org.etlforge.application.Application;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.plugin.pipeline.PipelinePluginSpecification;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.plugin.ui.pipeline.AppliedPipelinePluginsDialog;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class AssignPipelinePluginAction extends AbstractSingleActiveObjectAction<IntegrationEntity> {
   public static final String ID = "action.ic.AssignPlugin";
   private final ApplicationWindow appWin;
   private final ServerManager serverManager;

   public AssignPipelinePluginAction(ApplicationWindow var1, ServerManager var2) {
      super(var1.getActiveObjectService(), "action.ic.AssignPlugin", InterchangeActionDictionary.DICTIONARY, IntegrationEntity.class);
      this.appWin = var1;
      this.serverManager = var2;
      this.setEnabled(true);
   }

   protected boolean isEnabledFor(IntegrationEntity var1) {
      return this.isSupportedEntity(var1);
   }

   private boolean isSupportedEntity(IntegrationEntity var1) {
      PipelinePluginSpecification var2 = PipelinePluginSpecification.getLatest();
      return var2.isEntitySupported(var1);
   }

   protected void runOnObject(final IntegrationEntity var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            AssignPipelinePluginAction.this.saveAndOpenPipelineUi(var1);
         }
      });
   }

   private void saveAndOpenPipelineUi(IntegrationEntity var1) {
      boolean var2 = UiCompanion.of(var1).saveIfDirty();
      if (var2) {
         this.openPluginDialog(var1);
      }
   }

   private void openPluginDialog(IntegrationEntity var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      AppliedPipelinePluginsDialog var3 = new AppliedPipelinePluginsDialog(
         var1, this.serverManager, var2.getPipelinePlugins(), Application.getCacheService().getCache(PipelinePluginManifestServerCache.class)
      );
      var3.open(this.appWin.getFrame());
   }
}
