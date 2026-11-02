package org.etlforge.integration.data.entity.document;

import com.google.common.base.Optional;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.entity.link.AbstractEntityLink;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.ScriptArgument;
import org.etlforge.integration.data.structure.text.validation.ValidationRule;

public final class FieldValidationScript extends AbstractEntityLink {
   public FieldValidationScript(Script var1, TextDocument var2) {
      super(var1, var2);
   }

   @Override
   public boolean isStrong() {
      return false;
   }

   @Override
   protected void mainObjectDeleted() {
      super.mainObjectDeleted();
      ScriptId var1 = (ScriptId)this.getMainObject().getID();
      this.removeValidation(var1);
   }

   private void removeValidation(ScriptId var1) {
      TextDocument var2 = (TextDocument)this.getDependantObject();

      for (Field var5 : (SimpleTextStructure)var2.getDataStructure()) {
         Optional var6 = var5.getValidation();
         if (!var6.isPresent()) {
            return;
         }

         Optional var7 = ((FieldValidation)var6.get()).getRule();
         if (!var7.isPresent()) {
            return;
         }

         OperatorArgument var8 = ((ValidationRule)var7.get()).getArgument();
         if (var8 instanceof ScriptArgument && ((ScriptArgument)var8).getId().equals(var1)) {
            FieldValidation var9 = ((FieldValidation)var6.get()).withoutOperator();
            var5.setValidation(var9);
         }
      }
   }
}
