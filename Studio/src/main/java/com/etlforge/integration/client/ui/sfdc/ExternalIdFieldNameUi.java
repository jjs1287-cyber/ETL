package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.salesforce.ui.NameDisplayMode;
import com.etlforge.integration.salesforce.ui.renderer.Renderers;
import java.awt.Color;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.ComboBoxBinding;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.combobox.KongaComboBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.util.Property;

public final class ExternalIdFieldNameUi implements UiProvider {
   private final KongaComboBox dropdown;
   private final KongaLabel required;

   public ExternalIdFieldNameUi(ObjectSelectionModel var1) {
      this.dropdown = this.createDropdown(var1);
      this.required = this.createRequiredMarker();
      this.bindToModel(var1);
   }

   private KongaComboBox createDropdown(ObjectSelectionModel var1) {
      KongaComboBox var2 = new KongaComboBox();
      var2.setLabel("&Select the field to use as external ID:");
      var2.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXX");
      var2.setMaximumRowCount(16);
      NameDisplayMode var3 = var1.getFieldNameDisplayMode();
      var2.setRenderer(Renderers.forList(var3));
      return var2;
   }

   private KongaLabel createRequiredMarker() {
      KongaLabel var1 = WizardStyleSheet.SMALL_CAPTION_STYLE.makeLabel("Required");
      var1.setForeground(Color.RED);
      return var1;
   }

   private void bindToModel(ObjectSelectionModel var1) {
      Property var2 = var1.getProperty("availableFields");
      Property var3 = var1.getProperty("selectedField");
      ComboBoxBinding var4 = new ComboBoxBinding(this.dropdown, var2, var3);
      var4.setSelectionPolicyOnContentChange(ComboBoxBinding.SelectionPolicy.NULL_OUT);
      var4.setNullSelectionMessage("Select a field...");
      var4.setDisableWhenEmpty(true);
      var4.syncUi();
      ExternalIdFieldNameUi.RequiredMarkerBinding var5 = new ExternalIdFieldNameUi.RequiredMarkerBinding(var3);
      var5.syncUi();
   }

   public boolean isEnabled() {
      return this.dropdown.isEnabled();
   }

   public SObjectSchema.Field getSelectedField() {
      Object var1 = this.dropdown.getSelectedItem();
      return var1 instanceof SObjectSchema.Field ? (SObjectSchema.Field)var1 : null;
   }

   public String getSelectedFieldName() {
      SObjectSchema.Field var1 = this.getSelectedField();
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
      Horizontal.grid().withHGap(10).of(this.dropdown, this.required).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.dropdown.requestFocusInWindow();
   }

   public void addToParent(InputPanel var1) {
      var1.addInputField(new ComboBoxInputField("External Field ID", this.dropdown));
   }

   private class RequiredMarkerBinding extends AbstractBinding<SObjectSchema.Field> {
      public RequiredMarkerBinding(Property<SObjectSchema.Field> var2) {
         super(var2);
      }

      protected void updateUiWithValue(SObjectSchema.Field var1) {
         ExternalIdFieldNameUi.this.required.setVisible(var1 == null);
      }
   }
}
