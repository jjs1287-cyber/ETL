package org.etlforge.integration.data.structure.text.validation;

import org.etlforge.integration.data.structure.text.FieldDataType;

final class RegexOperator extends Operator {
   public RegexOperator() {
      super("Regex", StringArgument.class);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return true;
   }
}
