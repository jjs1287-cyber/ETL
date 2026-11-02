package org.etlforge.ui.input;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.input.undo.ComboUndoableEdit;
import org.etlforge.ui.selection.SelectionSource;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.combobox.ComboBoxSelectionSource;
import org.etlforge.ui.widget.label.KongaLabel;

public class ComboBoxInputField extends InputField {
   private final ActionListener editListener;
   private SelectionSource selectionSource;

   public ComboBoxInputField(String var1, JComboBox var2) {
      this(var1, var2, (JLabel)null);
   }

   public ComboBoxInputField(String var1, JComboBox var2, String var3) {
      this(var1, var2, toJLabel(var3));
   }

   private static JLabel toJLabel(String var0) {
      return var0 == null ? null : KongaLabel.withMnemonic(var0);
   }

   public ComboBoxInputField(String var1, JComboBox var2, JLabel var3) {
      super(var1, var2, var3);
      Object var4 = this.getSelectedItem();
      this.editListener = this.createEditListener(var4);
      var2.addActionListener(this.editListener);
   }

   public JComboBox getInputComponent() {
      return (JComboBox)super.getInputComponent();
   }

   public SelectionSource asSelectionSource() {
      if (this.selectionSource == null) {
         this.selectionSource = new ComboBoxSelectionSource(this.getInputComponent());
      }

      return this.selectionSource;
   }

   public Object getSelectedItem() {
      int var1 = this.getSelectedIndex();
      return var1 == -1 ? null : this.getInputComponent().getSelectedItem();
   }

   public Object[] getSelectedObjects() {
      return this.getInputComponent().getSelectedObjects();
   }

   public int getSelectedIndex() {
      return this.getInputComponent().getSelectedIndex();
   }

   public void setSelectedItem(Object var1) {
      this.getInputComponent().setSelectedItem(var1);
   }

   public void setSelectedItemSilently(final Object var1) {
      this.updateSilently(new Runnable() {
         @Override
         public void run() {
            ComboBoxInputField.this.setSelectedItem(var1);
         }
      });
   }

   public void setSelectedIndex(int var1) {
      this.getInputComponent().setSelectedIndex(var1);
   }

   public void setSelectedIndexSilently(final int var1) {
      this.updateSilently(new Runnable() {
         @Override
         public void run() {
            ComboBoxInputField.this.setSelectedIndex(var1);
         }
      });
   }

   public void setMaximumRowCount(int var1) {
      this.getInputComponent().setMaximumRowCount(var1);
   }

   public void setRenderer(ListCellRenderer var1) {
      this.getInputComponent().setRenderer(var1);
   }

   @Override
   public void makeReadOnly() {
      JComboBox var1 = this.getInputComponent();
      var1.setEditable(false);
      var1.setFocusable(false);
      WidgetUtils.removeMouseListeners(var1);
   }

   @Override
   public void dispose() {
      this.getInputComponent().removeActionListener(this.editListener);
   }

   protected UndoableEdit createUndoableEdit(Object var1, Object var2) {
      return new ComboUndoableEdit(this, var1, var2);
   }

   protected ActionListener createEditListener(Object var1) {
      return new ComboBoxInputField.EditListener(var1);
   }

   private final class EditListener implements ActionListener {
      private Object selectedItem;

      public EditListener(Object var2) {
         this.selectedItem = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         Object var2 = ComboBoxInputField.this.getSelectedItem();
         if (var2 != this.selectedItem) {
            if (ComboBoxInputField.this.isSendingInputChangeEvents()) {
               ComboBoxInputField.this.fireInputChangeEvent(this.selectedItem, var2);
            }

            if (ComboBoxInputField.this.isSendingUndoEvents()) {
               UndoableEdit var3 = ComboBoxInputField.this.createUndoableEdit(this.selectedItem, var2);
               if (var3 != null) {
                  ComboBoxInputField.this.fireUndoableEditHappened(var3);
               }
            }

            this.selectedItem = var2;
         }
      }
   }
}
