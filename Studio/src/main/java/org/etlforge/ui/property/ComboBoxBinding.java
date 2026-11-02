package org.etlforge.ui.property;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.util.ConstantProperty;
import org.etlforge.util.Property;

public class ComboBoxBinding<T> implements Binding {
   private final JComboBox comboBox;
   private final Property<List<T>> values;
   private final Property<T> selectedValue;
   private boolean modelToUiEnabled = true;
   private boolean uiToModelEnabled = true;
   private final ComboBoxBinding<T>.ModelItemsListener modelItemsListener;
   private final ComboBoxBinding<T>.SelectedValueListener selectedValueListener;
   private final ComboBoxBinding<T>.ComboSelectionListener comboSelectionListener;
   private ComboBoxBinding.SelectionPolicy selectionPolicy = ComboBoxBinding.SelectionPolicy.DEFAULT;
   private String nullSelectionMessage;
   private boolean disableWhenEmpty;

   public static <E extends Enum<E>> ComboBoxBinding<E> forEnum(JComboBox var0, Class<E> var1, Property<E> var2) {
      return new ComboBoxBinding<>(var0, ImmutableList.copyOf(EnumSet.allOf(var1)), var2);
   }

   public ComboBoxBinding(JComboBox var1, List<T> var2, Property<T> var3) {
      this(var1, ConstantProperty.from("", var2), var3);
   }

   public ComboBoxBinding(JComboBox var1, Property<List<T>> var2, Property<T> var3) {
      this.comboBox = var1;
      this.values = var2;
      this.selectedValue = var3;
      this.modelItemsListener = new ComboBoxBinding.ModelItemsListener();
      var2.addListener(this.modelItemsListener);
      this.selectedValueListener = new ComboBoxBinding.SelectedValueListener();
      var3.addListener(this.selectedValueListener);
      this.comboSelectionListener = new ComboBoxBinding.ComboSelectionListener();
      var1.addItemListener(this.comboSelectionListener);
   }

   public void setSelectionPolicyOnContentChange(ComboBoxBinding.SelectionPolicy var1) {
      this.selectionPolicy = var1;
   }

   public void setNullSelectionMessage(String var1) {
      this.nullSelectionMessage = var1;
      Object var2 = this.selectedValue.get();
      this.applyNullMessage(var2);
   }

   public void setDisableWhenEmpty(boolean var1) {
      this.disableWhenEmpty = var1;
   }

   @Override
   public void syncUi() {
      this.setUiToModelEnabled(false);

      try {
         this.updateUi(this.values.get(), this.selectedValue.get());
      } finally {
         this.setUiToModelEnabled(true);
      }
   }

   private void updateUi(List<T> var1, T var2) {
      this.setItemsToShow(var1);
      this.setSelectedValue(var2);
   }

   private void setItemsToShow(List<T> var1) {
      this.comboBox.setModel(new DefaultComboBoxModel<>(var1.toArray()));
      if (this.disableWhenEmpty) {
         this.comboBox.setEnabled(var1.size() > 0);
      }
   }

   private void setSelectedValue(Object var1) {
      this.comboBox.setSelectedItem(var1);
      this.comboSelectionListener.selectedItem = var1;
      this.applyNullMessage(var1);
   }

   private void applyNullMessage(Object var1) {
      if (var1 == null && StringUtils.isNotEmpty(this.nullSelectionMessage)) {
         this.setUiToModelEnabled(false);
         this.comboBox.setSelectedIndex(-1);
         this.comboBox.getModel().setSelectedItem(this.nullSelectionMessage);
         this.setUiToModelEnabled(true);
      }
   }

   @Override
   public void syncModel() {
      this.setModelToUiEnabled(false);

      try {
         this.selectedValue.set((T)this.comboBox.getSelectedItem());
      } finally {
         this.setModelToUiEnabled(true);
      }
   }

   @Override
   public void setUiToModelEnabled(boolean var1) {
      this.uiToModelEnabled = var1;
   }

   @Override
   public void setModelToUiEnabled(boolean var1) {
      this.modelToUiEnabled = var1;
   }

   @Override
   public void dispose() {
      this.values.removeListener(this.modelItemsListener);
      this.selectedValue.removeListener(this.selectedValueListener);
      this.comboBox.removeItemListener(this.comboSelectionListener);
   }

   private class ComboSelectionListener implements ItemListener {
      public Object selectedItem;

      private ComboSelectionListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         if (ComboBoxBinding.this.uiToModelEnabled) {
            switch (var1.getStateChange()) {
               case 1:
               case 2:
                  Object var2 = ComboBoxBinding.this.comboBox.getSelectedItem();
                  if (var2 != this.selectedItem) {
                     this.selectedItem = var2;
                     ComboBoxBinding.this.syncModel();
                  }
            }
         }
      }
   }

   private class ModelItemsListener implements PropertyChangeListener {
      private ModelItemsListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         UiThreadSupplier.call(new Supplier<Void>() {
            public Void get() {
               if (ComboBoxBinding.this.modelToUiEnabled) {
                  Object var1x = ComboBoxBinding.this.comboBox.getSelectedItem();
                  ComboBoxBinding.this.setItemsToShow(ComboBoxBinding.this.values.get());
                  switch (ComboBoxBinding.this.selectionPolicy) {
                     case DEFAULT:
                     default:
                        break;
                     case RESTORE:
                        ComboBoxBinding.this.setSelectedValue(var1x);
                        break;
                     case NULL_OUT:
                        ComboBoxBinding.this.setSelectedValue(null);
                  }
               }

               return null;
            }
         });
      }
   }

   private class SelectedValueListener implements PropertyChangeListener {
      private SelectedValueListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         final Object var2 = var1.getNewValue();
         UiThreadSupplier.call(new Supplier<Void>() {
            public Void get() {
               if (ComboBoxBinding.this.modelToUiEnabled) {
                  ComboBoxBinding.this.setUiToModelEnabled(false);
                  ComboBoxBinding.this.setSelectedValue(var2);
                  ComboBoxBinding.this.setUiToModelEnabled(true);
               }

               return null;
            }
         });
      }
   }

   public enum SelectionPolicy {
      DEFAULT,
      RESTORE,
      NULL_OUT;
   }
}
