package org.etlforge.integration.client.ui.interchange.entity;

import java.util.EnumSet;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.input.ComboBoxInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputFieldValidator;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.LabelIconFieldDecorator;

public abstract class LocatableTopPanel extends InputPanel {
   private final SourceTarget locatableType;
   private final SourceTargetTypeComboBox typeComboBox;
   private final ComboBoxInputField typeSelector;

   protected LocatableTopPanel(Locatable var1, SourceTarget var2) {
      this.restrictLocationTypes(var1);
      this.locatableType = var2;
      this.typeComboBox = this.createComboBox(var1);
      String var3 = LocationResources.getString("Locatable.Type");
      this.typeSelector = this.typeComboBox.asInputField(this.getSelectorName(), var3);
      this.setLocatable(var1);
      this.addInputFields();
   }

   private void restrictLocationTypes(Locatable var1) {
      if (!((IntegrationEntity)var1).isTransient()) {
         DataLocation var2 = var1.getLocation();
         if (var2 != null) {
            DataLocationType var3 = var2.getDataLocationType();
            if (var3.isFileBased()) {
               var1.setAllowedLocationTypes(DataLocationType.getFileBasedTypes());
            } else {
               var1.setAllowedLocationTypes(EnumSet.of(var3));
            }
         }
      }
   }

   private SourceTargetTypeComboBox createComboBox(Locatable var1) {
      SourceTargetTypeComboBox var2 = SourceTargetTypeComboBox.getTypesComboBox(var1);
      var2.setSelectionReceiver(200, new Receiver<DataLocationType>() {
         public void handle(DataLocationType var1) {
            LocatableTopPanel.this.decorateFields();
            LocatableTopPanel.this.handleTypeSelection(var1);
         }
      });
      var2.setDefaultSelection();
      return var2;
   }

   public final void setLocatable(Locatable var1) {
      DataLocation var2 = var1.getLocation();
      if (var2 != null) {
         this.typeComboBox.setSelectedLocationType(var2.getDataLocationType());
      }
   }

   public final DataLocationType getType() {
      this.typeComboBox.forceDelayedSelection();
      return this.typeComboBox.getSelectedLocationType();
   }

   final InputField getTypeSelectorField() {
      return this.typeSelector;
   }

   protected abstract void handleTypeSelection(DataLocationType var1);

   protected abstract String getMissingTypeErrorMessage();

   private String getSelectorName() {
      return LocationResources.format("LocationTypeSelector.Name", this.locatableType);
   }

   private void addInputFields() {
      LabelIconFieldDecorator var1 = new LabelIconFieldDecorator();
      var1.setDecorateValidState(false);
      this.addInputField(this.typeSelector, new LocatableTopPanel.TypeValidator(), var1);
   }

   private final class TypeValidator implements InputFieldValidator {
      private TypeValidator() {
      }

      @Override
      public boolean isFieldRequired(InputField var1) {
         return true;
      }

      @Override
      public boolean isFieldValid(InputField var1) {
         return ((ComboBoxInputField)var1).getSelectedItem() != null;
      }

      @Override
      public void validateField(InputField var1) throws InputFieldException {
         if (!this.isFieldValid(var1)) {
            throw new InputFieldException(var1, LocatableTopPanel.this.getMissingTypeErrorMessage());
         }
      }
   }
}
