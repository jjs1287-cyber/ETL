package org.etlforge.integration.client.ui.project;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.application.ui.widget.TextFieldInputValidator;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.util.name.IllegalNameException;
import org.etlforge.util.name.NameValidator;
import org.etlforge.util.name.NameValidatorStore;

public final class ProjectNameInputFieldValidator extends TextFieldInputValidator {
   private final NameValidator validator = NameValidatorStore.getInstance().get(IntegrationProject.class);
   private final Map<String, String> messages = Maps.newHashMap();

   @Override
   public boolean validate(String var1) {
      if (this.messages.containsKey(var1)) {
         return false;
      }

      try {
         this.validator.validateName(var1);
         return true;
      } catch (IllegalNameException var3) {
         this.messages.put(var1, var3.getMessage());
         return false;
      }
   }

   @Override
   public String getErrorMessage(String var1) {
      String var2 = this.messages.get(var1);
      if (var2 == null) {
         try {
            this.validator.validateName(var1);
         } catch (IllegalNameException var4) {
            var2 = var4.getMessage();
            this.messages.put(var1, var2);
         }
      }

      return var2;
   }
}
