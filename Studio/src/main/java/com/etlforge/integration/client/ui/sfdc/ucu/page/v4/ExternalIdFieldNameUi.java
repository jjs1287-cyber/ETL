package com.etlforge.integration.client.ui.sfdc.ucu.page.v4;

import java.util.Collection;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.salesforce.SObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.combobox.KongaComboBox;

final class ExternalIdFieldNameUi implements UiProvider {
   private final KongaComboBox dropdown = new KongaComboBox();
   private final ExternalIdFieldNameUi.Ui ui;

   public ExternalIdFieldNameUi() {
      this(null);
   }

   public ExternalIdFieldNameUi(InputPanel var1) {
      this(var1, true);
   }

   public ExternalIdFieldNameUi(InputPanel var1, boolean var2) {
      this.dropdown.setLabel("&Select the field to use as external ID:");
      this.dropdown.setMaximumRowCount(16);
      this.dropdown.addSelectionListener(new ExternalIdFieldNameUi.SelectionHandler());
      if (var1 != null) {
         var1.addInputField(new ComboBoxInputField("External Field ID", this.dropdown));
      }

      this.ui = this.createUi(var2);
      this.setNullSelection();
   }

   private ExternalIdFieldNameUi.Ui createUi(boolean var1) {
      return var1 ? new ExternalIdFieldNameUi.UiWithRequiredMarker(this.dropdown) : new ExternalIdFieldNameUi.UiWithoutRequiredMarker(this.dropdown);
   }

   private void setNullSelection() {
      this.dropdown.setSelectedIndex(-1);
      this.dropdown.getModel().setSelectedItem("Select a field...");
      this.updateValidState();
   }

   private void updateValidState() {
      this.ui.setValid(this.isInputValid());
   }

   public void setEnabled(boolean var1) {
      this.dropdown.setEnabled(var1);
   }

   public boolean isEnabled() {
      return this.dropdown.isEnabled();
   }

   public void setFieldList(Collection<SObject.Field> var1) {
      DefaultComboBoxModel var2 = new DefaultComboBoxModel<>(var1.toArray());
      this.setNewComboModel(var2);
   }

   private void setNewComboModel(ComboBoxModel var1) {
      this.dropdown.setModel(var1);
      this.setNullSelection();
   }

   public void setObject(SObject var1) {
      ComboBoxModel var2 = this.createFieldModel(var1);
      this.setNewComboModel(var2);
   }

   private ComboBoxModel createFieldModel(SObject var1) {
      DefaultComboBoxModel var2 = new DefaultComboBoxModel();
      if (var1 != null) {
         for (SObject.Field var4 : var1.getFields()) {
            if (var4.isSimpleType()) {
               var2.addElement(var4);
            }
         }
      }

      return var2;
   }

   public void setSelectedFieldName(String var1) {
      if (var1 != null) {
         ComboBoxModel var2 = this.dropdown.getModel();
         int var3 = 0;

         for (int var4 = var2.getSize(); var3 < var4; var3++) {
            SObject.Field var5 = (SObject.Field)var2.getElementAt(var3);
            if (var5.getName().equals(var1)) {
               this.dropdown.setSelectedIndex(var3);
               return;
            }
         }
      }

      this.setNullSelection();
   }

   public SObject.Field getSelectedField() {
      Object var1 = this.dropdown.getSelectedItem();
      return var1 instanceof SObject.Field ? (SObject.Field)var1 : null;
   }

   public String getSelectedFieldName() {
      SObject.Field var1 = this.getSelectedField();
      return var1 == null ? null : var1.getName();
   }

   public boolean isInputValid() {
      return this.dropdown.getSelectedIndex() >= 0;
   }

   public void addChangeListener(SelectionListener var1) {
      this.dropdown.addSelectionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.ui.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.dropdown.requestFocusInWindow();
   }

   private class SelectionHandler implements SelectionListener {
      private SelectionHandler() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         ExternalIdFieldNameUi.this.updateValidState();
      }
   }

   private abstract static class Ui {
      protected final JLabel invalidStateMarker = WizardStyleSheet.REQUIRED_STYLE.makeLabel("");
      private boolean valid;

      public Ui() {
         this.setNewValidState(false);
      }

      public abstract void decorate(UiCanvas var1);

      public final void setValid(boolean var1) {
         if (this.valid != var1) {
            this.setNewValidState(var1);
            this.handleValidStateChange(this.valid);
         }
      }

      private void setNewValidState(boolean var1) {
         this.valid = var1;
         if (this.valid) {
            this.invalidStateMarker.setIcon(null);
            this.invalidStateMarker.setText(" ");
         } else {
            this.invalidStateMarker.setIcon(CommonIcons.ERROR_16);
            this.invalidStateMarker.setText("Required");
         }
      }

      protected void handleValidStateChange(boolean var1) {
      }
   }

   private static class UiWithRequiredMarker extends ExternalIdFieldNameUi.Ui {
      private final UiProvider impl;

      public UiWithRequiredMarker(JComboBox var1) {
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
         var2.center(var1).south(this.invalidStateMarker);
         this.impl = var2;
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.impl.decorate(var1);
      }
   }

   private static class UiWithoutRequiredMarker extends ExternalIdFieldNameUi.Ui {
      private final JComboBox dropdown;

      public UiWithoutRequiredMarker(JComboBox var1) {
         this.dropdown = var1;
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(this.dropdown);
      }
   }
}
