package org.etlforge.integration.client.plugin.ui.pipeline.selector;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.Map;
import javax.swing.JCheckBox;
import org.etlforge.collections.EnumSets;
import org.etlforge.integration.client.plugin.ui.resources.PluginUiResources;
import org.etlforge.integration.plugin.pipeline.RelativePosition;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class RelativePositionSelector implements UiProvider {
   private final PluginPositionSelectorModel model;
   private final Map<RelativePosition, JCheckBox> checkBoxes;

   public RelativePositionSelector(PluginPositionSelectorModel var1) {
      this.model = var1;
      this.checkBoxes = Maps.newLinkedHashMap();
      this.createCheckBoxes(var1.getSupportedRelativePositions(), var1.getSelectedRelativePositions());
   }

   private void createCheckBoxes(EnumSet<RelativePosition> var1, EnumSet<RelativePosition> var2) {
      ActionListener var3 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            RelativePositionSelector.this.selectionChanged();
         }
      };

      for (RelativePosition var5 : var1) {
         KongaCheckBox var6 = new KongaCheckBox(var5.toString(), var2.contains(var5));
         var6.addActionListener(var3);
         this.checkBoxes.put(var5, var6);
      }
   }

   private void selectionChanged() {
      EnumSet var1 = EnumSets.copyOf(Maps.filterValues(this.checkBoxes, ButtonUtils.TOGGLE_BUTTON_SELECTED).keySet(), RelativePosition.class);
      this.model.setSelectedRelativePositions(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      new RelativePositionSelector.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.checkBoxes.values().iterator().next().requestFocusInWindow();
   }

   private class Layout {
      private final OneRowPanel row = new OneRowPanel();

      public Layout() {
         this.row.setAnchor(OneRowPanel.WEST);
         this.row.withInsets(0, 0, 0, 10);
         this.layoutCaption();
         this.layoutCheckBoxes();
      }

      public void decorate(UiCanvas var1) {
         this.row.decorate(var1);
      }

      private void layoutCaption() {
         this.row.add(PluginUiResources.getString("PipelinePlugin.RelativePositionSelector.Caption"));
      }

      private void layoutCheckBoxes() {
         for (JCheckBox var2 : RelativePositionSelector.this.checkBoxes.values()) {
            this.row.add(var2);
         }
      }
   }
}
