package org.etlforge.integration.client.ui.entity.wizard;

import javax.swing.text.JTextComponent;
import org.etlforge.integration.data.DefaultIntegrationNameValidator;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;
import org.etlforge.util.name.Nameable;

public final class WizardNameInputField extends TextInputField2 {
   private final IntegrationEntity item;
   private NameValidator nameValidator;
   private String message;

   public WizardNameInputField(MnemonicString var1, IntegrationEntity var2, Folder var3) {
      super(var1.getText(), createTextField(), new KongaLabel(var1));
      this.item = var2;
      this.setParent(var3);
      this.setSelectAllWhenFocused(true);
      this.setValue(var2.getName());
      this.setAutoTrim(true);
   }

   public void setParent(Folder var1) {
      if (var1 == null) {
         this.nameValidator = new DefaultIntegrationNameValidator();
      } else {
         this.nameValidator = new WizardNameInputField.NameValidatorImpl(this.item, var1);
      }

      this.decorateField();
   }

   @Override
   public boolean isInputValid(String var1) {
      try {
         this.nameValidator.validateName(var1);
         return true;
      } catch (IllegalNameException var3) {
         this.message = var3.getMessage();
         return false;
      }
   }

   @Override
   public String getInvalidMessage(String var1) {
      return this.message;
   }

   private static JTextComponent createTextField() {
      return new KongaTextField(30);
   }

   private static class NameValidatorImpl implements NameValidator {
      private final NameValidator defaultValidator;
      private final IntegrationEntity item;
      private final Folder owner;

      public NameValidatorImpl(IntegrationEntity var1, Folder var2) {
         this.item = var1;
         this.owner = var2;
         this.defaultValidator = this.createImpl();
      }

      private NameValidator createImpl() {
         NameValidator var1 = NameValidatorStore.getInstance().get((Class<? extends Nameable>)this.item.getClass());
         if (var1 == null) {
            var1 = NameValidatorStore.getInstance().get(IntegrationEntity.class);
         }

         return var1;
      }

      @Override
      public void validateName(String var1) throws IllegalNameException {
         if (this.defaultValidator != null) {
            this.defaultValidator.validateName(var1);
         }

         for (IntegrationEntity var3 : this.owner.getAllChildren()) {
            if (var3 != this.item && var3.getClass().equals(this.item.getClass()) && var3.getName().equals(var1)) {
               throw new IllegalNameException("The name '" + var1 + "' is already in use.");
            }
         }
      }
   }
}
