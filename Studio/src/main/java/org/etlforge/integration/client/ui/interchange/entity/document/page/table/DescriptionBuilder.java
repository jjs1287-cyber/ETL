package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.IntegrationEntityMapLookup;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.ScriptArgument;
import org.etlforge.integration.data.structure.text.validation.StringOptions;
import org.etlforge.integration.data.structure.text.validation.ValidationRule;
import org.etlforge.util.string.StringLimiter;

final class DescriptionBuilder {
   private StringBuilder description;
   private final String separator;
   private IntegrationEntityLookup entityLookup = IntegrationEntityMapLookup.NULL;

   public DescriptionBuilder(String var1) {
      this.separator = var1;
   }

   public void setEntityLookup(IntegrationEntityLookup var1) {
      this.entityLookup = var1;
   }

   public String describe(FieldValidation var1) {
      this.description = new StringBuilder();
      this.addRequired(var1);
      this.addLength(var1);
      this.addValidationRule(var1);
      this.addStringOptions(var1);
      this.addAction(var1);
      return this.description.toString();
   }

   private void addRequired(FieldValidation var1) {
      if (var1.isRequired()) {
         this.add("Not Empty");
      }
   }

   private void add(String var1) {
      if (this.description.length() > 0) {
         this.description.append(this.separator);
      }

      this.description.append(var1);
   }

   private void addLength(FieldValidation var1) {
      Integer var2 = var1.getMaxLength().orNull();
      if (var2 != null) {
         this.add("Length: " + var2);
      }
   }

   private void addValidationRule(FieldValidation var1) {
      ValidationRule var2 = var1.getRule().orNull();
      if (var2 != null) {
         String var3 = var2.getOperator().getName();
         OperatorArgument var4 = var2.getArgument();
         String var5 = StringLimiter.limit(this.describeArgument(var4), 100);
         this.add(var5.isEmpty() ? var3 : var3 + ": " + var5);
      }
   }

   private String describeArgument(OperatorArgument var1) {
      if (var1 instanceof ScriptArgument) {
         ScriptId var2 = ((ScriptArgument)var1).getId();
         Script var3 = this.entityLookup.getEntity(var2, Script.class);
         return var3 == null ? var2.toString() : var3.getName();
      } else {
         return var1.toString();
      }
   }

   private void addStringOptions(FieldValidation var1) {
      StringOptions var2 = var1.getStringOptions().orNull();
      if (var2 != null) {
         if (var2.trimToLength) {
            this.add("Trim to length");
         }

         if (var2.ignoreCase) {
            this.add("Ignore case");
         }
      }
   }

   private void addAction(FieldValidation var1) {
      this.add("If invalid: " + var1.getAction().toString());
   }
}
