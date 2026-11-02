package org.etlforge.integration.loadsource.client;

import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.Application;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestServerCache;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.PluginPositionSelectionService;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.PluginPositionSelector;
import org.etlforge.integration.client.plugin.ui.pipeline.selector.PluginPositionSelectorModel;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.structure.edi.EdiPlugin;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DropDownPanel;

final class PluginSelector implements UiProvider, Disposable {
   private final PluginPositionSelectionService service;
   private final PluginPositionSelector selector;
   private LoadSourcePluginIdentifier[] previousPluginSetting;
   private DropDownPanel pluginDropDown;

   public PluginSelector(ServerManager var1, LoadSourcePluginIdentifier[] var2) {
      this.service = new PluginPositionSelectionService(
         EntityType.Source, var1, Application.getCacheService().getCache(PipelinePluginManifestServerCache.class)
      );
      this.previousPluginSetting = var2;
      this.selector = this.createSelector();
      if (!this.service.getModel().isEmpty()) {
         this.restoreInitialSelection();
      }
   }

   private PluginPositionSelector createSelector() {
      PluginPositionSelectorModel var1 = this.prepareModel();
      PluginPositionSelector var2 = new PluginPositionSelector(var1, this.service.getController());
      var2.setIncludeRelativePositionSelector(true);
      var2.setCaption("Select one or more plugins to apply to the selected file");
      return var2;
   }

   private PluginPositionSelectorModel prepareModel() {
      PluginPositionSelectorModel var1 = this.service.getModel();
      var1.setCheckSelectionEnabled(true);
      var1.setSupportedRelativePositions(EnumSet.of(RelativePosition.PRE, RelativePosition.POST));
      var1.setSelectedRelativePositions(EnumSet.of(RelativePosition.POST));
      var1.addPropertyChangeListener("pluginPositions", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            PluginSelector.this.restoreInitialSelection();
         }
      });
      return var1;
   }

   private void restoreInitialSelection() {
      if (this.selector != null) {
         UiUtils.nestedInvokeLater(2, new Runnable() {
            @Override
            public void run() {
               if (PluginSelector.this.previousPluginSetting != null && PluginSelector.this.previousPluginSetting.length != 0) {
                  PluginPositionSelectorModel var1 = PluginSelector.this.service.getModel();
                  HashSet var2 = Sets.newHashSet();

                  for (LoadSourcePluginIdentifier var6 : PluginSelector.this.previousPluginSetting) {
                     PipelinePosition var7 = var1.findPosition(var6.getId(), var6.getPosition());
                     if (var7 != null) {
                        var2.add(var7);
                     }
                  }

                  var1.setSelectedPluginPositions(var2);
                  if (!var2.isEmpty()) {
                     PluginSelector.this.pluginDropDown.expand();
                  }

                  PluginSelector.this.previousPluginSetting = null;
               }
            }
         });
      }
   }

   public void sourceIsEdi() {
      this.selectEdiPlugin();
   }

   private void selectEdiPlugin() {
      PluginPositionSelectorModel var1 = this.service.getModel();
      PipelinePosition var2 = EdiPlugin.forSource();
      var1.setPluginPositionSelected(var2, true);
   }

   @Override
   public void dispose() {
      this.service.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      JPanel var2 = BorderLayoutBuilder.fillWith(this.selector);
      var2.setOpaque(true);
      UiUtils.setPanelBackgrounds(var2, Colors.NEUTRAL_BACKGROUND);
      this.pluginDropDown = new DropDownPanel(var2, "Plugins (Optional)", false);
      this.pluginDropDown.setPreferenceStore(ProjectSourceDataFileInputProvider.DISPLAY_PLUGIN_SELECTOR);
      this.pluginDropDown.setRepackDialogWhenExpansionStateChanges(true);
      this.pluginDropDown.setTitleToolTip("Expand this control to select one or more plugins to be applied to the source file");
      JComponent var3 = this.pluginDropDown.getDisplayer();
      var3.setBorder(Empty.border(0, 10, 0, 0));
      var1.display(var3);
   }

   @Override
   public void requestFocus() {
      this.selector.requestFocus();
   }

   public PipelinePosition[] getSelectedPlugins() {
      Set var1 = this.service.getModel().getSelectedPluginPositions();
      return (PipelinePosition[]) var1.toArray(new PipelinePosition[var1.size()]);
   }
}
