package org.etlforge.integration.client.ui.entity.search;

import com.google.common.collect.ImmutableSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.integration.client.ui.entity.typeselector.EntityTypeSelectionEvent;
import org.etlforge.integration.client.ui.entity.typeselector.EntityTypeSelectionListener;
import org.etlforge.integration.client.ui.entity.typeselector.EntityTypeSelectionListeners;
import org.etlforge.integration.client.ui.entity.typeselector.EntityTypeSelectionPanel;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.event.ActionListeners;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class EntityTypePanel implements EntityTypeSelectionListener, ActionListener {
   private final EntitySearchModel model;
   private final JCheckBox includeAll;
   private final EntityTypeSelectionPanel typeSelectionPanel;
   private final JComponent displayer;
   private final EntityTypeSelectionListeners selectionListeners;
   private final ActionListeners actionListeners;

   public EntityTypePanel(EntitySearchModel var1) {
      this.model = var1;
      this.selectionListeners = new EntityTypeSelectionListeners();
      this.actionListeners = new ActionListeners();
      this.displayer = new JPanel();
      this.includeAll = new KongaCheckBox(EntityResources.getString("Search.TypeSelector.All"), true);
      this.includeAll.addActionListener(this);
      this.typeSelectionPanel = this.createTypeSelectionPanel(var1);
      this.layoutComponents();
      this.syncSelectionFromModel();
   }

   private EntityTypeSelectionPanel createTypeSelectionPanel(EntitySearchModel var1) {
      EntityTypeSelectionPanel var2 = new EntityTypeSelectionPanel(var1.getSupportedEntityTypes());
      var2.addEntityTypeSelectionListener(this);
      var2.setEnabled(false);
      return var2;
   }

   private void layoutComponents() {
      OneColumnPanel var1 = new OneColumnPanel(OneColumnPanel.WEST, OneColumnPanel.HORIZONTAL);
      var1.withInsets(2, 5, 5, 5);
      var1.setWeightX(1.0);
      var1.add(this.includeAll);
      var1.add(this.typeSelectionPanel.getPanel());
      var1.addToParent(this.displayer, null);
      UiUtils.setOpaqueRecursivelySkipDefaults(this.displayer, false);
   }

   public JComponent getDisplayer() {
      return this.displayer;
   }

   public void syncSelectionFromModel() {
      if (this.model.areAllEntityTypesSelected()) {
         this.includeAll.setSelected(true);
         this.typeSelectionPanel.setEnabled(false);
      } else {
         this.includeAll.setSelected(false);
         this.typeSelectionPanel.setEnabled(true);
         this.typeSelectionPanel.setSelection(this.model.getSelectedEntityTypes());
      }
   }

   public ImmutableSet<EntityType> getSelection() {
      return this.allSelected() ? EntityType.allTypes() : this.typeSelectionPanel.getSelection();
   }

   public boolean allSelected() {
      return this.includeAll.isSelected();
   }

   public void addSelectionListener(EntityTypeSelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   public void addActionListener(ActionListener var1) {
      this.actionListeners.add(var1);
   }

   @Override
   public void selectionChanged(EntityTypeSelectionEvent var1) {
      this.selectionListeners.call().selectionChanged(var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.typeSelectionPanel.setEnabled(!this.allSelected());
      this.actionListeners.call().actionPerformed(var1);
   }
}
