package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.util.Collection;
import java.util.Iterator;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.integration.client.ui.interchange.entity.document.page.ValidationAbortedException;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.TextStructureValidator;
import org.etlforge.integration.data.structure.text.TextValidatorFactory;
import org.etlforge.integration.data.structure.text.ValidationIssue;

final class FixedWidthFieldTableModelValidator {
   private final TextStructureValidator validator;

   FixedWidthFieldTableModelValidator(final FixedWidthFieldTableModel var1) {
      TextValidatorFactory var2 = new TextValidatorFactory();
      this.validator = var2.createFixedFieldValidator(new Iterable<Field>() {
         @Override
         public Iterator<Field> iterator() {
            return var1.getFieldIterator();
         }
      });
   }

   void validate() {
      Collection var1 = this.validator.validate();
      if (!var1.isEmpty()) {
         this.displayValidationErrors(var1);
      }
   }

   private void displayValidationErrors(Collection<? extends ValidationIssue> var1) {
      StringBuilder var2 = new StringBuilder("There are warnings associated with the defined structure");
      var2.append(":\n");

      for (ValidationIssue var4 : var1) {
         var2.append("\n");
         var2.append(var4.getDescription());
      }

      var2.append("\n\n");
      var2.append("Do you want to continue?");
      Answer var5 = Ask.yesOrNo(null, var2.toString(), "Warning - Continue?", Alert.Option.NO, Alert.Type.WARNING);
      if (var5.isNo()) {
         throw new ValidationAbortedException();
      }
   }
}
