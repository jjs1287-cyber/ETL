package org.etlforge.integration.client.ui.interchange.location.export;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangeSupport;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;

final class ExportablePropertyCheckBoxSelector implements ExportablePropertySelector {
   private final Map<JCheckBox, ExportableProperty> checkBoxes = Maps.newLinkedHashMap();
   private final SelectionChangeSupport selectionSupport = new SelectionChangeSupport(this);

   public ExportablePropertyCheckBoxSelector(Iterable<? extends ExportableProperty> var1) {
      this.createSelectionArea(var1);
      this.updateSelection();
   }

   @Override
   public void dispose() {
      this.selectionSupport.removeAllListeners();
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.selectionSupport.addListener(var1);
   }

   @Override
   public OrderedSelection getSelection() {
      return (OrderedSelection)this.selectionSupport.getCurrentSelection();
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = this.createDisplayer();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      Iterator var1 = this.checkBoxes.keySet().iterator();
      if (var1.hasNext()) {
         JCheckBox var2 = (JCheckBox)var1.next();
         var2.requestFocusInWindow();
      }
   }

   private void createSelectionArea(Iterable<? extends ExportableProperty> var1) {
      ActionListener var2 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ExportablePropertyCheckBoxSelector.this.updateSelection();
         }
      };
      this.createCheckBoxes(var1, var2);
   }

   private void updateSelection() {
      Collection var1 = Maps.filterKeys(this.checkBoxes, ButtonUtils.TOGGLE_BUTTON_SELECTED).values();
      OrderedSelection var2 = var1.isEmpty() ? OrderedSelection.EMPTY : SelectionFactory.newOrderedSelection(var1);
      this.selectionSupport.setNewSelection(var2);
   }

   private void createCheckBoxes(Iterable<? extends ExportableProperty> var1, ActionListener var2) {
      for (ExportableProperty var4 : var1) {
         this.checkBoxes.put(this.createCheckBox(var4, var2), var4);
      }
   }

   private JCheckBox createCheckBox(ExportableProperty var1, ActionListener var2) {
      KongaCheckBox var3 = new KongaCheckBox(var1.getName(), var1.isExportedByDefault());
      var3.addActionListener(var2);
      return var3;
   }

   private OneColumnPanel createDisplayer() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setWeightX(1.0);
      var1.withInsets(2, 5, 3, 5);
      this.createAndAddCaption(var1);
      this.addSelectionArea(var1);
      return var1.deeplyNonOpaque();
   }

   private void createAndAddCaption(OneColumnPanel var1) {
      String var2 = LocationResources.getString("ExportablePropertiesSelector.Caption");
      var1.add(TextStyles.InfoText.makeLabel(var2));
   }

   private void addSelectionArea(OneColumnPanel var1) {
      if (this.checkBoxes.size() > 0) {
         this.addCheckBoxes(var1);
      } else {
         this.addNoPropertiesMessage(var1);
      }
   }

   private void addCheckBoxes(OneColumnPanel var1) {
      Grid var2 = new Grid(4, 0).of(this.checkBoxes.keySet());
      var1.add(var2);
   }

   private void addNoPropertiesMessage(OneColumnPanel var1) {
      String var2 = LocationResources.getString("ExportSettingsDialog.NoProperties");
      KongaLabel var3 = TextStyles.DefaultErrorText.makeLabel(var2);
      var1.add(var3);
   }
}
