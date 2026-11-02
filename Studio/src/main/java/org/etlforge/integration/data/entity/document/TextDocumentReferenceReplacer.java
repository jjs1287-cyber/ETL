package org.etlforge.integration.data.entity.document;

import org.etlforge.collections.Pair;
import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.integration.data.structure.text.validation.Operator;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.ScriptArgument;
import org.etlforge.integration.data.structure.text.validation.ScriptValidationFilter;
import org.etlforge.integration.data.structure.text.validation.ValidationRule;

public final class TextDocumentReferenceReplacer {
   private final ReferenceReplacementService service;

   public TextDocumentReferenceReplacer(ReferenceReplacementService var1) {
      this.service = var1;
   }

   public void replaceReferences(TextDocument var1) {
      TextStructure var2 = var1.getDataStructure();
      if (var2 instanceof SimpleTextStructure) {
         this.replaceReferenceToValidationScripts((SimpleTextStructure)var2);
      }
   }

   private void replaceReferenceToValidationScripts(SimpleTextStructure var1) {
      for (Pair var3 : var1.getFieldsWithValidation(new ScriptValidationFilter())) {
         ValidationRule var4 = ((FieldValidation)var3.second).getRule().get();
         OperatorArgument var5 = var4.getArgument();
         ScriptId var6 = ((ScriptArgument)var5).getId();
         FieldValidation var7 = this.replaceScript((FieldValidation)var3.second, var4.getOperator(), var6);
         ((Field)var3.first).setValidation(var7);
      }
   }

   private FieldValidation replaceScript(FieldValidation var1, Operator var2, ScriptId var3) {
      Script var4 = this.service.getReplacement(var3, Script.class);
      if (var4 == null) {
         System.err.println("Could not find field validation script with GUID " + var3);
         return var1.withoutOperator();
      } else {
         ValidationRule var5 = ValidationRule.callScript(var4);
         return var1.withOperator(var5);
      }
   }
}
