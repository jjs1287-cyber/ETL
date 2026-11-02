package org.etlforge.integration.client.ui.interchange.entity.document.wizard;

import javax.swing.JLabel;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public final class DocumentWizardUtils {
   private DocumentWizardUtils() {
   }

   public static TextInputField2 getNameInputField(String var0, IntegrationEntity var1) {
      return new DocumentWizardUtils.NameInputField(var0, new DocumentWizardUtils.NameValidatorImpl(var1));
   }

   private static class NameInputField extends TextInputField2 {
      private final NameValidator nameValidator;
      private String message;

      public NameInputField(String var1, NameValidator var2) {
         super(var1, new KongaTextField(20), new JLabel(var1));
         this.nameValidator = var2;
         this.setSelectAllWhenFocused(true);
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
   }

   private static class NameValidatorImpl implements NameValidator {
      private static NameValidator DEFAULT_VALIDATOR = NameValidatorStore.getInstance().get(TextDocument.class);
      private final IntegrationEntity owner;

      public NameValidatorImpl(IntegrationEntity var1) {
         this.owner = var1;
      }

      @Override
      public void validateName(String var1) throws IllegalNameException {
         if (DEFAULT_VALIDATOR != null) {
            DEFAULT_VALIDATOR.validateName(var1);
         }

         for (IntegrationEntity var3 : this.owner.getAllChildren()) {
            if (var3 instanceof TextDocument && ((TextDocument)var3).getName().equals(var1)) {
               throw new IllegalNameException("The name '" + var1 + "' is already in use.");
            }
         }
      }

      static {
         if (DEFAULT_VALIDATOR == null) {
            DEFAULT_VALIDATOR = NameValidatorStore.getInstance().get(IntegrationEntity.class);
         }
      }
   }
}
