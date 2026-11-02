package org.etlforge.integration.client.ui.entity.typeselector;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableSet.Builder;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.JPanel;
import org.etlforge.integration.client.ui.entity.EntityTypeMnemonics;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Grid;

public final class EntityTypeSelectionPanel implements UiProvider, EntityTypeSelector {
   private final JPanel panel;
   private final LinkedHashMap<EntityType, CheckBoxCapsule> checks = Maps.newLinkedHashMap();
   private final EntityTypeSelectionListeners selectionListeners = new EntityTypeSelectionListeners();
   private ImmutableSet<EntityType> previousSelection = ImmutableSet.of();
   private boolean blockSelectionEvents;

   public EntityTypeSelectionPanel(Iterable<EntityType> var1) {
      this.createCheckboxes(var1);
      this.panel = this.layoutCheckBoxes();
   }

   private void createCheckboxes(Iterable<EntityType> var1) {
      EntityTypeSelectionPanel.CheckBoxListener var2 = new EntityTypeSelectionPanel.CheckBoxListener();

      for (EntityType var4 : var1) {
         CheckBoxCapsule var5 = new CheckBoxCapsule(var4);
         var5.addItemListener(var2);
         this.checks.put(var4, var5);
      }
   }

   private JPanel layoutCheckBoxes() {
      Grid var1 = new Grid(0, 3, 10, 5);
      var1.addAll(this.checks.values());
      return var1.container();
   }

   public JPanel getPanel() {
      return this.panel;
   }

   @Override
   public void addEntityTypeSelectionListener(EntityTypeSelectionListener var1) {
      this.selectionListeners.add(var1);
   }

   @Override
   public ImmutableSet<EntityType> getSelection() {
      Builder var1 = ImmutableSet.builder();

      for (EntityType var3 : this.checks.keySet()) {
         if (this.checks.get(var3).isSelected()) {
            var1.add(var3);
         }
      }

      return var1.build();
   }

   @Override
   public void setSelection(Set<EntityType> var1) {
      this.blockSelectionEvents = true;

      try {
         for (Entry var3 : this.checks.entrySet()) {
            ((CheckBoxCapsule)var3.getValue()).setSelected(var1.contains(var3.getKey()));
         }
      } finally {
         this.blockSelectionEvents = false;
      }
   }

   @Override
   public void removeEntityTypeSelectionListener(EntityTypeSelectionListener var1) {
      this.selectionListeners.remove(var1);
   }

   public void setEnabled(boolean var1) {
      for (CheckBoxCapsule var3 : this.checks.values()) {
         var3.setEnabled(var1);
      }
   }

   private void fireSelectionChanged() {
      if (!this.blockSelectionEvents) {
         ImmutableSet var1 = this.getSelection();
         if (!var1.equals(this.previousSelection)) {
            EntityTypeSelectionEvent var2 = new EntityTypeSelectionEvent(this, this.previousSelection, var1);
            this.previousSelection = var1;
            this.selectionListeners.call().selectionChanged(var2);
         }
      }
   }

   public void applyMnemonics() {
      EntityTypeMnemonics var1 = new EntityTypeMnemonics(this.checks.keySet());

      for (Entry var3 : this.checks.entrySet()) {
         MnemonicString var4 = var1.getMnemonicString((EntityType)var3.getKey());
         ((CheckBoxCapsule)var3.getValue()).setText(var4);
      }
   }

   @Override
   public void requestFocus() {
      Iterables.get(this.checks.values(), 0).requestFocus();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   private class CheckBoxListener implements ItemListener {
      private CheckBoxListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         EntityTypeSelectionPanel.this.fireSelectionChanged();
      }
   }
}
