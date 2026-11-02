package org.etlforge.integration.client.plugin.ui.pipeline.applied;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.ui.pipeline.AppliedPipelinePluginsTable;
import org.etlforge.integration.client.plugin.ui.pipeline.PluginTableItem;
import org.etlforge.integration.client.plugin.ui.pipeline.ViewPipelinePluginInfoAction;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.PluginPositionSelectionService;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.PluginPositionSelector;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.SelectedPosition;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityLabel;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.widget.label.KongaLabel;

public final class AppliedPipelinePluginsViewer implements UiProvider {
   private final AppliedPipelinePluginsModel model;
   private final AppliedPipelinePluginsController controller;
   private final AppliedPipelinePluginsTable currentPluginsTable;
   private final PluginPositionSelector pluginSelector;
   private final AppliedPipelinePluginsViewer.ViewDetailsAction viewDetailsAction;
   private final AppliedPipelinePluginsViewer.RemoveAction removeAction;
   private boolean includeApplyButton;

   public AppliedPipelinePluginsViewer(final AppliedPipelinePluginsModel var1, AppliedPipelinePluginsController var2, PluginPositionSelectionService var3) {
      this.model = var1;
      this.controller = var2;
      this.currentPluginsTable = new AppliedPipelinePluginsTable(var1);
      this.pluginSelector = this.createPluginSelector(var3);
      var3.setSelectionCallback(new Receiver<List<SelectedPosition>>() {
         public void handle(List<SelectedPosition> var1x) {
            var1.addPlugins(var1x);
         }
      });
      this.viewDetailsAction = new AppliedPipelinePluginsViewer.ViewDetailsAction();
      this.removeAction = new AppliedPipelinePluginsViewer.RemoveAction();
      this.currentPluginsTable.bindSelectionActions(this.removeAction);
      this.currentPluginsTable.setDefaultAction(this.viewDetailsAction);
   }

   private PluginPositionSelector createPluginSelector(PluginPositionSelectionService var1) {
      PluginPositionSelector var2 = new PluginPositionSelector(var1.getModel(), var1.getController());
      var2.setUseAssignAction(true);
      var2.setCaption(PluginUiResources.getString("AppliedPluginsViewer.SelectorCaption"));
      return var2;
   }

   public void setIncludeApplyButton(boolean var1) {
      this.includeApplyButton = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      new AppliedPipelinePluginsViewer.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.currentPluginsTable.requestFocus();
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 10);

      public Layout() {
         this.layoutCurrentPlugins();
         this.layoutPluginSelector();
      }

      private void layoutCurrentPlugins() {
         this.layout.north(CaptionLayout.of(this.createTopPart(), AppliedPipelinePluginsViewer.this.currentPluginsTable));
      }

      private JComponent createTopPart() {
         BoxBuilder var1 = Horizontal.box().bottomAligned();
         var1.add(this.createCaption()).rigid(20).glue();
         if (AppliedPipelinePluginsViewer.this.includeApplyButton) {
            var1.add(new ApplyPluginSettingsAction(AppliedPipelinePluginsViewer.this.model, AppliedPipelinePluginsViewer.this.controller)).rigid(10);
         }

         var1.addAllWithSpace(10, AppliedPipelinePluginsViewer.this.viewDetailsAction, AppliedPipelinePluginsViewer.this.removeAction);
         KongaLabel var2 = WizardStyleSheet.CAPTION_STYLE.makeLabel(PluginUiResources.getString("AppliedPluginsViewer.Title.Current"));
         return new OneColumnPanel().setWeightX(1.0).of(var2, var1).container();
      }

      private JComponent createCaption() {
         JLabel var1 = new JLabel(PluginUiResources.getString("AppliedPluginsViewer.Caption") + " ");
         IntegrationEntityLabel var2 = new IntegrationEntityLabel(AppliedPipelinePluginsViewer.this.model.getEntity(), TextStyles.AdminHint);
         JLabel var3 = new JLabel(":");
         return Horizontal.box().bottomAligned().of(var1, var2, var3).container();
      }

      public void layoutPluginSelector() {
         KongaLabel var1 = WizardStyleSheet.CAPTION_STYLE.makeLabel(PluginUiResources.getString("AppliedPluginsViewer.Title.Available"));
         this.layout.center(CaptionLayout.of(var1, AppliedPipelinePluginsViewer.this.pluginSelector));
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }

   private class RemoveAction extends AbstractSingleTypeSelectionAction<PluginTableItem> {
      public RemoveAction() {
         super(PluginTableItem.class);
         ActionDecorators.DELETE.decorate(this);
         this.putValue("ShortDescription", "Remove the selected plugins from this project item");
      }

      protected boolean isEnabledFor(PluginTableItem var1) {
         return !var1.isGlobal();
      }

      @Override
      protected void run(List<PluginTableItem> var1) {
         ArrayList var2 = Lists.newArrayList();

         for (PluginTableItem var4 : var1) {
            if (!var4.isGlobal()) {
               var2.add(var4.asAppliedPipelinePlugin(AppliedPipelinePluginsViewer.this.model.getEntity()));
            }
         }

         AppliedPipelinePluginsViewer.this.model.removePlugins(var2);
      }
   }

   private class ViewDetailsAction extends ViewPipelinePluginInfoAction {
      private ViewDetailsAction() {
      }

      @Override
      protected PipelinePluginManifestCache getManifestCache(PluginIdentifier var1) {
         PipelinePluginManifestCache var2 = AppliedPipelinePluginsViewer.this.model.getManifestCache(var1);
         if (var2 == null) {
            var2 = AppliedPipelinePluginsViewer.this.pluginSelector.getModel().getManifestCache(var1);
         }

         return var2;
      }
   }
}
