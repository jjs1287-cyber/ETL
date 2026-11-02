package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import java.awt.Component;
import java.util.EnumSet;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.plugin.pipeline.cache.PipelinePluginManifestCache;
import org.etlforge.integration.client.plugin.ui.pipeline.PipelinePositionTable;
import org.etlforge.integration.client.plugin.ui.pipeline.ViewPipelinePluginInfoAction;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.plugin.PluginIdentifier;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.ActionEnabledBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.util.Property;

public final class PluginPositionSelector implements UiProvider {
   private final PluginPositionSelectorModel model;
   private final PluginPositionSelectorController controller;
   private final KongaComboBox serverSelector;
   private final PipelinePositionTable pluginTable;
   private final PluginPositionSelector.ViewDetailsAction viewDetailsAction;
   private final RefreshAction refreshAction;
   private AssignAction assignAction;
   private String caption;
   private RelativePositionSelector relativePositionSelector;

   public PluginPositionSelector(PluginPositionSelectorModel var1, PluginPositionSelectorController var2) {
      this.model = var1;
      this.controller = var2;
      this.serverSelector = createServerSelector();
      this.pluginTable = new PipelinePositionTable(var1);
      this.viewDetailsAction = new PluginPositionSelector.ViewDetailsAction();
      this.pluginTable.addSelectionAction(this.viewDetailsAction);
      this.refreshAction = new RefreshAction(var2);
      this.bindProperties();
   }

   private static KongaComboBox createServerSelector() {
      KongaComboBox var0 = new KongaComboBox();
      var0.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXX");
      var0.setRenderer(new PluginPositionSelector.ComboRenderer());
      return var0;
   }

   private void bindProperties() {
      Property var1 = this.model.getProperty("selectedServer");
      Bindings var2 = new Bindings(
         new ComboBoxBinding(this.serverSelector, this.model.getProperty("knownServers"), var1),
         new PluginPositionSelector.RelativePositionBinding(),
         ActionEnabledBinding.notNull(this.refreshAction, var1)
      );
      var2.syncUi();
   }

   public PluginPositionSelectorModel getModel() {
      return this.model;
   }

   public void setIncludeRelativePositionSelector(boolean var1) {
      if (var1) {
         if (this.relativePositionSelector == null) {
            this.relativePositionSelector = new RelativePositionSelector(this.model);
         }
      } else {
         this.relativePositionSelector = null;
      }
   }

   public void setCaption(String var1) {
      this.caption = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      new PluginPositionSelector.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.serverSelector.requestFocus();
   }

   public void setUseAssignAction(boolean var1) {
      if (var1) {
         if (this.assignAction == null) {
            this.assignAction = new AssignAction(this.controller);
            this.pluginTable.setDefaultAction(this.assignAction);
         }
      } else if (this.assignAction != null) {
         this.assignAction.setSelectionSource(null);
         this.assignAction = null;
      }
   }

   private static class ComboRenderer extends DefaultListCellRenderer {
      private ComboRenderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof ServerInfo) {
            this.setText(((ServerInfo)var2).getName());
            this.setIcon(ClientIcons.APPLICATION_16);
         } else if (var2 == null) {
            this.setText("[Select a ETLForge Server]");
            this.setIcon(null);
         }

         return this;
      }
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder(0, 5);

      public Layout() {
         this.layoutServerSelector();
         this.layoutTable();
      }

      private void layoutServerSelector() {
         JComponent var1 = this.layoutServerAndRelativePositionSelectors();
         if (StringUtils.isEmpty(PluginPositionSelector.this.caption)) {
            this.layout.north(var1);
         } else {
            KongaLabel var2 = TextStyles.InfoText.makeLabel(PluginPositionSelector.this.caption).withBorder(Empty.border(0, 0, 5, 0));
            OneColumnPanel var3 = Vertical.column().setWeightX(1.0).of(var2, var1);
            this.layout.north(var3);
         }
      }

      private JComponent layoutServerAndRelativePositionSelectors() {
         return new OneRowPanel()
            .withInsets(0, 0, 0, 20)
            .setAnchor(OneRowPanel.WEST)
            .addAll(PluginPositionSelector.this.serverSelector, PluginPositionSelector.this.relativePositionSelector)
            .invisible();
      }

      private void layoutTable() {
         BoxBuilder var1 = Horizontal.box()
            .bottomAligned()
            .add(PluginUiResources.getString("PipelinePositionSelector.TableCaption"))
            .glue()
            .addAllWithSpace(
               10, PluginPositionSelector.this.assignAction, PluginPositionSelector.this.viewDetailsAction, PluginPositionSelector.this.refreshAction
            );
         this.layout.center(CaptionLayout.of(var1, PluginPositionSelector.this.pluginTable));
      }

      public void decorate(UiCanvas var1) {
         this.layout.decorate(var1);
      }
   }

   private class RelativePositionBinding extends AbstractBinding<EnumSet<RelativePosition>> {
      public RelativePositionBinding() {
         super(PluginPositionSelector.this.model, "selectedRelativePosition");
      }

      protected void updateUiWithValue(EnumSet<RelativePosition> var1) {
         PluginPositionSelector.this.pluginTable.installRelativePositionFilter(var1);
      }
   }

   private class ViewDetailsAction extends ViewPipelinePluginInfoAction {
      private ViewDetailsAction() {
      }

      @Override
      protected PipelinePluginManifestCache getManifestCache(PluginIdentifier var1) {
         return PluginPositionSelector.this.model.getManifestCache(var1);
      }
   }
}
