package org.etlforge.integration.loadsource.client;

import java.io.File;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.data.structure.edi.EdiPlugin;
import org.etlforge.integration.plugin.pipeline.PipelinePosition;
import org.etlforge.lang.Arrays2;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;

final class ProjectSourceDataFileInputUi implements Disposable, UiProvider {
   private final SourceFileSelector fileSelector;
   private final PluginSelector pluginSelector;
   private final boolean ediSource;

   public ProjectSourceDataFileInputUi(SourceFileOption[] var1, PluginSelector var2, boolean var3) {
      this.fileSelector = new SourceFileSelector(var1);
      this.pluginSelector = var2;
      this.ediSource = var3;
   }

   @Override
   public void dispose() {
      this.fileSelector.dispose();
      this.pluginSelector.dispose();
   }

   public LoadSourceDataFileInput getSelectedFile() {
      File var1 = this.fileSelector.getSelectedFile();
      if (var1 == null) {
         return null;
      }

      PipelinePosition[] var2 = this.getSelectedPlugins();
      return new LoadSourceDataFileInput(var1, var2);
   }

   private PipelinePosition[] getSelectedPlugins() {
      PipelinePosition[] var1 = this.pluginSelector.getSelectedPlugins();
      if (this.ediSource) {
         var1 = this.ensureEdiPluginIsSelected(var1);
      }

      return var1;
   }

   private PipelinePosition[] ensureEdiPluginIsSelected(PipelinePosition[] var1) {
      assert this.ediSource;
      PipelinePosition var2 = EdiPlugin.forSource();

      for (PipelinePosition var6 : var1) {
         if (var6.getPluginId().equals(var2.getPluginId())) {
            return var1;
         }
      }

      return Arrays2.append(var1, new PipelinePosition[]{var2});
   }

   public void setSelectedOption(SourceFileOption var1) {
      this.fileSelector.setSelectedOption(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().withBorder(Empty.border(10)).setFill(OneColumnPanel.BOTH).setWeightX(1.0);
      if (ClientInfo.isDataLoader()) {
         var2.setWeightY(1.0);
         this.layoutFileSelector(var2, false);
      } else {
         var2.setWeightY(0.5).withInsets(0, 0, 20, 0);
         this.layoutFileSelector(var2, true);
         this.layoutPluginSelector(var2);
      }

      UiUtils.setPanelBackgrounds(var2.container(), Colors.NEUTRAL_BACKGROUND);
      var2.decorate(var1);
   }

   private void layoutFileSelector(OneColumnPanel var1, boolean var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(this.fileSelector);
      if (var2) {
         var3.withBorder(StyleSheet.getLineTitleBorder("Input File"));
      } else {
         var3.withEmptyBorder(5);
      }

      var1.add(var3);
   }

   private void layoutPluginSelector(OneColumnPanel var1) {
      var1.add(this.pluginSelector);
   }

   @Override
   public void requestFocus() {
      this.fileSelector.requestFocus();
   }

   public void setFinalSelectionMonitor(Receiver<Void> var1) {
      this.fileSelector.setFinalSelectionMonitor(var1);
   }
}
