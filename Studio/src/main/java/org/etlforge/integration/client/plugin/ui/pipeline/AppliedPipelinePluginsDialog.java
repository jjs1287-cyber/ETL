package org.etlforge.integration.client.plugin.ui.pipeline;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.etlforge.application.ui.help.WindowHelpButton;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.integration.client.plugin.pipeline.ProjectPluginInfo;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.plugin.ui.pipeline.applied.AppliedPipelinePluginsController;
import org.etlforge.integration.client.plugin.ui.pipeline.applied.AppliedPipelinePluginsModel;
import org.etlforge.integration.client.plugin.ui.pipeline.applied.AppliedPipelinePluginsViewer;
import org.etlforge.integration.client.plugin.ui.pipeline.applied.ApplyPluginSettingsAction;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.PluginPositionSelectionService;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.Property;

public final class AppliedPipelinePluginsDialog {
   private final IntegrationEntity entity;
   private final ServerManager serverManager;
   private final ProjectPluginInfo projectPlugins;
   private final PipelinePluginManifestServerCache pluginCache;

   public AppliedPipelinePluginsDialog(IntegrationEntity var1, ServerManager var2, ProjectPluginInfo var3, PipelinePluginManifestServerCache var4) {
      this.entity = var1;
      this.serverManager = var2;
      this.projectPlugins = var3;
      this.pluginCache = var4;
   }

   public void open(JFrame var1) {
      PluginPositionSelectionService var2 = new PluginPositionSelectionService(this.entity.getEntityType(), this.serverManager, this.pluginCache);
      AppliedPipelinePluginsModel var3 = new AppliedPipelinePluginsModel(this.projectPlugins, this.entity);
      AppliedPipelinePluginsController var4 = new AppliedPipelinePluginsController(var3, this.projectPlugins);
      AppliedPipelinePluginsViewer var5 = new AppliedPipelinePluginsViewer(var3, var4, var2);
      AppliedPipelinePluginsDialog.Dialog var6 = new AppliedPipelinePluginsDialog.Dialog(var1, var5, var3, var4);
      this.installWaitIndication(var2, var6);
      var6.setVisible(true);
      var2.dispose();
   }

   private void installWaitIndication(PluginPositionSelectionService var1, AppliedPipelinePluginsDialog.Dialog var2) {
      Property var3 = var1.getModel().getProperty("busy");
      WaitStateBinding var4 = new WaitStateBinding(var3, var2);
      var4.syncUi();
   }

   private static class Dialog extends WizardStyleDialog {
      private final AppliedPipelinePluginsModel model;
      private final AppliedPipelinePluginsController controller;
      private final JButton helpButton;

      public Dialog(JFrame var1, AppliedPipelinePluginsViewer var2, AppliedPipelinePluginsModel var3, AppliedPipelinePluginsController var4) {
         super(var1, PluginUiResources.getString("AppliedPluginsViewerDialog.Title"), wrapViewer(var2), null, true);
         this.model = var3;
         this.controller = var4;
         this.helpButton = new WindowHelpButton(this, "Applying_Pipeline_Plugins", true).getButton();
         this.getDefaultLayout().south(this.layoutButtons());
         this.pack();
         this.manageLocation(AppliedPipelinePluginsDialog.class);
         this.setOkButtonAsDefault();
      }

      private static JComponent wrapViewer(AppliedPipelinePluginsViewer var0) {
         BorderLayoutBuilder var1 = new BorderLayoutBuilder(var0);
         var1.withBorder(Empty.border(10));
         UiUtils.setPanelBackgrounds(var1.container(), Colors.NEUTRAL_BACKGROUND);
         return var1.container();
      }

      @Override
      protected void onOk() {
         try {
            this.controller.applyCurrentSettings();
         } finally {
            super.onOk();
         }
      }

      private UiProvider layoutButtons() {
         JButton var1 = this.createApplyButton();
         var1.setAction(new ApplyPluginSettingsAction(this.model, this.controller));
         return Horizontal.box()
            .add(this.helpButton)
            .glue()
            .addAllWithSpace(10, this.createOKButton(), this.createCancelButton(), var1)
            .withEmptyBorder(10)
            .withBackground(Colors.ETLFORGE_BLUE);
      }
   }
}
