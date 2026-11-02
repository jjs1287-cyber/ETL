package org.etlforge.integration.client.ui.entity;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import org.etlforge.integration.client.ui.entity.resources.EntityResources;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferSupport;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public final class IntegrationEntityNameInputField extends TextInputField2 {
   private final IntegrationEntity entity;
   private IllegalNameException lastException;
   private NameValidator nameValidator;

   public IntegrationEntityNameInputField(final IntegrationEntity var1, String var2, final JTextComponent var3, JLabel var4) {
      super(var2, var3, var4);
      var3.addFocusListener(new FocusAdapter() {
         @Override
         public void focusGained(FocusEvent var1x) {
            if (var1.isTransient()) {
               var3.selectAll();
            }
         }
      });
      DeTemplateTransferSupport.installRejector(var3);
      this.setUndoRedoPresentation(EntityResources.getString("NameInputField.UndoRedoPresentation"));
      this.setAutoTrim(true);
      this.entity = var1;
   }

   public void setNameValidator(NameValidator var1) {
      this.nameValidator = var1;
   }

   @Override
   public boolean isInputValid(String var1) {
      this.lastException = null;

      try {
         this.validateName(var1);
         return true;
      } catch (IllegalNameException var3) {
         this.lastException = var3;
         return false;
      }
   }

   private void validateName(String var1) throws IllegalNameException {
      if (this.nameValidator == null) {
         this.nameValidator = getDefaultNameValidator(this.entity);
      }

      this.nameValidator.validateName(var1);
   }

   @Override
   public String getInvalidMessage(String var1) {
      return this.lastException != null ? this.lastException.getMessage() : super.getInvalidMessage(var1);
   }

   public static NameValidator getDefaultNameValidator(final IntegrationEntity var0) {
      return new NameValidator() {
         @Override
         public void validateName(String var1) throws IllegalNameException {
            NameValidator var2 = NameValidatorStore.getInstance().get(var0);
            if (var2 != null) {
               var2.validateName(var1);
            }

            if (!EntityUtils.isNameUnique(var0, var1)) {
               String var3 = EntityResources.format("DuplicateName.Message", var1);
               throw new IllegalNameException(var3);
            }
         }
      };
   }
}
