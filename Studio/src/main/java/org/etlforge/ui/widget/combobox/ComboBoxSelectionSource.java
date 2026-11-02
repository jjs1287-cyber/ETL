package org.etlforge.ui.widget.combobox;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.MutableSelectionSource;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionConverter;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.selection.SelectionSourceWidgetSupport;

public final class ComboBoxSelectionSource implements MutableSelectionSource {
   private final JComboBox comboBox;
   private final SelectionSourceWidgetSupport support;
   private final ComboBoxSelectionSource.ComboListener comboListener;
   private int currentIndex;
   private IndexedSelection currentSelection;
   private SelectionConverter converter;

   public ComboBoxSelectionSource(JComboBox var1) {
      this(var1, SelectionConverter.TRIVIAL);
   }

   public ComboBoxSelectionSource(JComboBox var1, SelectionConverter var2) {
      this.comboBox = Preconditions.checkNotNull(var1);
      this.converter = var2;
      this.comboListener = new ComboBoxSelectionSource.ComboListener();
      this.comboBox.addActionListener(this.comboListener);
      this.comboBox.addPropertyChangeListener("model", this.comboListener);
      this.support = new SelectionSourceWidgetSupport(this, this.comboBox);
      this.readCurrentSelectionFromComboBox();
   }

   private void readCurrentSelectionFromComboBox() {
      this.currentIndex = this.comboBox.getSelectedIndex();
      Object var1 = this.comboBox.getSelectedItem();
      Object var2 = this.converter.fromSelection(var1);
      if (var1 != null && var2 == null) {
         this.currentSelection = IndexedSelection.EMPTY;
      } else {
         this.currentSelection = SelectionFactory.newIndexedSelection(this.converter.fromSelection(var1), this.currentIndex);
      }
   }

   public void setConverter(SelectionConverter var1) {
      this.converter = var1 != null ? var1 : SelectionConverter.TRIVIAL;
      this.readCurrentSelectionFromComboBox();
   }

   public void dispose() {
      this.comboBox.removeActionListener(this.comboListener);
   }

   public IndexedSelection getSelection() {
      return this.currentSelection;
   }

   @Override
   public void setSelection(Selection var1) {
      if (var1 == null || var1.isEmpty()) {
         this.setEmptySelection();
      } else if (var1 instanceof IndexedSelection) {
         this.setIndexedSelection((IndexedSelection)var1);
      } else {
         if (!(var1 instanceof OrderedSelection)) {
            throw new IllegalArgumentException("Invalid selection type: " + var1.getClass());
         }

         this.setOrderedSelection((OrderedSelection)var1);
      }
   }

   @Override
   public void removeSelectionListener(SelectionListener var1) {
      this.support.removeListener(var1);
   }

   @Override
   public void addSelectionListener(SelectionListener var1) {
      this.support.addListener(var1);
   }

   private void setEmptySelection() {
      this.currentSelection = IndexedSelection.EMPTY;
      this.currentIndex = -1;
      this.comboBox.setSelectedIndex(-1);
   }

   private void setIndexedSelection(IndexedSelection var1) {
      this.comboBox.setSelectedIndex(var1.headIndex());
      this.readCurrentSelectionFromComboBox();
   }

   private void setOrderedSelection(OrderedSelection var1) {
      Object var2 = this.converter.toSelection(var1.head());
      this.comboBox.setSelectedItem(var2);
      this.readCurrentSelectionFromComboBox();
   }

   private final class ComboListener implements ActionListener, PropertyChangeListener {
      private ComboListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = ComboBoxSelectionSource.this.comboBox.getSelectedIndex();
         if (var2 != ComboBoxSelectionSource.this.currentIndex) {
            ComboBoxSelectionSource.this.readCurrentSelectionFromComboBox();
            ComboBoxSelectionSource.this.support.fireSelectionChange(ComboBoxSelectionSource.this.currentSelection);
         }
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "model".equals(var1.getPropertyName());
         ComboBoxSelectionSource.this.readCurrentSelectionFromComboBox();
         ComboBoxSelectionSource.this.support.fireSelectionChange(ComboBoxSelectionSource.this.currentSelection);
      }
   }
}
