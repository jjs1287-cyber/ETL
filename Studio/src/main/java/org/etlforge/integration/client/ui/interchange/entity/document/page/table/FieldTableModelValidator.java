package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.util.Iterator;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.TextStructureValidator;
import org.etlforge.integration.data.structure.text.TextValidatorFactory;

final class FieldTableModelValidator extends AbstractModelValidator {
   private final FieldTableModel model;

   FieldTableModelValidator(FieldTableModel var1) {
      this.model = var1;
   }

   @Override
   protected TextStructureValidator getValidator() {
      TextValidatorFactory var1 = new TextValidatorFactory();
      return var1.createFieldValidator(new Iterable<Field>() {
         @Override
         public Iterator<Field> iterator() {
            return FieldTableModelValidator.this.model.getFieldIterator();
         }
      });
   }
}
